package com.data.remote.deserializers.order

import com.data.remote.response.order.ImageUploadedForReturnDeviceRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ImageUploadedForReturnDeviceRsDs : JsonDeserializer<ImageUploadedForReturnDeviceRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): ImageUploadedForReturnDeviceRs {


        val imageUploadedForReturnDeviceRs = ImageUploadedForReturnDeviceRs()

        json?.let {
            val jsonObject = it.asJsonObject

            imageUploadedForReturnDeviceRs.decodeResult(json)
        }

        return imageUploadedForReturnDeviceRs

    }
}