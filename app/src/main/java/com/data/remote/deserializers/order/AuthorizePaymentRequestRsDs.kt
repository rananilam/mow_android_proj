package com.data.remote.deserializers.order

import com.data.remote.response.order.AuthorizePaymentRequestRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.remote.response.order.SaveOrderListRs
import java.lang.reflect.Type

class AuthorizePaymentRequestRsDs : JsonDeserializer<AuthorizePaymentRequestRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): AuthorizePaymentRequestRs? {


        val authorizePaymentRequestRs = AuthorizePaymentRequestRs()

        json?.let {
            val jsonObject = it.asJsonObject
            authorizePaymentRequestRs.decodeResult(json)
        }

        return authorizePaymentRequestRs

    }
}