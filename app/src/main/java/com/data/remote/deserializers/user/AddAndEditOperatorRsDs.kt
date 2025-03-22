package com.data.remote.deserializers.user

import com.data.model.user.User
import com.data.remote.GsonInterface
import com.data.remote.response.user.AddAndEditOperatorRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class AddAndEditOperatorRsDs : JsonDeserializer<AddAndEditOperatorRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): AddAndEditOperatorRs {

        val addAndEditOperatorRs = AddAndEditOperatorRs()

        json?.let {
            val jsonObject = it.asJsonObject

            addAndEditOperatorRs.decodeResult(json)

            if(addAndEditOperatorRs.result.isStatus) {

                val operator =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                                User::class.java
                        )
                addAndEditOperatorRs.operator = operator
            }
        }

        return addAndEditOperatorRs

    }
}