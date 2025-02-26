package com.mrbysco.uhc.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.format.DateTimeFormatter;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientHandler {

    private static String shrinkTime = "00:00";
    private static String respawnTime = "00";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static int respawnTimeInSeconds = 0;
    private static int shrinkTimeInSeconds = 0;
    private static String playerTeam = "";

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {

            int minutes = shrinkTimeInSeconds / 60;
            int seconds = shrinkTimeInSeconds % 60;
            int secondsRespawn = (respawnTimeInSeconds % 1200) / 20;
            shrinkTime = String.format("%02d:%02d", seconds, minutes);
            respawnTime = String.format("%02d", secondsRespawn);
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        RenderSystem.enableBlend();

        guiGraphics.drawString(minecraft.font, "Время игры: " + shrinkTime, 10, 25, 0xFF5555);

        if (playerTeam.equals("spectator")) {
            guiGraphics.drawString(minecraft.font, "Респаун через: " + respawnTime, 10, 40, 0x00FF00);
        }

        RenderSystem.disableBlend();
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        respawnTimeInSeconds = 0;
        playerTeam = "";
    }

    public static void setShrinkTime(int shrinkTimeSeconds) {
        shrinkTimeInSeconds = shrinkTimeSeconds;
    }

    public static void setRespawnTime(int respawnTimeSeconds, String team) {
        respawnTimeInSeconds = respawnTimeSeconds;
        playerTeam = team;
    }
}
