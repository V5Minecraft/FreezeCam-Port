package com.DSG.mc.FreezeCam;

import com.DSG.mc.FreezeCam.Utils.DSGUtils;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class DSG_iKeyHandler {
   static KeyBinding Freeze = new KeyBinding("FreezeCam", 25, "FreezeCam");
   static KeyBinding Gui = new KeyBinding("FreezeCam GUI", 34, "FreezeCam");
   static KeyBinding[] keyBindings;
   static boolean[] repeat;
   public static boolean controlEnabled = false;

   public DSG_iKeyHandler() {
      ClientRegistry.registerKeyBinding(Freeze);
      ClientRegistry.registerKeyBinding(Gui);
   }

   @SubscribeEvent
   public void tick(InputEvent.KeyInputEvent event) {
      if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
         if (Freeze.isPressed()) {
            if (DSGUtils.getInstance().getCamEntity() == null) {
               DSGUtils.getInstance().CreateEntity(EnumFunction.FreezeCam, "FreezeCam");
               controlEnabled = !controlEnabled;
            } else {
               DSGUtils.getInstance().RemoveCam();
               controlEnabled = false;
            }
         }

         if (Gui.isPressed()) {
            FMLClientHandler.instance().displayGuiScreen(FMLClientHandler.instance().getClient().player, new CamGui());
         }
      }
   }

   static {
      keyBindings = new KeyBinding[]{Freeze, Gui};
      repeat = new boolean[]{false, false, false};
   }
}
