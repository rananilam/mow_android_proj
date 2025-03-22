package com.data.remote.deserializers.user

import com.data.model.user.Config
import com.data.remote.GsonInterface
import com.data.remote.response.user.GetAppConfigurationRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetAppConfigurationRsDs : JsonDeserializer<GetAppConfigurationRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetAppConfigurationRs {

        val getAppConfigurationRs = GetAppConfigurationRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getAppConfigurationRs.decodeResult(json)

            if(getAppConfigurationRs.result.isStatus) {

                val config =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                                Config::class.java
                        )
                getAppConfigurationRs.config = config
            }
        }

        return getAppConfigurationRs

    }
}