package com.data.remote.request.order


data class GetPromotionDetailByLocationIDNCodeRq(
    val promocode: String,
    val DestinationID: Int
)