package com.data.remote.deserializers.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.remote.response.order.GetOrderBillingProfileRs
import java.lang.reflect.Type

class GetOrderBillingProfileRsDs : JsonDeserializer<GetOrderBillingProfileRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): GetOrderBillingProfileRs? {


        val getOrderBillingProfileRs = GetOrderBillingProfileRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getOrderBillingProfileRs.decodeResult(json)


            if (jsonObject.has("BillingProfileID") && !jsonObject["BillingProfileID"].isJsonNull)
                getOrderBillingProfileRs.billingProfileID = jsonObject["BillingProfileID"].asInt

            if (jsonObject.has("Description") && !jsonObject["Description"].isJsonNull)
                getOrderBillingProfileRs.description = jsonObject["Description"].asString

            if (jsonObject.has("RegularPrice") && !jsonObject["RegularPrice"].isJsonNull)
                getOrderBillingProfileRs.regularPrice = jsonObject["RegularPrice"].asString

            if (jsonObject.has("RewardPoint") && !jsonObject["RewardPoint"].isJsonNull)
                getOrderBillingProfileRs.rewardPoint = jsonObject["RewardPoint"].asFloat

            if (jsonObject.has("SecondNdTripPrice") && !jsonObject["SecondNdTripPrice"].isJsonNull)
                getOrderBillingProfileRs.secondNdTripPrice = jsonObject["SecondNdTripPrice"].asString

            /*if (jsonObject.has("TotalPrice") && !jsonObject["TotalPrice"].isJsonNull)
                getOrderBillingProfileRs.totalPrice = jsonObject["TotalPrice"].asString*/
        }

        return getOrderBillingProfileRs

    }
}