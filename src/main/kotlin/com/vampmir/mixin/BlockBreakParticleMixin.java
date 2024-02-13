package com.vampmir.mixin;

import com.vampmir.GSM;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderer.class)
public class BlockBreakParticleMixin {
    @Inject(method = "addEffect", at = @At("HEAD"), cancellable = true)
    void addEffect(EntityFX effect, CallbackInfo ci) {
        if (effect instanceof EntityDiggingFX && GSM.config.getRemoveBreakingParticles()) {
            ci.cancel();
        }
    }
}
