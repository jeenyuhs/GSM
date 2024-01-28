package com.vampmir.features.commands

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

abstract class Base(private val command: String) : CommandBase() {
    override fun getCommandName(): String = command
    override fun getCommandUsage(sender: ICommandSender?): String = "/$command"
    override fun getRequiredPermissionLevel(): Int = 0

    abstract fun process(player: EntityPlayerSP, args: Array<String>)
    override fun processCommand(sender: ICommandSender?, args: Array<String>) = process(sender as EntityPlayerSP, args)
}