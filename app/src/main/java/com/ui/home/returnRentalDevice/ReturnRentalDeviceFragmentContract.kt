package com.ui.home.returnRentalDevice

import com.ui.core.IBasePresenter
import com.ui.core.IBaseView
import libraries.image.helper.models.MediaResult


interface ReturnRentalDeviceFragmentContract {

    interface View : IBaseView {

        fun imageUploadedForReturnDeviceSuccessfully()
        fun imageUploadedForReturnDeviceFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {

        fun imageUploadedForReturnDevice(orderId: Int, mediaResult: MediaResult)
    }
}