package com.sarinsa.starvingtotem.client.renderer.entity;

import com.sarinsa.starvingtotem.common.core.StarvingTotem;
import com.sarinsa.starvingtotem.common.entity.FamilyAltarEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

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
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURES[0];
    }
}
