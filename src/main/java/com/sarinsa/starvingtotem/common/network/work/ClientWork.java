package com.sarinsa.starvingtotem.common.network.work;

import com.sarinsa.starvingtotem.common.entity.FamilyAltarEntity;
import com.sarinsa.starvingtotem.common.network.message.S2CUpdateAltarOrientation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

public class ClientWork {

    public static void handleAltarRotationUpdate(S2CUpdateAltarOrientation message) {
        ClientWorld world = Minecraft.getInstance().level;

        if (world == null)
            return;

        Entity entity = world.getEntity(message.entityId);

        if (entity instanceof FamilyAltarEntity) {
            entity.setYBodyRot(message.rotation);
        }
    }
}
