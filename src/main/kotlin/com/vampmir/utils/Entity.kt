package com.vampmir.utils

import com.vampmir.GSM
import com.vampmir.utils.Render.drawOutlinedBoundingBox
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import java.awt.Color

object Entity {
    fun outlineEntity(entity: EntityOtherPlayerMP, partialTicks: Float) {
        val pos = entity.playerLocation ?: return
        drawOutlinedBoundingBox(
            AxisAlignedBB(
                pos.x.toDouble() - 0.5,
                pos.y.toDouble() - 2,
                pos.z.toDouble() - 0.5,
                (pos.x + 1).toDouble() + 0.5,
                (pos.y + 1).toDouble(),
                (pos.z + 1).toDouble() + 0.5
        ), Color(255, 0, 0), 3f, partialTicks);
    }

    inline fun <reified R : Entity> getEntities(): Sequence<R> = getAllEntities().filterIsInstance<R>()

    fun getAllEntities(): Sequence<Entity> = GSM.minecraft.theWorld?.loadedEntityList?.let {
        if (GSM.minecraft.isCallingFromMinecraftThread) it else it.toMutableList()
    }?.asSequence()?.filterNotNull() ?: emptySequence()
}