package com.vampmir.features.qol

import com.vampmir.GSM
import com.vampmir.utils.Player
import com.vampmir.utils.TitleRender
import com.vampmir.utils.chat
import gg.essential.universal.ChatColor
import gg.essential.universal.UMatrixStack
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

object Drops {
    private val dropPattern: Regex = Regex("(?<dropType>.*) DROP! (?<dropName>.*) .*\\((?<magicFind>.*)\\).*")

    var title: TitleRender? = null

    @SubscribeEvent
    fun onGameOverlayRender(event: RenderGameOverlayEvent.Text) {
        if (title != null) {
            title!!.render(UMatrixStack())
        }
    }

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (!Player.onSkyblock) return

        val message = event.message.formattedText ?: return

        if (dropPattern.containsMatchIn(message)) {
            if (GSM.config.announceRareDrops) {
                val dropType = dropPattern.matchEntire(message)!!.groups["dropType"]!!.value
                val dropName = dropPattern.matchEntire(message)!!.groups["dropName"]!!.value
                val magicFind = dropPattern.matchEntire(message)!!.groups["magicFind"]!!.value
                title = TitleRender("$dropType DROP!", "$dropName §r§b($magicFind)§r", 120f, 2000f, 200f)
            }

            if (GSM.config.copyRareDrops) {
                GSM.minecraft.thePlayer.chat("${ChatColor.GOLD}[${ChatColor.YELLOW}GSM${ChatColor.GOLD}] ${ChatColor.DARK_GRAY}> ${ChatColor.GOLD}Added drop to clipboard!")
                Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(event.message.unformattedText), null)
            }
        }
    }
}