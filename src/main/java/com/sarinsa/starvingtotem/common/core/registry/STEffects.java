package com.sarinsa.starvingtotem.common.core.registry;

import com.sarinsa.starvingtotem.common.core.StarvingTotem;
import com.sarinsa.starvingtotem.common.effect.BitterCurseEffect;
import com.sarinsa.starvingtotem.common.effect.SweetBlessingEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class STEffects {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, StarvingTotem.MODID);


    public static final RegistryObject<Effect> BITTER_CURSE = register("bitter_curse", () -> new BitterCurseEffect(EffectType.NEUTRAL, 0x467773));
    public static final RegistryObject<Effect> SWEET_BLESSING = register("sweet_blessing", () -> new SweetBlessingEffect(EffectType.BENEFICIAL, 0x467773));


    private static <T extends Effect> RegistryObject<T> register(String name, Supplier<T> effectSupplier) {
        return EFFECTS.register(name, effectSupplier);
    }
}
