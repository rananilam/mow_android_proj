package com.data.remote.deserializers.order

import com.data.model.order.ECheckCard
import com.data.remote.GsonInterface
import com.data.remote.response.order.GetAllECheckPaymentProfileByCustomerIDRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GetAllECheckPaymentProfileByCustomerIDRsDs : JsonDeserializer<GetAllECheckPaymentProfileByCustomerIDRs> {
    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): GetAllECheckPaymentProfileByCustomerIDRs? {


        val getAllECheckPaymentProfileByCustomerIDRs = GetAllECheckPaymentProfileByCustomerIDRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getAllECheckPaymentProfileByCustomerIDRs.decodeResult(json)

            if(getAllECheckPaymentProfileByCustomerIDRs.result.isStatus) {

                if (jsonObject.has("listAuthorizeECheckPayment") &&
                        !jsonObject["listAuthorizeECheckPayment"].isJsonNull
                ) {

                    val listAuthorizePaymentJsonArray = jsonObject.getAsJsonArray("listAuthorizeECheckPayment")
                    val listType = object : TypeToken<List<ECheckCard>>() {}.type
                    val cardList = GsonInterface.getInstance().gson.fromJson<List<ECheckCard>>(
                            listAuthorizePaymentJsonArray,
                            listType
                    )
                    getAllECheckPaymentProfileByCustomerIDRs.cardList = cardList
                }
            }
        }

        return getAllECheckPaymentProfileByCustomerIDRs

    }
}