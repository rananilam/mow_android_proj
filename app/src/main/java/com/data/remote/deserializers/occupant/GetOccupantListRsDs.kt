package com.data.remote.deserializers.occupant

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.data.remote.GsonInterface
import com.data.model.user.User
import com.data.remote.response.occupant.GetOccupantListRs
import java.lang.reflect.Type

class GetOccupantListRsDs : JsonDeserializer<GetOccupantListRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetOccupantListRs {


        val getOccupantListRs = GetOccupantListRs()

        json?.let {
            val jsonArray = it.asJsonArray

            val listType = object : TypeToken<List<User>>() {}.type
            val occupantList = GsonInterface.getInstance().gson.fromJson<List<User>>(
                    jsonArray,
                    listType
            )
            getOccupantListRs.occupantList = occupantList
        }

        return getOccupantListRs
    }
}