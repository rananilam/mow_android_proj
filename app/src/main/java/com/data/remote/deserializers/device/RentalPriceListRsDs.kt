package com.data.remote.deserializers.device

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.data.remote.GsonInterface
import com.data.remote.response.device.RentalPriceListRs
import java.lang.reflect.Type

class RentalPriceListRsDs : JsonDeserializer<RentalPriceListRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): RentalPriceListRs? {


        val rentalPriceListRs = RentalPriceListRs()

        json?.let {
            val jsonObject = it.asJsonObject

            rentalPriceListRs.decodeResult(json)

            if(rentalPriceListRs.result.isStatus) {

                if (jsonObject.has("ListRentalPriceTag") &&
                    !jsonObject["ListRentalPriceTag"].isJsonNull
                ) {

                    val rentalPriceJsonArray = jsonObject.getAsJsonArray("ListRentalPriceTag")
                    val listType = object : TypeToken<List<String>>() {}.type
                    val rentalPriceList = GsonInterface.getInstance().gson.fromJson<List<String>>(
                            rentalPriceJsonArray,
                        listType
                    )
                    rentalPriceListRs.rentalPriceList = rentalPriceList
                }
            }
        }

        return rentalPriceListRs
    }
}