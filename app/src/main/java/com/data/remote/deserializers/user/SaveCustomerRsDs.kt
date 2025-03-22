package com.data.remote.deserializers.user

import com.data.remote.response.user.SaveCustomerRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class SaveCustomerRsDs : JsonDeserializer<SaveCustomerRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): SaveCustomerRs {

        val saveCustomerRs = SaveCustomerRs()

        json?.let {
            saveCustomerRs.decodeResult(it)
        }

        return saveCustomerRs

    }
}