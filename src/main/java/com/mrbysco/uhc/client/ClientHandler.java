package com.mrbysco.uhc.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientHandler {

    private static String currentTime = "";
    private static String shrinkTime = "00:00";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static int shrinkTimeInSeconds = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            currentTime = LocalTime.now().format(TIME_FORMAT);

            int minutes = shrinkTimeInSeconds / 60;
            int seconds = shrinkTimeInSeconds % 60;
            shrinkTime = String.format("%02d:%02d", seconds, minutes);
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        RenderSystem.enableBlend();

        guiGraphics.drawString(minecraft.font, "Сжатие через: " + shrinkTime, 10, 25, 0xFF5555);

        RenderSystem.disableBlend();
    }

    public static void setShrinkTime(int shrinkTimeSeconds) {
        shrinkTimeInSeconds = shrinkTimeSeconds;
    }
}
