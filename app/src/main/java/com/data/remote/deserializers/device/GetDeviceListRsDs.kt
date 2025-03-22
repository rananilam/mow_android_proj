package com.data.remote.deserializers.device

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.data.remote.GsonInterface
import com.data.model.device.Device
import com.data.remote.response.device.GetDeviceListRs
import java.lang.reflect.Type

class GetDeviceListRsDs : JsonDeserializer<GetDeviceListRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetDeviceListRs? {


        val getDeviceListRs = GetDeviceListRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getDeviceListRs.decodeResult(json)

            if(getDeviceListRs.result.isStatus) {

                if (jsonObject.has("listRestricatedDevice") &&
                    !jsonObject["listRestricatedDevice"].isJsonNull
                ) {

                    val deviceListDataJsonArray = jsonObject.getAsJsonArray("listRestricatedDevice")
                    val listType = object : TypeToken<List<Device>>() {}.type
                    val deviceList = GsonInterface.getInstance().gson.fromJson<List<Device>>(
                        deviceListDataJsonArray,
                        listType
                    )
                    getDeviceListRs.deviceList = deviceList
                }
            }
        }

        return getDeviceListRs

    }
}