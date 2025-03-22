package com.data.remote.deserializers.notification

import com.data.remote.response.notification.AddUpdateFCMTokenRs
import com.data.remote.response.notification.MarkNotificationAsReadRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class AddUpdateFCMTokenRsDs : JsonDeserializer<AddUpdateFCMTokenRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): AddUpdateFCMTokenRs {


        val addUpdateFCMTokenRs = AddUpdateFCMTokenRs()

        json?.let {
            val jsonObject = it.asJsonObject
            addUpdateFCMTokenRs.decodeResult(jsonObject)
        }

        return addUpdateFCMTokenRs

    }
}