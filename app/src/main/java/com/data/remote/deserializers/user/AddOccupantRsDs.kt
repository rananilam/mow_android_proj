package com.data.remote.deserializers.user

import com.data.model.user.User
import com.data.remote.GsonInterface
import com.data.remote.response.user.AddOccupantRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class AddOccupantRsDs : JsonDeserializer<AddOccupantRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): AddOccupantRs {

        val addOccupantRs = AddOccupantRs()

        json?.let {
            val jsonObject = it.asJsonObject

            addOccupantRs.decodeResult(json)

            if(addOccupantRs.result.isStatus) {

                val occupant =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                                User::class.java
                        )
                addOccupantRs.occupant = occupant
            }
        }

        return addOccupantRs

    }
}