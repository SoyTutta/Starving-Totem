package com.sarinsa.starvingtotem.common.event;

import com.sarinsa.starvingtotem.common.core.StarvingTotem;
import com.sarinsa.starvingtotem.common.core.config.STCommonConfig;
import com.sarinsa.starvingtotem.common.entity.FamilyAltarEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ConfigEvents {

    @SubscribeEvent
    public void onConfigLoad(ModConfig.Loading event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON) {
            refreshCakeList();
        }
    }

    @SubscribeEvent
    public void onConfigReload(ModConfig.Reloading event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON) {
            refreshCakeList();
        }
    }

    private static void refreshCakeList() {
        FamilyAltarEntity.ACCEPTED_CAKES.clear();

        List<? extends String> itemIds = STCommonConfig.COMMON.getCakeConfig();

        if (itemIds.isEmpty()) {
            return;
        }
        List<Item> cakes = new ArrayList<>();

        for (String s : itemIds) {
            ResourceLocation itemId = ResourceLocation.tryParse(s);

            if (itemId == null) {
                logWarning("Found invalid item id \"{}\" in the cake list.", s);
                continue;
            }
            if (ForgeRegistries.ITEMS.containsKey(itemId)) {
                cakes.add(ForgeRegistries.ITEMS.getValue(itemId));
            }
        }
        if (!cakes.isEmpty()) {
            FamilyAltarEntity.ACCEPTED_CAKES.addAll(cakes);
        }
    }

    private static void logWarning(String message, Object... params) {
        StarvingTotem.LOGGER.warn("[Config] " + message, params);
    }
}
