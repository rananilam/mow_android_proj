package com.data.remote.deserializers.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.data.model.Result
import com.data.remote.GsonInterface
import com.data.model.order.DeviceTrainingVideoData
import com.data.remote.response.order.GetDeviceTrainingVideoAndDataRs
import java.lang.reflect.Type

class GetDeviceTrainingVideoAndDataRsDs : JsonDeserializer<GetDeviceTrainingVideoAndDataRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetDeviceTrainingVideoAndDataRs? {


        val getDeviceTrainingVideoAndDataRs = GetDeviceTrainingVideoAndDataRs()

        json?.let {
            val jsonArray = it.asJsonArray

            val result = Result()
            if(jsonArray.size() >= 1) {
                result.isStatus = true
            } else {
                result.isStatus = false
                result.message = "Data not found"
                result.errorMessage = "Data not found"
            }

            getDeviceTrainingVideoAndDataRs.result = result

            if(getDeviceTrainingVideoAndDataRs.result.isStatus) {

                val listType = object : TypeToken<List<DeviceTrainingVideoData>>() {}.type
                val deviceTrainingVideoDataList = GsonInterface.getInstance().gson.fromJson<List<DeviceTrainingVideoData>>(
                    jsonArray,
                    listType
                )
                getDeviceTrainingVideoAndDataRs.deviceTrainingVideoDataList = deviceTrainingVideoDataList
            }
        }

        return getDeviceTrainingVideoAndDataRs

    }
}