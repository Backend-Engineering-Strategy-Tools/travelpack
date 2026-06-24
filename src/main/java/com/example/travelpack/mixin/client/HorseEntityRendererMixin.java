package com.example.travelpack.mixin.client;

import com.example.travelpack.BedrollEquipped;
import com.example.travelpack.client.BedrollStateAccess;
import com.example.travelpack.client.SleepingBagHorseLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.entity.state.HorseEntityRenderState;
import net.minecraft.entity.passive.HorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseEntityRenderer.class)
public abstract class HorseEntityRendererMixin
        extends LivingEntityRenderer<HorseEntity, HorseEntityRenderState, HorseEntityModel> {

    // Compiler stub — never actually called
    protected HorseEntityRendererMixin(EntityRendererFactory.Context ctx, HorseEntityModel model,
                                       float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void mymod_addBedrollLayer(EntityRendererFactory.Context context, CallbackInfo ci) {
        @SuppressWarnings("unchecked")
        FeatureRendererContext<HorseEntityRenderState, HorseEntityModel> ctx =
                (FeatureRendererContext<HorseEntityRenderState, HorseEntityModel>) (Object) this;
        this.addFeature(new SleepingBagHorseLayer(ctx, context.getItemModelManager()));
    }

    @Inject(
        method = "updateRenderState(Lnet/minecraft/entity/passive/HorseEntity;Lnet/minecraft/client/render/entity/state/HorseEntityRenderState;F)V",
        at = @At("TAIL")
    )
    private void mymod_copyBedrollToState(HorseEntity entity, HorseEntityRenderState state,
                                          float tickDelta, CallbackInfo ci) {
        if (entity instanceof BedrollEquipped equipped && state instanceof BedrollStateAccess access) {
            access.mymod_setBedrollStack(equipped.mymod_getBedrollStack());
        }
    }
}
