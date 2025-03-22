package com.ui.home.activeOrder

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.order.OrderH
import com.data.remote.response.notification.GetBadgeCountRs
import com.data.remote.response.order.GetAllActiveOrderByCustomerIDRs
import com.data.remote.response.order.GetOrderDetailRs
import com.ui.core.BasePresenter
import com.util.Utility
import libraries.retrofit.RestError

class ActiveOrdersFragmentPresenter(
    val activeOrdersView: ActiveOrdersFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<ActiveOrdersFragmentContract.View>(), ActiveOrdersFragmentContract.Presenter {

    private var activeOrderList = mutableListOf<OrderH>()

    override fun getActiveOrders() {
        activeOrdersView.setProgressBar(true)

        dataRepository.getAllActiveOrderByCustomerID(
            dataRepository.user.id,
            object : CallbackSubscriber<GetAllActiveOrderByCustomerIDRs>() {
                override fun onSuccess(response: GetAllActiveOrderByCustomerIDRs?) {
                    // Access headers

                    activeOrdersView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {

                            response.activeOrderList.map {
                                it.remainingTime = Utility.getRemainingTime(it.arrivalDate, it.departureDate)
                            }
                            activeOrderList.clear()
                            activeOrderList.addAll(response.activeOrderList)

                            activeOrdersView.showActiveOrders(activeOrderList)
                        } else {
                            activeOrdersView.getActiveOrdersFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    activeOrdersView.setProgressBar(false)
                    activeOrdersView.processErrorWithToast(restError)
                }
            })
    }

    override fun getOrderDetail(orderId: Int) {
        activeOrdersView.setProgressBar(true)
        dataRepository.getOrderDetailsByOrderID(orderId,  object : CallbackSubscriber<GetOrderDetailRs>() {
            override fun onSuccess(response: GetOrderDetailRs?) {

                activeOrdersView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {

                        response.order?.let {
                            if(it.message.isNullOrEmpty()) {
                                activeOrdersView.showOrderDetail(response.order)
                            } else {
                                activeOrdersView.getOrderDetailFail(it.message)
                            }
                        }

                    } else {
                        activeOrdersView.getOrderDetailFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                activeOrdersView.setProgressBar(false)
                activeOrdersView.processErrorWithToast(restError)
            }
        })
    }

    override fun checkForReturnDeviceImage(orderId: Int) {

        activeOrdersView.setProgressBar(true)
        dataRepository.checkForReturnDeviceImage(orderId, object : CallbackSubscriber<String>() {
            override fun onSuccess(response: String?) {

                activeOrdersView.setProgressBar(false)

                response?.let {
                    if(it.equals("true",true)) {
                        activeOrdersView.isReturnDeviceImage(true)
                    } else {
                        activeOrdersView.isReturnDeviceImage(false)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                activeOrdersView.setProgressBar(false)
                activeOrdersView.processErrorWithToast(restError)
            }
        })

    }

    override fun updateTimer() {

        if(activeOrderList.isNotEmpty()) {

            activeOrderList.map {
                it.remainingTime = Utility.getRemainingTime(it.arrivalDate, it.departureDate)
            }
            activeOrdersView.showActiveOrders(activeOrderList)
        }

    }

    override fun getBadgeCount() {
        activeOrdersView.setProgressBar(true)
        dataRepository.getBadgeCount(
            object : CallbackSubscriber<GetBadgeCountRs>() {
                override fun onSuccess(response: GetBadgeCountRs?) {

                    activeOrdersView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            activeOrdersView.showBadgeNotificationCount(response)
                        } else {
                            activeOrdersView.getBadgeCountFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    activeOrdersView.setProgressBar(false)
                    activeOrdersView.processErrorWithToast(restError)
                }
            })
    }


}


