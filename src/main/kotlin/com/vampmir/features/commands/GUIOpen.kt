package com.vampmir.features.commands

import com.vampmir.GSM
import net.minecraft.client.entity.EntityPlayerSP

object GUIOpen : Base(command = "gsm") {
    override fun process(player: EntityPlayerSP, args: Array<String>) {
        GSM.currentGui = GSM.config.gui()
    }
}