package com.data.remote.deserializers.destination

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.remote.GsonInterface
import com.data.model.destination.Location
import com.data.remote.response.destination.GetLocationByIdRs
import java.lang.reflect.Type

class GetLocationByIdRsDs : JsonDeserializer<GetLocationByIdRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetLocationByIdRs? {

        val getLocationByIdRs = GetLocationByIdRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getLocationByIdRs.decodeResult(json)

            if(getLocationByIdRs.result.isStatus) {

                val location =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                                Location::class.java
                        )
                getLocationByIdRs.location = location
            }
        }

        return getLocationByIdRs

    }
}