package com.data.remote.deserializers.order

import com.data.remote.response.order.BlackoutDatesRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class BlackoutDatesDs : JsonDeserializer<BlackoutDatesRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BlackoutDatesRs? {


        val BlackoutDates = BlackoutDatesRs()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("StartDate") && !jsonObject["StartDate"].isJsonNull)
                BlackoutDates.startDate = jsonObject["StartDate"].asString

            if (jsonObject.has("EndDate") && !jsonObject["EndDate"].isJsonNull)
                BlackoutDates.endDate = jsonObject["EndDate"].asString



        }

        return BlackoutDates

    }
}