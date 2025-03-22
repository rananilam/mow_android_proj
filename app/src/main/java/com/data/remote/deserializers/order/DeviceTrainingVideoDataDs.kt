package com.data.remote.deserializers.order

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.data.model.order.DeviceTrainingVideoData
import java.lang.reflect.Type

class DeviceTrainingVideoDataDs : JsonDeserializer<DeviceTrainingVideoData>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): DeviceTrainingVideoData? {

        val deviceTrainingVideoData =
            DeviceTrainingVideoData()

        json?.let {
            val jsonObject = it.asJsonObject

            if (jsonObject.has("AttestationID") && !jsonObject["AttestationID"].isJsonNull)
                deviceTrainingVideoData.attestationID = jsonObject["AttestationID"].asInt

            if (jsonObject.has("CheckOutText") && !jsonObject["CheckOutText"].isJsonNull)
                deviceTrainingVideoData.checkOutText = jsonObject["CheckOutText"].asString

            if (jsonObject.has("CompanyName") && !jsonObject["CompanyName"].isJsonNull)
                deviceTrainingVideoData.companyName = jsonObject["CompanyName"].asString

            if (jsonObject.has("DeviceTypeID") && !jsonObject["DeviceTypeID"].isJsonNull)
                deviceTrainingVideoData.deviceTypeID = jsonObject["DeviceTypeID"].asInt

            if (jsonObject.has("IsPayorOperatorSame") && !jsonObject["IsPayorOperatorSame"].isJsonNull)
                deviceTrainingVideoData.isPayorOperatorSame = jsonObject["IsPayorOperatorSame"].asBoolean

            if (jsonObject.has("LocationID") && !jsonObject["LocationID"].isJsonNull)
                deviceTrainingVideoData.locationID = jsonObject["LocationID"].asInt

            if (jsonObject.has("ModifiedTermsAndConditionText") && !jsonObject["ModifiedTermsAndConditionText"].isJsonNull)
                deviceTrainingVideoData.modifiedTermsAndConditionText = jsonObject["ModifiedTermsAndConditionText"].asString

            if (jsonObject.has("OperatorName") && !jsonObject["OperatorName"].isJsonNull)
                deviceTrainingVideoData.operatorName = jsonObject["OperatorName"].asString

            if (jsonObject.has("PayorName") && !jsonObject["PayorName"].isJsonNull)
                deviceTrainingVideoData.payorName = jsonObject["PayorName"].asString

            if (jsonObject.has("RentalAgreementText") && !jsonObject["RentalAgreementText"].isJsonNull)
                deviceTrainingVideoData.rentalAgreementText = jsonObject["RentalAgreementText"].asString

            if (jsonObject.has("RentalCompanyName") && !jsonObject["RentalCompanyName"].isJsonNull)
                deviceTrainingVideoData.rentalCompanyName = jsonObject["RentalCompanyName"].asString

            if (jsonObject.has("RequestPortal") && !jsonObject["RequestPortal"].isJsonNull)
                deviceTrainingVideoData.requestPortal = jsonObject["RequestPortal"].asString

            if (jsonObject.has("TermsAndConditionText") && !jsonObject["TermsAndConditionText"].isJsonNull)
                deviceTrainingVideoData.termsAndConditionText = jsonObject["TermsAndConditionText"].asString

            if (jsonObject.has("TrainingVideoURL") && !jsonObject["TrainingVideoURL"].isJsonNull)
                deviceTrainingVideoData.trainingVideoURL = jsonObject["TrainingVideoURL"].asString

            if (jsonObject.has("TraningVideoText") && !jsonObject["TraningVideoText"].isJsonNull)
                deviceTrainingVideoData.traningVideoText = jsonObject["TraningVideoText"].asString

        }

        return deviceTrainingVideoData

    }
}