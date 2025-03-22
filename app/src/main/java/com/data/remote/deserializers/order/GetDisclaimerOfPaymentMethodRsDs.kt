package com.data.remote.deserializers.order

import com.data.remote.response.order.GetDisclaimerOfPaymentMethodRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetDisclaimerOfPaymentMethodRsDs : JsonDeserializer<GetDisclaimerOfPaymentMethodRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): GetDisclaimerOfPaymentMethodRs {


        val getDisclaimerOfPaymentMethodRs = GetDisclaimerOfPaymentMethodRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getDisclaimerOfPaymentMethodRs.decodeResult(json)
            if (jsonObject.has("DisclaimerMessage") && !jsonObject["DisclaimerMessage"].isJsonNull)
                getDisclaimerOfPaymentMethodRs.DisclaimerMessage = jsonObject["DisclaimerMessage"].asString

        }

        return getDisclaimerOfPaymentMethodRs

    }
}