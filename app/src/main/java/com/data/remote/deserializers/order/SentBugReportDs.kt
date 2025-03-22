package com.data.remote.deserializers.order

import com.data.remote.response.order.SentBugReportRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class SentBugReportDs : JsonDeserializer<SentBugReportRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): SentBugReportRs? {


        val sentBugReportRs = SentBugReportRs()

        json?.let {
            val jsonObject = it.asJsonObject
            sentBugReportRs.decodeResult(jsonObject)
        }

        return sentBugReportRs

    }
}