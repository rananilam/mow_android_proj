package com.data.remote.deserializers.order

import com.data.model.order.BonusDayBillingProfileInfo
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class BonusDayBillingProfileInfoDs : JsonDeserializer<BonusDayBillingProfileInfo>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BonusDayBillingProfileInfo? {


        val bonusDayBillingProfileInfo = BonusDayBillingProfileInfo()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("BillingProfileID") && !jsonObject["BillingProfileID"].isJsonNull)
                bonusDayBillingProfileInfo.billingProfileID = jsonObject["BillingProfileID"].asInt

            if (jsonObject.has("CompPrice") && !jsonObject["CompPrice"].isJsonNull)
                bonusDayBillingProfileInfo.compPrice = jsonObject["CompPrice"].asFloat

            if (jsonObject.has("Description") && !jsonObject["Description"].isJsonNull)
                bonusDayBillingProfileInfo.description = jsonObject["Description"].asString

            if (jsonObject.has("FromHour") && !jsonObject["FromHour"].isJsonNull)
                bonusDayBillingProfileInfo.fromHour = jsonObject["FromHour"].asInt

            if (jsonObject.has("IsBonusDayProfile") && !jsonObject["IsBonusDayProfile"].isJsonNull)
                bonusDayBillingProfileInfo.isBonusDayProfile = jsonObject["IsBonusDayProfile"].asBoolean

            if (jsonObject.has("IsDispalyOnReservationPortal") && !jsonObject["IsDispalyOnReservationPortal"].isJsonNull)
                bonusDayBillingProfileInfo.isDispalyOnReservationPortal = jsonObject["IsDispalyOnReservationPortal"].asString

            if (jsonObject.has("RegularPrice") && !jsonObject["RegularPrice"].isJsonNull)
                bonusDayBillingProfileInfo.regularPrice = jsonObject["RegularPrice"].asFloat

            if (jsonObject.has("RewardPoint") && !jsonObject["RewardPoint"].isJsonNull)
                bonusDayBillingProfileInfo.rewardPoint = jsonObject["RewardPoint"].asFloat

            if (jsonObject.has("ToHour") && !jsonObject["ToHour"].isJsonNull)
                bonusDayBillingProfileInfo.toHour = jsonObject["ToHour"].asInt

            if (jsonObject.has("newRenatlhours") && !jsonObject["newRenatlhours"].isJsonNull)
                bonusDayBillingProfileInfo.newRenatlhours = jsonObject["newRenatlhours"].asInt
        }

        return bonusDayBillingProfileInfo

    }
}