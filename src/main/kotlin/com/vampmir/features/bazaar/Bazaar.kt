package com.vampmir.features.bazaar

object Bazaar
//
//import com.google.gson.Gson
//import com.google.gson.annotations.SerializedName
//import com.vampmir.GSM
//import com.vampmir.utils.Player
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers.IO
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import net.minecraft.client.gui.GuiScreen
//import net.minecraft.client.gui.inventory.GuiChest
//import net.minecraft.inventory.ContainerChest
//import net.minecraft.inventory.IInventory
//import net.minecraftforge.client.event.ClientChatReceivedEvent
//import net.minecraftforge.common.MinecraftForge
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
//import okhttp3.*
//import java.io.IOException
//import java.util.regex.Pattern
//
//enum class OrderStatus {
//    OUTDATED,
//    MATCHED
//}
//
//enum class OrderType {
//    SELL,
//    BUY
//}
//
//data class BazaarOrder(val internalId: String, val type: OrderType, val totalPrice: Int, val quantity: Int, val timestamp: Int, val status: OrderStatus)
//
//data class Summary(
//    val amount: Int,
//    val pricePerUnit: Float,
//    val orders: Int
//)
//
//data class BazaarProducts(
//    @SerializedName("product_id")
//    val internalId: String,
//
//    @SerializedName("sell_summary")
//    val sellSummary: List<Summary>,
//
//    @SerializedName("buy_summary")
//    val buySummary: List<Summary>,
//
//)
//
//data class BazaarData(
//    val products: Map<String, BazaarProducts>
//)
//
//object Bazaar {
//    private val client = OkHttpClient()
//
//    private val gson: Gson = Gson()
//
//    private val offerPattern = Pattern.compile("\\[Bazaar] (?<type>.*) (?:Offer|Order) Setup! (?<amount>\\d*)x (?<name>.*) for (?<cost>.*) coins\\.")
//    private val buyClaimPattern = Pattern.compile("\\[Bazaar] (?<type>.*) (\\d*)x (.*) worth (?<cost>.*) coins .*")
//    private val sellClaimPattern = Pattern.compile("\\[Bazaar] Claimed .* coins from selling (?<amount>\\d*)x (?<name>.*) at (?<cost>.*) each!")
//    private val cancelPattern = Pattern.compile("\\[Bazaar] Cancelled! Refunded (?:(?<amount>\\d)x (.*)|(?<cost>.*) coins) from cancelling (?<type>.*) (?:Order|Offer)")
//
//    private val filledPattern = Pattern.compile("\\[Bazaar] Your (?<type>Sell|Buy) (?:Offer|Order) for (?<amount>\\d*)x (?<name>.*) was filled!")
//
//    var orders: List<BazaarOrder> = emptyList()
//    var bazaarData: BazaarData? = null
//
//    fun initialize() {
//        MinecraftForge.EVENT_BUS.register(this)
//
//        CoroutineScope(IO).launch {
//            while (true) {
//                getBazaarData()
//
//                // ratelimit is about 1 request per second, so
//                // this should be fine
//                delay(5000)
//            }
//        }
//    }
//
//    private fun getBazaarData() {
//        if (!GSM.config.alertWhenOrderUpdate || !Player.onSkyblock) {
//            // if the user, doesn't want this,
//            // don't constantly request
//            return
//        }
//
//        val request = Request.Builder()
//            .url("https://api.hypixel.net/v2/skyblock/bazaar")
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.use {
//                    if (!it.isSuccessful) throw IOException("Call to Bazaar API was not successful. $it")
//
//                    bazaarData = gson.fromJson(it.body!!.string(), BazaarData::class.java)
//                }
//            }
//
//        })
//    }
//
//    @SubscribeEvent
//    fun onChatReceieved(event: ClientChatReceivedEvent) {
//        val message = event.message.unformattedText ?: return
//
//        if (offerPattern.matcher(message).find()) {
//            val matcher = offerPattern.matcher(message)
//            GSM.logger.info(message)
//            val itemName = matcher.group("name") ?: return
//            GSM.logger.info(itemName)
//        }
//        if (buyClaimPattern.matcher(message).find()) {
//            GSM.logger.info("Caught a message matching the buy claim pattern.")
//        }
//
//        if (sellClaimPattern.matcher(message).find()) {
//            GSM.logger.info("Caught a message matching the sell claim pattern.")
//        }
//        if (cancelPattern.matcher(message).find()) {
//            GSM.logger.info("Caught a message matching the cancel pattern.")
//        }
//    }
//
//    @SubscribeEvent
//    fun onTick(event: ClientTickEvent) {
//        val screen: GuiScreen = GSM.minecraft.currentScreen ?: return
//
//        if (screen is GuiChest) {
//            val lowerChest: IInventory = (screen.inventorySlots as ContainerChest).lowerChestInventory ?: return
//
//            if (lowerChest.name != "Your Bazaar Orders") {
//                return
//            }
//
//            for (i in 0..lowerChest.sizeInventory) {
//                val item = lowerChest.getStackInSlot(i) ?: continue
//            }
//        }
//    }
//}