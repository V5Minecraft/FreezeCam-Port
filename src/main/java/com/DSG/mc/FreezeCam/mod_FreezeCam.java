package com.DSG.mc.FreezeCam;

import com.DSG.mc.FreezeCam.Utils.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "freezecam", name = "FreezeCam", version = "1.0")
public class mod_FreezeCam {
   public static boolean OverrideFunction = true;
   public static boolean OverrideScreenFunction = true;
   public static boolean CheckForUpdate = true;
   private final DSGUtils DSGUtils = new DSGUtils();

   @Mod.EventHandler
   public void PreLoad(FMLPreInitializationEvent event) {
      Configuration config = new Configuration(event.getSuggestedConfigurationFile());
      config.load();
      config.addCustomCategoryComment("general", "Disable this when it conflicts with another mods");
      OverrideFunction = config.get("general", "Override functions", true).getBoolean(true);
      OverrideScreenFunction = config.get("general", "Disable fire and water/lava Events on screen", false).getBoolean(true);
      CheckForUpdate = config.get("general", "Check for updates", true).getBoolean(true);
      config.save();
   }

   @Mod.EventHandler
   public void load(FMLInitializationEvent event) {
      this.DSGUtils.initUtils(FMLClientHandler.instance().getClient());
      MinecraftForge.EVENT_BUS.register(new CamEntityDeSpawner());
      FMLCommonHandler.instance().bus().register(new DSG_iKeyHandler());
      MinecraftForge.EVENT_BUS.register(new FreezeCamHandler());
   }
}
