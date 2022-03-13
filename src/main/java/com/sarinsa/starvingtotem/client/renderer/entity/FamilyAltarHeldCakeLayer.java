package com.sarinsa.starvingtotem.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sarinsa.starvingtotem.common.entity.FamilyAltarEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class FamilyAltarHeldCakeLayer<T extends FamilyAltarEntity, M extends FamilyAltarModel<T>> extends LayerRenderer<T, M> {

    public FamilyAltarHeldCakeLayer(IEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T familyAltar, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack heldCake = familyAltar.getHeldCake();

        if (!heldCake.isEmpty()) {
            matrixStack.pushPose();
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-45.0F));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            matrixStack.mulPose(Vector3f.ZN.rotationDegrees(180.0F));
            matrixStack.translate(0.0D, -1.2D, -0.8D);
            Minecraft.getInstance().getItemInHandRenderer().renderItem(familyAltar, heldCake, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, false, matrixStack, buffer, packedLight);
            matrixStack.popPose();
        }
    }
}
