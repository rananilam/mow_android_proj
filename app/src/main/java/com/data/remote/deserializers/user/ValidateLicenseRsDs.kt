package com.data.remote.deserializers.user

import com.data.remote.response.user.ValidateLicenseRs
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


class ValidateLicenseRsDs : JsonDeserializer<ValidateLicenseRs> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ValidateLicenseRs {


        val validateLicenseRs = ValidateLicenseRs()
        json?.let {
            val jsonObject = it.asJsonObject
            validateLicenseRs.decodeResult(json)


            if(validateLicenseRs.result.isStatus) {

                if (jsonObject.has("IsActiveOrderAval") && !jsonObject["IsActiveOrderAval"].isJsonNull)
                    validateLicenseRs.isActiveOrderAval = jsonObject["IsActiveOrderAval"].asBoolean

                if (jsonObject.has("IsValidLicenseForPayor") && !jsonObject["IsValidLicenseForPayor"].isJsonNull)
                    validateLicenseRs.isValidLicenseForPayor = jsonObject["IsValidLicenseForPayor"].asBoolean


                if (jsonObject.has("IsLicenseAvailableForPayor") && !jsonObject["IsLicenseAvailableForPayor"].isJsonNull)
                    validateLicenseRs.isLicenseAvailableForPayor = jsonObject["IsLicenseAvailableForPayor"].asBoolean



                if (jsonObject.has("NotValidLicenseForOperator") &&
                    !jsonObject["NotValidLicenseForOperator"].isJsonNull
                ) {


                    val notValidLicenseForOperatorJsonArray = jsonObject.getAsJsonArray("NotValidLicenseForOperator")

                    val listType = object : TypeToken<LinkedList<Int?>?>() {}.type
                    val numbers: List<Int> = Gson().fromJson(notValidLicenseForOperatorJsonArray, listType)

                    validateLicenseRs.notValidLicenseForOperator.addAll(numbers)

                }


                if (jsonObject.has("lstLicenseNotAvailableForOperator") &&
                    !jsonObject["lstLicenseNotAvailableForOperator"].isJsonNull
                ) {


                    val lstLicenseNotAvailableForOperatorJsonArray = jsonObject.getAsJsonArray("lstLicenseNotAvailableForOperator")

                    val listType = object : TypeToken<LinkedList<Int?>?>() {}.type
                    val numbers: List<Int> = Gson().fromJson(lstLicenseNotAvailableForOperatorJsonArray, listType)

                    validateLicenseRs.lstLicenseNotAvailableForOperator.addAll(numbers)

                }
            }
        }

        return validateLicenseRs

    }
}