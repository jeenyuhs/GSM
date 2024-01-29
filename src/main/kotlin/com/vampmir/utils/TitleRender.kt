package com.vampmir.utils

import com.vampmir.GSM
import gg.essential.elementa.font.DefaultFonts
import gg.essential.universal.ChatColor
import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.StringUtils
import java.awt.Color


class TitleRender(
    private var title: String,
    private var subtitle: String,
    private val fadeIn: Float,
    private val stay: Float,
    private val fadeOut: Float,
) {
    private val originalTitle = title
    private val flickerTitle = ChatColor.BOLD + StringUtils.stripControlCodes(title)

    private val scaledResolution = ScaledResolution(GSM.minecraft)
    private val scaledWidth = scaledResolution.scaledWidth
    private val scaledHeight = scaledResolution.scaledHeight

    private enum class State {
        FADE_IN,
        DISPLAY,
        FLICKER,
        FADE_OUT
    }

    private var state = State.FADE_IN
    private var titleOpacity = 0.0f
    private var lastFrameTime = System.currentTimeMillis()
    private var elapsedTime = 0L
    fun render(matrixStack: UMatrixStack) {
        val currentTime = System.currentTimeMillis()
        val deltaTime: Long = currentTime - lastFrameTime // Time since last frame in seconds
        lastFrameTime = currentTime
        elapsedTime += deltaTime

        when (state) {
            State.FADE_IN -> {
                titleOpacity += deltaTime / fadeIn // 120ms for full opacity
                if (titleOpacity >= 1.0f) {
                    titleOpacity = 1.0f
                    state = State.DISPLAY
                }
            }

            State.DISPLAY -> {
                if (elapsedTime <= stay) {
                    state = State.FLICKER
                }
            }

            State.FLICKER -> {
                title = if ((elapsedTime / 100f).toInt() % 2 == 0) { // Flicker every 250ms
                    flickerTitle
                } else {
                    originalTitle
                }
                if (elapsedTime >= fadeIn + stay) { // Flicker for 1000ms
                    state = State.FADE_OUT
                }
            }

            State.FADE_OUT -> {
                titleOpacity -= deltaTime / fadeOut // 200ms for full fade out
                if (titleOpacity <= 0.0f) {
                    titleOpacity = 0.0f
                    elapsedTime = 0L
                }
            }
        }
        matrixStack.push()
        matrixStack.translate((scaledWidth / 2).toFloat(), (scaledHeight / 2.5).toFloat(), 0.0f)
        UGraphics.enableBlend()
        UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
        matrixStack.push()
        matrixStack.scale(4.0f * 0.7f, 4.0f * 0.7f, 4.0f * 0.7f)
        DefaultFonts.VANILLA_FONT_RENDERER.drawString(
            matrixStack,
            title,
            Color(255, 255, 255, (titleOpacity * 255).toInt()),
            (-GSM.minecraft.fontRendererObj.getStringWidth(title) / 2).toFloat(),
            -10.0f,
            1f,
            1f
        )
        matrixStack.pop()
        matrixStack.push()
        matrixStack.scale(2.0f * 0.7f, 2.0f * 0.7f, 2.0f * 0.7f)
        DefaultFonts.VANILLA_FONT_RENDERER.drawString(
            matrixStack,
            subtitle,
            Color(255, 255, 255, (titleOpacity * 255).toInt()),
            (-GSM.minecraft.fontRendererObj.getStringWidth(subtitle) / 2).toFloat(),
            5f,
            1f,
            1f
        )
        matrixStack.pop()
        UGraphics.disableBlend()
        matrixStack.pop()
    }
}