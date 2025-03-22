package com.data.remote.deserializers.operator

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.data.remote.GsonInterface
import com.data.model.user.User
import com.data.remote.response.operator.GetOperatorListRs
import java.lang.reflect.Type

class GetOperatorListRsDs : JsonDeserializer<GetOperatorListRs>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetOperatorListRs? {


        val getOperatorListRs = GetOperatorListRs()

        json?.let {
            val jsonObject = it.asJsonObject

            getOperatorListRs.decodeResult(json)

            if(getOperatorListRs.result.isStatus) {

                if (jsonObject.has("listOperator") &&
                    !jsonObject["listOperator"].isJsonNull
                ) {

                    val listOperatorJsonArray = jsonObject.getAsJsonArray("listOperator")
                    val listType = object : TypeToken<List<User>>() {}.type
                    val operatorList = GsonInterface.getInstance().gson.fromJson<List<User>>(
                            listOperatorJsonArray,
                        listType
                    )
                    getOperatorListRs.operatorList = operatorList
                }
            }
        }

        return getOperatorListRs
    }
}