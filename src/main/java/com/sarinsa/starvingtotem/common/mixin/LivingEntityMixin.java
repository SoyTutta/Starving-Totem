package com.sarinsa.starvingtotem.common.mixin;

import com.sarinsa.starvingtotem.common.hooks.mixin.CommonMixinHooks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"))
    public void onCheckTotemDeathProtection(DamageSource damageSource, CallbackInfoReturnable<Boolean> ci) {
        CommonMixinHooks.checkTotemDeathProtection((LivingEntity) (Object) this, ci);
    }
}
