package com.data.remote.deserializers.destination

import com.data.model.user.Human
import com.data.remote.GsonInterface
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class HumanDs : JsonDeserializer<Human>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Human? {


        val humanRs = Human()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("name") && !jsonObject["name"].isJsonNull) {
                humanRs.name = jsonObject["name"].asString
            }
            if (jsonObject.has("children") &&
                !jsonObject["children"].isJsonNull
            ) {

                val childJsonArray = jsonObject.getAsJsonArray("children")

                if(childJsonArray.size() >= 1) {
                    val listType = object : TypeToken<List<Human>>() {}.type
                    val childList = GsonInterface.getInstance().gson.fromJson<List<Human>>(
                        childJsonArray,
                        listType
                    )
                    humanRs.childList = childList
                }


            }
        }

        return humanRs
    }
}