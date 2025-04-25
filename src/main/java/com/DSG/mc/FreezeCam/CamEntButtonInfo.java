package com.DSG.mc.FreezeCam;

import com.DSG.mc.FreezeCam.Utils.DSGUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class CamEntButtonInfo {
   public int xPos;
   public int yPos;
   public String title;
   public int width;
   public int height;
   public CamEntity cam;
   public CamGui iScreen;

   public CamEntButtonInfo(CamGui screen, CamEntity iCam, int xPos, int yPos) {
      this.iScreen = screen;
      this.title = iCam.getDisplayName().getFormattedText();
      this.xPos = xPos;
      this.yPos = yPos;
      this.width = 120;
      this.height = 12;
      this.cam = iCam;
   }

   public CamEntButtonInfo(CamGui screen, String name, int xPos, int yPos) {
      this.iScreen = screen;
      this.title = name;
      this.xPos = xPos;
      this.yPos = yPos;
      this.width = 120;
      this.height = 12;
      this.cam = null;
   }

   public void draw() {
      if (this.width <= DSGUtils.getMC().fontRenderer.getStringWidth(this.getTitle())) {
         drawBorderedRect(this.xPos, this.yPos - 1, this.xPos + DSGUtils.getMC().fontRenderer.getStringWidth(this.getTitle()) + 5, this.yPos + this.height + 1, 1, -1862270977, 1610612736);
      } else {
         drawBorderedRect(this.xPos, this.yPos - 1, this.xPos + this.width, this.yPos + this.height + 1, 1, -1862270977, 1610612736);
      }

      int var10002 = this.xPos + 3;
      int var10003 = this.yPos + 2;
      DSGUtils.getMC().fontRenderer.drawString(this.title, var10002, var10003, -16742145);
   }

   public void mouseClicked(int x, int y, int button) {
      if (this.clickedInside(x, y)) {
         this.iScreen.isChanging = true;
         DSGUtils.getInstance();
         DSGUtils.getMC().displayGuiScreen(new CamGui(this.cam.cloneMe()));
      }

   }

   public boolean clickedInside(int x, int y) {
      return x > this.xPos && y > this.yPos && x < this.xPos + this.width && y < this.yPos + this.height;
   }

   public String getTitle() {
      return this.title;
   }

   public CamGui getGui() {
      return this.iScreen;
   }

   public static void drawBorderedRect(int x, int y, int x1, int y1, int size, int borderC, int insideC) {
      drawRect(x + size, y + size, x1 - size + 1, y1 - size, insideC);
      drawRect(x + size, y + size, x1 + 1, y, borderC);
      drawRect(x, y, x + size, y1 - 1, borderC);
      drawRect(x1, y1, x1 + size, y + size, borderC);
      drawRect(x, y1 - size, x1, y1, borderC);
   }

   public static void drawRect(int par0, int par1, int par2, int par3, int par4) {
      int var5;
      if (par0 < par2) {
         var5 = par0;
         par0 = par2;
         par2 = var5;
      }

      if (par1 < par3) {
         var5 = par1;
         par1 = par3;
         par3 = var5;
      }

      float var10 = (float)(par4 >> 24 & 255) / 255.0F;
      float var6 = (float)(par4 >> 16 & 255) / 255.0F;
      float var7 = (float)(par4 >> 8 & 255) / 255.0F;
      float var8 = (float)(par4 & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.color(var6, var7, var8, var10);
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
      bufferbuilder.pos(par0, par3, 0.0D).endVertex();
      bufferbuilder.pos(par2, par3, 0.0D).endVertex();
      bufferbuilder.pos(par2, par1, 0.0D).endVertex();
      bufferbuilder.pos(par0, par1, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }
}
