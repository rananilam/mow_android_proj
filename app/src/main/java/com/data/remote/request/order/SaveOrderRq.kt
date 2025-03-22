package com.data.remote.request.order

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.util.Utility.Companion.roundUp
import iCode.utility.DateFormatHelper
import java.io.Serializable
import java.util.Calendar

class SaveOrderRq : Serializable {

    //DEVICE INFORMATION
    var compPriceDescription = ""
    var deviceTypeID = 0
    var inventoryID = 0
    var itemImagePath = ""
    var itemName = ""
    var regularPriceDescription = ""
    var processingFeeFigre = 0.0F
    var processing_fee_amount = 0.0F
    var feeType = 0


    //DEVICE_INFO DETAIL
    var chairPadPrice = 0.0F
    var description = ""
    var devicePropertyIDs = ""
    var deviceTypeName = ""
    var deviceTypeShortName = ""
    var deviceItemShortDescription = ""
    var operatorOccupantSame = false

    //OPERATOR INFO
    var operatorID = 0
    var operatorName = ""
    var isDefault = false

    //OCCUPANT INFO
    var occupantID = 0
    var occupantName = ""

    //MAIN LOCATION INFO
    var locationID = 0
    var locationName = ""
    var taxRate = 0.0F
    var deliveryFee = 0.0F
    var isGenerateAcceptBonusDay = false

    //PICKUP LOCATION INFO
    var pickUpLocationID = 0
    var pickUpLocationName = ""

    //ACCESSORY INFO
    var accessoryID = 0
    var accessoryTypeName = ""


    //JOYSTICK INFO
    var joystickID = 0
    var joystickName = ""

    //WHEELCHAIR INFO
    var wheelchairSizeID = 0
    var wheelchairSizeName = ""

    //CHAIRPAD INFO
    var chairpadID = 0
    var chairpadName = ""

    //HANDCONTROLLER INFO
    var handControllerID = 0
    var handControllerName = ""

    //ARRIVAL AND DEPARTURE DATE&TIME
    var arrivalDateAndTime = 0L
    var departureDateAndTime = 0L
    var newDepartureDateAndTime = 0L


    //ORDER BILLING PROFILE INFO
    var totalBonusDays = 0
    var priceAdjustment = 0.0F
    var isBonusBillingProfile = false


    var billingProfileID = 0
    var paidBillingProfileID = 0
    var billingRewardPoint = 0.0F
    var paidBillingRewardPoint = 0.0F
    var billingDescription = ""
    var paidBillingDescription = ""
    var isBonusDayProfile = false
    var isBonusDayCheck = false
    var paidIsBonusDayProfile = false


    var billingRegularPrice = 0.0F
    //var billingTotalPrice = 0.0F


    //PROMOCODE INFO
    var isPromoCodeUsed = false
    var promoCode = ""
    var promotionFigure = 0.0F
    var promotionID = 0
    var promotionType = false

    //ADDRESS RELATED INFO
    var isShippingAddress = false
    var shippingAddressLine1 = ""
    var shippingAddressLine2 = ""
    var shippingZipcode = ""
    var shippingCity = ""
    var shippingDeliveryNote = ""
    var shippingStateID = 0
    var shippingStateName = ""


    //ORDER RELATED INFO

    // 1. EXTRA INFO
    var isOrderCompleted = false
    var localOrderId = 0
    var isExtendOrder = false

    //2. ORDER INFO
    var orderId = 0
    var isPrimaryOrder = true
    var primaryOrderId = 0
    var customerID = 0
    var paymentProfileID = 0
    var paymentMethodId = 0
    var isRewardTurnOn = false
    var note = ""


    override fun equals(other: Any?): Boolean {
        return (other as SaveOrderRq).localOrderId == localOrderId
    }

    fun convertToJson(orderDateAndLocationMap: Map<Int, Long>,isCreditCardRadioButtonSelected:Boolean): JsonObject {
        val jsonObject = JsonObject()

        Log.e("Nilam","hello isCreditCardRadioButtonSelected ---->"+isCreditCardRadioButtonSelected)
        if(orderDateAndLocationMap.containsKey(locationID)) {
            val value = orderDateAndLocationMap[locationID]
            value?.let {
                if(it == arrivalDateAndTime)
                    deliveryFee = 0.00F
            }
        }

        jsonObject.addProperty("CreatedBy", 0)
        jsonObject.addProperty("IsCompanyOrder", false)
        jsonObject.addProperty("BounceBack", 0)
        jsonObject.addProperty("CompanyID", 0)
        jsonObject.addProperty("CompanyTaxRate", 0.00F)

        jsonObject.addProperty("PriceAdjustment", priceAdjustment.roundUp())
        jsonObject.addProperty("TotalBonusDays", totalBonusDays)
        jsonObject.addProperty("OccupantID", occupantID)

        jsonObject.addProperty("OrderId", orderId)
        jsonObject.addProperty("IsPrimaryOrder", isPrimaryOrder)
        jsonObject.addProperty("PrimaryOrderId", primaryOrderId)
        jsonObject.addProperty("CustomerID", customerID)
        jsonObject.addProperty("PickupLocationID", locationID)
        jsonObject.addProperty("DeliveryFee", deliveryFee.roundUp())


        jsonObject.addProperty(
            "DeliveryFeeWithTax",
            deliveryFee + (deliveryFee * (taxRate / 100.0F)).roundUp()
        )
        jsonObject.addProperty("OperatorId", operatorID)
        jsonObject.addProperty("DeviceOrignalPriceWithChairPad", (billingRegularPrice+chairPadPrice).roundUp())
        jsonObject.addProperty("CustomerPickupLocationID", pickUpLocationID)
        jsonObject.addProperty("PickupLocationTaxRate", taxRate.roundUp())
        jsonObject.addProperty("IsAcceptBonusDayLocation", isGenerateAcceptBonusDay)


        //NEED TO CALCULATION BASED ON PROMO AND REWARD POINTS

        //decimal cashTotal = string.IsNullOrEmpty(Convert.ToString(productDetailModel.ChairPadPrice)) ? productDetailModel.Price : productDetailModel.Price + productDetailModel.ChairPadPrice;


        var promoValue = 0.00F

        if(isPromoCodeUsed) {

            if(promotionType) {
                promoValue = promotionFigure
            } else {
                promoValue = ((billingRegularPrice+priceAdjustment)*promotionFigure)/100
            }

        }
        val cashTotal = billingRegularPrice + chairPadPrice - promoValue
        val cashTax = (cashTotal + deliveryFee) * (taxRate/100.0F)
        // Nilam add processing fee in pricewithtax
        val priceWithTax = (cashTax + cashTotal + deliveryFee ).roundUp()
        jsonObject.addProperty("PriceWithTax",priceWithTax)
        jsonObject.addProperty("CashTotal", 0)
        if(isCreditCardRadioButtonSelected){
            jsonObject.addProperty("CheckTotal", 0)
            jsonObject.addProperty("CreditTotal",priceWithTax)
            jsonObject.addProperty("IsCheckOrder", false)
        }else{
            jsonObject.addProperty("CheckTotal", priceWithTax)
            jsonObject.addProperty("CreditTotal",0)
            jsonObject.addProperty("IsCheckOrder", true)

        }

        jsonObject.addProperty("TaxOnPrice", cashTax.roundUp())






        //NEED TO CALCULATION BASED ON PROMO AND REWARD POINTS


        jsonObject.addProperty("IsRewardTurnOn", isRewardTurnOn)
        jsonObject.addProperty("PaymentProfileID", paymentProfileID)


        //jsonObject.addProperty("listEquipmentOrderDetail",null)


        //CALL SPECIFIC FUNCTION

        /*
        1) FULL BONUS
        isBonusBillingProfile = TRUE
        isBonusDayProfile = TRUE
        isFullBonusDayProfile = TRUE

        2) HALF BONUS

        2.1 HALF FREE
        isBonusBillingProfile = TRUE
        isBonusDayProfile = TRUE
        isFullBonusDayProfile = FALSE

        2.2 HALF PAID
        isBonusBillingProfile = TRUE
        isBonusDayProfile = FALSE
        isFullBonusDayProfile = FALSE

        3) NOT BONUS PROFILE
         */
        if(isBonusBillingProfile) {

            var isFullBonusDayProfile = false

            if(billingProfileID != 0 && paidBillingProfileID == 0)
                isFullBonusDayProfile = true

            jsonObject.add("equipmentOrderDetailRequest", getBillingDetailRequest(true, isFullBonusDayProfile))

            if(paidBillingProfileID != 0) {
                val listEquipmentOrderDetailJsonArray = JsonArray()
                listEquipmentOrderDetailJsonArray.add(getBillingDetailRequest(false, isFullBonusDayProfile))
                jsonObject.add("listEquipmentOrderDetail", listEquipmentOrderDetailJsonArray)
            }
        } else {
            jsonObject.add("equipmentOrderDetailRequest", getBillingDetailRequest(false, false))
        }


        return jsonObject
    }


    //SPECIFIC OBJECT
    private fun getBillingDetailRequest(isBonusDayProfile: Boolean, isFullBonusDayProfile: Boolean): JsonObject {

        val equipmentOrderDetailRequest = getBillingDetailBaseObject()


        /*
        1) FULL BONUS
        isBonusBillingProfile = TRUE
        isBonusDayProfile = TRUE
        isFullBonusDayProfile = TRUE

        2) HALF BONUS

        2.1 HALF FREE
        isBonusBillingProfile = TRUE
        isBonusDayProfile = TRUE
        isFullBonusDayProfile = FALSE

        2.2 HALF PAID
        isBonusBillingProfile = TRUE
        isBonusDayProfile = FALSE
        isFullBonusDayProfile = FALSE

        3) NOT BONUS PROFILE
         */
        //
        //
        if (isBonusBillingProfile) {
            if (isBonusDayProfile) {
                //EquipmentOrderDetail
                if(isFullBonusDayProfile) {
                    equipmentOrderDetailRequest.addProperty("IsBonusBillingProfile",isBonusBillingProfile)
                    equipmentOrderDetailRequest.addProperty("BillingProfileID", billingProfileID)
                    equipmentOrderDetailRequest.addProperty("RiderRewardPoint", billingRewardPoint)
                    //equipmentOrderDetailRequest.addProperty("ExtPrice", billingRegularPrice)
                    equipmentOrderDetailRequest.addProperty("ExtPrice", (billingRegularPrice+chairPadPrice).roundUp())
                    equipmentOrderDetailRequest.addProperty("RentalPeriod", billingDescription)
                    equipmentOrderDetailRequest.addProperty("PaymentMethodID", if(paymentMethodId == 0) 4 else paymentMethodId)
                    equipmentOrderDetailRequest.addProperty("Price", (billingRegularPrice).roundUp())
                    equipmentOrderDetailRequest.addProperty("ProcessingFeeFigure", (processingFeeFigre).roundUp())
                    equipmentOrderDetailRequest.addProperty("FeeType", feeType)
                }
                else {
                    equipmentOrderDetailRequest.addProperty("IsBonusBillingProfile",isBonusBillingProfile)
                    equipmentOrderDetailRequest.addProperty("BillingProfileID", billingProfileID)
                    equipmentOrderDetailRequest.addProperty("RiderRewardPoint", paidBillingRewardPoint)
                    //equipmentOrderDetailRequest.addProperty("ExtPrice", billingRegularPrice)
                    equipmentOrderDetailRequest.addProperty("ExtPrice", (billingRegularPrice+chairPadPrice).roundUp())
                    equipmentOrderDetailRequest.addProperty("RentalPeriod", billingDescription)
                    equipmentOrderDetailRequest.addProperty("PaymentMethodID", if(paymentMethodId == 0) 1 else paymentMethodId)
                    equipmentOrderDetailRequest.addProperty("Price", (billingRegularPrice).roundUp())
                    equipmentOrderDetailRequest.addProperty("ProcessingFeeFigure", (processingFeeFigre).roundUp())
                    equipmentOrderDetailRequest.addProperty("FeeType", feeType)
                }
            } else {
                //listEquipmentOrderDetail
                equipmentOrderDetailRequest.addProperty("IsBonusBillingProfile", false)
                equipmentOrderDetailRequest.addProperty("BillingProfileID", paidBillingProfileID)
                equipmentOrderDetailRequest.addProperty("RiderRewardPoint", billingRewardPoint)
                equipmentOrderDetailRequest.addProperty("ExtPrice", billingRegularPrice.roundUp())
                equipmentOrderDetailRequest.addProperty("RentalPeriod", paidBillingDescription)
               // equipmentOrderDetailRequest.addProperty("PaymentMethodID", if(paymentMethodId == 0) 4 else paymentMethodId)
                equipmentOrderDetailRequest.addProperty("PaymentMethodID",  4 )
                equipmentOrderDetailRequest.addProperty("Price", 0.00F)
            }
        } else {
            equipmentOrderDetailRequest.addProperty("IsBonusBillingProfile", isBonusBillingProfile)
            equipmentOrderDetailRequest.addProperty("BillingProfileID", billingProfileID)
            equipmentOrderDetailRequest.addProperty("RiderRewardPoint", billingRewardPoint)
            //equipmentOrderDetailRequest.addProperty("ExtPrice", billingRegularPrice)
            equipmentOrderDetailRequest.addProperty("RentalPeriod", billingDescription)
            equipmentOrderDetailRequest.addProperty("PaymentMethodID", if(paymentMethodId == 0) 1 else paymentMethodId)
            equipmentOrderDetailRequest.addProperty("Price", billingRegularPrice.roundUp())
            equipmentOrderDetailRequest.addProperty("ProcessingFeeFigure", (processingFeeFigre).roundUp())
            equipmentOrderDetailRequest.addProperty("FeeType", feeType)

            if (isPromoCodeUsed) {

                if (promotionType) {
                    equipmentOrderDetailRequest.addProperty(
                        "ExtPrice",
                        ((billingRegularPrice+chairPadPrice) - promotionFigure).roundUp()
                    )
                } else {

                    val less = ((billingRegularPrice) * promotionFigure) / 100.00F
                    equipmentOrderDetailRequest.addProperty("ExtPrice", ((billingRegularPrice+chairPadPrice) - less).roundUp())
                }
            } else {
                equipmentOrderDetailRequest.addProperty("ExtPrice", (billingRegularPrice+chairPadPrice).roundUp())
            }

        }

        return equipmentOrderDetailRequest
    }

    //GENERAL OBJECT
    private fun getBillingDetailBaseObject(): JsonObject {

        val equipmentOrderDetailRequest = JsonObject()

        equipmentOrderDetailRequest.addProperty("AccessoryTypeID", accessoryID)
        equipmentOrderDetailRequest.addProperty("DeviceTypeID", deviceTypeID)
        equipmentOrderDetailRequest.addProperty("CustomerID", customerID)
        equipmentOrderDetailRequest.addProperty("OperatorID", operatorID)


        var nNote = ""

        if(joystickName.isNotEmpty())
            nNote = nNote + "Joystick Position: "+joystickName+" "

        if(wheelchairSizeName.isNotEmpty())
            nNote = nNote + "Preferred Wheelchair Size: "+wheelchairSizeName+" "

        if(chairpadName.isNotEmpty())
            nNote = nNote + chairpadName+" Requirement"+" "

        if(handControllerName.isNotEmpty())
            nNote = nNote + "Hand Controller: "+handControllerName+" "

        equipmentOrderDetailRequest.addProperty(
            "Note",
            (nNote + note).trim()
        )
        equipmentOrderDetailRequest.addProperty("IsSignOnFile", false)

        val nArrivalDateAndTime = if(newDepartureDateAndTime != 0L) departureDateAndTime else arrivalDateAndTime
        val nDepartureDateAndTime = if(newDepartureDateAndTime != 0L) newDepartureDateAndTime else departureDateAndTime
        if (nArrivalDateAndTime != 0L) {
            val arrivalDateCal = Calendar.getInstance()
            arrivalDateCal.timeInMillis = nArrivalDateAndTime


            val date = DateFormatHelper.getStringFromCalendar(
                DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                arrivalDateCal
            )
            val time = DateFormatHelper.getStringFromCalendar(
                DateFormatHelper.FORMAT_HH_MM_SS_A,
                arrivalDateCal
            )

            equipmentOrderDetailRequest.addProperty("ArrivalDate", "$date $time")
        }

        if (nDepartureDateAndTime != 0L) {
            val departureDateCal = Calendar.getInstance()
            departureDateCal.timeInMillis = nDepartureDateAndTime

            val date = DateFormatHelper.getStringFromCalendar(
                DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                departureDateCal
            )
            val time = DateFormatHelper.getStringFromCalendar(
                DateFormatHelper.FORMAT_HH_MM_SS_A,
                departureDateCal
            )
            equipmentOrderDetailRequest.addProperty("DepartureDate", "$date $time")
        }

        if (isPromoCodeUsed) {
            equipmentOrderDetailRequest.addProperty("PromotionCode", promoCode)
            equipmentOrderDetailRequest.addProperty("PromotionID", promotionID)
            equipmentOrderDetailRequest.addProperty("IsPromoCodeUsed", isPromoCodeUsed)
            equipmentOrderDetailRequest.addProperty("PromotionType", promotionType)
            equipmentOrderDetailRequest.addProperty("PromotionFigure", promotionFigure.roundUp())
        }

        equipmentOrderDetailRequest.addProperty("PickupLocationID", pickUpLocationID)

        if (joystickID != 0) {
            equipmentOrderDetailRequest.addProperty("JoystickID", joystickID)
        }

        if (wheelchairSizeID != 0) {
            equipmentOrderDetailRequest.addProperty("WheelchairSizeID", wheelchairSizeID)
        }

        if (chairpadID != 0) {
            equipmentOrderDetailRequest.addProperty("ChairpadID", chairpadID)
        }

        if (handControllerID != 0) {
            equipmentOrderDetailRequest.addProperty("HandControllerID", handControllerID)
        }

        equipmentOrderDetailRequest.addProperty("ChairPadPrice", chairPadPrice)
        equipmentOrderDetailRequest.addProperty("IsShippingAddress", isShippingAddress)

        if (isShippingAddress) {
            equipmentOrderDetailRequest.addProperty("ShippingAddressLine1", shippingAddressLine1)
            equipmentOrderDetailRequest.addProperty("ShippingAddressLine2", shippingAddressLine2)
            equipmentOrderDetailRequest.addProperty("ShippingZipcode", shippingZipcode)
            equipmentOrderDetailRequest.addProperty("ShippingCity", shippingCity)
            equipmentOrderDetailRequest.addProperty("ShippingDeliveryNote", shippingDeliveryNote)
            equipmentOrderDetailRequest.addProperty("ShippingStateID", shippingStateID)
            equipmentOrderDetailRequest.addProperty("ShippingStateName", shippingStateName)
        }
        //Check styleName param
        /*if (styleName.isNotEmpty())
            jsonObject.addProperty("StyleName", styleName)*/

        return equipmentOrderDetailRequest

    }


}