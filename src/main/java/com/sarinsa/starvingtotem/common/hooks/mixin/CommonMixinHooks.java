package com.sarinsa.starvingtotem.common.hooks.mixin;

import com.sarinsa.starvingtotem.common.core.registry.STEffects;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class CommonMixinHooks {

    public static void checkTotemDeathProtection(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> ci) {
        if (livingEntity.hasEffect(STEffects.BITTER_CURSE.get())) {
            ci.setReturnValue(false);
        }
    }
}
