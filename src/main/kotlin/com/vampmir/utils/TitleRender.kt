package com.vampmir.utils

import com.vampmir.GSM
import gg.essential.elementa.font.DefaultFonts
import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import net.minecraft.client.gui.ScaledResolution
import java.awt.Color



class TitleRender(
    private var title: String,
    private var subtitle: String? = null,
) {
    private val originalTitle = title

    private val scaledResolution = ScaledResolution(GSM.minecraft)
    private val scaledWidth = scaledResolution.scaledWidth
    private val scaledHeight = scaledResolution.scaledHeight

    private enum class State {
        FADE_IN,
        DISPLAY,
        FLICKER,
        FADE_OUT
    }

    private data class Animation(
        val type: State,
        val time: Float,

        val flickerText: String? = null
    )

    private var titleOpacity = 0.0f
    private var lastFrameTime = System.currentTimeMillis()
    private var elapsedTime = 0L

    private var animations: ArrayList<Animation> = ArrayList()

    fun fadeIn(time: Float): TitleRender {
        animations.add(Animation(type = State.FADE_IN, time = time))
        return this
    }

    fun fadeOut(time: Float): TitleRender {
        animations.add(Animation(type = State.FADE_OUT, time = time))
        return this
    }

    fun stay(time: Float): TitleRender {
        animations.add(Animation(type = State.DISPLAY, time = time))
        return this
    }

    fun flicker(flicker: String, flickerFor: Float): TitleRender {
        animations.add(Animation(
            type = State.FLICKER,
            time = flickerFor,
            flickerText = flicker
        ))
        animations.add(Animation(type = State.DISPLAY, time = flickerFor))
        return this
    }

    fun roll(time: Float): TitleRender {
        val titleNoColor = net.minecraft.util.StringUtils.stripControlCodes(title)
        val timeInBetween =  time / titleNoColor.length
        for (i in titleNoColor.indices) {
            val builder = StringBuilder()

            if (i > 0) {
                builder.append("§r§9§l${titleNoColor.substring(0, i)}")
            }

            builder.append("§r§l${titleNoColor[i]}")

            if (i < titleNoColor.length - 1) {
                builder.append("§r§9§l${titleNoColor.substring(i + 1)}")
            }

            animations.add(Animation(
                type = State.FLICKER,
                time = timeInBetween + 50,
                flickerText = builder.toString()
            ))
            animations.add(Animation(type = State.DISPLAY, time = timeInBetween))
        }

        return this
    }

    private fun getCurrentAnimation(): Animation? {
        var totalTime = 0f
        animations.forEach {
            totalTime += it.time

            if (elapsedTime <= totalTime) {
                return it
            }
        }

        return null
    }

    fun render(matrixStack: UMatrixStack) {
        val animation = getCurrentAnimation() ?: return

        val currentTime = System.currentTimeMillis()
        val deltaTime: Long = currentTime - lastFrameTime // Time since last frame in seconds
        lastFrameTime = currentTime
        elapsedTime += deltaTime


        when (animation.type) {
            State.FADE_IN -> {
                titleOpacity += deltaTime / animation.time // 120ms for full opacity
                if (titleOpacity >= 1.0f) {
                    titleOpacity = 1.0f
                }
            }

            State.DISPLAY -> {
                titleOpacity = 1.0f
                title = originalTitle
            }

            State.FLICKER -> {
                title = animation.flickerText!!
            }

            State.FADE_OUT -> {
                titleOpacity -= deltaTime / animation.time // 200ms for full fade out

                if (titleOpacity <= 0.0f) {
                    titleOpacity = 0.0f
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
        if (subtitle != null) {
            matrixStack.scale(2.0f * 0.7f, 2.0f * 0.7f, 2.0f * 0.7f)
            DefaultFonts.VANILLA_FONT_RENDERER.drawString(
                matrixStack,
                subtitle!!,
                Color(255, 255, 255, (titleOpacity * 255).toInt()),
                (-GSM.minecraft.fontRendererObj.getStringWidth(subtitle!!) / 2).toFloat(),
                5f,
                1f,
                1f
            )
            matrixStack.pop()
        }
        UGraphics.disableBlend()
        matrixStack.pop()
    }

    fun reset() {
        elapsedTime = 0L
        lastFrameTime = System.currentTimeMillis()
    }
}