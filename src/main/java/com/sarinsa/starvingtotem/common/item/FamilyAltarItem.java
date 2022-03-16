package com.sarinsa.starvingtotem.common.item;

import com.google.common.graph.Network;
import com.sarinsa.starvingtotem.common.core.registry.STEntities;
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

public class FamilyAltarItem extends Item {

    public FamilyAltarItem() {
        super(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_MISC));
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
                    float yRot = useContext.getHorizontalDirection().getOpposite().toYRot();
                    familyAltar.moveTo(familyAltar.getX(), familyAltar.getY(), familyAltar.getZ(), yRot, 0.0F);
                    familyAltar.setYBodyRot(yRot);
                    FamilyAltarEntity.setAltarState(useContext.getItemInHand().getOrCreateTag(), familyAltar);
                    serverWorld.addFreshEntityWithPassengers(familyAltar);
                    NetworkHelper.sendAltarRotationUpdate(familyAltar, yRot);
                    world.playSound(null, familyAltar.getX(), familyAltar.getY(), familyAltar.getZ(), SoundEvents.METAL_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
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
