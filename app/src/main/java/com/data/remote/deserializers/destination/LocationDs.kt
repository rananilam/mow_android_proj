package com.data.remote.deserializers.destination

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.model.destination.Location
import java.lang.reflect.Type

class LocationDs : JsonDeserializer<Location>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Location? {


        val location = Location()

        json?.let {
            val jsonObject = it.asJsonObject

            if (jsonObject.has("ID") && !jsonObject["ID"].isJsonNull)
                location.id = jsonObject["ID"].asInt

            if (jsonObject.has("LocationCategoryName") && !jsonObject["LocationCategoryName"].isJsonNull)
                location.locationCategoryName = jsonObject["LocationCategoryName"].asString

            if (jsonObject.has("LocationName") && !jsonObject["LocationName"].isJsonNull)
                location.locationName = jsonObject["LocationName"].asString

            if (jsonObject.has("CustomerPickupLocationID") && !jsonObject["CustomerPickupLocationID"].isJsonNull)
                location.customerPickupLocationID = jsonObject["CustomerPickupLocationID"].asInt

            if (jsonObject.has("CustomerPickuplocationName") && !jsonObject["CustomerPickuplocationName"].isJsonNull)
                location.customerPickuplocationName = jsonObject["CustomerPickuplocationName"].asString

            if (jsonObject.has("IsAvailableOrNot") && !jsonObject["IsAvailableOrNot"].isJsonNull)
                location.isAvailableOrNot = jsonObject["IsAvailableOrNot"].asBoolean

            if (jsonObject.has("IsShippingAddress") && !jsonObject["IsShippingAddress"].isJsonNull)
                location.isShippingAddress = jsonObject["IsShippingAddress"].asBoolean

            if (jsonObject.has("DeliveryFee") && !jsonObject["DeliveryFee"].isJsonNull)
                location.deliveryFee = jsonObject["DeliveryFee"].asFloat

            if (jsonObject.has("IsDistinctLocationCategory") && !jsonObject["IsDistinctLocationCategory"].isJsonNull)
                location.isDistinctLocationCategory = jsonObject["IsDistinctLocationCategory"].asBoolean

            if (jsonObject.has("IsGenerateAcceptBonusDay") && !jsonObject["IsGenerateAcceptBonusDay"].isJsonNull)
                location.isGenerateAcceptBonusDay = jsonObject["IsGenerateAcceptBonusDay"].asBoolean

            if (jsonObject.has("IsTwentyFourBySevenAvailability") && !jsonObject["IsTwentyFourBySevenAvailability"].isJsonNull)
                location.isTwentyFourBySevenAvailability = jsonObject["IsTwentyFourBySevenAvailability"].asBoolean

            if (jsonObject.has("TaxRate") && !jsonObject["TaxRate"].isJsonNull)
                location.taxRate = jsonObject["TaxRate"].asFloat
        }

        return location

    }
}