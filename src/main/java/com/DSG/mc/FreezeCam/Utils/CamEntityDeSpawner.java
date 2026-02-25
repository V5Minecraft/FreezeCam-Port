package com.DSG.mc.FreezeCam.Utils;

import com.DSG.mc.FreezeCam.CamEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public class CamEntityDeSpawner {
   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public void DeSpawnCamEnt(EntityInteract ev) {
      if (ev.getTarget() instanceof CamEntity) {
         ((CamEntity)ev.getTarget()).Interacted();
      }
   }
}
