package com.data.remote.deserializers.order

import com.data.model.order.State
import com.data.remote.GsonInterface
import com.data.remote.response.order.GetAllStateRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GetAllStateRsDs : JsonDeserializer<GetAllStateRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetAllStateRs {


        val getAllStateRs = GetAllStateRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getAllStateRs.decodeResult(json)

            if(getAllStateRs.result.isStatus) {

                if (jsonObject.has("listState") &&
                    !jsonObject["listState"].isJsonNull
                ) {

                    val stateJsonArray = jsonObject.getAsJsonArray("listState")
                    val listType = object : TypeToken<List<State>>() {}.type
                    val stateList = GsonInterface.getInstance().gson.fromJson<List<State>>(
                        stateJsonArray,
                        listType
                    )
                    getAllStateRs.stateList = stateList
                }
            }
        }

        return getAllStateRs

    }
}