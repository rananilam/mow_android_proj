package com.data.remote.deserializers.user

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.model.user.User
import java.lang.Exception
import java.lang.reflect.Type

class UserDs : JsonDeserializer<User> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): User? {


        val user = User()
        json?.let {
            val jsonObject = it.asJsonObject

            if (jsonObject.has("AccountName") && !jsonObject["AccountName"].isJsonNull)
                user.accountName = jsonObject["AccountName"].asString

            if (jsonObject.has("AdminID") && !jsonObject["AdminID"].isJsonNull)
                user.adminID = jsonObject["AdminID"].asInt

            if (jsonObject.has("CompanyEmail") && !jsonObject["CompanyEmail"].isJsonNull)
                user.companyEmail = jsonObject["CompanyEmail"].asString

            if (jsonObject.has("CompanyID") && !jsonObject["CompanyID"].isJsonNull)
                user.companyId = jsonObject["CompanyID"].asInt

            if (jsonObject.has("Token") && !jsonObject["Token"].isJsonNull)
                user.token = jsonObject["Token"].asString

            if (jsonObject.has("InActive") && !jsonObject["InActive"].isJsonNull)
                user.inActive = jsonObject["InActive"].asBoolean


            //COMMON
            if (jsonObject.has("CustomerId") && !jsonObject["CustomerId"].isJsonNull)
                user.customerId = jsonObject["CustomerId"].asInt

            if (jsonObject.has("DOB") && !jsonObject["DOB"].isJsonNull)
                user.dateOfBirth = jsonObject["DOB"].asString

            if (jsonObject.has("Email") && !jsonObject["Email"].isJsonNull)
                user.email = jsonObject["Email"].asString
            else if(jsonObject.has("EmailId") && !jsonObject["EmailId"].isJsonNull)
                user.email = jsonObject["EmailId"].asString

            if (jsonObject.has("ExpiryDate") && !jsonObject["ExpiryDate"].isJsonNull)
                user.expiryDate = jsonObject["ExpiryDate"].asString

            if (jsonObject.has("FileName") && !jsonObject["FileName"].isJsonNull)
                user.fileName = jsonObject["FileName"].asString

            if (jsonObject.has("FirstName") && !jsonObject["FirstName"].isJsonNull)
                user.firstName = jsonObject["FirstName"].asString

            if (jsonObject.has("FullName") && !jsonObject["FullName"].isJsonNull)
                user.fullName = jsonObject["FullName"].asString

            if (jsonObject.has("LastName") && !jsonObject["LastName"].isJsonNull)
                user.lastName = jsonObject["LastName"].asString

            if (jsonObject.has("LicenseNo") && !jsonObject["LicenseNo"].isJsonNull)
                user.licenseNo = jsonObject["LicenseNo"].asString
            else if (jsonObject.has("LicenceNo") && !jsonObject["LicenceNo"].isJsonNull)
                user.licenseNo = jsonObject["LicenceNo"].asString

            if (jsonObject.has("MiddleName") && !jsonObject["MiddleName"].isJsonNull)
                user.middleName = jsonObject["MiddleName"].asString

            if (jsonObject.has("Notes") && !jsonObject["Notes"].isJsonNull)
                user.notes = jsonObject["Notes"].asString

            if (jsonObject.has("CellNumber") && !jsonObject["CellNumber"].isJsonNull)
                user.cellNumber = jsonObject["CellNumber"].asString
            else if (jsonObject.has("OperatorCellNumber") && !jsonObject["OperatorCellNumber"].isJsonNull)
                user.cellNumber = jsonObject["OperatorCellNumber"].asString

            if (jsonObject.has("HomeNumber") && !jsonObject["HomeNumber"].isJsonNull)
                user.homeNumber = jsonObject["HomeNumber"].asString
            else if (jsonObject.has("OperatorHomeNumber") && !jsonObject["OperatorHomeNumber"].isJsonNull)
                user.homeNumber = jsonObject["OperatorHomeNumber"].asString

            if (jsonObject.has("AccessoryTypeID") && !jsonObject["AccessoryTypeID"].isJsonNull) {

                val accessoryTypeIDStr = jsonObject["AccessoryTypeID"].asString
                if (accessoryTypeIDStr.isNotEmpty()) {
                    user.accessoryTypeID = accessoryTypeIDStr.trim().toInt()
                }
            }

            if (jsonObject.has("HeightFeet") && !jsonObject["HeightFeet"].isJsonNull)
                user.heightFeet = jsonObject["HeightFeet"].asInt

            if (jsonObject.has("HeightInch") && !jsonObject["HeightInch"].isJsonNull)
                user.heightInch = jsonObject["HeightInch"].asInt

            if (jsonObject.has("ID") && !jsonObject["ID"].isJsonNull)
                user.id = jsonObject["ID"].asInt

            if (jsonObject.has("OperatorID") && !jsonObject["OperatorID"].isJsonNull)
                user.operatorID = jsonObject["OperatorID"].asInt

            if (jsonObject.has("OperatorName") && !jsonObject["OperatorName"].isJsonNull)
                user.operatorName = jsonObject["OperatorName"].asString

            if (jsonObject.has("Weight") && !jsonObject["Weight"].isJsonNull)
                user.weight = jsonObject["Weight"].asInt

            if (jsonObject.has("isDefault") && !jsonObject["isDefault"].isJsonNull)
                user.isDefault = jsonObject["isDefault"].asBoolean

            // PAYOR

            if (jsonObject.has("AlternateName") && !jsonObject["AlternateName"].isJsonNull)
                user.alternateName = jsonObject["AlternateName"].asString

            if (jsonObject.has("BillAddress1") && !jsonObject["BillAddress1"].isJsonNull)
                user.billAddress1 = jsonObject["BillAddress1"].asString

            if (jsonObject.has("BillAddress2") && !jsonObject["BillAddress2"].isJsonNull)
                user.billAddress2 = jsonObject["BillAddress2"].asString

            if (jsonObject.has("BillCity") && !jsonObject["BillCity"].isJsonNull)
                user.billCity = jsonObject["BillCity"].asString

            if (jsonObject.has("BillStateID") && !jsonObject["BillStateID"].isJsonNull)
                user.billStateID = jsonObject["BillStateID"].asInt

            if (jsonObject.has("BillZip") && !jsonObject["BillZip"].isJsonNull)
                user.billZip = jsonObject["BillZip"].asString

            if (jsonObject.has("Country") && !jsonObject["Country"].isJsonNull)
                user.country = jsonObject["Country"].asString

            if (jsonObject.has("IsBlacklisted") && !jsonObject["IsBlacklisted"].isJsonNull)
                user.isBlacklisted = jsonObject["IsBlacklisted"].asBoolean

            if (jsonObject.has("OtherBillStateName") && !jsonObject["OtherBillStateName"].isJsonNull)
                user.otherBillStateName = jsonObject["OtherBillStateName"].asString

            if (jsonObject.has("RewardTurn") && !jsonObject["RewardTurn"].isJsonNull)
                user.rewardTurn = jsonObject["RewardTurn"].asBoolean

            if (jsonObject.has("RewardsPoint") && !jsonObject["RewardsPoint"].isJsonNull)
                user.rewardsPoint = jsonObject["RewardsPoint"].asFloat

            if (jsonObject.has("StateName") && !jsonObject["StateName"].isJsonNull)
                user.stateName = jsonObject["StateName"].asString

            //OPERATOR

            if (jsonObject.has("IsDefaultOccupant") && !jsonObject["IsDefaultOccupant"].isJsonNull)
                user.isDefaultOccupant = jsonObject["IsDefaultOccupant"].asBoolean

            if (jsonObject.has("OccupantID") && !jsonObject["OccupantID"].isJsonNull)
                user.occupantID = jsonObject["OccupantID"].asInt

            if (jsonObject.has("OperatorIDOccupantID") && !jsonObject["OperatorIDOccupantID"].isJsonNull)
                user.operatorIDOccupantID = jsonObject["OperatorIDOccupantID"].asInt

            //OCCUPANT
            if (jsonObject.has("DeviceStyle") && !jsonObject["DeviceStyle"].isJsonNull)
                user.deviceStyle = jsonObject["DeviceStyle"].asString

            if (jsonObject.has("DeviceTypeID") && !jsonObject["DeviceTypeID"].isJsonNull)
                user.deviceTypeID = jsonObject["DeviceTypeID"].asInt

            if (jsonObject.has("OccupantName") && !jsonObject["OccupantName"].isJsonNull)
                user.occupantName = jsonObject["OccupantName"].asString

        }

        return user

    }
}