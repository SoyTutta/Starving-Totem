package com.sarinsa.starvingtotem.common.core.registry;

import com.sarinsa.starvingtotem.common.core.StarvingTotem;
import com.sarinsa.starvingtotem.common.item.AltarItem;
import com.sarinsa.starvingtotem.common.item.ProtectionTotemItem;
import com.sarinsa.starvingtotem.common.item.RegenTotemItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class STItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StarvingTotem.MODID);


    public static RegistryObject<Item> TOTEM_OF_REGENERATION = register("totem_of_regeneration", RegenTotemItem::new);
    public static RegistryObject<Item> TOTEM_OF_PROTECTION= register("totem_of_regeneration", ProtectionTotemItem::new);
    public static RegistryObject<Item> FAMILY_ALTAR = register("family_altar", () -> new AltarItem(STEntities.FAMILY_ALTAR::get));
    public static RegistryObject<Item> TRIBAL_ALTAR = register("tribal_altar", () -> new AltarItem(STEntities.FAMILY_ALTAR::get));
    public static RegistryObject<Item> WARDING_ALTAR = register("warding_altar", () -> new AltarItem(STEntities.FAMILY_ALTAR::get));


    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }
}
