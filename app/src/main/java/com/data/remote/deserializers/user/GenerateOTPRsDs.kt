package com.data.remote.deserializers.user

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.remote.response.user.GenerateOTPRs
import java.lang.reflect.Type

class GenerateOTPRsDs : JsonDeserializer<GenerateOTPRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GenerateOTPRs? {

        val generateOTPRs = GenerateOTPRs()

        json?.let {
            val jsonObject = it.asJsonObject

            generateOTPRs.decodeResult(json)

            if(generateOTPRs.result.isStatus) {

                if (jsonObject.has("OTP") && !jsonObject["OTP"].isJsonNull)
                    generateOTPRs.otp = jsonObject["OTP"].asString
            }
        }

        return generateOTPRs

    }
}