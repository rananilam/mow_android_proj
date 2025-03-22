package com.data.remote.deserializers.device

import com.data.model.device.DeviceProperty
import com.data.remote.GsonInterface
import com.data.remote.response.device.GetDevicePropertyOptionRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GetDevicePropertyOptionRsDs : JsonDeserializer<GetDevicePropertyOptionRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetDevicePropertyOptionRs? {


        val getDevicePropertyOptionRs = GetDevicePropertyOptionRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getDevicePropertyOptionRs.decodeResult(json)

            if(getDevicePropertyOptionRs.result.isStatus) {

                if (jsonObject.has("listDevicePropertyOption") &&
                    !jsonObject["listDevicePropertyOption"].isJsonNull
                ) {

                    val devicePropertOptionListDataJsonArray = jsonObject.getAsJsonArray("listDevicePropertyOption")
                    val listType = object : TypeToken<List<DeviceProperty>>() {}.type
                    val devicePropertyOptionList = GsonInterface.getInstance().gson.fromJson<List<DeviceProperty>>(
                        devicePropertOptionListDataJsonArray,
                        listType
                    )
                    getDevicePropertyOptionRs.devicePropertyList = devicePropertyOptionList
                }
            }
        }

        return getDevicePropertyOptionRs

    }
}