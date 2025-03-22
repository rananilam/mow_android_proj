package com.data.remote.deserializers.reservation

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.remote.response.reservation.GetSameDayReservationRs
import java.lang.reflect.Type

class GetSameDayReservationRsDs : JsonDeserializer<GetSameDayReservationRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetSameDayReservationRs? {


        val getSameDayReservationRs = GetSameDayReservationRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getSameDayReservationRs.decodeResult(json)


            if (jsonObject.has("Day") && !jsonObject["Day"].isJsonNull)
                getSameDayReservationRs.day = jsonObject["Day"].asString

            if (jsonObject.has("DeviceTypeId") && !jsonObject["DeviceTypeId"].isJsonNull)
                getSameDayReservationRs.deviceTypeId = jsonObject["DeviceTypeId"].asInt

            if (jsonObject.has("EndTime") && !jsonObject["EndTime"].isJsonNull)
                getSameDayReservationRs.endTime = jsonObject["EndTime"].asString

            if (jsonObject.has("ID") && !jsonObject["ID"].isJsonNull)
                getSameDayReservationRs.id = jsonObject["ID"].asInt

            if (jsonObject.has("LocationId") && !jsonObject["LocationId"].isJsonNull)
                getSameDayReservationRs.locationId = jsonObject["LocationId"].asInt

            if (jsonObject.has("StartTime") && !jsonObject["StartTime"].isJsonNull)
                getSameDayReservationRs.startTime = jsonObject["StartTime"].asString
        }

        return getSameDayReservationRs

    }
}