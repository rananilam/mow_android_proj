package com.data.remote.deserializers.entityDefination

import com.data.model.Result
import com.data.remote.response.entityDefination.GetEntityDefinationRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetEntityDefinationRsDs : JsonDeserializer<GetEntityDefinationRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): GetEntityDefinationRs {


        val getEntityDefinationRs = GetEntityDefinationRs()

        json?.let {
            val jsonObject = it.asJsonObject

            val result = Result()
            result.isStatus = true

            if (jsonObject.has("DefineOccupant") && !jsonObject["DefineOccupant"].isJsonNull)
                getEntityDefinationRs.defineOccupant = jsonObject["DefineOccupant"].asString
            else
                result.isStatus = false

            if (jsonObject.has("DefineOperator") && !jsonObject["DefineOperator"].isJsonNull)
                getEntityDefinationRs.defineOperator = jsonObject["DefineOperator"].asString
            else
                result.isStatus = false


            getEntityDefinationRs.result = result

        }

        return getEntityDefinationRs

    }
}