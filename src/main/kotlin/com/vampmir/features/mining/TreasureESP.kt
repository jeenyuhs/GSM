package com.vampmir.features.mining

import com.vampmir.GSM
import com.vampmir.utils.Player
import com.vampmir.utils.Render
import gg.essential.universal.ChatColor
import gg.essential.universal.UMatrixStack
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import org.lwjgl.util.vector.Vector3f
import kotlin.math.abs
import kotlin.math.sqrt


data class TimedTreasure(
    val position: BlockPos,
    var lastUpdated: Long
)

object TreasureESP {
    private var chestsLocation: ArrayList<TimedTreasure> = ArrayList()

    @SubscribeEvent
    fun onWorldLastRender(event: RenderWorldLastEvent) {
        if (!Player.onSkyblock || chestsLocation.isEmpty() || !GSM.config.enableTreasureESP) return
        val matrixStack = UMatrixStack()
        chestsLocation.forEachIndexed { index, it ->
            if (abs(it.lastUpdated - System.currentTimeMillis()) > 200) return@forEachIndexed
            GlStateManager.disableDepth()
            GlStateManager.disableCull()
            Render.renderWaypoint("${ChatColor.GOLD}Treasure", Vector3f(
                (it.position.x + 0.5).toFloat(),
                (it.position.y + 1.5).toFloat(),
                (it.position.z + 0.5).toFloat()
            ), event.partialTicks, matrixStack)
            GlStateManager.disableLighting()
            GlStateManager.enableDepth()
            GlStateManager.enableCull()
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!GSM.config.enableTreasureESP || !Player.inCrystalHollows) return

        val world: World = GSM.minecraft.theWorld ?: return
        val player: EntityPlayerSP = GSM.minecraft.thePlayer ?: return

        world.loadedTileEntityList.forEach { tile: TileEntity? ->
            if (tile == null) return@forEach
            if (tile.blockType != Blocks.chest) return@forEach

            val viewerX: Double = player.lastTickPosX + (player.posX - player.lastTickPosX) * Render.getPartialTicks()
            val viewerY: Double = player.lastTickPosY + (player.posY - player.lastTickPosY) * Render.getPartialTicks()
            val viewerZ: Double = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * Render.getPartialTicks()

            val x: Double = tile.pos.x - viewerX + 0.5f
            val y: Double = tile.pos.y - viewerY - player.eyeHeight
            val z: Double = tile.pos.z - viewerZ + 0.5f

            val distSq = x * x + y * y + z * z
            val dist = sqrt(distSq)

            val chest = chestsLocation.find {
                it.position == tile.pos
            }

            if (chest == null) {
                if (dist <= 15) {
                    val treasure = TimedTreasure(tile.pos, System.currentTimeMillis())
                    chestsLocation.add(treasure)
                }
            } else {
                // only update the treasure, if the player is within a 15 block radius.
                if (dist <= 15) {
                    chest.lastUpdated = System.currentTimeMillis()
                }
            }
        }
    }

    @SubscribeEvent
    fun onWorldRender(event: WorldEvent.Load) {
        chestsLocation = ArrayList()
    }
}
