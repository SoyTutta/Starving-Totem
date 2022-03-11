package com.sarinsa.starvingtotem.common.item;

import com.sarinsa.starvingtotem.common.core.registry.STEntities;
import com.sarinsa.starvingtotem.common.entity.FamilyAltarEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class FamilyAltarItem extends Item {

    public FamilyAltarItem() {
        super(new Item.Properties());
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
                    FamilyAltarEntity familyAltar = STEntities.FAMILY_ALTAR.get().create(serverWorld, itemStack.getTag(), null, useContext.getPlayer(), pos, SpawnReason.SPAWN_EGG, true, true);

                    if (familyAltar == null) {
                        return ActionResultType.FAIL;
                    }
                    serverWorld.addFreshEntityWithPassengers(familyAltar);
                    float yRot = (float) MathHelper.floor((MathHelper.wrapDegrees(useContext.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                    familyAltar.moveTo(familyAltar.getX(), familyAltar.getY(), familyAltar.getZ(), yRot, 0.0F);
                    world.addFreshEntity(familyAltar);
                    world.playSound(null, familyAltar.getX(), familyAltar.getY(), familyAltar.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
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
