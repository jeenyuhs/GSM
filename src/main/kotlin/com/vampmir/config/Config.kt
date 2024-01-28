package com.vampmir.config

import com.vampmir.GSM
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File

object Config : Vigilant(
    File(GSM.configDirectory, "config.toml"),
    "Glob's Skyblock Mod"
) {
    // DUNGEONS CATEGORY

    @Property(
        type = PropertyType.SWITCH, name = "Show Livid",
        description = "Highlights the real Livid.",
        category = "Dungeons", subcategory = "Livid (F5)"
    )
    var highlightLivid: Boolean = false

    @Property(
        type = PropertyType.SWITCH, name = "Instant requeue",
        description = "Instantly queues your dungeon party to a new instance.",
        category = "Dungeons", subcategory = "QOL"
    )
    var instantRequeue: Boolean = false

    @Property(
        type = PropertyType.SWITCH, name = "Auto close rewards",
        description = "If the reward is an item, it'll automatically close the chest.",
        category = "Dungeons", subcategory = "QOL"
    )
    var closeReward: Boolean = false

    // QOL CATEGORY
    @Property(
        type = PropertyType.SWITCH, name = "Alert wrong book combination",
        description = "Alerts the player,when they're about to combine two books together, that aren't the same.",
        category = "QOL", subcategory = "Enchanting"
    )
    var alertWrongCombination: Boolean = false

    @Property(
        type = PropertyType.SWITCH, name = "Completely prevent it wrong combinations",
        description = "Goes hand in hand with the above, except it prevents the player from combining them.",
        category = "QOL", subcategory = "Enchanting"
    )
    var preventWrongCombination: Boolean = false

    // BAZAAR
    @Property(
        type = PropertyType.SWITCH, name = "Alert upon order update",
        description = "Alerts the player, if their order is outdated or matched.",
        category = "Bazaar"
    )
    var alertWhenOrderUpdate: Boolean = false

    @Property(
        type = PropertyType.SWITCH, name = "Alert periodically",
        description = "If `update upon order update` and this is enabled, it will periodically alert the player (every 2 minutes).",
        category = "Bazaar"
    )
    var alertOrderUpdatePeriodically: Boolean = false

    // MINING CATEGORY
    @Property(
        type = PropertyType.SWITCH, name = "Show where treasures are",
        description = "This will put a waypoint, where the recently dug treasured is.",
        category = "Mining", subcategory = "Crystal Hollows"
    )
    var enableTreasureESP: Boolean = false

    @Property(
        type = PropertyType.SWITCH, name = "Ignore useless treasure messages",
        description = "Hides messages such as \"You received 80 ⸕ Rough Amber Gemstone.\"",
        category = "Mining", subcategory = "Crystal Hollows"
    )
    var ignoreUselessTreasureChat: Boolean = false

    @Property(
        type = PropertyType.SWITCH, name = "Show Fairy Grotto",
        description = "Will put a waypoint where the fairy grotto is, if any.",
        category = "Mining", subcategory = "Crystal Hollows"
    )
    var findFairyGrotto: Boolean = false

    // DEV CATEGORY
    @Property(
        type =  PropertyType.SWITCH, name = "Debug mode",
        description = "Shows debug messages.", category = "Dev"
    )
    var debugMode: Boolean = false

    init {
        initialize()
    }
}