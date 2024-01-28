package com.vampmir.features.mining

import net.minecraftforge.common.MinecraftForge

object Mining {
    fun initialize() {
        listOf(
            TreasureESP,
            HideUselessTreasureMessages,
            FindFairyGrotto
        ).forEach(MinecraftForge.EVENT_BUS::register)
    }
}