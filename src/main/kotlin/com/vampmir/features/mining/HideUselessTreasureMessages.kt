package com.vampmir.features.mining

import com.vampmir.GSM
import com.vampmir.utils.Player
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object HideUselessTreasureMessages {
    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (!Player.onSkyblock || !GSM.config.ignoreUselessTreasureChat) return

        val message = event.message.unformattedText ?: return

        if (message.startsWith("You received")) {
            if (!message.endsWith("Mithril Powder.") && !message.endsWith("Gemstone Powder.")) {
                GSM.logger.info(message)
                event.isCanceled = true
            }
        }
    }
}