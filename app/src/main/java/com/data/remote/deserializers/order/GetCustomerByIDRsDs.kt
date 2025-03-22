package com.data.remote.deserializers.order

import com.data.model.user.User
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.remote.GsonInterface
import com.data.remote.response.order.GetCustomerByIDRs
import java.lang.reflect.Type

class GetCustomerByIDRsDs : JsonDeserializer<GetCustomerByIDRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetCustomerByIDRs? {

        val getCustomerByIDRs = GetCustomerByIDRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getCustomerByIDRs.decodeResult(json)

            if(getCustomerByIDRs.result.isStatus) {

                val customer =
                        GsonInterface.getInstance().gson.fromJson(
                                jsonObject,
                                User::class.java
                        )
                getCustomerByIDRs.customer = customer
            }
        }

        return getCustomerByIDRs

    }
}