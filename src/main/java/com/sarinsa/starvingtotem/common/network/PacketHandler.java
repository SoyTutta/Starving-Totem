package com.sarinsa.starvingtotem.common.network;

import com.sarinsa.starvingtotem.common.core.StarvingTotem;
import com.sarinsa.starvingtotem.common.network.message.S2CUpdateAltarOrientation;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler {

    private static final String PROTOCOL_NAME = "STARVINGTOTEM";
    private static int messageIndex;

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(StarvingTotem.resourceLoc("channel"))
            .serverAcceptedVersions(PROTOCOL_NAME::equals)
            .clientAcceptedVersions(PROTOCOL_NAME::equals)
            .networkProtocolVersion(() -> PROTOCOL_NAME)
            .simpleChannel();

    public static void registerMessages() {
        registerMessage(S2CUpdateAltarOrientation.class, S2CUpdateAltarOrientation::encode, S2CUpdateAltarOrientation::decode, S2CUpdateAltarOrientation::handle);
    }

    private static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        CHANNEL.registerMessage(messageIndex++, messageType, encoder, decoder, messageConsumer, Optional.empty());
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayerEntity player) {
        CHANNEL.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
