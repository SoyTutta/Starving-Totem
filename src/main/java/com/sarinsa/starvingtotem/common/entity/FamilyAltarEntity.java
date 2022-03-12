package com.sarinsa.starvingtotem.common.entity;

import com.google.common.collect.ImmutableList;
import com.sarinsa.starvingtotem.common.core.StarvingTotem;
import com.sarinsa.starvingtotem.common.core.registry.STEffects;
import com.sarinsa.starvingtotem.common.core.registry.STItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A modified copy-paste of {@link net.minecraft.entity.item.ArmorStandEntity}
 */
public class FamilyAltarEntity extends LivingEntity {

    public static final List<Item> ACCEPTED_CAKES = new ArrayList<>();

    // 0 - Neutral
    // 1 - Happy
    // 2 - Angered
    public static final DataParameter<Integer> ALTAR_STATE = EntityDataManager.defineId(FamilyAltarEntity.class, DataSerializers.INT);

    private final ImmutableList<ItemStack> armorItems = ImmutableList.of();
    public long lastHit;
    private int stateTime;

    public FamilyAltarEntity(EntityType<? extends FamilyAltarEntity> entityType, World world) {
        super(entityType, world);
        this.maxUpStep = 0.0F;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ALTAR_STATE, AltarState.NEUTRAL.ordinal());
    }

    public AltarState getAltarState() {
        return AltarState.values()[this.entityData.get(ALTAR_STATE)];
    }

    public void setAltarState(@Nonnull AltarState state) {
        if (this.getAltarState() == state)
            return;

        this.entityData.set(ALTAR_STATE, state.ordinal());

        if (state != AltarState.NEUTRAL) {
            this.resetStateTime();
        }
        else {
            this.stateTime = 0;
        }
    }

    public static void setAltarState(CompoundNBT tag, FamilyAltarEntity altarEntity) {
        if (tag.contains("AltarState", Constants.NBT.TAG_ANY_NUMERIC)) {
            int stateId = tag.getInt("AltarState");

            if (stateId > AltarState.values().length) {
                return;
            }
            AltarState state = AltarState.values()[stateId];
            altarEntity.setAltarState(state);

            if (tag.contains("StateTime", Constants.NBT.TAG_ANY_NUMERIC)) {
                int stateTime = tag.getInt("StateTime");

                if (stateTime < 0)
                    return;

                altarEntity.stateTime = stateTime;
            }
        }
    }

    private void resetStateTime() {
        // 10 minutes in ticks
        this.stateTime = 12000;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
            if (this.getAltarState() != AltarState.NEUTRAL) {
                if (this.stateTime > 0) {
                    --this.stateTime;
                }
                if (this.stateTime <= 0) {
                    this.setAltarState(AltarState.NEUTRAL);
                }
            }
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return this.armorItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType slotType) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotType, ItemStack itemStack) {
    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public void refreshDimensions() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        super.refreshDimensions();
        this.setPos(x, y, z);
    }

    @Override
    public void kill() {
        this.remove();
    }

    @Override
    protected float tickHeadTurn(float p_110146_1_, float p_110146_2_) {
        this.yBodyRotO = this.yRotO;
        this.yBodyRot = this.yRot;
        return 0.0F;
    }

    @Override
    public void setYBodyRot(float rot) {
        this.yBodyRotO = this.yRotO = rot;
        this.yHeadRotO = this.yHeadRot = rot;
    }

    @Override
    public void setYHeadRot(float rot) {
        this.yBodyRotO = this.yRotO = rot;
        this.yHeadRotO = this.yHeadRot = rot;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if (!this.level.isClientSide && this.isAlive()) {
            if (DamageSource.OUT_OF_WORLD.equals(damageSource)) {
                this.remove();
                return false;
            }
            else if (!this.isInvulnerableTo(damageSource)) {
                if (damageSource.isExplosion()) {
                    this.remove();
                    return false;
                }
                else if (DamageSource.IN_FIRE.equals(damageSource)) {
                    if (this.isOnFire()) {
                        this.causeDamage(0.15F);
                    }
                    else {
                        this.setSecondsOnFire(5);
                    }
                    return false;
                }
                else if (DamageSource.ON_FIRE.equals(damageSource) && this.getHealth() > 0.5F) {
                    this.causeDamage(4.0F);
                    return false;
                }
                else {
                    boolean flag = damageSource.getDirectEntity() instanceof AbstractArrowEntity;
                    boolean flag1 = flag && ((AbstractArrowEntity)damageSource.getDirectEntity()).getPierceLevel() > 0;
                    boolean flag2 = "player".equals(damageSource.getMsgId());

                    if (!flag2 && !flag) {
                        return false;
                    }
                    else if (damageSource.getEntity() instanceof PlayerEntity && !((PlayerEntity)damageSource.getEntity()).abilities.mayBuild) {
                        return false;
                    }
                    else if (damageSource.isCreativePlayer()) {
                        this.playBrokenSound();
                        this.showBreakingParticles();
                        this.remove();
                        return flag1;
                    }
                    else {
                        if (damageSource.getEntity() instanceof PlayerEntity) {
                            PlayerEntity player = (PlayerEntity) damageSource.getEntity();

                            boolean hasSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem()) > 0;

                            if (!hasSilkTouch) {
                                player.addEffect(new EffectInstance(STEffects.BITTER_CURSE.get(), 12000));

                                if (this.getAltarState() != AltarState.ANGERED) {
                                    this.setAltarState(AltarState.ANGERED);
                                    this.showStateChangeParticles(AltarState.ANGERED);
                                }
                            }
                        }
                        long gameTime = this.level.getGameTime();

                        if (gameTime - this.lastHit > 5L && !flag) {
                            this.level.broadcastEntityEvent(this, (byte)32);
                            this.lastHit = gameTime;
                        }
                        else {
                            ItemStack stack = new ItemStack(STItems.FAMILY_ALTAR.get());
                            CompoundNBT tag = stack.getOrCreateTag();

                            tag.putInt("AltarState", this.getAltarState().ordinal());
                            tag.putInt("StateTime", this.stateTime);

                            stack.setTag(tag);

                            Block.popResource(this.level, this.blockPosition(), stack);
                            this.showBreakingParticles();
                            this.remove();
                        }
                        return true;
                    }
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    private void showStateChangeParticles(AltarState state) {
        if (this.level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            BasicParticleType type = null;

            if (state == AltarState.ANGERED) {
                type = ParticleTypes.ANGRY_VILLAGER;
            }
            else if (state == AltarState.HAPPY) {
                type = ParticleTypes.HAPPY_VILLAGER;
            }

            if (type != null) {
                for (int i = 0; i < 5; ++i) {
                    double d0 = serverWorld.random.nextGaussian() * 0.02D;
                    double d1 = serverWorld.random.nextGaussian() * 0.02D;
                    double d2 = serverWorld.random.nextGaussian() * 0.02D;
                    serverWorld.sendParticles(type, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), 1, 0.05D, d0, d1, d2);
                }
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte eventId) {
        if (eventId == 32) {
            if (this.level.isClientSide) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_STAND_HIT, this.getSoundSource(), 0.3F, 1.0F, false);
                this.lastHit = this.level.getGameTime();
            }
        }
        else {
            super.handleEntityEvent(eventId);
        }
    }

    @Override
    protected SoundEvent getFallDamageSound(int distance) {
        return SoundEvents.ARMOR_STAND_FALL;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ARMOR_STAND_HIT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ARMOR_STAND_BREAK;
    }

    @Override
    public void thunderHit(ServerWorld serverWorld, LightningBoltEntity lightningBolt) {
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        return entity instanceof PlayerEntity && !this.level.mayInteract((PlayerEntity) entity, this.blockPosition());
    }

    private void causeDamage(float damage) {
        float health = this.getHealth();
        health -= damage;

        if (health <= 0.5F) {
            this.playBrokenSound();
            this.remove();
        }
        else {
            this.setHealth(health);
        }
    }

    private void showBreakingParticles() {
        if (this.level instanceof ServerWorld) {
            ((ServerWorld)this.level).sendParticles(new BlockParticleData(
                    ParticleTypes.BLOCK,
                    Blocks.OAK_PLANKS.defaultBlockState()),
                    this.getX(),
                    this.getY(0.6666666666666666D),
                    this.getZ(),
                    10,
                    this.getBbWidth() / 4.0F,
                    this.getBbHeight() / 4.0F,
                    this.getBbWidth() / 4.0F,
                    0.05D);
        }
    }

    private void playBrokenSound() {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_STAND_BREAK, this.getSoundSource(), 1.0F, 1.0F);
    }

    public enum AltarState {
        NEUTRAL,
        HAPPY,
        ANGERED;
    }
}
