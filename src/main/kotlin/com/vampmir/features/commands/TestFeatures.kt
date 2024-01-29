package com.vampmir.features.commands

import com.vampmir.GSM
import com.vampmir.features.qol.Drops
import com.vampmir.utils.TitleRender
import com.vampmir.utils.chat
import net.minecraft.client.entity.EntityPlayerSP

object TestFeatures : Base(command="test") {
    override fun process(player: EntityPlayerSP, args: Array<String>) {
        if (args.count() == 1 && args[0] == "drop.announce") {
            Drops.title = TitleRender("§r§9§lVERY RARE DROP!", "§r§f§r§a◆ Bite Rune I§r§7 §r§b(+111% §r§b✯ Magic Find§r§b)§r", 120f, 2000f, 200f)
        }


        GSM.minecraft.thePlayer.chat("§aSuccess!")
    }
}