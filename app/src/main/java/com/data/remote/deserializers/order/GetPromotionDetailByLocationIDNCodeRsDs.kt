package com.data.remote.deserializers.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.remote.response.order.GetPromotionDetailByLocationIDNCodeRs
import java.lang.reflect.Type

class GetPromotionDetailByLocationIDNCodeRsDs : JsonDeserializer<GetPromotionDetailByLocationIDNCodeRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): GetPromotionDetailByLocationIDNCodeRs? {


        val getPromotionDetailByLocationIDNCodeRs = GetPromotionDetailByLocationIDNCodeRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getPromotionDetailByLocationIDNCodeRs.decodeResult(json)


            if (jsonObject.has("PromotionFigure") && !jsonObject["PromotionFigure"].isJsonNull)
                getPromotionDetailByLocationIDNCodeRs.promotionFigure = jsonObject["PromotionFigure"].asFloat

            if (jsonObject.has("PromotionID") && !jsonObject["PromotionID"].isJsonNull)
                getPromotionDetailByLocationIDNCodeRs.promotionID = jsonObject["PromotionID"].asInt

            if (jsonObject.has("PromotionType") && !jsonObject["PromotionType"].isJsonNull)
                getPromotionDetailByLocationIDNCodeRs.promotionType = jsonObject["PromotionType"].asBoolean
        }

        return getPromotionDetailByLocationIDNCodeRs

    }
}