package com.data.remote.deserializers.operator

import com.data.model.user.User
import com.data.remote.GsonInterface
import com.data.remote.response.occupant.GetOccupantByIDRs
import com.data.remote.response.operator.GetOperatorByIDRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetOperatorByIDRsDs : JsonDeserializer<GetOperatorByIDRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetOperatorByIDRs? {

        val getOperatorByIDRs = GetOperatorByIDRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getOperatorByIDRs.decodeResult(json)

            if(getOperatorByIDRs.result.isStatus) {

                val operator =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                                User::class.java
                        )
                getOperatorByIDRs.operator = operator
            }
        }

        return getOperatorByIDRs

    }
}