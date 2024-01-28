package com.vampmir

import com.vampmir.config.Config
import com.vampmir.config.PersistentData
import com.vampmir.features.commands.*
import com.vampmir.features.dungeons.Dungeon
import com.vampmir.features.mining.Mining
import com.vampmir.features.qol.QOL
import com.vampmir.utils.Player
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.apache.logging.log4j.LogManager
import java.io.File

@Mod(modid = "GSM", useMetadata = true, clientSideOnly = true)
class GSM {
    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        val directory = File(event.modConfigurationDirectory, event.modMetadata.modId)
        directory.mkdirs()
        configDirectory = directory
        config = Config
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        config.initialize()

        MinecraftForge.EVENT_BUS.register(this)

        Player.initialize()
        Dungeon.initialize()
        QOL.initialize()
        Mining.initialize()
    }

    @Mod.EventHandler
    fun onPostInit(e: FMLPostInitializationEvent) {
        PersistentData.loadData()
    }

    @Mod.EventHandler
    fun loadComplete(e: FMLLoadCompleteEvent) {
        listOf(
            GUIOpen,
            Data,
            DungeonCommand,
            SetWaypoint
        ).forEach(ClientCommandHandler.instance::registerCommand)
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || currentGui == null) return
        minecraft.displayGuiScreen(currentGui)
        currentGui = null
    }

    companion object {
        val minecraft: Minecraft = Minecraft.getMinecraft()
        var currentGui: GuiScreen? = null

        var logger = LogManager.getLogger("GSM")

        lateinit var configDirectory: File
        lateinit var config: Config
    }
}
