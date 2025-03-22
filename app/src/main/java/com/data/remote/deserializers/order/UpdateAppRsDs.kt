package com.data.remote.deserializers.order

import com.data.objectforupdate.UpdateAppRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UpdateAppRsDs : JsonDeserializer<UpdateAppRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): UpdateAppRs {

        val updateAppRs = UpdateAppRs()
        json?.let {
            val jsonObject = it.asJsonObject

            updateAppRs.androidAppVersionMessage = jsonObject["AndroidAppVersionMessage"].asString
            updateAppRs.androidAppVersionRequired = jsonObject["AndroidAppVersionRequired"].asString
            updateAppRs.iosAppVersionMessage = jsonObject["IosAppVersionMessage"].asString
            updateAppRs.iosAppVersionRequired = jsonObject["IosAppVersionRequired"].asString


        }

        return updateAppRs

    }
}