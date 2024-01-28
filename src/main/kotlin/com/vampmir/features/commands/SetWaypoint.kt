package com.vampmir.features.commands

import com.vampmir.features.qol.RenderSetWaypoints
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.BlockPos

object SetWaypoint : Base(command="setwaypoint") {
    override fun process(player: EntityPlayerSP, args: Array<String>) {
        if (args.count() < 3) return
        RenderSetWaypoints.waypoints.add(BlockPos(args[0].toDouble(), args[1].toDouble(), args[2].toDouble()))
    }
}