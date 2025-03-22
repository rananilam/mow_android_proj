package com.data.remote.deserializers.destination

import com.data.model.destination.PickupLocationInstruction
import com.data.remote.GsonInterface
import com.data.remote.response.destination.GetPickupLocationInstructionRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetPickupLocationInstructionRsDs : JsonDeserializer<GetPickupLocationInstructionRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetPickupLocationInstructionRs? {

        val getPickupLocationInstructionRs = GetPickupLocationInstructionRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getPickupLocationInstructionRs.decodeResult(json)

            if(getPickupLocationInstructionRs.result.isStatus) {

                val pickupLocationInstruction =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                            PickupLocationInstruction::class.java
                        )
                getPickupLocationInstructionRs.pickupLocationInstruction = pickupLocationInstruction
            }
        }

        return getPickupLocationInstructionRs

    }
}