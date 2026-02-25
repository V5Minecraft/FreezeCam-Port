package com.DSG.mc.FreezeCam.UpdateManager;

import com.DSG.mc.FreezeCam.Utils.DSGUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class UpdateChecker implements Runnable {
   public static String a;

   public static String DownloadString(String link) {
      try {
         URL url = new URL(link);
         BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
         a = null;
         return (a = reader.readLine()) != null ? a : "***ERROR***";
      } catch (Exception var3) {
         return "***ERROR***";
      }
   }

   public static void CheckUpdate(EntityPlayer player) {
      try {
         String UpdateMessage = DownloadString("https://dl.dropboxusercontent.com/s/fst1a5slcqiaxij/UpdateFile.txt");
         if (UpdateMessage == "***ERROR***") {
            System.out.println("[FreezeCam] UpdateManager failed to check for updates.");
            return;
         }

         String[] Spliter = UpdateMessage.split("#");
         String Version = Spliter[0];
         String ImportantUpdate = Spliter[1];
         String MCVersion = Spliter[2];
         String[] Message = Spliter[3].split("@");
         if (!Version.matches("4.1")) {
            if (ImportantUpdate.matches("0")) {
               sendChat(player, "[FreezeCam] New Update found, version " + Version + " for mc " + MCVersion);
            } else if (ImportantUpdate.matches("1")) {
               sendChat(player, "[FreezeCam] IMPORTANT Update found, version " + Version + " for mc " + MCVersion);
            } else if (ImportantUpdate.matches("2")) {
               sendChat(player, "[FreezeCam] Information found for version " + Version);
            } else {
               sendChat(player, "[FreezeCam] could not find the Update sort message !!!");
               sendChat(player, "[FreezeCam] Please report this to the mod owner with this screen.");
            }

            for(int x = 0; x < Message.length; ++x) {
               sendChat(player, "-" + Message[x]);
            }
         } else {
            sendChat(player, "[FreezeCam] No updates founded.");
         }
      } catch (Exception var8) {
         sendChat(player, "[FreezeCam] Something has failed while receiving the updatefile");
      }

   }

   public void run() {
      CheckUpdate(DSGUtils.getMC().player);
   }

   public static void sendChat(EntityPlayer player, String msg) {
      ITextComponent Chat = new TextComponentString(msg);
      player.sendMessage(Chat);
   }
}
