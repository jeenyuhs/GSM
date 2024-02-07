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
    private val balloonBarragePos = BlockPos(-43, 73, 20)
    private var timer: Long = 0L

    private var barrageIncomingTitle: TitleRender? = null
    private var barrageFinishedTitle: TitleRender? = null

    @SubscribeEvent
    fun onGameOverlayRender(event: RenderGameOverlayEvent.Text) {
        val matrixStack = UMatrixStack()

        barrageIncomingTitle?.render(matrixStack)
        barrageFinishedTitle?.render(matrixStack)
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (Dungeon.floor != DungeonFloor.Bonzo || !GSM.config.showBalloonTimer) return
        // -43 / 73 / 20
        val nametags = Entity.getEntities<EntityArmorStand>().filter {
            it.name == "§r§lSHOOOOOOOOWWWWWWW TIMEEEEEEE!"
        }

        if (!nametags.none()) {
            // alert user
            barrageFinishedTitle = null

            if (barrageIncomingTitle == null)
                barrageIncomingTitle = TitleRender("§c§lBALLOON BARRAGE INCOMING!!!")
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)
                    .flicker("§lBALLOON BARRAGE INCOMING!!!", 100f)

            // in position, start countdown
            if (nametags.first().position == balloonBarragePos || timer == 0L) {
                // this is about right....
                timer = if (!Dungeon.isMM) {
                    System.currentTimeMillis() + (8 * 1000)
                } else {
                    System.currentTimeMillis() + (14 * 1000)
                }
            }
        }

        if (timer - System.currentTimeMillis() <= 0 && timer != 0L) {
            barrageIncomingTitle = null

            if (barrageFinishedTitle == null)
                barrageFinishedTitle = TitleRender("§a§lBalloon barrage attack is done!")
                    .fadeIn(100f)
                    .flicker("§lBalloon barrage attack is done!", 100f)
                    .flicker("§lBalloon barrage attack is done!", 100f)
                    .flicker("§lBalloon barrage attack is done!", 100f)
                    .flicker("§lBalloon barrage attack is done!", 100f)
                    .flicker("§lBalloon barrage attack is done!", 100f)
                    .fadeOut(100f)

            timer = 0
        }
    }
}