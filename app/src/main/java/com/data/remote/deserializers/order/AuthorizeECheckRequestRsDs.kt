package com.data.remote.deserializers.order

import com.data.remote.response.order.AuthorizeECheckRequestRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class AuthorizeECheckRequestRsDs : JsonDeserializer<AuthorizeECheckRequestRs> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): AuthorizeECheckRequestRs {


        val authorizeECheckRequestRs = AuthorizeECheckRequestRs()

        json?.let {
            val jsonObject = it.asJsonObject
            authorizeECheckRequestRs.decodeResult(jsonObject)
        }

        return authorizeECheckRequestRs

    }

}