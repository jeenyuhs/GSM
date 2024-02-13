package com.vampmir.features.qol

import com.vampmir.GSM
import com.vampmir.utils.Player
import gg.essential.elementa.font.DefaultFonts
import gg.essential.universal.UMatrixStack
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.awt.Color

object WrongBookCombiner {
    private var warnPlayer: Boolean = false

    private fun getEnchantmentName(item: ItemStack): String? {
        val tag: NBTTagCompound = item.tagCompound ?: return null
        val display: NBTTagCompound = tag.getTag("display") as? NBTTagCompound ?: return null
        val lore: NBTTagList = display.getTag("Lore") as? NBTTagList ?: return null
        val enchantment: NBTBase = lore.get(0) ?: return null
        return StringUtils.stripControlCodes(enchantment.toString())
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!GSM.config.alertWrongCombination || !Player.onSkyblock)
            return

        val screen: GuiScreen? = GSM.minecraft.currentScreen

        if (screen == null && warnPlayer) {
            warnPlayer = false
            return
        }

        if (screen is GuiChest) {
            if (warnPlayer) warnPlayer = false

            val chestContainer: ContainerChest = screen.inventorySlots as ContainerChest
            val inventory: IInventory = chestContainer.lowerChestInventory ?: return

            if (inventory.name != "Anvil")
                return

            val left: ItemStack = inventory.getStackInSlot(29) ?: return
            val right: ItemStack = inventory.getStackInSlot(33) ?: return

            if (StringUtils.stripControlCodes(left.displayName) != "Enchanted Book" || StringUtils.stripControlCodes(right.displayName) != "Enchanted Book")
                return

            val leftName: String = getEnchantmentName(left) ?: return
            val rightName: String = getEnchantmentName(right) ?: return


            warnPlayer = leftName != rightName
        }
    }

    @SubscribeEvent
    fun onRenderGameOverlay(event:  GuiScreenEvent.BackgroundDrawnEvent) {
        if (warnPlayer) {
            val text = "You're about to combine two books together, that aren't the same type!"

            val strWidth = DefaultFonts.VANILLA_FONT_RENDERER.getStringWidth(text, 1f)
            val x = (GSM.minecraft.currentScreen.width - strWidth) / 2
            DefaultFonts.VANILLA_FONT_RENDERER.drawString(
                UMatrixStack(),
                text,
                Color(255, 85, 20),
                x,
                75f,
                1f,
                1f,
                true,
            )
        }
    }

    fun preventWrongBookCombine(slot: Slot?, slotId: Int, clickedButton: Int, clickType: Int, ci: CallbackInfo) {
        if (slotId == 22 && warnPlayer && GSM.config.preventWrongCombination) {
            ci.cancel()
        }
    }
}