package com.data.remote.deserializers.notification

import com.data.remote.response.notification.GetBadgeCountRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetBadgeCountRsDs : JsonDeserializer<GetBadgeCountRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetBadgeCountRs {


        val getBadgeCountRs = GetBadgeCountRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getBadgeCountRs.decodeResult(jsonObject)

            if(getBadgeCountRs.result.isStatus) {

                if (jsonObject.has("BadgeCount") && !jsonObject["BadgeCount"].isJsonNull)
                    getBadgeCountRs.badgeCount = jsonObject["BadgeCount"].asInt
            }
        }

        return getBadgeCountRs

    }
}