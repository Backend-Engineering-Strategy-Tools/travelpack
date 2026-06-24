package com.example.travelpack.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.HorseEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
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
    public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
                       HorseEntityRenderState state, float limbAngle, float limbDistance) {
        if (!(state instanceof BedrollStateAccess bsa)) return;
        ItemStack stack = bsa.mymod_getBedrollStack();
        if (stack.isEmpty()) return;

        itemRenderState.clear();
        itemModelManager.clearAndUpdate(itemRenderState, stack, ItemDisplayContext.FIXED,
                MinecraftClient.getInstance().world, null, 0);

        matrices.push();
        matrices.translate(0.0f, 0.01f, 0.4f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-85));
        matrices.scale(0.65f, 0.65f, 0.65f);
        itemRenderState.render(matrices, queue, light, OverlayTexture.DEFAULT_UV, EntityRenderState.NO_OUTLINE);
        matrices.pop();
    }
}
