package com.vampmir.features.dungeons

import com.vampmir.GSM
import com.vampmir.utils.Player
import com.vampmir.utils.Render
import gg.essential.universal.UMatrixStack
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.StringUtils
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.awt.Color

object Highlighter {
    private var starredMobs: List<Entity> = emptyList()
    private var shadowAssassin: EntityOtherPlayerMP? = null

    @SubscribeEvent
    fun onWorldLastRender(event: RenderWorldLastEvent) {
        try {
            val (viewerX, viewerY, viewerZ) = Render.getViewerPos(event.partialTicks)
            val matrixStack = UMatrixStack()

            starredMobs.forEach {
                val x = it.posX - viewerX
                val y = it.posY - viewerY
                val z = it.posZ - viewerZ

                Render.drawFilledBoundingBox(
                    matrixStack,
                    AxisAlignedBB(
                        x - 0.5, y, z - 0.5,
                        x + 0.5, y - 2, z + 0.5
                    ),
                    GSM.config.highlightStarredMobsColor,
                    0.25f
                )
            }
            if (shadowAssassin != null) {
                val x = shadowAssassin!!.posX - viewerX
                val y = shadowAssassin!!.posY - viewerY
                val z = shadowAssassin!!.posZ - viewerZ
                Render.drawFilledBoundingBox(
                    matrixStack,
                    AxisAlignedBB(
                        x - 0.5, y, z - 0.5,
                        x + 0.5, y + 2, z + 0.5
                    ),
                    GSM.config.highlightSAColor,
                    0.5f
                )
            }

        } catch (_: ConcurrentModificationException) {}
    }
    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!Player.inDungeons) return

        val world: World = GSM.minecraft.theWorld

        if (GSM.config.highlightStarredMobs) {
            starredMobs = world.loadedEntityList.filterIsInstance<EntityArmorStand>().filter {
                StringUtils.stripControlCodes(it.customNameTag).contains(Regex("✯ (\\w|\\s)* (\\d|,)*"))
            }
        }

        if (GSM.config.highlightSA) {
            shadowAssassin = world.loadedEntityList.filterIsInstance<EntityOtherPlayerMP>().find {
                it.name == "Shadow Assassin"
            }
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        starredMobs = emptyList()
        shadowAssassin = null
    }
}