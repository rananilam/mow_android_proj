package com.data.remote.deserializers.order

import com.data.model.order.BonusDayBillingProfileInfo
import com.data.remote.GsonInterface
import com.data.remote.response.order.GetBonusDayBillingProfileInfoRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetBonusDayBillingProfileInfoRsDs : JsonDeserializer<GetBonusDayBillingProfileInfoRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): GetBonusDayBillingProfileInfoRs? {


        val getBonusDayBillingProfileInfoRs = GetBonusDayBillingProfileInfoRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getBonusDayBillingProfileInfoRs.decodeResult(json)

            if(getBonusDayBillingProfileInfoRs.result.isStatus) {

                val bonusDayBillingProfileInfo =
                    GsonInterface.getInstance().gson.fromJson(
                        jsonObject,
                        BonusDayBillingProfileInfo::class.java
                    )
                getBonusDayBillingProfileInfoRs.bonusDayBillingProfileInfo = bonusDayBillingProfileInfo
            }

        }

        return getBonusDayBillingProfileInfoRs

    }
}