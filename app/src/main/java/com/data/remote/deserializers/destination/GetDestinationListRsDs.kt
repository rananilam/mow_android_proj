package com.data.remote.deserializers.destination

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.data.remote.GsonInterface
import com.data.model.destination.Location
import com.data.remote.response.destination.GetDestinationListRs
import java.lang.reflect.Type

class GetDestinationListRsDs : JsonDeserializer<GetDestinationListRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetDestinationListRs? {


        val getDestinationListRs = GetDestinationListRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getDestinationListRs.decodeResult(json)

            if(getDestinationListRs.result.isStatus) {

                if (jsonObject.has("ListLocation") && !jsonObject["ListLocation"].isJsonNull) {


                    if (jsonObject.has("ListLocation") &&
                        !jsonObject["ListLocation"].isJsonNull
                    ) {

                        val destinationnListDataJsonArray = jsonObject.getAsJsonArray("ListLocation")
                        val listType = object : TypeToken<List<Location>>() {}.type
                        val destinationList = GsonInterface.getInstance().gson.fromJson<List<Location>>(
                            destinationnListDataJsonArray,
                            listType
                        )
                        getDestinationListRs.destinationList = destinationList
                    }
                }
            }
        }

        return getDestinationListRs

    }
}