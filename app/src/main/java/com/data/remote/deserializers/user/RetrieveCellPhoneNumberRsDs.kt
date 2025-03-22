package com.data.remote.deserializers.user

import com.data.remote.response.user.RetrieveCellPhoneNumberRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class RetrieveCellPhoneNumberRsDs : JsonDeserializer<RetrieveCellPhoneNumberRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): RetrieveCellPhoneNumberRs {

        val retrieveCellPhoneNumberRs = RetrieveCellPhoneNumberRs()

        json?.let {
            retrieveCellPhoneNumberRs.decodeResult(json)
        }

        return retrieveCellPhoneNumberRs

    }
}