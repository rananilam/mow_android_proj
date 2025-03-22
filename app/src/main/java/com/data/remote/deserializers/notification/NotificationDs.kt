package com.data.remote.deserializers.notification

import com.data.model.notification.Notification
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class NotificationDs : JsonDeserializer<Notification>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Notification? {


        val notification = Notification()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("ID") && !jsonObject["ID"].isJsonNull)
                notification.id = jsonObject["ID"].asInt

            if (jsonObject.has("Title") && !jsonObject["Title"].isJsonNull)
                notification.title = jsonObject["Title"].asString

            if (jsonObject.has("Body") && !jsonObject["Body"].isJsonNull)
                notification.message = jsonObject["Body"].asString

            if (jsonObject.has("IsRead") && !jsonObject["IsRead"].isJsonNull)
                notification.isRead = jsonObject["IsRead"].asBoolean

            if (jsonObject.has("CreatedDate") && !jsonObject["CreatedDate"].isJsonNull) {
                notification.createdDate = jsonObject["CreatedDate"].asString
            }
        }
        return notification
    }
}