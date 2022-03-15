package com.sarinsa.starvingtotem.common.hooks.mixin;

import com.sarinsa.starvingtotem.common.core.config.STCommonConfig;
import com.sarinsa.starvingtotem.common.core.registry.STEffects;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

public class CommonMixinHooks {

    public static void checkTotemDeathProtection(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> ci) {
        if (livingEntity.hasEffect(STEffects.BITTER_CURSE.get())) {
            ci.setReturnValue(false);
        }
    }

    public static int getBlessingStackSize(LivingEntity entity, Random random, int originalSize) {
        if (entity.hasEffect(STEffects.SWEET_BLESSING.get())) {
            double keepTotemChance = STCommonConfig.COMMON.getKeepTotemChance();

            if (keepTotemChance <= 0.0D || keepTotemChance > 1.0D)
                return originalSize;

            if (random.nextFloat() <= STCommonConfig.COMMON.getKeepTotemChance()) {
                return 0;
            }
        }
        return originalSize;
    }
}
