package com.ui.home.deviceList

import com.data.model.device.Device
import com.data.model.order.OrderH
import com.data.remote.response.notification.GetBadgeCountRs
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface DeviceListFragmentContract {

    interface View : IBaseView {
        fun showDeviceList(deviceList: List<Device>)
        fun getDeviceListFail(errorMessage: String?)

        fun showActiveOrders(activeOrders: List<OrderH>)
        fun getActiveOrdersFail(errorMessage: String?)

        fun showBadgeNotificationCount(getBadgeCount: GetBadgeCountRs)
        fun getBadgeCountFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {

        fun getDeviceListByLocationId()
        fun getActiveOrders()
        fun getBadgeCount()
    }
}