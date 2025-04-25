package com.DSG.mc.FreezeCam.Utils;

import com.DSG.mc.FreezeCam.CamEntity;
import com.DSG.mc.FreezeCam.EnumFunction;
import com.DSG.mc.FreezeCam.mod_FreezeCam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.opengl.GL11;

public class DSGUtils {
   private static DSGUtils instance = null;
   public boolean firstGui = true;
   private Minecraft mc;
   private CamEntity CamEnt;
   public CamEntity Statue;
   public boolean followPlayer;
   public int followDistance;
   public boolean aimPlayer;
   public EntityRenderer entityRenderer = null;

   public void initUtils(Minecraft minecraft) {
      this.mc = minecraft;
      this.followDistance = 3;
      this.aimPlayer = false;
      this.followPlayer = false;
      this.CamEnt = null;
      instance = this;
      System.out.println("[FreezeCam] Utils Init is completed");
      if (mod_FreezeCam.OverrideFunction) {
         this.entityRenderer = getMC().entityRenderer = new DSGEntityRenderer(getMC());
         System.out.println("[FreezeCam WARNING] EntityRenderer was overriden and can conflit with another mods !!!");
      }
   }

   public static DSGUtils getInstance() {
      return instance;
   }

   public static Minecraft getMC() {
      return getInstance().mc;
   }

   public CamEntity getCamEntity() {
      return this.CamEnt;
   }

   public void faceEntity(Entity Me, Entity lookToMe) {
      this.faceEntity(Me, lookToMe, 150.0F, 150.0F);
   }

   public void faceEntity(Entity me, Entity par1Entity, float par2, float par3) {
      double var4 = par1Entity.posX - me.posX;
      double var6 = par1Entity.posZ - me.posZ;
      double var8;
      if (par1Entity instanceof EntityLivingBase) {
         EntityLivingBase var10 = (EntityLivingBase)par1Entity;
         var8 = var10.posY + (double)var10.getEyeHeight() - (me.posY + (double)me.getEyeHeight());
      } else {
         var8 = (par1Entity.getEntityBoundingBox().minY + par1Entity.getEntityBoundingBox().maxY) / 2.0D - (me.posY + (double)me.getEyeHeight());
      }

      var8 += 0.8D;
      double var14 = MathHelper.sqrt(var4 * var4 + var6 * var6);
      float var12 = (float)(Math.atan2(var6, var4) * 180.0D / Math.PI) - 90.0F;
      float var13 = (float)(-(Math.atan2(var8, var14) * 180.0D / Math.PI));
      me.rotationPitch = updateRotation(me.rotationPitch, var13, par3);
      me.rotationYaw = updateRotation(me.rotationYaw, var12, par2);
      ((CamEntity)me).rotationYawHead = updateRotation(((CamEntity)me).rotationYawHead, var12, par2);
   }

   private static float updateRotation(float par1, float par2, float par3) {
      float var4 = MathHelper.wrapDegrees(par2 - par1);
      if (var4 > par3) {
         var4 = par3;
      }

      if (var4 < -par3) {
         var4 = -par3;
      }

      return par1 + var4;
   }

   public static void AddChat(String msg) {
      getMC().player.sendMessage(new TextComponentString("§6[FreezeCam] §r" + msg));
   }

   public int ParseDistance() {
      return this.followDistance;
   }

   public void RemoveCam() {
      if (this.CamEnt != null && getMC().world != null) {
         this.CamEnt.setHealth(0.0F);
         this.CamEnt.setDead();
         getMC().world.removeEntity(this.CamEnt);
         this.CamEnt = null;
      }
      getMC().setRenderViewEntity(getMC().player);
   }

   public void CreateEntity(EnumFunction function, boolean isSneaking, String Name) {
      if (getMC().player != null && getMC().world != null) {
         CamEntity Ent;
         if (function == EnumFunction.FreezeCam) {
            Ent = this.CamEnt = new CamEntity(getMC().world, Name, isSneaking, function);
         } else {
            Ent = this.Statue = new CamEntity(getMC().world, Name, isSneaking, function);
         }

         Ent.setPosition(getMC().player.posX, getMC().player.posY, getMC().player.posZ);
         getMC().world.spawnEntity(Ent);
         Ent.rotationPitch = getMC().player.rotationPitch;
         Ent.rotationYaw = getMC().player.rotationYaw;
         Ent.rotationYawHead = getMC().player.rotationYawHead;
         if (function == EnumFunction.FreezeCam) {
            getMC().setRenderViewEntity(Ent);
         }
      }
   }

   public void CreateEntity(EnumFunction function, String Name) {
      if (getMC().player != null && getMC().world != null) {
         CamEntity Ent;
         if (function == EnumFunction.FreezeCam) {
            Ent = this.CamEnt = new CamEntity(getMC().world, Name, function);
         } else {
            Ent = this.Statue = new CamEntity(getMC().world, Name, function);
         }

         Ent.setPosition(getMC().player.posX, getMC().player.posY, getMC().player.posZ);
         getMC().world.spawnEntity(Ent);
         Ent.rotationPitch = getMC().player.rotationPitch;
         Ent.rotationYaw = getMC().player.rotationYaw;
         Ent.rotationYawHead = getMC().player.rotationYawHead;
         if (function == EnumFunction.FreezeCam) {
            getMC().setRenderViewEntity(this.CamEnt);
         }
      }
   }

   public static void setViewEntity(EntityLivingBase entity) {
      getMC().setRenderViewEntity(entity);
   }

   public static void DrawEntityPlayer(int par0, int par1, int par2, float par3, float par4, EntityLivingBase par5EntityLivingBase) {
      GL11.glEnable(2903);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par0, (float)par1, 50.0F);
      GL11.glScalef((float)(-par2), (float)par2, (float)par2);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      float var6 = par5EntityLivingBase.renderYawOffset;
      float var7 = par5EntityLivingBase.rotationYaw;
      float var8 = par5EntityLivingBase.rotationPitch;
      float var9 = par5EntityLivingBase.prevRotationYawHead;
      float var10 = par5EntityLivingBase.rotationYawHead;
      GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-((float)Math.atan((par4 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
      par5EntityLivingBase.renderYawOffset = (float)Math.atan((par3 / 40.0F)) * 20.0F;
      par5EntityLivingBase.rotationYaw = (float)Math.atan((par3 / 40.0F)) * 40.0F;
      par5EntityLivingBase.rotationPitch = -((float)Math.atan((par4 / 40.0F))) * 20.0F;
      par5EntityLivingBase.rotationYawHead = par5EntityLivingBase.rotationYaw;
      par5EntityLivingBase.prevRotationYawHead = par5EntityLivingBase.rotationYaw;
      GL11.glTranslatef(0.0F, (float) par5EntityLivingBase.getYOffset(), 0.0F);
      getMC().getRenderManager().playerViewY = 180.0F;
      getMC().getRenderManager().renderEntity(par5EntityLivingBase, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
      par5EntityLivingBase.renderYawOffset = var6;
      par5EntityLivingBase.rotationYaw = var7;
      par5EntityLivingBase.rotationPitch = var8;
      par5EntityLivingBase.prevRotationYawHead = var9;
      par5EntityLivingBase.rotationYawHead = var10;
      GL11.glPopMatrix();
      RenderHelper.disableStandardItemLighting();
      GL11.glDisable(32826);
      OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glDisable(3553);
      OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   public void DrawLabel(String s, int X, int Y) {
      int i3 = getMC().fontRenderer.getStringWidth(s);
      int j3 = 8;
      int k3 = -267386864;
      this.drawGradientRect(X - 3, Y - 4, X + i3 + 3, Y - 3, k3, k3);
      this.drawGradientRect(X - 3, Y + j3 + 3, X + i3 + 3, Y + j3 + 4, k3, k3);
      this.drawGradientRect(X - 3, Y - 3, X + i3 + 3, Y + j3 + 3, k3, k3);
      this.drawGradientRect(X - 4, Y - 3, X - 3, Y + j3 + 3, k3, k3);
      this.drawGradientRect(X + i3 + 3, Y - 3, X + i3 + 4, Y + j3 + 3, k3, k3);
      int l3 = 1347420415;
      int i4 = (l3 & 16711422) >> 1 | l3 & -16777216;
      this.drawGradientRect(X - 3, Y - 3 + 1, X - 3 + 1, Y + j3 + 3 - 1, l3, i4);
      this.drawGradientRect(X + i3 + 2, Y - 3 + 1, X + i3 + 3, Y + j3 + 3 - 1, l3, i4);
      this.drawGradientRect(X - 3, Y - 3, X + i3 + 3, Y - 3 + 1, l3, l3);
      this.drawGradientRect(X - 3, Y + j3 + 2, X + i3 + 3, Y + j3 + 3, i4, i4);
      getMC().fontRenderer.drawStringWithShadow(s, X, Y, -1);
   }

   public void drawGradientRect(int x, int y, int x2, int y2, int col1, int col2) {
      float f = (float)(col1 >> 24 & 255) / 255.0F;
      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
      float f3 = (float)(col1 & 255) / 255.0F;
      float f4 = (float)(col2 >> 24 & 255) / 255.0F;
      float f5 = (float)(col2 >> 16 & 255) / 255.0F;
      float f6 = (float)(col2 >> 8 & 255) / 255.0F;
      float f7 = (float)(col2 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glPushMatrix();
      GL11.glBegin(7);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glVertex2d(x2, y);
      GL11.glVertex2d(x, y);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d(x, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glShadeModel(7424);
   }
}
