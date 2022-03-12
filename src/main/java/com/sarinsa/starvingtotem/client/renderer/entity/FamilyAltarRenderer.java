package com.sarinsa.starvingtotem.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sarinsa.starvingtotem.common.core.StarvingTotem;
import com.sarinsa.starvingtotem.common.entity.FamilyAltarEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class FamilyAltarRenderer<T extends FamilyAltarEntity> extends LivingRenderer<T, FamilyAltarModel<T>> {

    private final ResourceLocation[] TEXTURES = {
            StarvingTotem.resourceLoc("textures/entity/altar/neutral_family_altar.png"),
            StarvingTotem.resourceLoc("textures/entity/altar/happy_family_altar.png"),
            StarvingTotem.resourceLoc("textures/entity/altar/angry_family_altar.png")
    };

    public FamilyAltarRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new FamilyAltarModel<>(), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(T familyAltar) {
        return TEXTURES[familyAltar.getAltarState().ordinal()];
    }

    @Override
    protected void setupRotations(T familyAltar, MatrixStack matrixStack, float p_225621_3_, float p_225621_4_, float partialTick) {
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - p_225621_4_));
        float f = (float)(familyAltar.level.getGameTime() - familyAltar.lastHit) + partialTick;

        if (f < 5.0F) {
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.sin(f / 1.5F * (float)Math.PI) * 3.0F));
        }
    }

    @Override
    protected boolean shouldShowName(T familyAltar) {
        double distSqr = this.entityRenderDispatcher.distanceToSqr(familyAltar);
        float maxDist = familyAltar.isCrouching() ? 32.0F : 64.0F;
        return !(distSqr >= (double) (maxDist * maxDist)) && familyAltar.isCustomNameVisible();
    }
}
