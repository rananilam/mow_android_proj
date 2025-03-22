package com.data.remote.deserializers.order

import com.data.remote.response.order.CreditCardRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class CreditCardRsDs : JsonDeserializer<CreditCardRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): CreditCardRs {


        val creditCardRs = CreditCardRs()

        json?.let {
            val jsonObject = it.asJsonObject

            creditCardRs.decodeResult(json)


            if (jsonObject.has("card_number") && !jsonObject["card_number"].isJsonNull)
                creditCardRs.card_number = jsonObject["card_number"].asString

            if (jsonObject.has("expiry_date") && !jsonObject["expiry_date"].isJsonNull)
                creditCardRs.expiry_date = jsonObject["expiry_date"].asString

            if (jsonObject.has("cardholder_name") && !jsonObject["cardholder_name"].isJsonNull)
                creditCardRs.cardholder_name = jsonObject["cardholder_name"].asString

            if (jsonObject.has("issuer") && !jsonObject["issuer"].isJsonNull)
                creditCardRs.issuer = jsonObject["issuer"].asString

            if (jsonObject.has("card_type") && !jsonObject["card_type"].isJsonNull)
                creditCardRs.card_type = jsonObject["card_type"].asString

            if (jsonObject.has("country") && !jsonObject["country"].isJsonNull)
                creditCardRs.country = jsonObject["country"].asString


        }

        return creditCardRs

    }
}