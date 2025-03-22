package com.data.remote.deserializers.order

import com.data.model.order.ValidateOrderTime
import com.data.remote.GsonInterface
import com.data.remote.response.order.ValidateOrderListTimeRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ValidateOrderListTimeRsDs : JsonDeserializer<ValidateOrderListTimeRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ValidateOrderListTimeRs? {


        val validateOrderListTimeRs = ValidateOrderListTimeRs()

        json?.let {
            val validateOrderListTimeRsListJsonArray = it.asJsonArray

            if(validateOrderListTimeRsListJsonArray.isJsonArray && validateOrderListTimeRsListJsonArray.size() >= 1) {

                val listType = object : TypeToken<List<ValidateOrderTime>>() {}.type
                val validateOrderTimeList = GsonInterface.getInstance().gson.fromJson<List<ValidateOrderTime>>(
                    validateOrderListTimeRsListJsonArray,
                    listType
                )
                validateOrderListTimeRs.validateOrderTimeList = validateOrderTimeList

            }
        }

        return validateOrderListTimeRs

    }
}