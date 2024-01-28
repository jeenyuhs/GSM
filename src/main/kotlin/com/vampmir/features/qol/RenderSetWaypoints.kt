package com.vampmir.features.qol

import com.vampmir.utils.Render
import gg.essential.universal.UMatrixStack
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object RenderSetWaypoints {
    val waypoints: ArrayList<BlockPos> = ArrayList()

    @SubscribeEvent
    fun onWorldLastRender(event: RenderWorldLastEvent) {
        val matrixStack = UMatrixStack()
        try {
            waypoints.forEach {
                GlStateManager.disableCull()
                GlStateManager.disableDepth()

                Render.renderWaypoint(it.toString(), it, event.partialTicks, matrixStack)

                GlStateManager.enableDepth()
                GlStateManager.enableCull()
            }
        } catch (_: ConcurrentModificationException) {}
    }
}