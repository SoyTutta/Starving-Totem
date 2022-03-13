package com.sarinsa.starvingtotem.common.core;

import com.sarinsa.starvingtotem.common.core.config.STCommonConfig;
import com.sarinsa.starvingtotem.common.core.registry.STEffects;
import com.sarinsa.starvingtotem.common.core.registry.STEntities;
import com.sarinsa.starvingtotem.common.core.registry.STItems;
import com.sarinsa.starvingtotem.common.event.ConfigEvents;
import com.sarinsa.starvingtotem.common.event.EntityEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(StarvingTotem.MODID)
public class StarvingTotem {

    public static final String MODID = "starvingtotem";

    public static final Logger LOGGER = LogManager.getLogger(MODID);


    public StarvingTotem() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(STEntities::createEntityAttributes);
        modEventBus.register(new ConfigEvents());

        MinecraftForge.EVENT_BUS.register(new EntityEvents());

        STItems.ITEMS.register(modEventBus);
        STEntities.ENTITIES.register(modEventBus);
        STEffects.EFFECTS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, STCommonConfig.COMMON_SPEC);
    }

    public static ResourceLocation resourceLoc(String path) {
        return new ResourceLocation(MODID, path);
    }
}
