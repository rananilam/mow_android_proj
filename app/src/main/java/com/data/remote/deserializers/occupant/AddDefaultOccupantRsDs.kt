package com.data.remote.deserializers.occupant

import com.data.model.user.User
import com.data.remote.GsonInterface
import com.data.remote.response.occupant.AddDefaultOccupantRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class AddDefaultOccupantRsDs : JsonDeserializer<AddDefaultOccupantRs> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): AddDefaultOccupantRs {

        val addDefaultOccupantRs = AddDefaultOccupantRs()

        json?.let {
            val jsonObject = it.asJsonObject

            addDefaultOccupantRs.decodeResult(json)

            if (addDefaultOccupantRs.result.isStatus) {

                if (jsonObject.has("SelectedOccupant") && !jsonObject["SelectedOccupant"].isJsonNull) {
                    addDefaultOccupantRs.selectedOccupantID = jsonObject["SelectedOccupant"].asInt
                }

                if (jsonObject.has("occupantMapper") && !jsonObject["occupantMapper"].isJsonNull) {


                    val listType = object : TypeToken<List<User>>() {}.type
                    val occupantList = GsonInterface.getInstance().gson.fromJson<List<User>>(
                        jsonObject.get("occupantMapper"),
                        listType
                    )
                    addDefaultOccupantRs.occupantList = occupantList
                }


            }
        }

        return addDefaultOccupantRs

    }
}