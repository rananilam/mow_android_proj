package com.data.remote.deserializers.order

import com.data.model.order.OrderH
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class OrderHDs : JsonDeserializer<OrderH>  {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): OrderH? {


        val orderH = OrderH()

        json?.let {
            val jsonObject = it.asJsonObject


            if (jsonObject.has("ArrivalDate") && !jsonObject["ArrivalDate"].isJsonNull)
                orderH.arrivalDate = jsonObject["ArrivalDate"].asString

            if (jsonObject.has("CapturePaymentTransactionDate") && !jsonObject["CapturePaymentTransactionDate"].isJsonNull)
                orderH.capturePaymentTransactionDate = jsonObject["CapturePaymentTransactionDate"].asString

            if (jsonObject.has("CardExpDate") && !jsonObject["CardExpDate"].isJsonNull)
                orderH.cardExpDate = jsonObject["CardExpDate"].asString

            if (jsonObject.has("CardLastFourDigit") && !jsonObject["CardLastFourDigit"].isJsonNull)
                orderH.cardLastFourDigit = jsonObject["CardLastFourDigit"].asString

            if (jsonObject.has("CardTypeName") && !jsonObject["CardTypeName"].isJsonNull)
                orderH.cardTypeName = jsonObject["CardTypeName"].asString

            if (jsonObject.has("CashTotal") && !jsonObject["CashTotal"].isJsonNull)
                orderH.cashTotal = jsonObject["CashTotal"].asFloat

            if (jsonObject.has("CreatedDate") && !jsonObject["CreatedDate"].isJsonNull)
                orderH.createdDate = jsonObject["CreatedDate"].asString

            if (jsonObject.has("CreditTotal") && !jsonObject["CreditTotal"].isJsonNull)
                orderH.creditTotal = jsonObject["CreditTotal"].asFloat

            if (jsonObject.has("DepartureDate") && !jsonObject["DepartureDate"].isJsonNull)
                orderH.departureDate = jsonObject["DepartureDate"].asString

            if (jsonObject.has("DeviceTypeName") && !jsonObject["DeviceTypeName"].isJsonNull)
                orderH.deviceTypeName = jsonObject["DeviceTypeName"].asString

            if (jsonObject.has("EAID") && !jsonObject["EAID"].isJsonNull)
                orderH.eaId = jsonObject["EAID"].asInt

            if (jsonObject.has("EquipmentOrderID") && !jsonObject["EquipmentOrderID"].isJsonNull)
                orderH.equipmentOrderID = jsonObject["EquipmentOrderID"].asInt

            if (jsonObject.has("ExtensionNumber") && !jsonObject["ExtensionNumber"].isJsonNull)
                orderH.extensionNumber = jsonObject["ExtensionNumber"].asInt

            if (jsonObject.has("FormatedOrderID") && !jsonObject["FormatedOrderID"].isJsonNull)
                orderH.formatedOrderID = jsonObject["FormatedOrderID"].asString

            if (jsonObject.has("IsCompanyOrder") && !jsonObject["IsCompanyOrder"].isJsonNull)
                orderH.isCompanyOrder = jsonObject["IsCompanyOrder"].asBoolean

            if (jsonObject.has("IsCreditBilled") && !jsonObject["IsCreditBilled"].isJsonNull)
                orderH.isCreditBilled = jsonObject["IsCreditBilled"].asBoolean

            if (jsonObject.has("AccountLastFourDigit") && !jsonObject["AccountLastFourDigit"].isJsonNull)
                orderH.accountLastFourDigit = jsonObject["AccountLastFourDigit"].asString


            if (jsonObject.has("IsCheckBilled") && !jsonObject["IsCheckBilled"].isJsonNull)
                orderH.isCheckBilled = jsonObject["IsCheckBilled"].asBoolean

            if (jsonObject.has("IsDeclined") && !jsonObject["IsDeclined"].isJsonNull)
                orderH.isDeclined = jsonObject["IsDeclined"].asBoolean

            if (jsonObject.has("IsDelete") && !jsonObject["IsDelete"].isJsonNull)
                orderH.isDelete = jsonObject["IsDelete"].asBoolean

            if (jsonObject.has("IsOnlineOrdered") && !jsonObject["IsOnlineOrdered"].isJsonNull)
                orderH.isOnlineOrdered = jsonObject["IsOnlineOrdered"].asBoolean

            if (jsonObject.has("IsPOCharged") && !jsonObject["IsPOCharged"].isJsonNull)
                orderH.isPOCharged = jsonObject["IsPOCharged"].asBoolean

            if (jsonObject.has("IsPrimaryOrder") && !jsonObject["IsPrimaryOrder"].isJsonNull)
                orderH.isPrimaryOrder = jsonObject["IsPrimaryOrder"].asBoolean

            if (jsonObject.has("IsRefundCash") && !jsonObject["IsRefundCash"].isJsonNull)
                orderH.isRefundCash = jsonObject["IsRefundCash"].asBoolean

            if (jsonObject.has("IsRefundCredit") && !jsonObject["IsRefundCredit"].isJsonNull)
                orderH.isRefundCredit = jsonObject["IsRefundCredit"].asBoolean

            if (jsonObject.has("IsReturnImageUploaded") && !jsonObject["IsReturnImageUploaded"].isJsonNull)
                orderH.isReturnImageUploaded = jsonObject["IsReturnImageUploaded"].asBoolean

            if (jsonObject.has("IsReturned") && !jsonObject["IsReturned"].isJsonNull)
                orderH.isReturned = jsonObject["IsReturned"].asBoolean


            if (jsonObject.has("LocationName") && !jsonObject["LocationName"].isJsonNull)
                orderH.locationName = jsonObject["LocationName"].asString

            if (jsonObject.has("OperatorName") && !jsonObject["OperatorName"].isJsonNull)
                orderH.operatorName = jsonObject["OperatorName"].asString

            if (jsonObject.has("OperatorOccupantName") && !jsonObject["OperatorOccupantName"].isJsonNull)
                orderH.operatorOccupantName = jsonObject["OperatorOccupantName"].asString

            if (jsonObject.has("OperatorStatus") && !jsonObject["OperatorStatus"].isJsonNull)
                orderH.operatorStatus = jsonObject["OperatorStatus"].asBoolean

            if (jsonObject.has("OriginalDepartureDate") && !jsonObject["OriginalDepartureDate"].isJsonNull)
                orderH.originalDepartureDate = jsonObject["OriginalDepartureDate"].asString

            if (jsonObject.has("PayorStatus") && !jsonObject["PayorStatus"].isJsonNull)
                orderH.payorStatus = jsonObject["PayorStatus"].asBoolean

            if (jsonObject.has("PrimaryOrderId") && !jsonObject["PrimaryOrderId"].isJsonNull)
                orderH.primaryOrderId = jsonObject["PrimaryOrderId"].asInt

            if (jsonObject.has("RecepientName") && !jsonObject["RecepientName"].isJsonNull)
                orderH.recepientName = jsonObject["RecepientName"].asString

            if (jsonObject.has("RefundType") && !jsonObject["RefundType"].isJsonNull)
                orderH.refundType = jsonObject["RefundType"].asString

            if (jsonObject.has("EMVID") && !jsonObject["EMVID"].isJsonNull)
                orderH.emvid = jsonObject["EMVID"].asString

            if (jsonObject.has("IsGPSEnabled") && !jsonObject["IsGPSEnabled"].isJsonNull)
                orderH.isGPSEnabled = jsonObject["IsGPSEnabled"].asBoolean

            if (jsonObject.has("OrderID") && !jsonObject["OrderID"].isJsonNull)
                orderH.orderID = jsonObject["OrderID"].asInt

            if (jsonObject.has("ProfilePrice") && !jsonObject["ProfilePrice"].isJsonNull)
                orderH.profilePrice = jsonObject["ProfilePrice"].asFloat

            if (jsonObject.has("RateSelected") && !jsonObject["RateSelected"].isJsonNull)
                orderH.rateSelected = jsonObject["RateSelected"].asString

            if (jsonObject.has("RemainingTime") && !jsonObject["RemainingTime"].isJsonNull)
                orderH.remainingTime = jsonObject["RemainingTime"].asString

            if (jsonObject.has("ShadowPinDeviceUniqueID") && !jsonObject["ShadowPinDeviceUniqueID"].isJsonNull)
                orderH.shadowPinDeviceUniqueID = jsonObject["ShadowPinDeviceUniqueID"].asString

            if (jsonObject.has("CurrentDateTime") && !jsonObject["CurrentDateTime"].isJsonNull)
                orderH.currentDateTime = jsonObject["CurrentDateTime"].asString





            if (jsonObject.has("ResendAttestationTo") && !jsonObject["ResendAttestationTo"].isJsonNull)
                orderH.resendAttestationTo = jsonObject["ResendAttestationTo"].asString

            if (jsonObject.has("IsResendAttestationDisable") && !jsonObject["IsResendAttestationDisable"].isJsonNull)
                orderH.isResendAttestationDisable = jsonObject["IsResendAttestationDisable"].asBoolean

            if (jsonObject.has("OrderStatus") && !jsonObject["OrderStatus"].isJsonNull)
                orderH.orderStatus = jsonObject["OrderStatus"].asString

            if (jsonObject.has("DeviceImagePath") && !jsonObject["DeviceImagePath"].isJsonNull)
                orderH.deviceImagePath = jsonObject["DeviceImagePath"].asString

            if (jsonObject.has("BatteryLevel") && !jsonObject["BatteryLevel"].isJsonNull)
                orderH.batteryLevel = jsonObject["BatteryLevel"].asString

            if (jsonObject.has("payorEleAttestationUrl") && !jsonObject["payorEleAttestationUrl"].isJsonNull)
                orderH.payorEleAttestationUrl = jsonObject["payorEleAttestationUrl"].asString

            if (jsonObject.has("operatorEleAttestationUrl") && !jsonObject["operatorEleAttestationUrl"].isJsonNull)
                orderH.operatorEleAttestationUrl = jsonObject["operatorEleAttestationUrl"].asString



        }

        return orderH

    }
}