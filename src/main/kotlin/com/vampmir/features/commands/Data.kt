package com.vampmir.features.commands

import com.vampmir.utils.Player
import com.vampmir.utils.debug
import net.minecraft.client.entity.EntityPlayerSP

object Data : Base(command = "what") {
    override fun process(player: EntityPlayerSP, args: Array<String>) {
        player.debug("in dungeon: " + Player.inDungeons.toString())
        player.debug("on skyblock: " + Player.onSkyblock.toString())
        player.debug("in dwarven mines: " + Player.inDwarvenMines.toString())
        player.debug("in crystal hollows: " + Player.inCrystalHollows.toString())
    }
}