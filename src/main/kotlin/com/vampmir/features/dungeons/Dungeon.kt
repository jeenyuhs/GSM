package com.vampmir.features.dungeons

import com.vampmir.GSM
import com.vampmir.utils.Player
import com.vampmir.utils.debug
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.StringUtils
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

enum class DungeonFloor(val floor: Int) {
    Unknown(0),
    Bonzo(1),
    Scarf(2),
    Professor(3),
    Thorn(4),
    Livid(5),
    Sadan(6),
    Necron(7)
}

object Dungeon {
    var floor: DungeonFloor = DungeonFloor.Unknown
    var fightingBoss: Boolean = false

    var isMM: Boolean = false

    private val floorRegex = Regex(".*\\(F(\\d)\\).*")

    fun initialize() {
        listOf(
            this,
            CloseRewards,
            InstantRequeue,
            LividSolver
        ).forEach(MinecraftForge.EVENT_BUS::register)
    }

    private fun fixScoreboardLines(line: String): String {
        // remove the real funny emojis in the scoreboard.
        return line.replace(Regex("[^\\x00-\\x7F]"), "")
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!Player.inDungeons) return
        val world: World = GSM.minecraft.theWorld ?: return
        val scoreboard = world.scoreboard.getObjectiveInDisplaySlot(1) ?: return

        val scores = world.scoreboard.getSortedScores(scoreboard)

        scores.forEach {
            // get the score line
            val teamName = world.scoreboard.getPlayersTeam(it.playerName) ?: return@forEach
            val line = fixScoreboardLines(StringUtils.stripControlCodes(ScorePlayerTeam.formatPlayerName(teamName, it.playerName)))

            // use regex to find the floor number.
            if (floorRegex.matches(line) && floor == DungeonFloor.Unknown) {
                val floorNumber: Int  = floorRegex.matchEntire(line)?.groupValues?.get(1)?.toInt() ?: return@forEach

                if (floorNumber > 7 || floorNumber < 1) {
                    GSM.minecraft.thePlayer.debug("Unexpected floor. Parsed floor $floorNumber?")
                    return@forEach
                }

                floor = DungeonFloor.values()[floorNumber]
            }

            if (line.contains(floor.name.lowercase()) && !fightingBoss) {
                fightingBoss = true
            }
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        isMM = false
        fightingBoss = false
        floor = DungeonFloor.Unknown
    }


}