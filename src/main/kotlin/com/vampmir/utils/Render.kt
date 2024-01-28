package com.vampmir.utils

import com.vampmir.GSM
import com.vampmir.mixin.accessors.AccessorMinecraft
import gg.essential.elementa.font.DefaultFonts
import gg.essential.elementa.font.ElementaFonts
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.ChatColor
import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Vector3f
import java.awt.Color
import kotlin.math.roundToInt
import kotlin.math.sqrt

// Most of these functions are taken from Skytils' render utilities

internal fun <T> Color.withParts(block: (Int, Int, Int, Int) -> T) =
    block(this.red, this.green, this.blue, this.alpha)

object Render {
    fun getPartialTicks() = (GSM.minecraft as AccessorMinecraft).timer.renderPartialTicks

    fun interpolate(currentValue: Double, lastValue: Double, multiplier: Float): Double {
        return lastValue + (currentValue - lastValue) * multiplier
    }

    @JvmStatic
    fun drawOutlinedBoundingBox(aabb: AxisAlignedBB?, color: Color, width: Float, partialTicks: Float) {
        val render = GSM.minecraft.renderViewEntity
        val realX = interpolate(render.posX, render.lastTickPosX, partialTicks)
        val realY = interpolate(render.posY, render.lastTickPosY, partialTicks)
        val realZ = interpolate(render.posZ, render.lastTickPosZ, partialTicks)
        GlStateManager.pushMatrix()
        GlStateManager.translate(-realX, -realY, -realZ)
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GL11.glLineWidth(width)
        RenderGlobal.drawOutlinedBoundingBox(aabb, color.red, color.green, color.blue, color.alpha)
        GlStateManager.translate(realX, realY, realZ)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }

    fun drawFilledBoundingBox(matrixStack: UMatrixStack, aabb: AxisAlignedBB, c: Color, alphaMultiplier: Float = 1f) {
        UGraphics.enableBlend()
        UGraphics.disableLighting()
        UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
        val wr = UGraphics.getFromTessellator()
        wr.beginWithDefaultShader(UGraphics.DrawMode.QUADS, DefaultVertexFormats.POSITION_COLOR)
        val adjustedAlpha = (c.alpha * alphaMultiplier).toInt().coerceAtMost(255)

        // vertical
        c.withAlpha(adjustedAlpha).withParts { r, g, b, a ->
            // bottom
            wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
            // top
            wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
        }

        // x axis
        c.withParts { r, g, b, a ->
            Color(
                (r * 0.8f).toInt(),
                (g * 0.8f).toInt(),
                (b * 0.8f).toInt(),
                adjustedAlpha
            )
        }.withParts { r, g, b, a ->
            // west
            wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
            // east
            wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
        }

        // z axis
        c.withParts { r, g, b, a ->
            Color(
                (r * 0.9f).toInt(),
                (g * 0.9f).toInt(),
                (b * 0.9f).toInt(),
                adjustedAlpha
            )
        }.withParts { r, g, b, a ->
            // north
            wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex()
            // south
            wr.pos(matrixStack, aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
            wr.pos(matrixStack, aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex()
        }

        wr.drawDirect()
        UGraphics.disableBlend()
        UGraphics.enableLighting()
    }

    fun renderWaypoint(
        str: String,
        loc: BlockPos,
        partialTicks: Float,
        matrixStack: UMatrixStack
    ) = renderWaypoint(str, Vector3f(loc.x.toFloat(), loc.y.toFloat(), loc.z.toFloat()), partialTicks, matrixStack)

    fun renderWaypoint(
        str: String,
        loc: Vector3f,
        partialTicks: Float,
        matrixStack: UMatrixStack
    ) {
        GlStateManager.alphaFunc(516, 0.1f)
        matrixStack.push()
        val (viewerX, viewerY, viewerZ) = getViewerPos(partialTicks)
        val distX = loc.x - viewerX
        val distY = loc.y - viewerY - GSM.minecraft.renderViewEntity.eyeHeight
        val distZ = loc.z - viewerZ
        val dist = sqrt(distX * distX + distY * distY + distZ * distZ)
        val renderX: Double
        val renderY: Double
        val renderZ: Double
        if (dist > 12) {
            renderX = distX * 12 / dist + viewerX
            renderY = distY * 12 / dist + viewerY + GSM.minecraft.renderViewEntity.eyeHeight
            renderZ = distZ * 12 / dist + viewerZ
        } else {
            renderX = loc.x.toDouble()
            renderY = loc.y.toDouble()
            renderZ = loc.z.toDouble()
        }
        drawNametag(renderX, renderY, renderZ, "$str ${ChatColor.DARK_GRAY}/ ${ChatColor.YELLOW}${dist.roundToInt()}${ChatColor.GOLD}m", Color.WHITE, partialTicks, matrixStack)
        matrixStack.rotate(-GSM.minecraft.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        matrixStack.rotate(GSM.minecraft.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        matrixStack.pop()
        UGraphics.disableLighting()
    }

    private fun drawNametag(
        x: Double, y: Double, z: Double,
        str: String, color: Color,
        partialTicks: Float, matrixStack: UMatrixStack,
        shadow: Boolean = true, scale: Float = 1f, background: Boolean = false
    ) {
        val player = GSM.minecraft.thePlayer
        val x1 = x - player.lastTickPosX + (x - player.posX - (x - player.lastTickPosX)) * partialTicks
        val y1 = y - player.lastTickPosY + (y - player.posY - (y - player.lastTickPosY)) * partialTicks
        val z1 = z - player.lastTickPosZ + (z - player.posZ - (z - player.lastTickPosZ)) * partialTicks
        val f1 = 0.0266666688
        val width = GSM.minecraft.fontRendererObj.getStringWidth(str) / 2
        matrixStack.push()
        matrixStack.translate(x1, y1, z1)
        GL11.glNormal3f(0f, 1f, 0f)
        matrixStack.rotate(-GSM.minecraft.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        matrixStack.rotate(GSM.minecraft.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        matrixStack.scale(-f1, -f1, -f1)
        UGraphics.disableLighting()
        UGraphics.depthMask(false)
        UGraphics.disableBlend()
        UGraphics.enableBlend()
        UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
        if (background) {
            val worldRenderer = UGraphics.getFromTessellator()
            worldRenderer.beginWithDefaultShader(UGraphics.DrawMode.QUADS, DefaultVertexFormats.POSITION_COLOR)
            worldRenderer.pos(matrixStack, (-width - 1.0), -1.0, 0.0).color(0f, 0f, 0f, 0.25f).endVertex()
            worldRenderer.pos(matrixStack, (-width - 1.0), 8.0, 0.0).color(0f, 0f, 0f, 0.25f).endVertex()
            worldRenderer.pos(matrixStack, width + 1.0, 8.0, 0.0).color(0f, 0f, 0f, 0.25f).endVertex()
            worldRenderer.pos(matrixStack, width + 1.0, -1.0, 0.0).color(0f, 0f, 0f, 0.25f).endVertex()
            worldRenderer.drawDirect()
        }
        GlStateManager.enableTexture2D()
        DefaultFonts.VANILLA_FONT_RENDERER.drawString(
            matrixStack,
            str,
            color,
            -width.toFloat(),
            ElementaFonts.MINECRAFT.getBelowLineHeight() * scale,
            width * 2f,
            scale,
            shadow
        )
        UGraphics.depthMask(true)
        UGraphics.enableBlend()
        UGraphics.enableDepth()
        matrixStack.pop()
    }

    fun getViewerPos(partialTicks: Float): Triple<Double, Double, Double> {
        val viewer = GSM.minecraft.renderViewEntity
        val viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks
        return Triple(viewerX, viewerY, viewerZ)
    }
}