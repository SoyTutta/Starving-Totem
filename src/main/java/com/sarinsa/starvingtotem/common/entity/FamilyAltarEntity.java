package com.sarinsa.starvingtotem.common.entity;

import com.google.common.collect.ImmutableList;
import com.sarinsa.starvingtotem.common.core.StarvingTotem;
import com.sarinsa.starvingtotem.common.core.registry.STEffects;
import com.sarinsa.starvingtotem.common.core.registry.STItems;
import com.sarinsa.starvingtotem.common.util.References;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.*;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A modified copy-paste of {@link net.minecraft.entity.item.ArmorStandEntity}
 */
public class FamilyAltarEntity extends LivingEntity {

    public enum AltarState {
        NEUTRAL,
        HAPPY,
        ANGERED;
    }

    public static final List<Item> ACCEPTED_CAKES = new ArrayList<>();

    public static final DataParameter<Integer> ALTAR_STATE_ID = EntityDataManager.defineId(FamilyAltarEntity.class, DataSerializers.INT);
    public static final DataParameter<ItemStack> HELD_CAKE = EntityDataManager.defineId(FamilyAltarEntity.class, DataSerializers.ITEM_STACK);

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
        this.entityData.define(ALTAR_STATE_ID, AltarState.NEUTRAL.ordinal());
        this.entityData.define(HELD_CAKE, ItemStack.EMPTY);
    }

    public AltarState getAltarState() {
        return AltarState.values()[this.entityData.get(ALTAR_STATE_ID)];
    }

    public void setAltarState(@Nonnull AltarState state) {
        if (this.getAltarState() == state)
            return;

        this.entityData.set(ALTAR_STATE_ID, state.ordinal());

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

    public ItemStack getHeldCake() {
        return this.entityData.get(HELD_CAKE);
    }

    public void setHeldCake(@Nonnull ItemStack itemStack) {
        this.entityData.set(HELD_CAKE, itemStack);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
            AltarState state = this.getAltarState();

            if (state != AltarState.NEUTRAL) {
                if (this.stateTime > 0) {
                    --this.stateTime;
                }
                if (this.stateTime <= 0) {
                    if (state == AltarState.HAPPY) {
                        this.setHeldCake(ItemStack.EMPTY);
                        this.showCakeVanishParticles();
                    }
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
        return this.getHeldCake();
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotType, ItemStack itemStack) {
        if (slotType == EquipmentSlotType.MAINHAND) {
            this.setHeldCake(itemStack);
        }
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
    protected float tickHeadTurn(float no, float way) {
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
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        CompoundNBT cakeTag = new CompoundNBT();

        if (!this.getHeldCake().isEmpty()) {
            this.getHeldCake().save(cakeTag);
            compoundNBT.put("HeldCake", cakeTag);
        }
        compoundNBT.putInt("AltarState", this.getAltarState().ordinal());
        compoundNBT.putInt("StateTime", this.stateTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (compoundNBT.contains("HeldCake", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT cakeTag = compoundNBT.getCompound("HeldCake");
            this.setHeldCake(ItemStack.of(cakeTag));
        }
        if (compoundNBT.contains("AltarState", Constants.NBT.TAG_ANY_NUMERIC)) {
            int state = compoundNBT.getInt("AltarState");

            if (state > AltarState.values().length || state < 0) {
                StarvingTotem.LOGGER.error("Tried to load altar state of Family Altar, but the read state ID was not valid. Received invalid state ID \"{}\"; must range from 0 to \"{}\".", state, AltarState.values().length);
                StarvingTotem.LOGGER.error("Problematic Family Altar location: {}, {}", this.level.dimension().toString(), this.blockPosition().toString());
            }
            else {
                this.setAltarState(AltarState.values()[state]);
            }
        }
        if (compoundNBT.contains("StateTime", Constants.NBT.TAG_ANY_NUMERIC)) {
            int stateTime = compoundNBT.getInt("StateTime");

            if (stateTime < 0) {
                StarvingTotem.LOGGER.error("Tried to load Family Altar state time, but state time was less than 0 ticks. This is an error");
                StarvingTotem.LOGGER.error("Problematic Family Altar location: {}, {}", this.level.dimension().toString(), this.blockPosition().toString());
            }
        }
    }

    @Override
    public ActionResultType interactAt(PlayerEntity player, Vector3d vec3d, Hand hand) {
        ItemStack heldCake = this.getHeldCake();

        if (player.isShiftKeyDown()) {
            this.setHeldCake(ItemStack.EMPTY);
            this.setAltarState(this.getAltarState() == AltarState.ANGERED ? AltarState.ANGERED : AltarState.NEUTRAL);
            Block.popResource(this.level, this.blockPosition(), writeDataToStack(this));
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.IRON_GOLEM_ATTACK, SoundCategory.BLOCKS, 0.75F, 0.8F);
            this.remove();
            return ActionResultType.sidedSuccess(this.level.isClientSide);
        }
        ItemStack itemStack = player.getItemInHand(hand);

        if (!heldCake.isEmpty()) {
            return ActionResultType.FAIL;
        }
        else {
            if (ACCEPTED_CAKES.contains(itemStack.getItem())) {
                AltarState state = this.getAltarState();

                if (state == AltarState.HAPPY) {
                    return ActionResultType.PASS;
                }
                else {
                    if (state != AltarState.ANGERED) {
                        player.addEffect(new EffectInstance(STEffects.SWEET_BLESSING.get(), 12000, 0, false, false));
                        this.setAltarState(AltarState.HAPPY);
                    }
                    else {
                        this.setAltarState(AltarState.NEUTRAL);
                    }
                    this.level.broadcastEntityEvent(this, (byte) 6);
                    this.setHeldCake(itemStack.copy());
                    itemStack.shrink(1);
                    player.removeEffect(STEffects.BITTER_CURSE.get());
                    return ActionResultType.SUCCESS;
                }
            }
            else {
                if (!itemStack.isEmpty()) {
                    player.displayClientMessage(new TranslationTextComponent(References.ALTAR_INTERACT_TEXT), true);
                    return ActionResultType.SUCCESS;
                }
                this.level.broadcastEntityEvent(this, (byte) 5);
                return ActionResultType.PASS;
            }
        }
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
                    boolean isArrow = damageSource.getDirectEntity() instanceof AbstractArrowEntity;
                    boolean hasPiercing = isArrow && ((AbstractArrowEntity)damageSource.getDirectEntity()).getPierceLevel() > 0;
                    boolean isPlayer = "player".equals(damageSource.getMsgId());

                    if (!isPlayer && !isArrow) {
                        return false;
                    }
                    else if (damageSource.getEntity() instanceof PlayerEntity && !((PlayerEntity)damageSource.getEntity()).abilities.mayBuild) {
                        return false;
                    }
                    else if (damageSource.isCreativePlayer()) {
                        this.playBrokenSound();
                        this.showBreakingParticles();
                        this.remove();
                        return hasPiercing;
                    }
                    else {
                        if (damageSource.getEntity() instanceof PlayerEntity) {
                            PlayerEntity player = (PlayerEntity) damageSource.getEntity();

                            boolean hasSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem()) > 0;

                            if (!hasSilkTouch) {
                                player.addEffect(new EffectInstance(STEffects.BITTER_CURSE.get(), 12000, 0, false, false));
                                player.removeEffect(STEffects.SWEET_BLESSING.get());

                                if (this.getAltarState() != AltarState.ANGERED) {
                                    this.setAltarState(AltarState.ANGERED);
                                    this.showStateChangeParticles(AltarState.ANGERED);

                                    if (!this.getHeldCake().isEmpty()) {
                                        this.setHeldCake(ItemStack.EMPTY);
                                        this.showCakeVanishParticles();
                                    }
                                }
                            }
                        }
                        long gameTime = this.level.getGameTime();

                        if (gameTime - this.lastHit > 5L && !isArrow) {
                            this.level.broadcastEntityEvent(this, (byte)4);
                            this.lastHit = gameTime;
                        }
                        else {
                            Block.popResource(this.level, this.blockPosition(), writeDataToStack(this));
                            this.showBreakingParticles();
                            this.playBrokenSound();
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

    private static ItemStack writeDataToStack(FamilyAltarEntity entity) {
        ItemStack stack = new ItemStack(STItems.FAMILY_ALTAR.get());
        CompoundNBT tag = stack.getOrCreateTag();

        tag.putInt("AltarState", entity.getAltarState().ordinal());
        tag.putInt("StateTime", entity.stateTime);

        stack.setTag(tag);
        return stack;
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
                    double xSpeed = serverWorld.random.nextGaussian() * 0.02D;
                    double ySpeed = serverWorld.random.nextGaussian() * 0.02D;
                    double zSpeed = serverWorld.random.nextGaussian() * 0.02D;
                    serverWorld.sendParticles(type, this.getRandomX(1.0D), this.getRandomY() + 0.4D, this.getRandomZ(1.0D), 1, 0.05D, xSpeed, ySpeed, zSpeed);
                }
            }
        }
    }

    private void showCakeVanishParticles() {
        if (this.level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) this.level;

            for (int i = 0; i < 7; ++i) {
                double xSpeed = serverWorld.random.nextGaussian() * 0.02D;
                double ySpeed = serverWorld.random.nextGaussian() * 0.02D;
                double zSpeed = serverWorld.random.nextGaussian() * 0.02D;
                serverWorld.sendParticles(ParticleTypes.SMOKE, this.getRandomX(1.0D), this.getRandomY() + 0.4D, this.getRandomZ(1.0D), 1, 0.05D, xSpeed, ySpeed, zSpeed);
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte eventId) {
        switch (eventId) {
            case 4:
                if (this.level.isClientSide) {
                    this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.IRON_GOLEM_STEP, this.getSoundSource(), 0.9F, 1.0F, false);
                    this.lastHit = this.level.getGameTime();
                }
                break;
            case 5:
                if (this.level.isClientSide) {
                    this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.IRON_GOLEM_ATTACK, this.getSoundSource(), 0.3F, 1.0F, false);
                }
                break;
            case 6:
                if (this.level.isClientSide) {
                    this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.WOOL_PLACE, this.getSoundSource(), 0.5F, 1.0F, false);
                }
            default:
                super.handleEntityEvent(eventId);
                break;
        }
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
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.METAL_BREAK;
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

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(STItems.FAMILY_ALTAR.get());
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
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.METAL_BREAK, this.getSoundSource(), 1.0F, 1.0F);
    }
}
