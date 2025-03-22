package com.data.remote.deserializers.user

import com.data.model.user.Config
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ConfigDs : JsonDeserializer<Config> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Config {


        val config = Config()
        json?.let {
            val jsonObject = it.asJsonObject

            if (jsonObject.has("idanalyzerApiKey") && !jsonObject["idanalyzerApiKey"].isJsonNull)
                config.idanalyzerApiKey = jsonObject["idanalyzerApiKey"].asString

            if (jsonObject.has("minimumAndroidVersion") && !jsonObject["minimumAndroidVersion"].isJsonNull)
                config.minimumAndroidVersion = jsonObject["minimumAndroidVersion"].asFloat

            if (jsonObject.has("minimumIOSVersion") && !jsonObject["minimumIOSVersion"].isJsonNull)
                config.minimumIOSVersion = jsonObject["minimumIOSVersion"].asFloat

            if (jsonObject.has("timeZoneOffset") && !jsonObject["timeZoneOffset"].isJsonNull)
                config.timeZoneOffset = jsonObject["timeZoneOffset"].asLong
        }

        return config

    }
}