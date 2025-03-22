package com.data.remote.request.order

data class ImageUploadedForReturnDeviceRq(

    val OrderId: Int,
    val RentalImage: String,
    val MimeType: String,
    val FileName: String
)