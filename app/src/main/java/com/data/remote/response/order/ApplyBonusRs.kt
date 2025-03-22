package com.data.remote.response.order

import com.data.remote.response.BaseRs

class ApplyBonusRs : BaseRs() {

    var billingProfileID = 0
    var chairPadPrice = 0.0F
    var compPrice = 0.0F
    var deliveryFee = 0
    var description = ""
    var deviceTypeID = 0
    var extPrice = 0.0F
    var extPriceWithOutPromoCodeEffect = 0
    var extPriceWithPromoCodeEffect = 0
    var extensionDescription = ""
    var isAcceptBonusDayLocation = false
    var isBonusDayProfile = false
    var isBonusDayCheck = true
    var isWholeBonusDayProfileAvailable = false
    var locationID = 0
    var pickUpDate = ""
    var pickUpTime = ""
    var pickupLocationTaxRate = 0.0F
    var price = 0.0F
    var priceAdjustment = 0.0F
    var priceWithTax = 0.0F
    var rentalPeriod = ""
    var returnDate = ""
    var returnTime = ""
    var rewardPoint = 0.0F
    var taxOnPrice = 0.0F
    var totalBonusDay = 0
    var subTotal = 0

    var paidBillingProfileID = 0
    var paidBillingRewardPoint = 0.0F
    var paidBillingDescription = ""
    var paidIsBonusDayProfile = false

}
