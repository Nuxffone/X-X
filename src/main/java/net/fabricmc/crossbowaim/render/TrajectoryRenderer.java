package net.fabricmc.crossbowaim.render;
import net.fabricmc.crossbowaim.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.SimpleGizmoCollector;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.*;
import java.util.ArrayList;
import java.util.List;
public class TrajectoryRenderer{
private static final double GRAVITY=0.05;
private static final double DRAG=0.99;
private static final double INITIAL_SPEED=3.15;
private static final int MAX_TICKS=100;
public static void renderTrajectory(){if(!ModConfig.trajectoryEnabled)return;Minecraft client=Minecraft.getInstance();LocalPlayer player=client.player;if(player==null||client.level==null||client.levelRenderer==null)return;boolean holdingCrossbow=isHoldingChargedCrossbow(player);boolean holdingBow=isHoldingBow(player);if(!holdingCrossbow&&!holdingBow)return;ItemStack weapon=getWeapon(player,holdingCrossbow);if(weapon==null)return;float pullProgress=holdingCrossbow?getCrossbowPull(weapon):getBowPullProgress(player);double speed=INITIAL_SPEED*pullProgress;if(speed<0.5)return;float partialTicks=client.getDeltaTracker().getGameTimeDeltaTicks();Vec3 startPos=player.getEyePosition(partialTicks);Vec3 lookVec=player.getViewVector(1.0f);SimpleGizmoCollector collector=new SimpleGizmoCollector();try(var ignored=Gizmos.withCollector(collector)){Vec3 velocity=lookVec.scale(speed);Vec3 currentPos=startPos;List<Vec3> points=new ArrayList<>();points.add(currentPos);boolean hit=false;Vec3 hitPoint=null;for(int tick=0;tick<MAX_TICKS;tick++){Vec3 nextPos=currentPos.add(velocity);velocity=velocity.scale(DRAG);velocity=velocity.add(0,-GRAVITY,0);if(tick%ModConfig.dotSpacing==0)points.add(nextPos);BlockHitResult blockHit=client.level.clip(new ClipContext(currentPos,nextPos.add(velocity.normalize().scale(1.0)),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,player));if(blockHit.getType()!=HitResult.Type.MISS){hitPoint=blockHit.getLocation();hit=true;points.add(hitPoint);break;}currentPos=nextPos;if(currentPos.distanceToSqr(startPos)>ModConfig.maxDistance*ModConfig.maxDistance){hitPoint=currentPos;points.add(hitPoint);break;}}for(int i=1;i<points.size();i++){Vec3 from=points.get(i-1);Vec3 to=points.get(i);float progress=(float)i/points.size();int color=ModConfig.getTrajectoryColor(progress);Gizmos.line(from,to,color,1.5f).persistForMillis(50);}if(hitPoint!=null&&ModConfig.showEndpointMarker){int markColor=hit?ModConfig.obstacleColor:ModConfig.endpointColor;Vec3 half=new Vec3(0.15,0.15,0.15);Gizmos.cuboid(new AABB(hitPoint.subtract(half),hitPoint.add(half)),GizmoStyle.stroke(markColor,2.0f)).persistForMillis(50);}}client.levelRenderer.addMainThreadGizmos(collector.drainGizmos());}
private static boolean isHoldingChargedCrossbow(LocalPlayer player){ItemStack mainHand=player.getMainHandItem();if(mainHand.getItem() instanceof CrossbowItem&&CrossbowItem.isCharged(mainHand))return true;ItemStack offHand=player.getOffhandItem();return offHand.getItem() instanceof CrossbowItem&&CrossbowItem.isCharged(offHand);}
private static boolean isHoldingBow(LocalPlayer player){return player.getMainHandItem().is(Items.BOW)||player.getOffhandItem().is(Items.BOW);}
private static ItemStack getWeapon(LocalPlayer player,boolean crossbow){if(crossbow){ItemStack mh=player.getMainHandItem();if(mh.getItem() instanceof CrossbowItem)return mh;return player.getOffhandItem();}ItemStack mh=player.getMainHandItem();if(mh.is(Items.BOW))return mh;ItemStack oh=player.getOffhandItem();if(oh.is(Items.BOW))return oh;return null;}
private static float getCrossbowPull(ItemStack crossbow){if(CrossbowItem.isCharged(crossbow))return 1.0f;return CrossbowItem.getChargeDuration(crossbow,null)>0?0.5f:0f;}
private static float getBowPullProgress(LocalPlayer player){ItemStack stack=player.getMainHandItem();if(!stack.is(Items.BOW))stack=player.getOffhandItem();if(!stack.is(Items.BOW))return 0f;int useTime=player.getTicksUsingItem();float power=useTime/20f;power=(power*power+power*2f)/3f;return Math.min(power,1f);}}