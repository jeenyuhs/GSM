package com.vampmir.features.qol

import com.vampmir.GSM
import com.vampmir.utils.Player
import com.vampmir.utils.TitleRender
import com.vampmir.utils.chat
import gg.essential.universal.ChatColor
import gg.essential.universal.UMatrixStack
import net.minecraft.util.StringUtils
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

        if (dropPattern.matches(message)) {
            if (GSM.config.announceRareDrops) {
                val dropType = dropPattern.matchEntire(message)!!.groups["dropType"]!!.value
                val dropName = dropPattern.matchEntire(message)!!.groups["dropName"]!!.value
                val magicFind = dropPattern.matchEntire(message)!!.groups["magicFind"]!!.value

                if (GSM.config.ignoredRareDrops.split(", ").find { it == StringUtils.stripControlCodes(dropName) } != null) {
                    if (GSM.config.hideIgnoredDropsMessages) event.isCanceled = true

                    return
                }

                val titleNoColor = "§l" + StringUtils.stripControlCodes("$dropType DROP!")

                title = TitleRender("$dropType DROP!", "$dropName §r§b($magicFind)§r").fadeIn(120f)
                    .flicker(titleNoColor, 100f)
                    .flicker(titleNoColor, 100f)
                    .flicker(titleNoColor, 100f)
                    .flicker(titleNoColor, 100f)
                    .fadeOut(200f)
            }
            if (GSM.config.copyRareDrops) {
                GSM.minecraft.thePlayer.chat("${ChatColor.GOLD}[${ChatColor.YELLOW}GSM${ChatColor.GOLD}] ${ChatColor.DARK_GRAY}> ${ChatColor.GOLD}Added drop to clipboard!")
                Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(event.message.unformattedText), null)
            }
        }
    }
}