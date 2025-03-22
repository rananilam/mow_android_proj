package com.data.remote.deserializers.idanalyzer

import com.data.model.idanalyzer.Document
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class DocumentDs : JsonDeserializer<Document> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Document {


        val document = Document()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("result") &&
                !jsonObject["result"].isJsonNull
            ) {

                val resultJsonObject = jsonObject.getAsJsonObject("result")

                if (resultJsonObject.has("documentNumber") && !resultJsonObject["documentNumber"].isJsonNull)
                    document.documentNumber = resultJsonObject["documentNumber"].asString

                if (resultJsonObject.has("firstName") && !resultJsonObject["firstName"].isJsonNull)
                    document.firstName = resultJsonObject["firstName"].asString



                if (resultJsonObject.has("middleName") && !resultJsonObject["middleName"].isJsonNull)
                    document.middleName = resultJsonObject["middleName"].asString

                if (resultJsonObject.has("lastName") && !resultJsonObject["lastName"].isJsonNull)
                    document.lastName = resultJsonObject["lastName"].asString

                if (resultJsonObject.has("nationality_iso3") && !resultJsonObject["nationality_iso3"].isJsonNull)
                    document.nationality_iso3 = resultJsonObject["nationality_iso3"].asString
                else if(resultJsonObject.has("issuerOrg_iso3") && !resultJsonObject["issuerOrg_iso3"].isJsonNull)
                    document.nationality_iso3 = resultJsonObject["issuerOrg_iso3"].asString

                if (resultJsonObject.has("address1") && !resultJsonObject["address1"].isJsonNull)
                    document.address1 = resultJsonObject["address1"].asString

                if (resultJsonObject.has("address2") && !resultJsonObject["address2"].isJsonNull) {

                    val address2 = resultJsonObject["address2"].asString
                    document.address2 = ""

                    val splitAddress2 = address2.split(",")

                    if(splitAddress2.isNotEmpty()) {
                        document.issueAuthority = splitAddress2[0]
                    }
                }


                /*if (resultJsonObject.has("issueAuthority") && !resultJsonObject["issueAuthority"].isJsonNull)
                    document.issueAuthority = resultJsonObject["issueAuthority"].asString*/

                if (resultJsonObject.has("issuerOrg_region_abbr") && !resultJsonObject["issuerOrg_region_abbr"].isJsonNull)
                    document.issuerOrgRegionAbbr = resultJsonObject["issuerOrg_region_abbr"].asString

                if (resultJsonObject.has("issuerOrg_region_full") && !resultJsonObject["issuerOrg_region_full"].isJsonNull)
                    document.issuerOrgRegionFull = resultJsonObject["issuerOrg_region_full"].asString

                if (resultJsonObject.has("postcode") && !resultJsonObject["postcode"].isJsonNull)
                    document.postcode = resultJsonObject["postcode"].asString

                if (resultJsonObject.has("expiry") && !resultJsonObject["expiry"].isJsonNull)
                    document.expiry = resultJsonObject["expiry"].asString

                if (resultJsonObject.has("dob") && !resultJsonObject["dob"].isJsonNull)
                    document.dob = resultJsonObject["dob"].asString

                if (resultJsonObject.has("height") && !resultJsonObject["height"].isJsonNull)
                    document.height = resultJsonObject["height"].asString

                if (resultJsonObject.has("documentSide") && !resultJsonObject["documentSide"].isJsonNull)
                    document.documentSide = resultJsonObject["documentSide"].asString
            }

            if (jsonObject.has("authentication") &&
                !jsonObject["authentication"].isJsonNull
            ) {

                val authenticationJsonObject = jsonObject.getAsJsonObject("authentication")
                if (authenticationJsonObject.has("score") && !authenticationJsonObject["score"].isJsonNull)
                    document.score = authenticationJsonObject["score"].asFloat
            }
        }

        return document

    }
}