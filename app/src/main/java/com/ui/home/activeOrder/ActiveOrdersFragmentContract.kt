package com.ui.home.activeOrder

import com.data.model.order.Order
import com.data.model.order.OrderH
import com.data.remote.response.notification.GetBadgeCountRs
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface ActiveOrdersFragmentContract {

    interface View : IBaseView {
        fun showActiveOrders(activeOrders: List<OrderH>)
        fun getActiveOrdersFail(errorMessage: String?)

        fun showOrderDetail(order: Order?)
        fun getOrderDetailFail(errorMessage: String?)

        fun isReturnDeviceImage(isReturn: Boolean)
        fun checkForReturnDeviceImageFail(errorMessage: String?)

        fun showBadgeNotificationCount(getBadgeCount: GetBadgeCountRs)
        fun getBadgeCountFail(errorMessage: String?)

    }

    interface Presenter : IBasePresenter<View> {

        fun getActiveOrders()
        fun getOrderDetail(orderId: Int)

        fun checkForReturnDeviceImage(orderId: Int)
        fun updateTimer()
        fun getBadgeCount()

    }
}