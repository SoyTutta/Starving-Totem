package com.sarinsa.starvingtotem.common.core.registry;

import com.sarinsa.starvingtotem.common.core.StarvingTotem;
import com.sarinsa.starvingtotem.common.entity.FamilyAltarEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class STEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, StarvingTotem.MODID);


    public static final RegistryObject<EntityType<FamilyAltarEntity>> FAMILY_ALTAR = register("family_altar", EntityType.Builder.of(FamilyAltarEntity::new, EntityClassification.MISC));


    public static void createEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(FAMILY_ALTAR.get(), FamilyAltarEntity.createLivingAttributes().build());
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builderSupplier) {
        return ENTITIES.register(name, () -> builderSupplier.build(name));
    }
}
