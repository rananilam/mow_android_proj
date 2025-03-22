package com.data.remote.deserializers.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.data.remote.GsonInterface
import com.data.model.order.OrderH
import com.data.remote.response.order.GetAllActiveOrderByCustomerIDRs
import java.lang.reflect.Type

class GetAllActiveOrderByCustomerIDRsDs : JsonDeserializer<GetAllActiveOrderByCustomerIDRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetAllActiveOrderByCustomerIDRs? {


        val getAllActiveOrderByCustomerIDRs = GetAllActiveOrderByCustomerIDRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getAllActiveOrderByCustomerIDRs.decodeResult(json)

            if(getAllActiveOrderByCustomerIDRs.result.isStatus) {

                if (jsonObject.has("getAllActiveOrderMappers") &&
                    !jsonObject["getAllActiveOrderMappers"].isJsonNull
                ) {

                    val orderHistoriesJsonArray = jsonObject.getAsJsonArray("getAllActiveOrderMappers")
                    val listType = object : TypeToken<List<OrderH>>() {}.type
                    val orderList = GsonInterface.getInstance().gson.fromJson<List<OrderH>>(
                            orderHistoriesJsonArray,
                        listType
                    )
                    getAllActiveOrderByCustomerIDRs.activeOrderList = orderList
                }
            }
        }

        return getAllActiveOrderByCustomerIDRs

    }
}