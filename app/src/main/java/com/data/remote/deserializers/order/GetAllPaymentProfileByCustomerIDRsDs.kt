package com.data.remote.deserializers.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.data.remote.GsonInterface
import com.data.model.order.Card
import com.data.remote.response.order.GetAllPaymentProfileByCustomerIDRs
import java.lang.reflect.Type

class GetAllPaymentProfileByCustomerIDRsDs : JsonDeserializer<GetAllPaymentProfileByCustomerIDRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): GetAllPaymentProfileByCustomerIDRs? {


        val getAllPaymentProfileByCustomerIDRs = GetAllPaymentProfileByCustomerIDRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getAllPaymentProfileByCustomerIDRs.decodeResult(json)

            if(getAllPaymentProfileByCustomerIDRs.result.isStatus) {

                if (jsonObject.has("listAuthorizePayment") &&
                        !jsonObject["listAuthorizePayment"].isJsonNull
                ) {

                    val listAuthorizePaymentJsonArray = jsonObject.getAsJsonArray("listAuthorizePayment")
                    val listType = object : TypeToken<List<Card>>() {}.type
                    val cardList = GsonInterface.getInstance().gson.fromJson<List<Card>>(
                            listAuthorizePaymentJsonArray,
                            listType
                    )
                    getAllPaymentProfileByCustomerIDRs.cardList = cardList
                }
            }
        }

        return getAllPaymentProfileByCustomerIDRs

    }
}