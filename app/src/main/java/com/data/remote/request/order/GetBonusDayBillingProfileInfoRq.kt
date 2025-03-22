package com.data.remote.request.order


data class GetBonusDayBillingProfileInfoRq(
    val DeviceTypeID: Int,
    val RentalHours: Int,
    val BillingProfileID: Int,
    val LocationID: Int,
    val rentalFromHours: Int,
    val rentalToHours: Int,
    val OrderId: Int
)

/*
{"DeviceTypeID":52,"RentalHours":24,"BillingProfileID":0,"LocationID":3,"rentalFromHours":0,"rentalToHours":0,"OrderId":0}
 */