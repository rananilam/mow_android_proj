package com.data.remote.request.order

data class CancelOrderByOrderIdRq(
    val OrderId: Int,
    val RefundType: String,
    val ComporId: Int
)