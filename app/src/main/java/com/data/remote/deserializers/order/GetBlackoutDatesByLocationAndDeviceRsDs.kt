package com.data.remote.deserializers.order

import com.data.remote.GsonInterface
import com.data.remote.response.order.BlackoutDatesRs
import com.data.remote.response.order.GetBlackoutDatesByLocationAndDeviceRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GetBlackoutDatesByLocationAndDeviceRsDs : JsonDeserializer<GetBlackoutDatesByLocationAndDeviceRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetBlackoutDatesByLocationAndDeviceRs? {


        val validateOrderListTimeRs = GetBlackoutDatesByLocationAndDeviceRs()

      /*  json?.let {
            val blackoutDatesListJsonArray = it.asJsonArray

            if(blackoutDatesListJsonArray.isJsonArray && blackoutDatesListJsonArray.size() >= 1) {

                val listType = object : TypeToken<List<BlackoutDates>>() {}.type
                val blackoutDatesList = GsonInterface.getInstance().gson.fromJson<List<BlackoutDates>>(
                    blackoutDatesListJsonArray,
                    listType
                )
                validateOrderListTimeRs.blackoutDatesList = blackoutDatesList

            }
        }*/

        json?.let {
            val jsonObject = it.asJsonObject

            validateOrderListTimeRs.decodeResult(json)

            if(validateOrderListTimeRs.result.isStatus) {

                if (jsonObject.has("BlackoutDates") &&
                    !jsonObject["BlackoutDates"].isJsonNull
                ) {

                    val orderHistoriesJsonArray = jsonObject.getAsJsonArray("BlackoutDates")
                    val listType = object : TypeToken<List<BlackoutDatesRs>>() {}.type
                    val blackDatesList = GsonInterface.getInstance().gson.fromJson<List<BlackoutDatesRs>>(
                        orderHistoriesJsonArray,
                        listType
                    )
                    validateOrderListTimeRs.blackoutDatesList = blackDatesList
                }
            }
        }


        return validateOrderListTimeRs





    }
}