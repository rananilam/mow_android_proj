package com.data.remote.request.notification

import java.io.Serializable

data class AddUpdateFCMTokenRq(
    val UserID: Int,
    val LoggedOn: Int,
    val DeviceToken: String,
    val HardwareNo: String
) : Serializable


