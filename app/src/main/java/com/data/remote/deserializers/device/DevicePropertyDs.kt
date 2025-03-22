package com.data.remote.deserializers.device

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.model.device.Device
import com.data.model.device.DeviceProperty
import java.lang.reflect.Type

class DevicePropertyDs : JsonDeserializer<DeviceProperty>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): DeviceProperty? {


        val deviceProperty = DeviceProperty()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("DevicePropertyID") && !jsonObject["DevicePropertyID"].isJsonNull)
                deviceProperty.devicePropertyID = jsonObject["DevicePropertyID"].asInt

            if (jsonObject.has("DevicePropertyOption") && !jsonObject["DevicePropertyOption"].isJsonNull)
                deviceProperty.devicePropertyOption = jsonObject["DevicePropertyOption"].asString

            if (jsonObject.has("DevicePropertyOptionID") && !jsonObject["DevicePropertyOptionID"].isJsonNull)
                deviceProperty.devicePropertyOptionID = jsonObject["DevicePropertyOptionID"].asInt


        }

        return deviceProperty

    }
}