package com.example.travelpack.client;

import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.entity.state.HorseEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.RotationAxis;

public class SleepingBagHorseLayer extends FeatureRenderer<HorseEntityRenderState, HorseEntityModel> {

    private final ItemModelManager itemModelManager;
    private final ItemRenderState itemRenderState = new ItemRenderState();

    public SleepingBagHorseLayer(FeatureRendererContext<HorseEntityRenderState, HorseEntityModel> context,
                                  ItemModelManager itemModelManager) {
        super(context);
        this.itemModelManager = itemModelManager;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       HorseEntityRenderState state, float limbAngle, float limbDistance) {
        if (!(state instanceof BedrollStateAccess bsa)) return;
        ItemStack stack = bsa.mymod_getBedrollStack();
        if (stack.isEmpty()) return;

        itemRenderState.clear();
        itemModelManager.update(itemRenderState, stack, ModelTransformationMode.FIXED, false, null, null, 0);

        matrices.push();
        // y=0.0 (up ~1 thickness from 0.05), z=0.2 behind saddle
        // -83° = face-up (-90°) + 7° tilt toward player for a natural resting angle
        matrices.translate(0.0f, 0.01f, 0.4f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-85));
        matrices.scale(0.65f, 0.65f, 0.65f);
        itemRenderState.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }
}
