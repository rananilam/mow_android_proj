package com.data.remote.deserializers.user

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.remote.GsonInterface
import com.data.model.user.User
import com.data.remote.response.user.VerifyOTPRs
import java.lang.reflect.Type

class VerifyOTPRsDs : JsonDeserializer<VerifyOTPRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): VerifyOTPRs? {

        val verifyOTPRs = VerifyOTPRs()

        json?.let {
            val jsonObject = it.asJsonObject

            verifyOTPRs.decodeResult(json)

            if(verifyOTPRs.result.isStatus) {

                val user =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                                User::class.java
                        )
                verifyOTPRs.user = user
            }
        }

        return verifyOTPRs

    }
}