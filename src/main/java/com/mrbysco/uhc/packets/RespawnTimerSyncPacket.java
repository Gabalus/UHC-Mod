package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.client.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RespawnTimerSyncPacket {
    private final int respawnTime;
    private final String teamName;

    public RespawnTimerSyncPacket(int respawnTime, String teamName) {
        this.respawnTime = respawnTime;
        this.teamName = teamName;
    }

    public static void encode(RespawnTimerSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.respawnTime);
        buf.writeUtf(msg.teamName);
    }

    public static RespawnTimerSyncPacket decode(FriendlyByteBuf buf) {
        return new RespawnTimerSyncPacket(buf.readInt(), buf.readUtf());
    }

    public static void handle(RespawnTimerSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientHandler.setRespawnTime(msg.respawnTime, msg.teamName);
        });
        ctx.get().setPacketHandled(true);
    }
}
