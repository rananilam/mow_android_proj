package com.data.remote.request.order

data class ValidateOrderTimeRq(

    val PickUpDate: String,
    val PickUpTime: String,
    val ReturnDate: String,
    val ReturnTime: String,
    val DeviceId: Int,
    val LocationId: Int
)