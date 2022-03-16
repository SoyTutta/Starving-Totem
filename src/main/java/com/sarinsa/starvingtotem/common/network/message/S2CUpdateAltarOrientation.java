package com.sarinsa.starvingtotem.common.network.message;

import com.sarinsa.starvingtotem.common.network.work.ClientWork;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CUpdateAltarOrientation {

    public final float rotation;
    public final int entityId;

    public S2CUpdateAltarOrientation(int entityId, float rotation) {
        this.entityId = entityId;
        this.rotation = rotation;
    }

    public static void handle(S2CUpdateAltarOrientation message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> ClientWork.handleAltarRotationUpdate(message));
        }
        context.setPacketHandled(true);
    }

    public static S2CUpdateAltarOrientation decode(PacketBuffer buffer) {
        return new S2CUpdateAltarOrientation(buffer.readInt(), buffer.readFloat());
    }

    public static void encode(S2CUpdateAltarOrientation message, PacketBuffer buffer) {
        buffer.writeInt(message.entityId);
        buffer.writeFloat(message.rotation);
    }
}
