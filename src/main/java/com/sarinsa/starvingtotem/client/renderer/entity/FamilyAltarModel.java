package com.sarinsa.starvingtotem.client.renderer.entity;

import com.google.common.collect.ImmutableList;
import com.sarinsa.starvingtotem.common.entity.FamilyAltarEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class FamilyAltarModel<T extends FamilyAltarEntity> extends SegmentedModel<T> {

    private final ModelRenderer head;
    private final ModelRenderer nose;
    private final ModelRenderer body;
    private final ModelRenderer wings;
    private final ModelRenderer arms;
    private final ModelRenderer arms_r1;
    private final ModelRenderer base;

    public FamilyAltarModel() {
        texWidth = 64;
        texHeight = 64;

        head = new ModelRenderer(this);
        head.setPos(0.0F, 16.0F, 3.0F);
        head.texOffs(2, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 9.0F, 8.0F, 0.0F, false);

        nose = new ModelRenderer(this);
        nose.setPos(-1.0F, 0.0F, 1.0F);
        head.addChild(nose);
        nose.texOffs(1, 1).addBox(0.0F, -2.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setPos(0.0F, 19.0F, 3.0F);
        body.texOffs(2, 14).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        wings = new ModelRenderer(this);
        wings.setPos(0.0F, 0.0F, 0.0F);
        body.addChild(wings);
        wings.texOffs(2, 25).addBox(-6.0F, -2.0F, 2.0F, 12.0F, 2.0F, 1.0F, 0.0F, false);
        wings.texOffs(18, 28).addBox(-5.0F, 0.0F, 2.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);

        arms = new ModelRenderer(this);
        arms.setPos(-1.0F, 16.0F, 4.0F);


        arms_r1 = new ModelRenderer(this);
        arms_r1.setPos(1.0F, 3.0F, -1.0F);
        arms.addChild(arms_r1);
        setRotationAngle(arms_r1, -1.1345F, 0.0F, 0.0F);
        arms_r1.texOffs(20, 18).addBox(-3.0F, -0.3954F, -2.0824F, 6.0F, 4.0F, 3.0F, 0.0F, false);

        base = new ModelRenderer(this);
        base.setPos(-1.0F, 16.0F, 4.0F);
        base.texOffs(0, 32).addBox(-6.0F, 7.0F, -11.0F, 14.0F, 1.0F, 14.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T familyAltar, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        //previously the render function, render code was moved to a method below
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.head, this.body, this.arms, this.base);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
