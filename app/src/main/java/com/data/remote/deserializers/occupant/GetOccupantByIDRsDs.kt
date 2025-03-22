package com.data.remote.deserializers.occupant

import com.data.model.user.User
import com.data.remote.GsonInterface
import com.data.remote.response.occupant.GetOccupantByIDRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetOccupantByIDRsDs : JsonDeserializer<GetOccupantByIDRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetOccupantByIDRs? {

        val getOccupantByIDRs = GetOccupantByIDRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getOccupantByIDRs.decodeResult(json)

            if(getOccupantByIDRs.result.isStatus) {

                val occupant =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                                User::class.java
                        )
                getOccupantByIDRs.occupant = occupant
            }
        }

        return getOccupantByIDRs

    }
}