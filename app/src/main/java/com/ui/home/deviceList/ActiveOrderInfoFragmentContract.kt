package com.ui.home.deviceList

import com.data.model.device.Device
import com.data.model.order.Order
import com.data.model.order.OrderH
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface ActiveOrderInfoFragmentContract {

    interface View : IBaseView {
        fun showOrderDetail(order: Order?)
        fun getOrderDetailFail(errorMessage: String?)

        fun isReturnDeviceImage(isReturn: Boolean)
        fun checkForReturnDeviceImageFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {

        fun getOrderDetail(orderId: Int)
        fun checkForReturnDeviceImage(orderId: Int)
    }
}