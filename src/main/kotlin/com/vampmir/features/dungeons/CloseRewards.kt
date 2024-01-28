package com.vampmir.features.dungeons

import com.vampmir.GSM
import com.vampmir.utils.Player
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object CloseRewards {
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!GSM.config.closeReward || !Player.inDungeons)
            return

        val screen: GuiScreen = GSM.minecraft.currentScreen ?: return

        if (screen is GuiChest) {
            val chest = screen.inventorySlots as ContainerChest

            // only close rewards chest and not things such as skyblock menu.
            if (chest.lowerChestInventory.name == "Large Chest" || chest.lowerChestInventory.name == "Chest") {
                GSM.minecraft.thePlayer.closeScreen()
            }
        }
    }
}