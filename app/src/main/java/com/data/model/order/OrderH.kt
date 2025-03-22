package com.data.model.order

import java.io.Serializable

class OrderH : Serializable{


    var arrivalDate = ""
    var capturePaymentTransactionDate = ""
    var accountLastFourDigit = ""
    var cardExpDate = ""
    var cardLastFourDigit = ""
    var cardTypeName = ""
    var cashTotal = 0.0F
    var createdDate = ""
    var creditTotal = 0.0F
    var departureDate = ""
    var deviceTypeName = ""
    var eaId = 0
    var equipmentOrderID = 0
    var extensionNumber= 0
    var formatedOrderID = ""
    var isCompanyOrder = false
    var isCheckBilled = false
    var isCreditBilled = false
    var isDeclined = false
    var isDelete = false
    var isOnlineOrdered = false
    var isPOCharged = false
    var isPrimaryOrder = false
    var isRefundCash = false
    var isRefundCredit = false
    var isReturnImageUploaded = false
    var isReturned = false
    var locationName = ""
    var operatorName = ""
    var operatorOccupantName = ""
    var operatorStatus = false
    var originalDepartureDate = ""
    var payorStatus = false
    var primaryOrderId = 0
    var recepientName = ""
    var refundType = ""
    var currentDateTime = ""
    var deviceImagePath = ""
    var batteryLevel = ""


    var resendAttestationTo = ""
    var isResendAttestationDisable = false
    var orderStatus = ""


    var emvid = ""
    var isGPSEnabled = false
    var orderID = 0
    var profilePrice = 0.0F
    var rateSelected = ""
    var remainingTime = ""
    var shadowPinDeviceUniqueID = ""
    var payorEleAttestationUrl = ""
    var operatorEleAttestationUrl = ""

}