package com.data.remote.deserializers.order

import com.data.remote.response.order.SaveOrderListRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class SaveOrderListRsDs : JsonDeserializer<SaveOrderListRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): SaveOrderListRs {


        val saveOrderListRs = SaveOrderListRs()

        json?.let {
            val jsonObject = it.asJsonObject

            saveOrderListRs.decodeResult(json)


            if (jsonObject.has("CheckCreditCardBalance") && !jsonObject["CheckCreditCardBalance"].isJsonNull)
                saveOrderListRs.checkCreditCardBalance = jsonObject["CheckCreditCardBalance"].asBoolean

            if (jsonObject.has("EquipmentOrderID") && !jsonObject["EquipmentOrderID"].isJsonNull)
                saveOrderListRs.equipmentOrderID = jsonObject["EquipmentOrderID"].asInt

            if (jsonObject.has("EquipmentOrderIDWithFormat") && !jsonObject["EquipmentOrderIDWithFormat"].isJsonNull)
                saveOrderListRs.equipmentOrderIDWithFormat = jsonObject["EquipmentOrderIDWithFormat"].asString

            if (jsonObject.has("PaymentMethod") && !jsonObject["PaymentMethod"].isJsonNull)
                saveOrderListRs.paymentMethod = jsonObject["PaymentMethod"].asString


        }

        return saveOrderListRs

    }
}