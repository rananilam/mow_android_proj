package com.data.remote.request.order

data class ApplyBonusRq(

    val ChairPadPrice: Float,
    val LocationID: Int,
    val DeviceTypeID: Int,
    val PickUpDate: String,
    val PickUpTime: String,
    val ReturnDate: String,
    val ReturnTime: String,
    val RewardPoint: Int,
    val DeliveryFee: Float,
    val PickupLocationTaxRate: Float,
    val OrderRegularPrice: Float,
    val IsBonusDayCheck:Boolean
)