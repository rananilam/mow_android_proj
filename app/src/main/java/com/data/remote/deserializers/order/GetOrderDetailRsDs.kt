package com.data.remote.deserializers.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.remote.GsonInterface
import com.data.model.order.Order
import com.data.remote.response.order.GetOrderDetailRs
import java.lang.reflect.Type

class GetOrderDetailRsDs : JsonDeserializer<GetOrderDetailRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetOrderDetailRs? {

        val getOrderDetailRs = GetOrderDetailRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getOrderDetailRs.decodeResult(json)

            if(getOrderDetailRs.result.isStatus) {

                val order =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                                Order::class.java
                        )
                getOrderDetailRs.order = order
            }
        }

        return getOrderDetailRs

    }
}