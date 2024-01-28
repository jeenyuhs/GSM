package com.vampmir.features.dungeons

import com.vampmir.GSM
import com.vampmir.utils.Player
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object InstantRequeue {
    @SubscribeEvent
    fun onChatReceive(event: ClientChatReceivedEvent) {
        if (event.type.toInt() != 0)
            return

        if (!GSM.config.instantRequeue || !Player.inDungeons)
            return

        if (event.message.unformattedText.contains("to re-queue into")) {
            GSM.minecraft.thePlayer.sendChatMessage("/instancerequeue")
        }
    }
}