package net.fabricmc.crossbowaim.config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.crossbowaim.CrossbowAimMod;
import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
public class ModConfig{
private static final Gson GSON=new GsonBuilder().setPrettyPrinting().create();
private static Path configPath;
public static boolean trajectoryEnabled=true;
public static boolean autofireEnabled=false;
public static double maxDistance=50.0;
public static int dotSpacing=3;
public static int predictionTicks=0;
public static int trajectoryColor=0x00FF00;
public static int obstacleColor=0xFF0000;
public static int endpointColor=0xFFFF00;
public static float lineAlpha=0.6f;
public static boolean showEndpointMarker=true;
public static void load(){try{configPath=FabricLoader.getInstance().getConfigDir().resolve("crossbowaim.json");if(Files.exists(configPath)){try{String json=Files.readString(configPath);ConfigData data=GSON.fromJson(json,ConfigData.class);if(data!=null){trajectoryEnabled=data.trajectoryEnabled;autofireEnabled=data.autofireEnabled;maxDistance=data.maxDistance;dotSpacing=data.dotSpacing;predictionTicks=data.predictionTicks;trajectoryColor=data.trajectoryColor;obstacleColor=data.obstacleColor;endpointColor=data.endpointColor;lineAlpha=data.lineAlpha;showEndpointMarker=data.showEndpointMarker;}}catch(Exception e){CrossbowAimMod.LOGGER.warn("Failed to load config, using defaults",e);}}try{save();}catch(Exception e){CrossbowAimMod.LOGGER.error("Failed to save config",e);}}catch(Exception e){CrossbowAimMod.LOGGER.error("Failed to init config",e);}}
public static void save(){if(configPath==null)return;try{ConfigData data=new ConfigData();Files.writeString(configPath,GSON.toJson(data));}catch(IOException e){CrossbowAimMod.LOGGER.error("Failed to save config",e);}}
public static int getTrajectoryColor(float progress){int r=(trajectoryColor>>16)&0xFF;int g=(trajectoryColor>>8)&0xFF;int b=trajectoryColor&0xFF;int a=(int)(lineAlpha*progress*255);return(a<<24)|(r<<16)|(g<<8)|b;}
static class ConfigData{boolean trajectoryEnabled=true;boolean autofireEnabled=false;double maxDistance=50.0;int dotSpacing=3;int predictionTicks=0;int trajectoryColor=0x00FF00;int obstacleColor=0xFF0000;int endpointColor=0xFFFF00;float lineAlpha=0.6f;boolean showEndpointMarker=true;}}