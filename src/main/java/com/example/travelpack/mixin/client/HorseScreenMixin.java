package com.example.travelpack.mixin.client;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.MountScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MountScreen.class)
public abstract class HorseScreenMixin {

    @Shadow protected LivingEntity mount;

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void mymod_drawBedrollSlotBackground(DrawContext context, float delta,
                                                  int mouseX, int mouseY, CallbackInfo ci) {
        if (!(this.mount instanceof HorseEntity)) return;
        // HandledScreen stores the computed (width-bgWidth)/2 offsets in its x and y fields.
        HandledScreenAccessor acc = (HandledScreenAccessor) (Object) this;
        int xOff = acc.mymod_getX();
        int yOff = acc.mymod_getY();
        // BedrollSlot is at handler coords (8, 54) → background at (xOff + 7, yOff + 53), 18×18
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED,
                Identifier.ofVanilla("container/slot"),
                xOff + 7, yOff + 53, 18, 18);
    }
}
