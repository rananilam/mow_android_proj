package com.data.remote.deserializers.accessory

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.data.remote.GsonInterface
import com.data.model.accessory.Accessory
import com.data.remote.response.accessory.GetAccessoryByDeviceTypeIdListRs
import java.lang.reflect.Type

class GetAccessoryByDeviceTypeIdListRsDs : JsonDeserializer<GetAccessoryByDeviceTypeIdListRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetAccessoryByDeviceTypeIdListRs? {


        val getAccessoryByDeviceTypeIdListRs = GetAccessoryByDeviceTypeIdListRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getAccessoryByDeviceTypeIdListRs.decodeResult(json)

            if(getAccessoryByDeviceTypeIdListRs.result.isStatus) {

                if (jsonObject.has("ListAccessory") &&
                        !jsonObject["ListAccessory"].isJsonNull
                ) {

                    val accessoryListDataJsonArray = jsonObject.getAsJsonArray("ListAccessory")
                    val listType = object : TypeToken<List<Accessory>>() {}.type
                    val accessoryList = GsonInterface.getInstance().gson.fromJson<List<Accessory>>(
                            accessoryListDataJsonArray,
                            listType
                    )
                    getAccessoryByDeviceTypeIdListRs.accessoryList = accessoryList
                }
            }
        }

        return getAccessoryByDeviceTypeIdListRs

    }
}