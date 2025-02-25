package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.client.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RespawnTimerSyncPacket {
    private final int respawnTime;

    public RespawnTimerSyncPacket(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    public static void encode(RespawnTimerSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.respawnTime);
    }

    public static RespawnTimerSyncPacket decode(FriendlyByteBuf buf) {
        return new RespawnTimerSyncPacket(buf.readInt());
    }

    public static void handle(RespawnTimerSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientHandler.setRespawnTime(msg.respawnTime);
        });
        ctx.get().setPacketHandled(true);
    }
}
