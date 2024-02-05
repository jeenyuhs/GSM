package com.vampmir.features.dungeons

import com.vampmir.GSM
import com.vampmir.features.qol.Drops
import com.vampmir.utils.Entity
import com.vampmir.utils.TitleRender
import com.vampmir.utils.chat
import gg.essential.universal.UMatrixStack
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import sun.java2d.SurfaceDataProxy.CountdownTracker

object BalloonTimer {
    var ticksCur = 0
    val balloonBarragePos = BlockPos(-43, 73, 20)
    var timer: Long = 0L

    var warningTitle: TitleRender? = null
    var barrageFinishedTitle: TitleRender? = null

    @SubscribeEvent
    fun onGameOverlayRender(event: RenderGameOverlayEvent.Text) {
        val matrixStack = UMatrixStack()
        if (warningTitle != null) {
            warningTitle!!.render(matrixStack)
        }

        if (barrageFinishedTitle != null) {
            barrageFinishedTitle!!.render(matrixStack)
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (Dungeon.floor != DungeonFloor.Bonzo && !GSM.config.showBalloonTimer) return
        // -43 / 73 / 20
        val nametags = Entity.getEntities<EntityArmorStand>().filter {
            it.name == "§r§lSHOOOOOOOOWWWWWWW TIMEEEEEEE!"
        }

        if (!nametags.none()) {
            if (warningTitle == null) {
                warningTitle = TitleRender("§c§lBALLOON BARRAGE INCOMING!!!")
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)
            }

            // in position, start countdown
            if (nametags.first().position == balloonBarragePos) {
                timer = System.currentTimeMillis() + (9 * 1000) // 10 seconds
            }
        }

        if (timer - System.currentTimeMillis() <= 0 && timer != 0L) {
             barrageFinishedTitle = TitleRender("§a§lBalloon barrage finished!").fadeIn(50f)
                 .stay(250f)
                 .flicker("§lBalloon barrage finished!", 250f)
                 .flicker("§lBalloon barrage finished!", 250f)
                 .flicker("§lBalloon barrage finished!", 250f)
                 .stay(500f)
                .fadeOut(100f)
            timer = 0

            // just to be sure, make warningTitle null, so if there is another balloon barrage
            // attack, it will display a new title.
            warningTitle = null
        }

        ticksCur++

    }
}