package com.DSG.mc.FreezeCam.Utils;

import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class DSGEntityRenderer extends EntityRenderer {
   public Entity pointedEntity;
   public Minecraft mc;

   public DSGEntityRenderer(Minecraft par1Minecraft) {
      super(par1Minecraft, par1Minecraft.getResourceManager());
      this.mc = par1Minecraft;
   }

   @Override
   public void getMouseOver(float p_78473_1_) {
      if (this.mc.getRenderViewEntity() != null && this.mc.world != null) {
         this.mc.mcProfiler.startSection("pick");
         this.mc.pointedEntity = null;
         double d0 = this.mc.playerController.getBlockReachDistance();
         this.mc.objectMouseOver = this.mc.player.rayTrace(d0, p_78473_1_);
         Vec3d vec3d = this.mc.player.getPositionEyes(p_78473_1_);
         boolean flag = false;
         double d1 = d0;
         if (this.mc.playerController.extendedReach()) {
            d1 = 6.0D;
            d0 = d1;
         } else if (d0 > 3.0D) {
            flag = true;
         }
         if (this.mc.objectMouseOver != null)
            d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3d);
         Vec3d vec3d1 = this.mc.player.getLook(1.0F);
         Vec3d vec3d2 = vec3d.addVector(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
         this.pointedEntity = null;
         Vec3d vec3d3 = null;
         List<Entity> list = this.mc.world.getEntitiesInAABBexcluding(this.mc.player, this.mc.player.getEntityBoundingBox().expand(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, p_apply_1_ -> (p_apply_1_ != null && p_apply_1_.canBeCollidedWith())));
         double d2 = d1;
         for (int j = 0; j < list.size(); j++) {
            Entity entity1 = list.get(j);
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(entity1.getCollisionBorderSize());
            RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);
            if (axisalignedbb.contains(vec3d)) {
               if (d2 >= 0.0D) {
                  this.pointedEntity = entity1;
                  vec3d3 = (raytraceresult == null) ? vec3d : raytraceresult.hitVec;
                  d2 = 0.0D;
               }
            } else if (raytraceresult != null) {
               double d3 = vec3d.distanceTo(raytraceresult.hitVec);
               if (d3 < d2 || d2 == 0.0D)
                  if (entity1.getLowestRidingEntity() == this.mc.player.getLowestRidingEntity() && !entity1.canRiderInteract()) {
                     if (d2 == 0.0D) {
                        this.pointedEntity = entity1;
                        vec3d3 = raytraceresult.hitVec;
                     }
                  } else {
                     this.pointedEntity = entity1;
                     vec3d3 = raytraceresult.hitVec;
                     d2 = d3;
                  }
            }
         }
         if (this.pointedEntity != null && flag) {
            assert vec3d3 != null;
            if (vec3d.distanceTo(vec3d3) > 3.0D) {
               this.pointedEntity = null;
               this.mc.objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, null, new BlockPos(vec3d3));
            }
         }
         if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
            assert vec3d3 != null;
            this.mc.objectMouseOver = new RayTraceResult(this.pointedEntity, vec3d3);
            if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame)
               this.mc.pointedEntity = this.pointedEntity;
         }
         this.mc.mcProfiler.endSection();
      }
   }
}
