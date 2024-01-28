package com.vampmir.features.commands

import com.vampmir.features.dungeons.Dungeon
import com.vampmir.utils.Player
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.ChatComponentText

object DungeonCommand : Base(command = "dungeon") {
    override fun process(player: EntityPlayerSP, args: Array<String>) {
        if (!Player.inDungeons) {
            player.addChatMessage(ChatComponentText("&cYou aren't in a dungeon."))
            return
        }

        player.addChatMessage(ChatComponentText("Master mode?: ${Dungeon.isMM}"))
        player.addChatMessage(ChatComponentText("Dungeon Floor: ${Dungeon.floor.ordinal}"))
        player.addChatMessage(ChatComponentText("Floor boss: ${Dungeon.floor.name}"))
        player.addChatMessage(ChatComponentText("Fighting boss?: ${Dungeon.fightingBoss}"))
    }
}