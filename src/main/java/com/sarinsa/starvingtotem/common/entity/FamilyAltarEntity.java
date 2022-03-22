package com.sarinsa.starvingtotem.common.entity;

import com.sarinsa.starvingtotem.common.core.registry.STItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FamilyAltarEntity extends AbstractAltarEntity {

    public static List<Item> ACCEPTED_CAKES = new ArrayList<>();


    public FamilyAltarEntity(EntityType<? extends FamilyAltarEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public List<Item> getValidOffering() {
        return ACCEPTED_CAKES;
    }

    @Override
    protected SoundEvent getFallDamageSound(int distance) {
        return SoundEvents.METAL_BREAK;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.IRON_GOLEM_STEP;
    }

    @Override
    public ItemStack getPickedResult() {
        return new ItemStack(STItems.FAMILY_ALTAR.get());
    }

    @Override
    public Supplier<BlockState> getBreakParticleBlock() {
        return Blocks.OAK_PLANKS::defaultBlockState;
    }

    @Override
    public Supplier<SoundEvent> getBreakSound() {
        return () -> SoundEvents.METAL_BREAK;
    }
}
