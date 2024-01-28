package com.vampmir.mixin.accessors;

import com.vampmir.features.mining.FindFairyGrotto;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chunk.class)
public class ChunkMixin {
    @Shadow @Final public int xPosition;

    @Shadow @Final public int zPosition;

    @Inject(method="fillChunk", at = @At("RETURN"))
    void fillChunk(byte[] p_177439_1_, int p_177439_2_, boolean p_177439_3_, CallbackInfo ci) {
        FindFairyGrotto.INSTANCE.onChunkFinishLoad(this.xPosition, this.zPosition, ci);
    }
}
