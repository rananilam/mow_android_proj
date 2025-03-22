package com.data.remote.deserializers.idanalyzer

import com.data.model.Result
import com.data.model.idanalyzer.Document
import com.data.remote.GsonInterface
import com.data.remote.response.idanalyzer.GetDocumentRs
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class GetDocumentRsDs : JsonDeserializer<GetDocumentRs> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GetDocumentRs {

        val getDocumentRs = GetDocumentRs()

        json?.let {
            val jsonObject = it.asJsonObject

            val result = Result()
            result.isStatus = true

            if (jsonObject.has("error") &&
                !jsonObject["error"].isJsonNull
            ) {

                result.isStatus = false
                val errorJsonObject = jsonObject.getAsJsonObject("error")
                if (errorJsonObject.has("message") && !errorJsonObject["message"].isJsonNull) {
                    result.message = errorJsonObject["message"].asString
                    result.errorMessage = errorJsonObject["message"].asString
                }

            }
            getDocumentRs.result = result

            if (getDocumentRs.result.isStatus) {

                val document =
                    GsonInterface.getInstance().gson.fromJson(
                        jsonObject,
                        Document::class.java
                    )
                getDocumentRs.document = document
            }
        }

        return getDocumentRs

    }
}