package com.DSG.mc.FreezeCam;

import com.DSG.mc.FreezeCam.Utils.DSGUtils;
import com.mojang.authlib.GameProfile;

import java.util.UUID;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class CamEntity extends EntityOtherPlayerMP {
   private EntityLivingBase follow;
   private int LookDistance;
   public EnumFunction MyFunction;
   private boolean isSneaking;

   public CamEntity(World MCworld, String iName, EnumFunction function) {
      this(MCworld, iName, false, function);
   }

   public CamEntity(World MCworld, String iName, boolean isSneaking, EnumFunction function) {
      super(MCworld, new GameProfile(UUID.fromString("8667ba71-b85a-4004-af54-457a9734eed7"), iName));
      this.follow = null;
      this.LookDistance = 10;
      super.capabilities.isFlying = true;
      this.MyFunction = function;
      this.isSneaking = isSneaking;
      super.noClip = true;
   }

   public void onLivingUpdate() {
      if (this.MyFunction == EnumFunction.FreezeCam && DSGUtils.getInstance().followPlayer) {
         try {
            this.follow = DSGUtils.getMC().player;
            DSGUtils.getMC().player.setSprinting(this.follow.isSprinting());
            DSGUtils.getInstance().faceEntity(this, this.follow, 100.0F, 100.0F);
            double high = DSGUtils.getMC().player.posY - super.posY;
            if (DSGUtils.getMC().player.getDistance(this) >= (float)DSGUtils.getInstance().ParseDistance()) {
               super.moveForward = (float)((double)DSGUtils.getMC().player.capabilities.getWalkSpeed() * (DSGUtils.getMC().player.capabilities.isFlying ? 10.0D : 2.0D) / 4.2D);
               if (high >= 2.0D) {
                  super.motionY = 0.2D;
               }
               if (high <= -2.0D) {
                  super.motionY = -0.2D;
               }
            } else {
               super.motionX = 0.0D;
               super.motionZ = 0.0D;
            }
         } catch (Exception var5) {
            System.out.println("Follow player has been disabled");
            DSGUtils.AddChat("Follow player has been disabled");
            DSGUtils.getInstance().followPlayer = false;
         }
      }
      if (this.MyFunction == EnumFunction.Look && !this.isSleeping()) {
         try {
            if (DSGUtils.getMC().player.getDistance(this) <= (float)this.LookDistance) {
               DSGUtils.getInstance().faceEntity(this, DSGUtils.getMC().player, 160.0F, 160.0F);
            }
         } catch (Exception var4) {
            System.out.println("IM CRASHED WITH FACING A PLAYER !!!");
            DSGUtils.AddChat("One of your statues is despawned!");
            super.dead = true;
         }
      }
      if (this.MyFunction == EnumFunction.Statue) {
         super.cameraPitch = super.rotationPitch;
      }
      super.onLivingUpdate();
   }

   public void Interacted() {
      if (this.MyFunction == EnumFunction.Look || this.MyFunction == EnumFunction.Statue) {
         this.setDead();
      }
   }

   public boolean isSneaking() {
      return this.isSneaking;
   }

   public void setSneaking(boolean flag) {
      this.isSneaking = flag;
   }

   public EnumFunction myFunction() {
      return this.MyFunction;
   }

   public CamEntity cloneMe() {
      return this;
   }

   public boolean isSleeping() {
      return super.sleeping;
   }

   public void setSleeping(boolean flag) {
      super.sleeping = flag;
   }

   public void setLookDistance(int newValue) {
      this.LookDistance = newValue;
   }

   public int getLookDistance() {
      return this.LookDistance;
   }

   public String getUsername() {
      return this.getDisplayName().getFormattedText();
   }

   protected boolean isMovementBlocked() {
      return false;
   }
}
