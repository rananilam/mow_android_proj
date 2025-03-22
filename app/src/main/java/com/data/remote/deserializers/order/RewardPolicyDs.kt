package com.data.remote.deserializers.order

import com.data.model.order.RewardPolicy
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class RewardPolicyDs : JsonDeserializer<RewardPolicy>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): RewardPolicy {


        val rewardPolicy = RewardPolicy()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("ID") && !jsonObject["ID"].isJsonNull)
                rewardPolicy.id = jsonObject["ID"].asInt

            if (jsonObject.has("TemplateContent") && !jsonObject["TemplateContent"].isJsonNull)
                rewardPolicy.templateContent = jsonObject["TemplateContent"].asString
        }

        return rewardPolicy

    }
}