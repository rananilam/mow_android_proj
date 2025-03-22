package com.data.remote.deserializers.device

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.model.device.Device
import java.lang.reflect.Type

class DeviceDs : JsonDeserializer<Device>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Device? {


        val device = Device()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("CompPriceDescription") && !jsonObject["CompPriceDescription"].isJsonNull)
                device.compPriceDescription = jsonObject["CompPriceDescription"].asString

            if (jsonObject.has("DeviceTypeID") && !jsonObject["DeviceTypeID"].isJsonNull)
                device.deviceTypeId = jsonObject["DeviceTypeID"].asInt

            if (jsonObject.has("InventoryID") && !jsonObject["InventoryID"].isJsonNull)
                device.inventoryId = jsonObject["InventoryID"].asInt

            if (jsonObject.has("ItemImagePath") && !jsonObject["ItemImagePath"].isJsonNull)
                device.itemImagePath = jsonObject["ItemImagePath"].asString

            if (jsonObject.has("ItemName") && !jsonObject["ItemName"].isJsonNull)
                device.itemName = jsonObject["ItemName"].asString

            if (jsonObject.has("RegularPriceDescription") && !jsonObject["RegularPriceDescription"].isJsonNull)
                device.regularPriceDescription = jsonObject["RegularPriceDescription"].asString

        }

        return device

    }
}