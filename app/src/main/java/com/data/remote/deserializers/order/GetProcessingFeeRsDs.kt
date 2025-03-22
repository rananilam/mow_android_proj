package com.data.remote.deserializers.order

import com.data.remote.response.order.GetProcessingFeeRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetProcessingFeeRsDs : JsonDeserializer<GetProcessingFeeRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): GetProcessingFeeRs {


        val getProcessingFeeRs = GetProcessingFeeRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getProcessingFeeRs.decodeResult(json)
            if (jsonObject.has("FeeFigure") && !jsonObject["FeeFigure"].isJsonNull)
                getProcessingFeeRs.fee_figure = jsonObject["FeeFigure"].asFloat

            if (jsonObject.has("FeeType") && !jsonObject["FeeType"].isJsonNull)
                getProcessingFeeRs.fee_type = jsonObject["FeeType"].asInt


            if (jsonObject.has("IsCardProcesssingFeeRequired") && !jsonObject["IsCardProcesssingFeeRequired"].isJsonNull)
                getProcessingFeeRs.is_card_processsing_fee_required = jsonObject["IsCardProcesssingFeeRequired"].asBoolean

            if (jsonObject.has("IsEcheckProcessingFeeRequired") && !jsonObject["IsEcheckProcessingFeeRequired"].isJsonNull)
                getProcessingFeeRs.is_eCheck_processsing_fee_required = jsonObject["IsEcheckProcessingFeeRequired"].asBoolean

            if (jsonObject.has("Note") && !jsonObject["Note"].isJsonNull)
                getProcessingFeeRs.note = jsonObject["Note"].asString

            if (jsonObject.has("ProcessingFeeAmount") && !jsonObject["ProcessingFeeAmount"].isJsonNull)
                getProcessingFeeRs.processing_fee_amount = jsonObject["ProcessingFeeAmount"].asFloat


        }

        return getProcessingFeeRs

    }
}