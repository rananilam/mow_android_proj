package com.data.remote.deserializers.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.model.order.Card
import com.data.model.order.CardType
import java.lang.reflect.Type

class CardDs : JsonDeserializer<Card>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Card? {


        val card = Card()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("Address1") && !jsonObject["Address1"].isJsonNull)
                card.address1 = jsonObject["Address1"].asString

            if (jsonObject.has("Address2") && !jsonObject["Address2"].isJsonNull)
                card.address2 = jsonObject["Address2"].asString

            if (jsonObject.has("CardExpDate") && !jsonObject["CardExpDate"].isJsonNull)
                card.cardExpDate = jsonObject["CardExpDate"].asString

            if (jsonObject.has("CardNumber") && !jsonObject["CardNumber"].isJsonNull)
                card.cardNumber = jsonObject["CardNumber"].asString


            if (jsonObject.has("CardType") && !jsonObject["CardType"].isJsonNull)
                card.cardType = CardType.findByName(jsonObject["CardType"].asString)

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

            if (jsonObject.has("ExtDate") && !jsonObject["ExtDate"].isJsonNull)
                card.extDate = jsonObject["ExtDate"].asString
        }

        return card

    }
}