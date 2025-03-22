package com.data.model.order

import android.util.Log
import com.data.remote.request.order.SaveOrderRq
import com.data.remote.request.order.ValidateOrderTimeRq
import com.data.remote.response.order.ApplyBonusRs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.util.Utility.Companion.roundUp
import iCode.utility.DateFormatHelper
import iCode.utility.PrefHelper
import java.util.Calendar


object OrderData {

    private val KEY_ORDER_RQ_LIST = "KEY_ORDER_RQ_LIST"
    private var saveOrderRqList = mutableListOf<SaveOrderRq>()


    init {
        val orderRsListJson = PrefHelper.getInstance().getString(KEY_ORDER_RQ_LIST, "")
        Log.i("SAVEORDERRQLIST", "Json: " + orderRsListJson)
        if (orderRsListJson.isNotEmpty()) {


            val listType = object : TypeToken<List<SaveOrderRq>>() {}.type
            val saveOrderRqList = Gson().fromJson<List<SaveOrderRq>>(
                orderRsListJson,
                listType
            )

            if (!saveOrderRqList.isNullOrEmpty()) {
                OrderData.saveOrderRqList.clear()
                OrderData.saveOrderRqList.addAll(saveOrderRqList)
            }
        }
    }

    fun add(saveOrder: SaveOrderRq) {

        if (saveOrderRqList.isNotEmpty()) {

            val iterator = saveOrderRqList.iterator()
            while (iterator.hasNext()) {
                val orderRq = iterator.next()
                if (orderRq.isOrderCompleted || orderRq.isExtendOrder) {
                    iterator.remove()
                }
            }
            /*for (item in saveOrderRqList) {

                if (item.isOrderCompleted) {
                    saveOrderRqList.remove(item)
                }
            }*/
        }
        // Nilam add saveOrderRqList.clear() for removing multiple record in reserved item list

        saveOrderRqList.add(saveOrder)
        saveInToPreference()
    }

    fun update(saveOrder: SaveOrderRq) {

        val index = saveOrderRqList.indexOf(saveOrder)
        if (index != -1) {
            saveOrderRqList.removeAt(index)
            saveOrderRqList.add(index, saveOrder)
            saveInToPreference()
        }
    }

    fun delete(saveOrder: SaveOrderRq) {
        saveOrderRqList.remove(saveOrder)
        saveInToPreference()
    }

    fun get(): List<SaveOrderRq> {
        return saveOrderRqList
    }

    fun updatePaymentProfileId(paymentProfileId: Int,priceWithTaxIsZero:Boolean) {


        val isBonusDayProfile = isBonusDayProfile()

        if(priceWithTaxIsZero){
            // paymentProfileID = 0
            for (item in saveOrderRqList) {

                if (isBonusDayProfile)
                    item.paymentProfileID = 0
                else
                    item.paymentProfileID = paymentProfileId

            }
        }
        else{
            // paymentProfileID != 0
            for (item in saveOrderRqList) {

                    item.paymentProfileID = paymentProfileId

            }
        }
        saveInToPreference()
    }


    fun updatePaymentMethodId(paymentMethodId: Int) {


        for (item in saveOrderRqList) {

            item.paymentMethodId = paymentMethodId


        }
        saveInToPreference()
    }


    private fun isBonusDayProfile(): Boolean {
        var isBonusDayProfile = true

        for (item in saveOrderRqList) {

            if (item.paidBillingProfileID != 0) {
                isBonusDayProfile = false
                break
            } else if (!item.isBonusDayProfile && item.paidBillingProfileID == 0) {
                isBonusDayProfile = false
                break
            }
        }

        return isBonusDayProfile
    }

    fun updateNote(note: String) {
        for (item in saveOrderRqList) {
            item.note = note
        }
        saveInToPreference()
    }

    fun updateRewardTurnOn(isRewardTurnOn: Boolean) {
        for (item in saveOrderRqList) {
            item.isRewardTurnOn = isRewardTurnOn
        }
        saveInToPreference()
    }

    fun updatePromoCode(
        promoCode: String,
        promotionFigure: Float,
        promotionID: Int,
        promotionType: Boolean,
    ) {

        if (saveOrderRqList.isNotEmpty()) {
            saveOrderRqList[0].isPromoCodeUsed = true
            saveOrderRqList[0].promoCode = promoCode
            saveOrderRqList[0].promotionFigure = promotionFigure
            saveOrderRqList[0].promotionID = promotionID
            saveOrderRqList[0].promotionType = promotionType
        }
        saveInToPreference()
    }

    fun updateProcesingFee(
        feeType: Int,
        processingFeeFigre: Float,
        processing_fee_amount: Float,

        ) {


        if (saveOrderRqList.isNotEmpty()) {
            saveOrderRqList[0].processing_fee_amount = processing_fee_amount

            for (item in saveOrderRqList) {
                item.processingFeeFigre = processingFeeFigre
                item.feeType = feeType
            }
            saveInToPreference()

        }
    }

    fun setOrderCompleted() {
        for (item in saveOrderRqList) {
            item.isOrderCompleted = true
        }
        saveInToPreference()
    }

    fun removeBonusAndPromo() {
        if (saveOrderRqList.isNotEmpty()) {
            saveOrderRqList[0].isBonusBillingProfile = false
            saveOrderRqList[0].isPromoCodeUsed = false
            saveOrderRqList[0].priceAdjustment = 0.0F
            saveInToPreference()
        }

    }

    fun applyBonus(applyBonusRs: ApplyBonusRs) {

        if (saveOrderRqList.isNotEmpty()) {


            saveOrderRqList[0].billingProfileID = applyBonusRs.billingProfileID // outer billinf id
            saveOrderRqList[0].billingRegularPrice = applyBonusRs.price //
            //saveOrderRqList[0].isGenerateAcceptBonusDay = applyBonusRs.isAcceptBonusDayLocation
            //  saveOrderRqList[0].isBonusBillingProfile = true //1 false
            saveOrderRqList[0].isBonusBillingProfile = applyBonusRs.isBonusDayCheck
            saveOrderRqList[0].isBonusDayProfile = applyBonusRs.isBonusDayProfile
            saveOrderRqList[0].isBonusDayCheck = applyBonusRs.isBonusDayCheck
            //saveOrderRqList[0].billingTotalPrice = applyBonusRs.price
            saveOrderRqList[0].billingRewardPoint = applyBonusRs.rewardPoint
            saveOrderRqList[0].billingDescription = applyBonusRs.description
            saveOrderRqList[0].priceAdjustment = applyBonusRs.priceAdjustment
            saveOrderRqList[0].totalBonusDays = applyBonusRs.totalBonusDay

            saveOrderRqList[0].paidBillingProfileID = applyBonusRs.paidBillingProfileID // inner billing id
            saveOrderRqList[0].paidBillingRewardPoint = applyBonusRs.paidBillingRewardPoint
            saveOrderRqList[0].paidBillingDescription = applyBonusRs.paidBillingDescription
            saveOrderRqList[0].paidIsBonusDayProfile = applyBonusRs.paidIsBonusDayProfile

            saveInToPreference()
            /*
            var paidBillingProfileID = 0 //done
    var paidBillingRewardPoint = 0.0F //done
    var paidBillingDescription = ""
    var paidIsBonusDayProfile = false
             */
        }
    }

    private fun saveInToPreference() {
        if (!saveOrderRqList.isNullOrEmpty()) {

            val json = Gson().toJson(saveOrderRqList)
            Log.i("SAVEORDERRQLIST", "toJson: " + json)
            PrefHelper.getInstance().putString(KEY_ORDER_RQ_LIST, json)
        } else {
            PrefHelper.getInstance().putString(KEY_ORDER_RQ_LIST, "")
        }
    }

    fun getSubTotal(isPromoConsider: Boolean): Float {

        var subTotal = 0.0F

        for (item in saveOrderRqList) {

            if (isPromoConsider && item.isPromoCodeUsed) {

                if (item.promotionType) {
                    subTotal += ((item.billingRegularPrice + item.chairPadPrice + item.priceAdjustment) - item.promotionFigure)
                } else {

                    val less =
                        ((item.billingRegularPrice + item.priceAdjustment) * item.promotionFigure) / 100

                    subTotal += ((item.billingRegularPrice + item.chairPadPrice + item.priceAdjustment) - less)
                }

            } else {
                if (item.isBonusBillingProfile && isPromoConsider)
                    subTotal += (item.billingRegularPrice + item.chairPadPrice)
                else
                    subTotal += (item.billingRegularPrice + item.chairPadPrice + item.priceAdjustment)
            }


        }
        return subTotal
    }

    fun getDeliveryFee(): Float {

        var deliveryFee = 0.0F
        val orderDateAndLocationMap = mutableMapOf<Int, Long>()

        for (item in saveOrderRqList) {

            var itemDeliveryFee = item.deliveryFee

            if (orderDateAndLocationMap.containsKey(item.locationID)) {
                val value = orderDateAndLocationMap[item.locationID]
                value?.let {
                    if (it == item.arrivalDateAndTime) {
                        itemDeliveryFee = 0.0F
                    }
                }
            }
            deliveryFee += itemDeliveryFee
            orderDateAndLocationMap.put(item.locationID, item.arrivalDateAndTime)
            //orderDateAndLocationMap.plus(Pair())
        }
        Log.i("DateTest", orderDateAndLocationMap.toString())
        return deliveryFee
    }

    fun getProcessingFee(): Float {

        var processingFee = 0.0F
        if (saveOrderRqList.isNotEmpty()) {
            processingFee = saveOrderRqList[0].processing_fee_amount
        }
        return processingFee
    }

    fun getTax(): Float {


        var tax = 0.0F

        val orderDateAndLocationMap = mutableMapOf<Int, Long>()

        for (item in saveOrderRqList) {

            var itemDeliveryFee = item.deliveryFee

            if (orderDateAndLocationMap.containsKey(item.locationID)) {
                val value = orderDateAndLocationMap[item.locationID]
                value?.let {
                    if (it == item.arrivalDateAndTime) {
                        itemDeliveryFee = 0.0F
                    }
                }
            }

            var promoValue = 0.0F

            if (item.isPromoCodeUsed) {

                if (item.promotionType) {
                    promoValue = item.promotionFigure
                } else {
                    promoValue =
                        ((item.billingRegularPrice + item.priceAdjustment) * item.promotionFigure) / 100
                }

            }
            val cashTotal = item.billingRegularPrice + item.chairPadPrice - promoValue
            val cashTax = (cashTotal + itemDeliveryFee) * (item.taxRate / 100.0F)

            tax += cashTax.roundUp()

            orderDateAndLocationMap.put(item.locationID, item.arrivalDateAndTime)
        }
        return tax
    }

    fun isPromoCodeUsed(): Boolean {

        var isPromoCodeUsed = false

        if (saveOrderRqList.isNotEmpty()) {
            isPromoCodeUsed = saveOrderRqList[0].isPromoCodeUsed
        }
        return isPromoCodeUsed
    }

    fun isBonusBillingProfile(): Boolean {

        var isBonusBillingProfile = false

        if (saveOrderRqList.isNotEmpty()) {
            isBonusBillingProfile = saveOrderRqList[0].isBonusBillingProfile
        }
        return isBonusBillingProfile
    }


    fun getPromotionFigureValue(): Float {

        var promotionFigure = 0.0F

        if (saveOrderRqList.isNotEmpty() && saveOrderRqList[0].isPromoCodeUsed) {
            promotionFigure = saveOrderRqList[0].promotionFigure
        }
        return promotionFigure
    }
    /*fun getPromotionFigure(): Float {

        var promotionFigure = 0.0F

        if(saveOrderRqList.isNotEmpty() && saveOrderRqList[0].isPromoCodeUsed) {

            if(saveOrderRqList[0].promotionType) {
                promotionFigure = saveOrderRqList[0].promotionFigure
            } else {
                promotionFigure = (saveOrderRqList[0].billingRegularPrice * saveOrderRqList[0].promotionFigure)/100
            }
        }
        return promotionFigure
    }*/

    fun getPriceAdjustment(): Float {

        var priceAdjustment = 0.0F

        if (saveOrderRqList.isNotEmpty() && saveOrderRqList[0].isBonusBillingProfile) {

            priceAdjustment = saveOrderRqList[0].priceAdjustment
        }
        return priceAdjustment
    }

    fun getPromotionType(): Boolean {


        if (saveOrderRqList.isNotEmpty() && saveOrderRqList[0].isPromoCodeUsed) {
            return saveOrderRqList[0].promotionType
        }
        return false
    }

    fun isCartEmpty(): Boolean {

        return getCartSize() < 1
    }

    fun getCartSize(): Int {

        var count = 0
        if (saveOrderRqList.isNotEmpty()) {

            val iterator = saveOrderRqList.iterator()
            while (iterator.hasNext()) {
                val orderRq = iterator.next()
                if (!orderRq.isOrderCompleted) {
                    count += 1
                }
            }
        }

        return count
    }

    fun isExtendOrder(): Boolean {

        var isExtendOrder = false
        if (saveOrderRqList.isNotEmpty()) {

            val iterator = saveOrderRqList.iterator()
            while (iterator.hasNext()) {
                val orderRq = iterator.next()
                if (!orderRq.isOrderCompleted && orderRq.isExtendOrder) {
                    isExtendOrder = true
                    break
                }
            }
        }

        return isExtendOrder
    }

    fun getValidateOrderTimeRq(): List<ValidateOrderTimeRq> {

        val validateOrderTimeListRq = mutableListOf<ValidateOrderTimeRq>()

        for (item in saveOrderRqList) {


            val arrivalDateAndTimeCal = Calendar.getInstance().apply {

                if (item.isExtendOrder)
                    timeInMillis = item.departureDateAndTime
                else
                    timeInMillis = item.arrivalDateAndTime
            }

            val departureDateAndTimeCal = Calendar.getInstance().apply {

                if (item.isExtendOrder)
                    timeInMillis = item.newDepartureDateAndTime
                else
                    timeInMillis = item.departureDateAndTime
            }

            validateOrderTimeListRq.add(
                ValidateOrderTimeRq(
                    DateFormatHelper.getStringFromCalendar(
                        DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                        arrivalDateAndTimeCal
                    ),
                    DateFormatHelper.getStringFromCalendar(
                        DateFormatHelper.FORMAT_S_HH_MM_A,
                        arrivalDateAndTimeCal
                    ),
                    DateFormatHelper.getStringFromCalendar(
                        DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                        departureDateAndTimeCal
                    ),
                    DateFormatHelper.getStringFromCalendar(
                        DateFormatHelper.FORMAT_S_HH_MM_A,
                        departureDateAndTimeCal
                    ),
                    item.deviceTypeID,
                    item.locationID
                )
            )
        }

        return validateOrderTimeListRq
    }

    fun clearData() {
        saveOrderRqList.clear()
        saveInToPreference()
    }
}


