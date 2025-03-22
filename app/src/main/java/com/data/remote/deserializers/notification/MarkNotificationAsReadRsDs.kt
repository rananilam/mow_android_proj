package com.data.remote.deserializers.notification

import com.data.remote.response.notification.MarkNotificationAsReadRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class MarkNotificationAsReadRsDs : JsonDeserializer<MarkNotificationAsReadRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MarkNotificationAsReadRs {


        val markNotificationAsReadRs = MarkNotificationAsReadRs()

        json?.let {
            val jsonObject = it.asJsonObject
            markNotificationAsReadRs.decodeResult(jsonObject)
        }

        return markNotificationAsReadRs

    }
}