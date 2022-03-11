package com.sarinsa.starvingtotem.common.core;

import com.sarinsa.starvingtotem.common.core.registry.STEffects;
import com.sarinsa.starvingtotem.common.core.registry.STEntities;
import com.sarinsa.starvingtotem.common.core.registry.STItems;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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

        STItems.ITEMS.register(modEventBus);
        STEntities.ENTITIES.register(modEventBus);
        STEffects.EFFECTS.register(modEventBus);
    }

    public static ResourceLocation resourceLoc(String path) {
        return new ResourceLocation(MODID, path);
    }
}
