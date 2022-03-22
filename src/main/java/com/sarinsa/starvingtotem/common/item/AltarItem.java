package com.sarinsa.starvingtotem.common.item;

import com.sarinsa.starvingtotem.common.entity.AbstractAltarEntity;
import com.sarinsa.starvingtotem.common.entity.FamilyAltarEntity;
import com.sarinsa.starvingtotem.common.network.NetworkHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Supplier;

public class AltarItem extends Item {

    private final Supplier<EntityType<? extends AbstractAltarEntity>> entityTypeSupplier;

    public AltarItem(Supplier<EntityType<? extends AbstractAltarEntity>> entityTypeSupplier) {
        super(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_MISC));
        this.entityTypeSupplier = entityTypeSupplier;
    }

    @Override
    public ActionResultType useOn(ItemUseContext useContext) {
        Direction direction = useContext.getClickedFace();

        if (direction == Direction.DOWN) {
            return ActionResultType.FAIL;
        }
        else {
            World world = useContext.getLevel();
            BlockItemUseContext blockItemUseContext = new BlockItemUseContext(useContext);
            BlockPos pos = blockItemUseContext.getClickedPos();
            ItemStack itemStack = useContext.getItemInHand();
            Vector3d vec3d = Vector3d.atBottomCenterOf(pos);
            AxisAlignedBB aabb = EntityType.ARMOR_STAND.getDimensions().makeBoundingBox(vec3d.x(), vec3d.y(), vec3d.z());

            if (world.noCollision(null, aabb, (entity) -> true) && world.getEntities(null, aabb).isEmpty()) {
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) world;
                    AbstractAltarEntity altarEntity = this.entityTypeSupplier.get().create(serverWorld, itemStack.getTag(), null, useContext.getPlayer(), pos, SpawnReason.SPAWN_EGG, true, true);

                    if (altarEntity == null) {
                        return ActionResultType.FAIL;
                    }
                    float yRot = useContext.getHorizontalDirection().getOpposite().toYRot();
                    altarEntity.moveTo(altarEntity.getX(), altarEntity.getY(), altarEntity.getZ(), yRot, 0.0F);
                    altarEntity.setYBodyRot(yRot);
                    FamilyAltarEntity.setAltarState(useContext.getItemInHand().getOrCreateTag(), altarEntity);
                    serverWorld.addFreshEntityWithPassengers(altarEntity);
                    NetworkHelper.sendAltarRotationUpdate(altarEntity, yRot);
                    world.playSound(null, altarEntity.getX(), altarEntity.getY(), altarEntity.getZ(), SoundEvents.METAL_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                }
                itemStack.shrink(1);
                return ActionResultType.sidedSuccess(world.isClientSide);
            }
            else {
                return ActionResultType.FAIL;
            }
        }
    }
}
