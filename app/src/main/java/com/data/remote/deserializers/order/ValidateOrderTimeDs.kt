package com.data.remote.deserializers.order

import com.data.model.order.ValidateOrderTime
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ValidateOrderTimeDs : JsonDeserializer<ValidateOrderTime>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ValidateOrderTime {

        val validateOrderTime = ValidateOrderTime()

        json?.let {
            val jsonObject = it.asJsonObject

            if (jsonObject.has("ErrorMessage") && !jsonObject["ErrorMessage"].isJsonNull)
                validateOrderTime.errorMessage = jsonObject["ErrorMessage"].asString

            if (jsonObject.has("Message") && !jsonObject["Message"].isJsonNull)
                validateOrderTime.message = jsonObject["Message"].asString

            if (jsonObject.has("StatusCode") && !jsonObject["StatusCode"].isJsonNull)
                validateOrderTime.statusCode = jsonObject["StatusCode"].asString
        }

        return validateOrderTime

    }
}