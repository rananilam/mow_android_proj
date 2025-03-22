package com.data.remote.deserializers.order

import com.data.model.order.RewardPolicy
import com.data.remote.GsonInterface
import com.data.remote.response.order.GetRewardPolicyRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetRewardPolicyRsDs : JsonDeserializer<GetRewardPolicyRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetRewardPolicyRs {

        val getRewardPolicyRs = GetRewardPolicyRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getRewardPolicyRs.decodeResult(json)

            if(getRewardPolicyRs.result.isStatus) {

                val rewardPolicy =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                                RewardPolicy::class.java
                        )
                getRewardPolicyRs.rewardPolicy = rewardPolicy
            }
        }

        return getRewardPolicyRs

    }
}