package com.DSG.mc.FreezeCam.Utils;

import com.DSG.mc.FreezeCam.CamEntity;
import com.DSG.mc.FreezeCam.DSG_iKeyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FreezeCamHandler {
    public Entity pointedEntity;
    public static Minecraft mc = Minecraft.getMinecraft();
    public boolean DropKey = false;

    @SubscribeEvent
    public void renderPlayerPre(RenderPlayerEvent.Pre event) {
        if (!DSG_iKeyHandler.controlEnabled) return;
        pointedEntity = mc.getRenderManager().renderViewEntity;
        mc.getRenderManager().renderViewEntity = mc.player;
    }

    @SubscribeEvent
    public void renderPlayerPost(RenderPlayerEvent.Post event) {
        if (!DSG_iKeyHandler.controlEnabled) return;
        mc.getRenderManager().renderViewEntity = pointedEntity;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END || mc.world == null || mc.player == null) return;

        if (mc.getRenderViewEntity() instanceof CamEntity) {
            EntityPlayerSP player = mc.player;

            MovementInput input = player.movementInput;
            player.moveStrafing = input.moveStrafe;
            player.moveForward = input.moveForward;
            player.setJumping(input.jump);
            player.setSneaking(input.sneak);

            if (player.capabilities.isFlying) {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    player.motionY += 0.1D;
                }
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    player.motionY -= 0.1D;
                }
            }

            mc.player.connection.sendPacket(new CPacketPlayer.Position(
                    player.posX, player.getEntityBoundingBox().minY, player.posZ, player.onGround
            ));

            if (mc.gameSettings.keyBindDrop.isKeyDown()) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(
                        player.rotationYawHead, player.rotationPitch, player.onGround
                ));
                DropKey = true;
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(
                        mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround
                ));
                DropKey = true;
            }

            if (mc.gameSettings.keyBindAttack.isKeyDown() && mc.objectMouseOver != null) {
                mc.playerController.onPlayerDamageBlock(mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit);
            }

            if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                EnumHand hand = player.getActiveHand() != null ? player.getActiveHand() : EnumHand.MAIN_HAND;
                mc.playerController.processRightClick(player, player.world, hand);
            }
        }
    }
}
