package com.data.remote.deserializers.destination

import com.data.model.destination.PickupLocationInstruction
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class PickupLocationInstructionDs : JsonDeserializer<PickupLocationInstruction>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PickupLocationInstruction? {


        val pickupLocationInstruction = PickupLocationInstruction()

        json?.let {
            val jsonObject = it.asJsonObject

            if (jsonObject.has("DeviceTypeID") && !jsonObject["DeviceTypeID"].isJsonNull)
                pickupLocationInstruction.deviceTypeID = jsonObject["DeviceTypeID"].asInt

            if (jsonObject.has("IsDeleted") && !jsonObject["IsDeleted"].isJsonNull)
                pickupLocationInstruction.isDeleted = jsonObject["IsDeleted"].asBoolean

            if (jsonObject.has("LocationID") && !jsonObject["LocationID"].isJsonNull)
                pickupLocationInstruction.locationID = jsonObject["LocationID"].asInt

            if (jsonObject.has("PickupInstructionContent") && !jsonObject["PickupInstructionContent"].isJsonNull)
                pickupLocationInstruction.pickupInstructionContent = jsonObject["PickupInstructionContent"].asString

            if (jsonObject.has("PickupInstructionID") && !jsonObject["PickupInstructionID"].isJsonNull)
                pickupLocationInstruction.pickupInstructionID = jsonObject["PickupInstructionID"].asInt
        }

        return pickupLocationInstruction

    }
}