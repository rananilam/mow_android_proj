package com.data.remote.request.user

import com.data.model.user.User
import com.google.gson.JsonObject
import java.io.Serializable

class AddOccupantRq(private val occupant: User) : Serializable {


    fun convertToJson(): JsonObject {
        val jsonObject = JsonObject()

        occupant.let {

            jsonObject.addProperty("ID", it.id)
            jsonObject.addProperty("FirstName", it.firstName)
            jsonObject.addProperty("MiddleName", it.middleName)
            jsonObject.addProperty("LastName", it.lastName)
            jsonObject.addProperty("HeightFeet", it.heightFeet)
            jsonObject.addProperty("HeightInch", it.heightInch)
            jsonObject.addProperty("Weight", it.weight)
            jsonObject.addProperty("Notes", "")
            jsonObject.addProperty("isDefault",it.isDefault)
            jsonObject.addProperty("OperatorID", it.operatorID)
        }

        return jsonObject
    }

}