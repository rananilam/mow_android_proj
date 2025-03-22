package com.data.remote.deserializers.device

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.model.device.DeviceInfo
import java.lang.reflect.Type

class DeviceInfoDs : JsonDeserializer<DeviceInfo>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): DeviceInfo? {


        val deviceInfo = DeviceInfo()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("ChairPadPrice") && !jsonObject["ChairPadPrice"].isJsonNull)
                deviceInfo.chairPadPrice = jsonObject["ChairPadPrice"].asFloat

            if (jsonObject.has("Description") && !jsonObject["Description"].isJsonNull)
                deviceInfo.description = jsonObject["Description"].asString

            if (jsonObject.has("DevicePropertyIDs") && !jsonObject["DevicePropertyIDs"].isJsonNull)
                deviceInfo.devicePropertyIDs = jsonObject["DevicePropertyIDs"].asString

            if (jsonObject.has("DeviceTypeName") && !jsonObject["DeviceTypeName"].isJsonNull)
                deviceInfo.deviceTypeName = jsonObject["DeviceTypeName"].asString

            if (jsonObject.has("DeviceTypeShortName") && !jsonObject["DeviceTypeShortName"].isJsonNull)
                deviceInfo.deviceTypeShortName = jsonObject["DeviceTypeShortName"].asString

            if (jsonObject.has("ID") && !jsonObject["ID"].isJsonNull)
                deviceInfo.id = jsonObject["ID"].asInt



            if (jsonObject.has("ItemFullDescription") && !jsonObject["ItemFullDescription"].isJsonNull)
                deviceInfo.itemFullDescription = jsonObject["ItemFullDescription"].asString

            if (jsonObject.has("ItemImagePath") && !jsonObject["ItemImagePath"].isJsonNull)
                deviceInfo.itemImagePath = jsonObject["ItemImagePath"].asString

            if (jsonObject.has("ItemShortDescription") && !jsonObject["ItemShortDescription"].isJsonNull)
                deviceInfo.itemShortDescription = jsonObject["ItemShortDescription"].asString


            if (jsonObject.has("OperatorOccupantSame") && !jsonObject["OperatorOccupantSame"].isJsonNull)
                deviceInfo.operatorOccupantSame = jsonObject["OperatorOccupantSame"].asBoolean

        }

        return deviceInfo

    }
}