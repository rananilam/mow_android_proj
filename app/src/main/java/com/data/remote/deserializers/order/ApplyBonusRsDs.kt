package com.data.remote.deserializers.order

import com.data.remote.response.order.ApplyBonusRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ApplyBonusRsDs : JsonDeserializer<ApplyBonusRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): ApplyBonusRs {


        val applyBonusRs = ApplyBonusRs()

        json?.let {
            val jsonObject = it.asJsonObject

            applyBonusRs.decodeResult(json)


            if (jsonObject.has("BillingProfileID") && !jsonObject["BillingProfileID"].isJsonNull)
                applyBonusRs.billingProfileID = jsonObject["BillingProfileID"].asInt

            if (jsonObject.has("ChairPadPrice") && !jsonObject["ChairPadPrice"].isJsonNull)
                applyBonusRs.chairPadPrice = jsonObject["ChairPadPrice"].asFloat

            if (jsonObject.has("CompPrice") && !jsonObject["CompPrice"].isJsonNull)
                applyBonusRs.compPrice = jsonObject["CompPrice"].asFloat

            if (jsonObject.has("DeliveryFee") && !jsonObject["DeliveryFee"].isJsonNull)
                applyBonusRs.deliveryFee = jsonObject["DeliveryFee"].asInt

            if (jsonObject.has("Description") && !jsonObject["Description"].isJsonNull)
                applyBonusRs.description = jsonObject["Description"].asString

            if (jsonObject.has("DeviceTypeID") && !jsonObject["DeviceTypeID"].isJsonNull)
                applyBonusRs.deviceTypeID = jsonObject["DeviceTypeID"].asInt

            if (jsonObject.has("ExtPrice") && !jsonObject["ExtPrice"].isJsonNull)
                applyBonusRs.extPrice = jsonObject["ExtPrice"].asFloat

            if (jsonObject.has("ExtPriceWithOutPromoCodeEffect") && !jsonObject["ExtPriceWithOutPromoCodeEffect"].isJsonNull)
                applyBonusRs.extPriceWithOutPromoCodeEffect = jsonObject["ExtPriceWithOutPromoCodeEffect"].asInt

            if (jsonObject.has("ExtPriceWithPromoCodeEffect") && !jsonObject["ExtPriceWithPromoCodeEffect"].isJsonNull)
                applyBonusRs.extPriceWithPromoCodeEffect = jsonObject["ExtPriceWithPromoCodeEffect"].asInt

            if (jsonObject.has("ExtensionDescription") && !jsonObject["ExtensionDescription"].isJsonNull)
                applyBonusRs.extensionDescription = jsonObject["ExtensionDescription"].asString

            if (jsonObject.has("IsAcceptBonusDayLocation") && !jsonObject["IsAcceptBonusDayLocation"].isJsonNull)
                applyBonusRs.isAcceptBonusDayLocation = jsonObject["IsAcceptBonusDayLocation"].asBoolean

            if (jsonObject.has("IsBonusDayProfile") && !jsonObject["IsBonusDayProfile"].isJsonNull)
                applyBonusRs.isBonusDayProfile = jsonObject["IsBonusDayProfile"].asBoolean
            else if (jsonObject.has("IsBonusdayProfile") && !jsonObject["IsBonusdayProfile"].isJsonNull)
                applyBonusRs.isBonusDayProfile = jsonObject["IsBonusdayProfile"].asBoolean

            if (jsonObject.has("IsBonusDayCheck") && !jsonObject["IsBonusDayCheck"].isJsonNull)
                applyBonusRs.isBonusDayCheck = jsonObject["IsBonusDayCheck"].asBoolean


            if (jsonObject.has("IsWholeBonusDayProfileAvailable") && !jsonObject["IsWholeBonusDayProfileAvailable"].isJsonNull)
                applyBonusRs.isWholeBonusDayProfileAvailable = jsonObject["IsWholeBonusDayProfileAvailable"].asBoolean

            if (jsonObject.has("LocationID") && !jsonObject["LocationID"].isJsonNull)
                applyBonusRs.locationID = jsonObject["LocationID"].asInt

            if (jsonObject.has("PickUpDate") && !jsonObject["PickUpDate"].isJsonNull)
                applyBonusRs.pickUpDate = jsonObject["PickUpDate"].asString

            if (jsonObject.has("PickUpTime") && !jsonObject["PickUpTime"].isJsonNull)
                applyBonusRs.pickUpTime = jsonObject["PickUpTime"].asString

            if (jsonObject.has("PickupLocationTaxRate") && !jsonObject["PickupLocationTaxRate"].isJsonNull)
                applyBonusRs.pickupLocationTaxRate = jsonObject["PickupLocationTaxRate"].asFloat

            if (jsonObject.has("Price") && !jsonObject["Price"].isJsonNull)
                applyBonusRs.price = jsonObject["Price"].asFloat

            if (jsonObject.has("PriceAdjustment") && !jsonObject["PriceAdjustment"].isJsonNull)
                applyBonusRs.priceAdjustment = jsonObject["PriceAdjustment"].asFloat

            if (jsonObject.has("PriceWithTax") && !jsonObject["PriceWithTax"].isJsonNull)
                applyBonusRs.priceWithTax = jsonObject["PriceWithTax"].asFloat

            if (jsonObject.has("RentalPeriod") && !jsonObject["RentalPeriod"].isJsonNull)
                applyBonusRs.rentalPeriod = jsonObject["RentalPeriod"].asString

            if (jsonObject.has("ReturnDate") && !jsonObject["ReturnDate"].isJsonNull)
                applyBonusRs.returnDate = jsonObject["ReturnDate"].asString

            if (jsonObject.has("ReturnTime") && !jsonObject["ReturnTime"].isJsonNull)
                applyBonusRs.returnTime = jsonObject["ReturnTime"].asString

            if (jsonObject.has("RewardPoint") && !jsonObject["RewardPoint"].isJsonNull)
                applyBonusRs.rewardPoint = jsonObject["RewardPoint"].asFloat

            if (jsonObject.has("TaxOnPrice") && !jsonObject["TaxOnPrice"].isJsonNull)
                applyBonusRs.taxOnPrice = jsonObject["TaxOnPrice"].asFloat

            if (jsonObject.has("TotalBonusDay") && !jsonObject["TotalBonusDay"].isJsonNull)
                applyBonusRs.totalBonusDay = jsonObject["TotalBonusDay"].asInt

            if (jsonObject.has("subTotal") && !jsonObject["subTotal"].isJsonNull)
                applyBonusRs.subTotal = jsonObject["subTotal"].asInt


            if (jsonObject.has("PaidBillingProfileResponse") &&
                !jsonObject["PaidBillingProfileResponse"].isJsonNull
            ) {

                val paidBillingProfileJsonObject = jsonObject.getAsJsonObject("PaidBillingProfileResponse")

                if (paidBillingProfileJsonObject.has("BillingProfileID") && !paidBillingProfileJsonObject["BillingProfileID"].isJsonNull) {
                    applyBonusRs.paidBillingProfileID = paidBillingProfileJsonObject["BillingProfileID"].asInt
                }

                if (paidBillingProfileJsonObject.has("Description") && !paidBillingProfileJsonObject["Description"].isJsonNull) {
                    applyBonusRs.paidBillingDescription = paidBillingProfileJsonObject["Description"].asString
                }

                if (paidBillingProfileJsonObject.has("IsBonusDayProfile") && !paidBillingProfileJsonObject["IsBonusDayProfile"].isJsonNull) {
                    applyBonusRs.paidIsBonusDayProfile = paidBillingProfileJsonObject["IsBonusDayProfile"].asBoolean
                }

                if (paidBillingProfileJsonObject.has("RewardPoint") && !paidBillingProfileJsonObject["RewardPoint"].isJsonNull) {
                    applyBonusRs.paidBillingRewardPoint = paidBillingProfileJsonObject["RewardPoint"].asFloat
                }
            }

        }

        return applyBonusRs

    }
}