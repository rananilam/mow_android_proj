package com.ui.home.deviceDetail

import com.data.model.device.DeviceInfo
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface DeviceDetailFragmentContract {

    interface View : IBaseView {
        fun showDeviceInfo(deviceInfo: DeviceInfo)
        fun getDeviceInfoFail(errorMessage: String?)

        fun showRentalRates(rateList: List<String>)
        fun getRentalRatesFail(errorMessage: String?)

    }

    interface Presenter : IBasePresenter<View> {

        fun getDeviceTypeByID(deviceId: Int)
        fun getRentalRatesByDeviceID(deviceTypeID: Int)
        fun isOperatorOccupantSame(): Boolean?


    }
}