package com.data.remote.deserializers.device

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.remote.GsonInterface
import com.data.model.device.DeviceInfo
import com.data.remote.response.device.GetDeviceInfoRs
import java.lang.reflect.Type

class GetDeviceInfoRsDs : JsonDeserializer<GetDeviceInfoRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetDeviceInfoRs? {

        val getDeviceInfoRs = GetDeviceInfoRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getDeviceInfoRs.decodeResult(json)

            if(getDeviceInfoRs.result.isStatus) {

                val deviceInfo =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                                DeviceInfo::class.java
                        )
                getDeviceInfoRs.deviceInfo = deviceInfo
            }
        }

        return getDeviceInfoRs

    }
}