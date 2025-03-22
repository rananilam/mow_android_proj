package com.data.remote.deserializers.user

import com.data.remote.response.user.AddDeviceInventoryIDRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class AddDeviceInventoryIDRsDs : JsonDeserializer<AddDeviceInventoryIDRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): AddDeviceInventoryIDRs {

        val addDeviceInventoryIDRs = AddDeviceInventoryIDRs()

        json?.let {
            val jsonObject = it.asJsonObject

            addDeviceInventoryIDRs.decodeResult(json)

        }

        return addDeviceInventoryIDRs

    }
}