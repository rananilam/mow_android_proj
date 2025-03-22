package com.data.remote.deserializers.order

import com.data.model.order.State
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class StateDs : JsonDeserializer<State>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): State {


        val state = State()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("Abbreviation") && !jsonObject["Abbreviation"].isJsonNull)
                state.abbreviation = jsonObject["Abbreviation"].asString

            if (jsonObject.has("ID") && !jsonObject["ID"].isJsonNull)
                state.id = jsonObject["ID"].asInt

            if (jsonObject.has("StateName") && !jsonObject["StateName"].isJsonNull)
                state.stateName = jsonObject["StateName"].asString
        }

        return state

    }
}