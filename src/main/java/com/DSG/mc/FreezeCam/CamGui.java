package com.DSG.mc.FreezeCam;

import com.DSG.mc.FreezeCam.UpdateManager.UpdateChecker;
import com.DSG.mc.FreezeCam.Utils.DSGUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Mouse;

public class CamGui extends GuiScreen {
   protected boolean isChanging = false;
   private GuiTextField SPentity;
   private boolean isSneaking;
   public GuiButton startFollow;
   public GuiButton stopFollow;
   public GuiButton ChangeDistancePlus;
   public GuiButton ChangeDistanceMin;
   public CamEntity customCam;
   private int ArrowCount = 0;
   private int BounceLabel = 0;
   private boolean isBack = true;
   private static final ArrayList<CamEntButtonInfo> buttons = new ArrayList<>();

   public CamGui() {
      this.customCam = DSGUtils.getInstance().getCamEntity();
      this.initGui();
   }

   public CamGui(CamEntity newControl) {
      this.customCam = newControl.cloneMe();
      this.initGui();
   }

   public void initGui() {
      if (this.customCam != null) {
         this.ArrowCount = this.customCam.getArrowCountInEntity();
      }

      this.SPentity = new GuiTextField(0, this.fontRenderer, this.width / 2 - 75, 200, 150, 20);
      this.SPentity.setFocused(true);
   }

   public void updateScreen() {
      this.SPentity.updateCursorCounter();
   }

   public void drawScreen(int i, int j, float f) {
      if (this.customCam != null) {
         DSGUtils.DrawEntityPlayer(super.width - 50, 75, 30, (float)(super.width - 50 - i), (float)(35 - j), this.customCam);
      }

      super.buttonList.clear();
      super.buttonList.add(new GuiButton(3, 0, super.height - 20, 100, 20, "Set view"));
      super.buttonList.add(new GuiButton(50, super.width / 2 - 70, 40, 20, 20, "^"));
      super.buttonList.add(new GuiButton(51, super.width / 2 - 90, 60, 20, 20, "<"));
      super.buttonList.add(new GuiButton(52, super.width / 2 - 50, 60, 20, 20, ">"));
      super.buttonList.add(new GuiButton(53, super.width / 2 - 70, 80, 20, 20, "v"));
      super.buttonList.add(new GuiButton(54, super.width / 2 - 70, 60, 20, 20, "stop"));
      this.drawCenteredString(super.mc.fontRenderer, "Move", super.width / 2 - 60, 30, -1);
      super.buttonList.add(new GuiButton(55, super.width / 2 + 30, 60, 20, 20, "<<"));
      super.buttonList.add(new GuiButton(56, super.width / 2 + 70, 60, 20, 20, ">>"));
      super.buttonList.add(new GuiButton(57, super.width / 2 + 50, 40, 20, 20, "^"));
      super.buttonList.add(new GuiButton(58, super.width / 2 + 50, 80, 20, 20, "v"));
      this.drawCenteredString(super.mc.fontRenderer, "Look direction", super.width / 2 + 60, 30, -1);
      super.buttonList.add(new GuiButton(59, super.width / 2 - 15, 50, 30, 20, "Up"));
      super.buttonList.add(new GuiButton(60, super.width / 2 - 15, 70, 30, 20, "Down"));
      super.buttonList.add(new GuiButton(505, super.width - 100, super.height - 20, 100, 20, "Check for update"));
      super.buttonList.add(new GuiButton(5, super.width / 2 - 100, 110, 200, 20, this.customCam == DSGUtils.getInstance().getCamEntity() ? "Camera look to you" : this.customCam.getUsername() + "'s statue look to you"));
      if (DSGUtils.getInstance().firstGui) {
         if (this.BounceLabel >= 15) {
            this.isBack = false;
         } else if (this.BounceLabel <= 0) {
            this.isBack = true;
         }

         if (this.isBack) {
            ++this.BounceLabel;
         } else {
            --this.BounceLabel;
         }

         this.drawString(super.fontRenderer, "<< If you add a statue or the freecam is active click here to change some options from it", 140 + this.BounceLabel, 5, -1);
      }

      if (this.customCam == DSGUtils.getInstance().getCamEntity()) {
         super.buttonList.add(this.startFollow = new GuiButton(1, super.width / 2 - 100, 140, 150, 20, "Follow me"));
         super.buttonList.add(this.stopFollow = new GuiButton(2, super.width / 2 - 100, 160, 150, 20, "Stop follow me"));
         this.startFollow.enabled = !DSGUtils.getInstance().followPlayer & !DSGUtils.getInstance().aimPlayer;
         this.stopFollow.enabled = DSGUtils.getInstance().followPlayer & !DSGUtils.getInstance().aimPlayer;
         this.drawString(super.fontRenderer, "Small bonus", super.width / 2 - 150, 180, -1);
         this.drawHorizontalLine(super.width / 2 - 150, super.width / 2 + 150, 190, -1);
         super.buttonList.add(new GuiButton(8, super.width / 2 + 80, 200, 50, 20, this.isSneaking ? "Sneaking" : "Standing"));
         this.drawString(super.mc.fontRenderer, "Player Name", super.width / 2 - 145, 206, -1);
         this.SPentity.drawTextBox();
         super.buttonList.add(new GuiButton(6, super.width / 2 - 75, 225, 75, 20, "Statue"));
         super.buttonList.add(new GuiButton(7, super.width / 2, 225, 75, 20, "Look at me"));
         super.buttonList.add(this.ChangeDistancePlus = new GuiButton(66, super.width / 2 + 80, 140, 20, 10, "-"));
         super.buttonList.add(this.ChangeDistanceMin = new GuiButton(67, super.width / 2 + 110, 140, 20, 10, "+"));
         this.ChangeDistanceMin.enabled = this.stopFollow.enabled;
         this.ChangeDistancePlus.enabled = this.stopFollow.enabled;
         this.drawString(DSGUtils.getMC().fontRenderer, "Follow distance: " + DSGUtils.getInstance().ParseDistance(), super.width / 2 + 60, 155, -1);
      } else {
         super.buttonList.add(new GuiButton(8, super.width / 2 - 100, 140, 50, 20, this.customCam.isSneaking() ? "Sneaking" : "Standing"));
         super.buttonList.add(new GuiButton(61, super.width / 2 - 50, 140, 50, 20, this.customCam.isSleeping() ? "Sleeping" : "Awake"));
         super.buttonList.add(new GuiButton(62, super.width / 2 - 100, 190, 100, 20, "Remove me"));
         super.buttonList.add(new GuiButton(63, super.width / 2 - 100, 160, 100, 20, "Clone Inventory"));
         super.buttonList.add(new GuiButton(64, super.width / 2 + 10, 150, 20, 10, "-"));
         super.buttonList.add(new GuiButton(65, super.width / 2 + 10, 140, 20, 10, "+"));
         this.drawString(DSGUtils.getMC().fontRenderer, "Arrows: " + this.ArrowCount, super.width / 2 + 35, 147, -1);
         if (this.customCam.myFunction() == EnumFunction.Look) {
            super.buttonList.add(new GuiButton(68, super.width / 2 + 10, 175, 20, 10, "-"));
            super.buttonList.add(new GuiButton(69, super.width / 2 + 10, 165, 20, 10, "+"));
            this.drawString(DSGUtils.getMC().fontRenderer, "Look distance: " + this.customCam.getLookDistance(), super.width / 2 + 35, 172, -1);
         }

         super.buttonList.add(new GuiButton(1000, super.width / 2 + 10, 180, 100, 20, "test"));
      }

      if (mod_FreezeCam.OverrideScreenFunction) {
         int X = Mouse.getEventX() * super.width / DSGUtils.getMC().displayWidth;
         int Y = super.height - Mouse.getEventY() * super.height / DSGUtils.getMC().displayHeight - 1;
         String ErrorMessage = "ScreenEvent function couldnt enabled because of MC updates";
         int MSGWidth = DSGUtils.getMC().fontRenderer.getStringWidth(ErrorMessage);
         if (X > super.width / 2 - MSGWidth / 2 - 5 && Y > 10 && X < super.width / 2 + MSGWidth / 2 + 5 && Y < 25) {
            ErrorMessage = "To disable this message, disable the ScreenOverride function in the config file";
            MSGWidth = DSGUtils.getMC().fontRenderer.getStringWidth(ErrorMessage);
         }

         CamEntButtonInfo.drawBorderedRect(super.width / 2 - MSGWidth / 2 - 5, 10, super.width / 2 + MSGWidth / 2 + 5, 25, 1, -1862336512, 1627324416);
         this.drawString(DSGUtils.getMC().fontRenderer, ErrorMessage, super.width / 2 - MSGWidth / 2, 14, -65536);
      }

      this.CreateCamButtons();
      super.drawScreen(i, j, f);
   }

   public void CreateCamButtons() {
      int count = 0;
      buttons.clear();
      List<Entity> e = DSGUtils.getMC().world.getLoadedEntityList();

      int aa;
      for(aa = 0; aa < e.size(); ++aa) {
         if (e.get(aa) instanceof CamEntity) {
            CamEntity cam = (CamEntity)e.get(aa);
            if (!cam.isDead) {
               CamEntButtonInfo nButton = new CamEntButtonInfo(this, cam, 3, 3 + 15 * count);
               buttons.add(nButton);
               ++count;
            }
         }
      }

      if (DSGUtils.getInstance().firstGui) {
         CamEntButtonInfo fakebutton = new CamEntButtonInfo(this, "Example (click to remove)", 3, 3 + 15 * count) {
            public void mouseClicked(int x, int y, int button) {
               if (this.clickedInside(x, y)) {
                  DSGUtils.getInstance().firstGui = false;
               }

            }

            public void draw() {
               super.draw();
               int X = Mouse.getEventX() * this.getGui().width / DSGUtils.getMC().displayWidth;
               int Y = this.getGui().height - Mouse.getEventY() * this.getGui().height / DSGUtils.getMC().displayHeight - 1;
               if (super.clickedInside(X, Y)) {
                  DSGUtils.getInstance().DrawLabel("If you can see you can click this like a button", X + 12, Y + 12);
               }

            }
         };
         buttons.add(fakebutton);
      }

      for(aa = 0; aa < buttons.size(); ++aa) {
         buttons.get(aa).draw();
      }
   }

   protected void actionPerformed(GuiButton par1GuiButton) {
      try {
         if (par1GuiButton.id == 6) {
            DSGUtils.getInstance().CreateEntity(EnumFunction.Statue, this.isSneaking, this.SPentity.getText().trim().isEmpty() ? "Statue" : this.SPentity.getText());
         }

         if (par1GuiButton.id == 7) {
            DSGUtils.getInstance().CreateEntity(EnumFunction.Look, this.isSneaking, this.SPentity.getText().trim().isEmpty() ? "I love you <3" : this.SPentity.getText());
         }

         if (par1GuiButton.id == 8) {
            this.isSneaking = !this.isSneaking;
            if (this.customCam != DSGUtils.getInstance().getCamEntity()) {
               this.customCam.setSneaking(!this.customCam.isSneaking());
               this.isSneaking = this.customCam.isSneaking();
            }
         }

         if (par1GuiButton.id == 1) {
            try {
               DSGUtils.getInstance().followPlayer = true;
            } catch (Exception var3) {
               System.out.println("IM CRASHED WITH SEARCHING A PLAYER !!!");
               DSGUtils.AddChat("Failed to find you");
            }
         }

         if (par1GuiButton.id == 2) {
            DSGUtils.getInstance().followPlayer = false;
         }

         if (par1GuiButton.id == 3) {
            DSGUtils.setViewEntity(this.customCam);
         }

         if (par1GuiButton.id == 5) {
            DSGUtils.getInstance().faceEntity(this.customCam, super.mc.player);
         }

         if (par1GuiButton.id == 50) {
            this.customCam.moveForward = 0.2F;
         }

         if (par1GuiButton.id == 51) {
            this.customCam.moveStrafing = 0.2F;
         }

         if (par1GuiButton.id == 52) {
            this.customCam.moveStrafing = -0.2F;
         }

         if (par1GuiButton.id == 53) {
            this.customCam.moveForward = -0.2F;
         }

         if (par1GuiButton.id == 54) {
            this.customCam.motionX = 0.0D;
            this.customCam.motionY = 0.0D;
            this.customCam.motionZ = 0.0D;
            this.customCam.moveForward = 0.0F;
            this.customCam.moveStrafing = 0.0F;
         }

         if (par1GuiButton.id == 55) {
            this.customCam.rotationYaw -= 5.0F;
            this.customCam.rotationYawHead -= 5.0F;
         }

         if (par1GuiButton.id == 56) {
            this.customCam.rotationYaw += 5.0F;
            this.customCam.rotationYawHead += 5.0F;
         }

         if (par1GuiButton.id == 57) {
            this.customCam.rotationPitch -= 5.0F;
         }

         if (par1GuiButton.id == 58) {
            this.customCam.rotationPitch += 5.0F;
         }

         if (par1GuiButton.id == 59) {
            this.customCam.motionY += 0.1D;
         }

         if (par1GuiButton.id == 60) {
            this.customCam.motionY -= 0.1D;
         }

         if (par1GuiButton.id == 61) {
            this.customCam.setSleeping(!this.customCam.isSleeping());
         }

         if (par1GuiButton.id == 62) {
            this.customCam.setDead();
            DSGUtils.getMC().displayGuiScreen(new CamGui());
         }

         if (par1GuiButton.id == 63) {
            this.customCam.inventory.copyInventory(DSGUtils.getMC().player.inventory);
         }

         if (par1GuiButton.id == 64) {
            if (this.ArrowCount >= 1) {
               --this.ArrowCount;
            }

            this.customCam.setArrowCountInEntity(this.ArrowCount);
         }

         if (par1GuiButton.id == 65) {
            if (this.ArrowCount <= 124) {
               ++this.ArrowCount;
            }

            this.customCam.setArrowCountInEntity(this.ArrowCount);
         }

         if (par1GuiButton.id == 66 && DSGUtils.getInstance().followDistance >= 1) {
            --DSGUtils.getInstance().followDistance;
         }

         if (par1GuiButton.id == 67) {
            ++DSGUtils.getInstance().followDistance;
         }

         if (par1GuiButton.id == 68) {
            this.customCam.setLookDistance(this.customCam.getLookDistance() - 1);
         }

         if (par1GuiButton.id == 69) {
            this.customCam.setLookDistance(this.customCam.getLookDistance() + 1);
         }

         if (par1GuiButton.id == 505) {
            (new Thread(new UpdateChecker())).start();
         }
      } catch (Exception var4) {
          throw new RuntimeException(var4);
      }
   }

   public void onGuiClosed() {
      DSGUtils.getInstance().firstGui = false;
   }

   protected void keyTyped(char par1, int par2) {
      this.SPentity.textboxKeyTyped(par1, par2);
      if (par2 == 1) {
         if (DSGUtils.getInstance().getCamEntity() == null) {
            DSGUtils.setViewEntity(DSGUtils.getMC().player);
         } else {
            DSGUtils.setViewEntity(DSGUtils.getInstance().getCamEntity());
         }

         super.mc.displayGuiScreen(null);
      }

   }

   protected void mouseClicked(int par1, int par2, int par3) {
      try {
         super.mouseClicked(par1, par2, par3);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      this.SPentity.mouseClicked(par1, par2, par3);

      for(int aa = 0; aa < buttons.size(); ++aa) {
         buttons.get(aa).mouseClicked(par1, par2, par3);
      }

   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
