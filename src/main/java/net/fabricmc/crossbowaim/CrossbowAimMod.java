package net.fabricmc.crossbowaim;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.crossbowaim.auto.AutoFireHandler;
import net.fabricmc.crossbowaim.config.ModConfig;
import net.fabricmc.crossbowaim.render.TrajectoryRenderer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CrossbowAimMod implements ClientModInitializer{public static final String MOD_ID="crossbowaim";public static final Logger LOGGER=LoggerFactory.getLogger(MOD_ID);public static KeyMapping keyToggleTrajectory;public static KeyMapping keyToggleAutofire;@Override public void onInitializeClient(){LOGGER.info("CrossbowAim initializing...");try{ModConfig.load();}catch(Exception e){LOGGER.error("Config init failed",e);}try{keyToggleTrajectory=KeyMappingHelper.registerKeyMapping(new KeyMapping("key.crossbowaim.toggle_trajectory",GLFW.GLFW_KEY_V,"category.crossbowaim"));keyToggleAutofire=KeyMappingHelper.registerKeyMapping(new KeyMapping("key.crossbowaim.toggle_autofire",GLFW.GLFW_KEY_B,"category.crossbowaim"));ClientTickEvents.END_CLIENT_TICK.register(CrossbowAimMod::onClientTick);LOGGER.info("CrossbowAim loaded successfully");}catch(Exception e){LOGGER.error("Failed to register key bindings",e);}}private static void onClientTick(Minecraft client){if(client.player==null)return;if(keyToggleTrajectory!=null)while(keyToggleTrajectory.consumeClick()){ModConfig.trajectoryEnabled=!ModConfig.trajectoryEnabled;ModConfig.save();client.player.displayClientMessage(Component.translatable("crossbowaim.config.toggle_trajectory",ModConfig.trajectoryEnabled?"ON":"OFF"),true);}if(keyToggleAutofire!=null)while(keyToggleAutofire.consumeClick()){ModConfig.autofireEnabled=!ModConfig.autofireEnabled;ModConfig.save();client.player.displayClientMessage(Component.translatable("crossbowaim.config.toggle_autofire",ModConfig.autofireEnabled?"ON":"OFF"),true);}TrajectoryRenderer.renderTrajectory();AutoFireHandler.onClientTick(client);}}