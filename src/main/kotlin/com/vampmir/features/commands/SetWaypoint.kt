package com.vampmir.features.commands

import com.vampmir.features.qol.RenderSetWaypoints
import com.vampmir.utils.chat
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.BlockPos

object SetWaypoint : Base(command="setwaypoint") {
    override fun process(player: EntityPlayerSP, args: Array<String>) {
        if (args.count() < 3) return

        if (args[0] == "reset") {
            RenderSetWaypoints.waypoints.clear()
            return
        }

        if (args[0] == "list") {
            player.chat("§aAll set waypoints:")

            RenderSetWaypoints.waypoints.forEachIndexed { index, it ->
                player.chat("#$index: $it")
            }
            return
        }

        RenderSetWaypoints.waypoints.add(BlockPos(args[0].toDouble(), args[1].toDouble(), args[2].toDouble()))
    }
}