package com.ui.home.orderHistory

import com.data.model.order.Order
import com.data.model.order.OrderH
import com.data.remote.response.notification.GetBadgeCountRs
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface OrderHistoryFragmentContract {

    interface View : IBaseView {
        fun showOrderHistory(orderList: List<OrderH>)
        fun getOrderHistoryFail(errorMessage: String?)

        fun showOrderDetail(order: Order?)
        fun getOrderDetailFail(errorMessage: String?)

        fun reSendAttestationSuccessfully(response: String)
        fun reSendAttestationFail(errorMessage: String?)

        fun cancelOrderByOrderIdSuccessfully(response: String)
        fun cancelOrderByOrderIdFail(errorMessage: String?)

        fun showBadgeNotificationCount(getBadgeCount: GetBadgeCountRs)
        fun getBadgeCountFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {

        fun getOrderHistory(isFromVoidCall: Boolean)
        fun search(text: String)

        fun getOrderDetail(orderId: Int)

        fun getRiderRewardsPoint(): Float

        fun reSendAttestation(equipmentOrderID: Int)

        fun cancelOrderByOrderId(order: OrderH)

        fun sortBy(option: Int, isDescending: Boolean)

        fun getBadgeCount()
    }
}