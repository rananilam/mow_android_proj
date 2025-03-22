package com.data.remote.request.user

import com.data.model.user.User
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.Serializable

class SaveCustomerRq : Serializable {

    var payor: User? = null
    var operatorList = mutableListOf<User>()

    fun convertToJson(): JsonObject {
        val jsonObject = JsonObject()

        payor?.let {

            jsonObject.addProperty("ID", it.id)
            jsonObject.addProperty("FirstName", it.firstName)
            jsonObject.addProperty("MiddleName", it.middleName)
            jsonObject.addProperty("LastName", it.lastName)
            jsonObject.addProperty("CellNumber", it.cellNumber)
            jsonObject.addProperty("HomeNumber", it.homeNumber)
            jsonObject.addProperty("Email", it.email)
            jsonObject.addProperty("Country", it.country)
            jsonObject.addProperty("BillAddress1", it.billAddress1)
            jsonObject.addProperty("BillAddress2", it.billAddress2)
            jsonObject.addProperty("BillCity", it.billCity)
            jsonObject.addProperty("BillStateID", it.billStateID)
            jsonObject.addProperty("OtherBillStateName", it.otherBillStateName)
            jsonObject.addProperty("BillZip", it.billZip)
            jsonObject.addProperty("Password", it.password)
            jsonObject.addProperty("FileContent", it.fileContent)
            jsonObject.addProperty("MimeType", it.mimeType)
            jsonObject.addProperty("FileName", it.fileName)
            jsonObject.addProperty("DateOfBirth", it.dateOfBirth)
            jsonObject.addProperty("ExpirationDate", it.expiryDate)
            jsonObject.addProperty("LicenseNo", it.licenseNo)
            jsonObject.addProperty("Notes", it.notes)
        }

        if(operatorList.isNotEmpty()) {
            val operatorListJsonArray = JsonArray()
            for (item in operatorList) {

                val operatorJsonObject = JsonObject()

                operatorJsonObject.addProperty("OperatorID", 0)
                operatorJsonObject.addProperty("CustomerID", 0)
                operatorJsonObject.addProperty("FirstName", item.firstName)
                operatorJsonObject.addProperty("MiddleName", item.middleName)
                operatorJsonObject.addProperty("LastName", item.lastName)
                operatorJsonObject.addProperty("HeightFeet", item.heightFeet)
                operatorJsonObject.addProperty("HeightInch", item.heightInch)
                operatorJsonObject.addProperty("Weight", item.weight)
                operatorJsonObject.addProperty("EmailId", item.email)
                operatorJsonObject.addProperty("FileContent", item.fileContent)
                operatorJsonObject.addProperty("MimeType", item.mimeType)
                operatorJsonObject.addProperty("FileName", item.fileName)
                operatorJsonObject.addProperty("DateOfBirth", item.dateOfBirth)
                operatorJsonObject.addProperty("ExpirationDate", item.expiryDate)
                operatorJsonObject.addProperty("OperatorCellNumber", item.cellNumber)
                operatorJsonObject.addProperty("OperatorHomeNumber", item.homeNumber)
                operatorJsonObject.addProperty("Notes", item.notes)
                operatorJsonObject.addProperty("LicenceNo", item.licenseNo)
                operatorJsonObject.addProperty("isdefault", item.isDefault)

                operatorListJsonArray.add(operatorJsonObject)
            }

            jsonObject.add("listOperatorRequest",operatorListJsonArray)
        }


        return jsonObject
    }


    fun convertToJsonForUpdateProfile(): JsonObject {
        val jsonObject = JsonObject()

        payor?.let {

            jsonObject.addProperty("ID", it.id)
            jsonObject.addProperty("FirstName", it.firstName)
            jsonObject.addProperty("MiddleName", it.middleName)
            jsonObject.addProperty("LastName", it.lastName)
            jsonObject.addProperty("CellNumber", it.cellNumber)
            jsonObject.addProperty("HomeNumber", it.homeNumber)
            jsonObject.addProperty("Email", it.email)
            jsonObject.addProperty("Country", it.country)
            jsonObject.addProperty("BillAddress1", it.billAddress1)
            jsonObject.addProperty("BillAddress2", it.billAddress2)
            jsonObject.addProperty("BillCity", it.billCity)
            jsonObject.addProperty("BillStateID", it.billStateID)
            jsonObject.addProperty("OtherBillStateName", it.otherBillStateName)
            jsonObject.addProperty("BillZip", it.billZip)
            jsonObject.addProperty("DOB", it.dateOfBirth)
            jsonObject.addProperty("ExpiryDate", it.expiryDate)
            jsonObject.addProperty("LicenseNo", it.licenseNo)
            jsonObject.addProperty("Notes", it.notes)



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