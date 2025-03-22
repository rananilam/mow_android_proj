package com.data.remote.request.user

import com.data.model.user.User
import com.google.gson.JsonObject
import java.io.Serializable

class AddAndEditOperatorRq(private val operator: User) : Serializable {


    fun convertToJson(): JsonObject {
        val jsonObject = JsonObject()

        operator.let {

            jsonObject.addProperty("OperatorID", it.operatorID)
            jsonObject.addProperty("CustomerID", it.customerId)
            jsonObject.addProperty("FirstName", it.firstName)
            jsonObject.addProperty("MiddleName", it.middleName)
            jsonObject.addProperty("LastName", it.lastName)
            jsonObject.addProperty("HeightFeet", it.heightFeet)
            jsonObject.addProperty("HeightInch", it.heightInch)
            jsonObject.addProperty("Weight", it.weight)
            jsonObject.addProperty("EmailId", it.email)
            jsonObject.addProperty("DateOfBirth", it.dateOfBirth)
            jsonObject.addProperty("ExpirationDate", it.expiryDate)
            jsonObject.addProperty("OperatorCellNumber", it.cellNumber)
            jsonObject.addProperty("OperatorHomeNumber", it.homeNumber)
            jsonObject.addProperty("Notes", it.notes)
            jsonObject.addProperty("LicenceNo", it.licenseNo)
            jsonObject.addProperty("isdefault", it.isDefault)

            if(it.fileContent.isNotEmpty())
                jsonObject.addProperty("FileContent", it.fileContent)

            if(it.mimeType.isNotEmpty())
                jsonObject.addProperty("MimeType", it.mimeType)

            if(it.fileName.isNotEmpty())
                jsonObject.addProperty("FileName", it.fileName)
        }

        return jsonObject
    }

}