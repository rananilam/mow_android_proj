package com.data.remote.deserializers.order

import com.data.model.order.ECheckCard
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ECheckCardDs : JsonDeserializer<ECheckCard>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ECheckCard? {


        val card = ECheckCard()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("Address1") && !jsonObject["Address1"].isJsonNull)
                card.address1 = jsonObject["Address1"].asString

            if (jsonObject.has("Address2") && !jsonObject["Address2"].isJsonNull)
                card.address2 = jsonObject["Address2"].asString

            if (jsonObject.has("BankAccountLast4") && !jsonObject["BankAccountLast4"].isJsonNull)
                card.bankAccountLast4 = jsonObject["BankAccountLast4"].asString

            if (jsonObject.has("BankName") && !jsonObject["BankName"].isJsonNull)
                card.bankName = jsonObject["BankName"].asString


            if (jsonObject.has("FirstName") && !jsonObject["FirstName"].isJsonNull)
                card.firstName = jsonObject["FirstName"].asString

            if (jsonObject.has("ID") && !jsonObject["ID"].isJsonNull)
                card.id = jsonObject["ID"].asInt

            if (jsonObject.has("LastName") && !jsonObject["LastName"].isJsonNull)
                card.lastName = jsonObject["LastName"].asString

            if (jsonObject.has("MiddleName") && !jsonObject["MiddleName"].isJsonNull)
                card.middleName = jsonObject["MiddleName"].asString

            if (jsonObject.has("Phone") && !jsonObject["Phone"].isJsonNull)
                card.phone = jsonObject["Phone"].asString

            if (jsonObject.has("ddlItem") && !jsonObject["ddlItem"].isJsonNull)
                card.ddlItem = jsonObject["ddlItem"].asString


        }

        return card

    }
}