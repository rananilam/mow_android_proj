package com.data.remote.deserializers.destination

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.data.remote.GsonInterface
import com.data.model.destination.Location
import com.data.remote.response.destination.GetCustomerLocationListRs
import java.lang.reflect.Type

class GetCustomerLocationListRsDs : JsonDeserializer<GetCustomerLocationListRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetCustomerLocationListRs? {


        val getCustomerLocationListRs = GetCustomerLocationListRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getCustomerLocationListRs.decodeResult(json)

            if(getCustomerLocationListRs.result.isStatus) {

                if (jsonObject.has("listCustomerPickupLocation") && !jsonObject["listCustomerPickupLocation"].isJsonNull) {


                    if (jsonObject.has("listCustomerPickupLocation") &&
                        !jsonObject["listCustomerPickupLocation"].isJsonNull
                    ) {

                        val customerLocationListJsonArray = jsonObject.getAsJsonArray("listCustomerPickupLocation")
                        val listType = object : TypeToken<List<Location>>() {}.type
                        val customerLocationList = GsonInterface.getInstance().gson.fromJson<List<Location>>(
                                customerLocationListJsonArray,
                            listType
                        )
                        getCustomerLocationListRs.customerLocationList = customerLocationList
                    }
                }
            }
        }

        return getCustomerLocationListRs
    }
}