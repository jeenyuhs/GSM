package com.vampmir.features.dungeons

import com.vampmir.GSM
import com.vampmir.utils.Entity
import com.vampmir.utils.Render
import com.vampmir.utils.debug
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.awt.Color

private enum class LividEntity(val id: Int) {
    White(0) {
        override fun getName(): String = "Vandetta Livid"
        override fun getColor(): Color = Color(255, 255, 255)
    },

    Pink(6) {
        override fun getName(): String = "Crossed Livid"
        override fun getColor(): Color = Color(255, 85, 255)
    },

    Red(14) {
        override fun getName(): String = "Hockey Livid"
        override fun getColor(): Color = Color(255, 85, 85)
    },

    Gray(7) {
        override fun getName(): String = "Doctor Livid"
        override fun getColor(): Color = Color(85, 85, 85)
    },

    Green(13) {
        override fun getName(): String = "Frog Livid"
        override fun getColor(): Color = Color(0, 170, 0)
    },

    Lime(5) {
        override fun getName(): String = "Smile Livid"
        override fun getColor(): Color = Color(85, 255, 85)
    },

    Blue(11) {
        override fun getName(): String = "Scream Livid"
        override fun getColor(): Color = Color(85, 85, 255)
    },

    Purple(10) {
        override fun getName(): String = "Purple Livid"
        override fun getColor(): Color = Color(170, 0, 170)
    },

    Yellow(4) {
        override fun getName(): String = "Arcade Livid"
        override fun getColor(): Color = Color(255, 255, 85)
    };

    abstract fun getName(): String
    abstract fun getColor(): Color
}

object LividSolver {
    private val colorPos: BlockPos = BlockPos(5, 108, 25)
    private var lividEntity: EntityOtherPlayerMP? = null
    private var livid: LividEntity? = null

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!GSM.config.highlightLivid) return
        if (Dungeon.floor != DungeonFloor.Livid || !Dungeon.fightingBoss) return
        val world: World = GSM.minecraft.theWorld
        val color = world.getChunkFromBlockCoords(colorPos)?.getBlockMetadata(colorPos) ?: return
        livid = LividEntity.values().find { it.id == color } ?: return

        Entity.getEntities<EntityOtherPlayerMP>().forEach {
            if (livid == null) return@forEach

            if (it.name == livid!!.getName()) {
                GSM.minecraft.thePlayer.debug("Real livid is ${it.name} and at location ${it.playerLocation.toString()}")
                lividEntity = it
            }
        }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (lividEntity == null || !GSM.config.highlightLivid) return
        if (Dungeon.floor != DungeonFloor.Livid || !Dungeon.fightingBoss) return

        GlStateManager.disableCull()
        GlStateManager.disableDepth()

        Render.drawOutlinedBoundingBox(lividEntity!!.entityBoundingBox, livid!!.getColor(), 3f, event.partialTicks)
        GlStateManager.disableLighting()

        GlStateManager.enableCull()
        GlStateManager.enableDepth()

    }
}