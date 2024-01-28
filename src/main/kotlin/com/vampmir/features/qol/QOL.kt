package com.vampmir.features.qol

import net.minecraftforge.common.MinecraftForge

object QOL {
    fun initialize() {
        listOf(WrongBookCombiner, RenderSetWaypoints).forEach(MinecraftForge.EVENT_BUS::register)
    }
}