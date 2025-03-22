package com.data.remote.deserializers.notification

import com.data.model.device.Device
import com.data.model.notification.Notification
import com.data.remote.GsonInterface
import com.data.remote.response.notification.NotificationListRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class NotificationListRsDs : JsonDeserializer<NotificationListRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): NotificationListRs {


        val notificationListRs = NotificationListRs()

        json?.let {
            val jsonObject = it.asJsonObject

            notificationListRs.decodeResult(jsonObject)

            if(notificationListRs.result.isStatus) {


                if (jsonObject.has("pushNotificationLogs") &&
                    !jsonObject["pushNotificationLogs"].isJsonNull
                ) {

                    val notificationJsonArray = jsonObject.getAsJsonArray("pushNotificationLogs")
                    val listType = object : TypeToken<List<Notification>>() {}.type
                    val notificationList = GsonInterface.getInstance().gson.fromJson<List<Notification>>(
                        notificationJsonArray,
                        listType
                    )
                    notificationListRs.notificationList = notificationList

                }
            }
        }

        return notificationListRs

    }
}