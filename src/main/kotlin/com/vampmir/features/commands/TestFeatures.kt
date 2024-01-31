package com.vampmir.features.commands

import com.vampmir.GSM
import com.vampmir.features.qol.Drops
import com.vampmir.utils.TitleRender
import com.vampmir.utils.chat
import net.minecraft.client.entity.EntityPlayerSP

object TestFeatures : Base(command="test") {
    override fun process(player: EntityPlayerSP, args: Array<String>) {
        if (args.count() == 1 && args[0] == "drop.announce") {
            val title = "§r§9§lVERY RARE DROP!"
            val titleWithoutColor = "§lVERY RARE DROP!"
            Drops.title = TitleRender(title, "§r§f§r§a◆ Bite Rune I§r§7 §r§b(+111% §r§b✯ Magic Find§r§b)§r")
                .fadeIn(120f)
                .stay(1000f)
                .flicker(titleWithoutColor, 100f)
                .stay(100f)
                .flicker(titleWithoutColor, 100f)
                .stay(100f)
                .flicker(titleWithoutColor, 100f)
                .stay(50f)
                .flicker(titleWithoutColor, 100f)
                .stay(1000f)
                .fadeOut(200f)
        }


        GSM.minecraft.thePlayer.chat("§aSuccess!")
    }
}