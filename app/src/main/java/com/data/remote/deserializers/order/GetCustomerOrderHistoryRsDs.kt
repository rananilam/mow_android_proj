package com.data.remote.deserializers.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.data.remote.GsonInterface
import com.data.model.order.OrderH
import com.data.remote.response.order.GetCustomerOrderHistoryRs
import java.lang.reflect.Type

class GetCustomerOrderHistoryRsDs : JsonDeserializer<GetCustomerOrderHistoryRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetCustomerOrderHistoryRs? {


        val getCustomerOrderHistoryRs = GetCustomerOrderHistoryRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getCustomerOrderHistoryRs.decodeResult(json)

            if(getCustomerOrderHistoryRs.result.isStatus) {


                if (jsonObject.has("UserRewardsPoint") &&
                    !jsonObject["UserRewardsPoint"].isJsonNull
                ) {
                    getCustomerOrderHistoryRs.userRewardsPoint = jsonObject["UserRewardsPoint"].asFloat
                }

                if (jsonObject.has("orderHistories") &&
                    !jsonObject["orderHistories"].isJsonNull
                ) {

                    val orderHistoriesJsonArray = jsonObject.getAsJsonArray("orderHistories")
                    val listType = object : TypeToken<List<OrderH>>() {}.type
                    val orderList = GsonInterface.getInstance().gson.fromJson<List<OrderH>>(
                            orderHistoriesJsonArray,
                        listType
                    )
                    getCustomerOrderHistoryRs.orderHList = orderList
                }
            }
        }

        return getCustomerOrderHistoryRs

    }
}