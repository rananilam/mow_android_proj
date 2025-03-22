package com.data.remote.deserializers.order

import com.data.remote.response.order.DeleteAuthorizedPaymentProfileRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class DeleteAuthorizedPaymentProfileRsDs : JsonDeserializer<DeleteAuthorizedPaymentProfileRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): DeleteAuthorizedPaymentProfileRs {


        val deleteAuthorizedPaymentProfileRs = DeleteAuthorizedPaymentProfileRs()

        json?.let {
            val jsonObject = it.asJsonObject
            deleteAuthorizedPaymentProfileRs.decodeResult(json)
        }

        return deleteAuthorizedPaymentProfileRs

    }
}