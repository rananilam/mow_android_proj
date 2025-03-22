package com.data.remote.deserializers.occupant

import com.data.model.user.User
import com.data.remote.GsonInterface
import com.data.remote.response.occupant.SearchOccupantListRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SearchOccupantListRsDs : JsonDeserializer<SearchOccupantListRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): SearchOccupantListRs {


        val searchOccupantListRs = SearchOccupantListRs()

        json?.let {
            val jsonArray = it.asJsonArray

            val listType = object : TypeToken<List<User>>() {}.type
            val occupantList = GsonInterface.getInstance().gson.fromJson<List<User>>(
                    jsonArray,
                    listType
            )
            searchOccupantListRs.occupantList = occupantList
        }

        return searchOccupantListRs
    }
}