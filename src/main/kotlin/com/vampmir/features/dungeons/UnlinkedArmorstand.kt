package com.vampmir.features.dungeons

import com.vampmir.GSM
import com.vampmir.utils.Entity
import com.vampmir.utils.Player
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Pattern

object UnlinkedArmorstand {
    private val namePattern = Regex(".*❤")
    @SubscribeEvent
    fun onArmorStandRender(event: RenderLivingEvent.Pre<EntityArmorStand>) {
        if (!Player.inDungeons || event.entity !is EntityArmorStand || !GSM.config.hideUselessArmorStands) return
        if (!StringUtils.stripControlCodes(event.entity.customNameTag).matches(namePattern)) return

        val world = GSM.minecraft.theWorld ?: return

        val pos = event.entity.position
        val a = pos.add(-2, -2 - 3, -2)
        val b = pos.add(2, -2 + 3, 2)
        val bb = AxisAlignedBB(a, b)

        val livingEntities = world.getEntitiesWithinAABB(EntityLiving::class.java, bb)
        val otherPlayerMPEntities = world.getEntitiesWithinAABB(EntityOtherPlayerMP::class.java, bb)

        if (livingEntities.isEmpty() && otherPlayerMPEntities.isEmpty()) event.isCanceled = true
    }
}