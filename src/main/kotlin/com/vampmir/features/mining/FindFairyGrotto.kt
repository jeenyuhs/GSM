package com.vampmir.features.mining

import com.vampmir.GSM
import com.vampmir.utils.Player
import com.vampmir.utils.Render
import gg.essential.universal.UMatrixStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.minecraft.block.BlockStainedGlass
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Blocks
import net.minecraft.item.EnumDyeColor
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.awt.Color


object FindFairyGrotto {
    var jasperGemstones: ArrayList<BlockPos> = ArrayList()
    val listOfFoundColors: ArrayList<EnumDyeColor> = ArrayList()

    private fun centeroid(): BlockPos? {
        var averageX = 0
        var averageY = 0
        var averageZ = 0

        try {
            jasperGemstones.forEach {
                averageX += it.x
                averageY += it.y
                averageZ += it.z
            }
        } catch(_: ConcurrentModificationException) {
            return null
        } finally {
            averageX /= jasperGemstones.count()
            averageY /= jasperGemstones.count()
            averageZ /= jasperGemstones.count()
        }
        return BlockPos(averageX, averageY, averageZ)
    }

    @SubscribeEvent
    fun onWorldLastRender(event: RenderWorldLastEvent) {
        if (!Player.inCrystalHollows || jasperGemstones.isEmpty() || !GSM.config.findFairyGrotto) return

        val matrixStack = UMatrixStack()
        val (viewerX, viewerY, viewerZ) = Render.getViewerPos(event.partialTicks)

        try {
            GlStateManager.disableDepth()
            GlStateManager.disableCull()
            val centeroid = centeroid() ?: jasperGemstones.last()
            Render.renderWaypoint("§dFairy Grotto", centeroid, event.partialTicks, matrixStack)
            jasperGemstones.forEach {
                val x = it.x - viewerX
                val y = it.y - viewerY
                val z = it.z - viewerZ
                GlStateManager.disableDepth()
                GlStateManager.disableCull()
                Render.drawFilledBoundingBox(
                    matrixStack,
                    AxisAlignedBB(
                        x, y, z,
                        x + 1, y + 1, z + 1
                    ),
                    Color(255, 85, 255),
                    0.25f
                )
                GlStateManager.disableLighting()

            }
            GlStateManager.enableDepth()
            GlStateManager.enableCull()
        } catch(_: ConcurrentModificationException) {
        }
    }

    fun onChunkFinishLoad(
        xPosition: Int,
        zPosition: Int,
        ci: CallbackInfo
    ) {
        if (!Player.inCrystalHollows || !GSM.config.findFairyGrotto) return
        CoroutineScope(Dispatchers.IO).launch {
            val world: World = GSM.minecraft.theWorld ?: return@launch
            val x = xPosition shl 4
            val y = 31
            val z = zPosition shl 4

            val x1 = x + 15
            val y1 = 188
            val z1 = z + 15

            val from = BlockPos(x.coerceAtLeast(202), y, z.coerceAtLeast(202))
            val to = BlockPos(x1.coerceAtMost(823), y1, z1.coerceAtMost(823))

            val blockPos = BlockPos.getAllInBox(from, to)

            val fromIgnore = BlockPos(529, 106, 549)
            val toIgnore = BlockPos(502, 130, 562)

            blockPos.forEach {
                val block = world.getBlockState(it) ?: return@forEach
                if (block.block == Blocks.stained_glass || block.block == Blocks.stained_glass_pane) {
                    val blockColor = block.getValue(BlockStainedGlass.COLOR) ?: return@forEach

                    if (!listOfFoundColors.contains(blockColor)) listOfFoundColors.add(blockColor)

                    // make sure it doesn't get the magenta glass in nucleus
                    if (it in fromIgnore..toIgnore) return@forEach

                    if (blockColor == EnumDyeColor.MAGENTA && !jasperGemstones.contains(it)) {
                        jasperGemstones.add(it)
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        jasperGemstones = ArrayList()
    }
}