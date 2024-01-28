package com.vampmir.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.vampmir.GSM
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.scoreboard.ScoreObjective
import net.minecraft.util.ChatComponentText
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object Player {
    private val parser = JsonParser()

    private var map = ""
    private var lobby = ""

    private var checkedLoc = false

    var onSkyblock = false
    var inDungeons = false

    var inDwarvenMines = false
    var inCrystalHollows = false

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!onSkyblock) {
            val player: EntityPlayerSP = GSM.minecraft.thePlayer ?: return
            val scoreboard: ScoreObjective = player.worldScoreboard.getObjectiveInDisplaySlot(1) ?: return
            onSkyblock = StringUtils.stripControlCodes(scoreboard.displayName).contains("SKYBLOCK")
        }

        if (!checkedLoc) {
            GSM.minecraft.thePlayer.sendChatMessage("/locraw")
            checkedLoc = true
        }
    }

    @SubscribeEvent
    fun onChatRecieve(event: ClientChatReceivedEvent) {
        if (event.type.toInt() != 0 || !onSkyblock)
            return

        val message = event.message.unformattedText ?: return

        if (message.startsWith("{") && message.endsWith("}")) {
            try {
                val info: JsonObject = parser.parse(event.message.unformattedText).asJsonObject
                event.isCanceled = true

                if (!(info.has("map") && info.has("server"))) {
                    return
                }

                map = info["map"].toString()
                lobby = info["server"].toString()

                when (map) {
                    "\"Dungeon\"" -> inDungeons = true
                    "\"Dwarven Mines\"" -> inDwarvenMines = true
                    "\"Crystal Hollows\"" -> inCrystalHollows = true
                    else -> {
                        GSM.logger.debug("Map `$map` not implemented or doesn't exist.")
                    }
                }
            } catch (ex: JsonSyntaxException) {
                return
            }
        }
    }

    @SubscribeEvent
    fun onWorldChange(event: WorldEvent.Load) {
        checkedLoc = false
        onSkyblock = false
        inDungeons = false

        inDwarvenMines = false
        inCrystalHollows = false

        lobby = ""
        map = ""
    }

    fun initialize() {
        MinecraftForge.EVENT_BUS.register(this)
    }

}

fun EntityPlayerSP.debug(s: String) {
    if (GSM.config.debugMode) {
        this.addChatMessage(ChatComponentText("§e[GSM DEBUGGER] §b$s"))
    }
}

fun EntityPlayerSP.chat(s: String) {
    this.addChatMessage(ChatComponentText(s))
}
