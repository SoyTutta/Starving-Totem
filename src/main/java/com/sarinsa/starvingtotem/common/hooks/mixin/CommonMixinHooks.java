package com.sarinsa.starvingtotem.common.hooks.mixin;

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
            if (random.nextInt(4) == 0) {
                return 0;
            }
        }
        return originalSize;
    }
}
