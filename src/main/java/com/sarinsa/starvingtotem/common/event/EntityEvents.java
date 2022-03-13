package com.sarinsa.starvingtotem.common.event;

import com.sarinsa.starvingtotem.common.core.registry.STEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityEvents {

    // TODO - What the fuck
    public void onLivingDeath(LivingDeathEvent event) {

    }

    @SubscribeEvent
    public void onEat(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            Item eaten = event.getItem().getItem();

            if (eaten.isEdible() && player.hasEffect(STEffects.SWEET_BLESSING.get())) {
                float saturation = eaten.getFoodProperties().getSaturationModifier();
                player.getFoodData().eat(0, saturation > 0 ? (float)(saturation * 1.25) : 0);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntityLiving().hasEffect(STEffects.BITTER_CURSE.get())) {
            event.setAmount(event.getAmount() * 1.5F);
        }
    }
}
