package net.fabricmc.crossbowaim.auto;
import net.fabricmc.crossbowaim.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
public class AutoFireHandler{
private static int cooldown=0;
public static void onClientTick(Minecraft client){if(!ModConfig.autofireEnabled)return;if(cooldown>0){cooldown--;return;}LocalPlayer player=client.player;if(player==null||client.gameMode==null)return;if(player.isSpectator()||player.isDeadOrDying())return;InteractionHand crossbowHand=getChargedCrossbowHand(player);if(crossbowHand==null)return;HitResult hit=player.pick(ModConfig.maxDistance,1.0f,false);if(!(hit instanceof EntityHitResult entityHit))return;if(!isValidTarget(entityHit.getEntity(),player))return;client.gameMode.useItem(player,crossbowHand);ItemStack crossbow=player.getItemInHand(crossbowHand);if(CrossbowItem.isCharged(crossbow)){client.gameMode.releaseUsingItem(player);}cooldown=10;}
private static InteractionHand getChargedCrossbowHand(LocalPlayer player){ItemStack mainHand=player.getMainHandItem();if(mainHand.getItem() instanceof CrossbowItem&&CrossbowItem.isCharged(mainHand))return InteractionHand.MAIN_HAND;ItemStack offHand=player.getOffhandItem();if(offHand.getItem() instanceof CrossbowItem&&CrossbowItem.isCharged(offHand))return InteractionHand.OFF_HAND;return null;}
private static boolean isValidTarget(net.minecraft.world.entity.Entity entity,LocalPlayer player){if(entity==player)return false;if(entity.isRemoved())return false;if(entity.isInvisibleTo(player))return false;if(entity instanceof LivingEntity living){if(living.isDeadOrDying())return false;if(living instanceof Player targetPlayer){if(targetPlayer.isCreative()||targetPlayer.isSpectator())return false;if(player.isAlliedTo(targetPlayer))return false;}if(living instanceof Monster)return true;}return entity instanceof LivingEntity;}}