package com.data.remote.deserializers.accessory

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.model.accessory.Accessory
import java.lang.reflect.Type

class AccessoryDs : JsonDeserializer<Accessory>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Accessory? {


        val accessory = Accessory()

        json?.let {
            val jsonObject = it.asJsonObject

            if (jsonObject.has("ID") && !jsonObject["ID"].isJsonNull)
                accessory.id = jsonObject["ID"].asInt

            if (jsonObject.has("AccessoryTypeName") && !jsonObject["AccessoryTypeName"].isJsonNull)
                accessory.accessoryTypeName = jsonObject["AccessoryTypeName"].asString
        }

        return accessory

    }
}