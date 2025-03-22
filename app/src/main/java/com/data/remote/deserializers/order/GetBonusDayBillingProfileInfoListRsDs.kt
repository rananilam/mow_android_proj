package com.data.remote.deserializers.order

import com.data.model.order.BonusDayBillingProfileInfo
import com.data.remote.GsonInterface
import com.data.remote.response.order.GetBonusDayBillingProfileInfoListRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GetBonusDayBillingProfileInfoListRsDs : JsonDeserializer<GetBonusDayBillingProfileInfoListRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetBonusDayBillingProfileInfoListRs? {


        val getBonusDayBillingProfileInfoListRs = GetBonusDayBillingProfileInfoListRs()

        json?.let {
            val bonusDayBillingProfileInfoListJsonArray = it.asJsonArray

            if(bonusDayBillingProfileInfoListJsonArray.isJsonArray && bonusDayBillingProfileInfoListJsonArray.size() >= 1) {

                val listType = object : TypeToken<List<BonusDayBillingProfileInfo>>() {}.type
                val bonusDayBillingProfileInfoList = GsonInterface.getInstance().gson.fromJson<List<BonusDayBillingProfileInfo>>(
                    bonusDayBillingProfileInfoListJsonArray,
                    listType
                )
                getBonusDayBillingProfileInfoListRs.bonusDayBillingProfileInfoList = bonusDayBillingProfileInfoList

            }
        }

        return getBonusDayBillingProfileInfoListRs

    }
}