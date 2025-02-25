package com.mrbysco.uhc.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import com.mrbysco.uhc.client.ClientHandler;

import java.util.function.Supplier;

public class ShrinkTimeSyncPacket {
    private final int shrinkTime;

    public ShrinkTimeSyncPacket(int shrinkTime) {
        this.shrinkTime = shrinkTime;
    }

    public static void encode(ShrinkTimeSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.shrinkTime);
    }

    public static ShrinkTimeSyncPacket decode(FriendlyByteBuf buf) {
        return new ShrinkTimeSyncPacket(buf.readInt());
    }

    public static void handle(ShrinkTimeSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientHandler.setShrinkTime(msg.shrinkTime);
        });
        ctx.get().setPacketHandled(true);
    }
}
