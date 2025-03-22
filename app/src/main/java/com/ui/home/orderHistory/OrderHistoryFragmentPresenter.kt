package com.ui.home.orderHistory

import android.util.Log
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.order.OrderH
import com.data.remote.request.order.CancelOrderByOrderIdRq
import com.data.remote.request.order.ReSendAttestationRq
import com.data.remote.response.notification.GetBadgeCountRs
import com.data.remote.response.order.GetCustomerOrderHistoryRs
import com.data.remote.response.order.GetOrderDetailRs
import com.ui.core.BasePresenter
import iCode.utility.DateFormatHelper
import libraries.retrofit.RestError

class OrderHistoryFragmentPresenter(
    val orderHistoryView: OrderHistoryFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<OrderHistoryFragmentContract.View>(), OrderHistoryFragmentContract.Presenter {


    private var getCustomerOrderHistoryRs: GetCustomerOrderHistoryRs? = null


    override fun getOrderHistory(isFromVoidCall: Boolean) {
        orderHistoryView.setProgressBar(true)
        if(getCustomerOrderHistoryRs == null || isFromVoidCall) {

            dataRepository.getCustomerOrderHistory(dataRepository.user.id,  object : CallbackSubscriber<GetCustomerOrderHistoryRs>() {
                override fun onSuccess(response: GetCustomerOrderHistoryRs?) {

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if(response.orderHList.isNotEmpty()) {
                                getCustomerOrderHistoryRs = response
                            }
                            orderHistoryView.showOrderHistory(response.orderHList)

                        } else {
                            orderHistoryView.getOrderHistoryFail(response.result.message)
                        }
                    }
                    orderHistoryView.setProgressBar(false)
                }

                override fun onFailure(restError: RestError?) {
                    orderHistoryView.setProgressBar(false)
                    orderHistoryView.processErrorWithToast(restError)
                }
            })
        } else {
            orderHistoryView.setProgressBar(false)
            orderHistoryView.showOrderHistory(getCustomerOrderHistoryRs!!.orderHList)
        }
    }

    override fun getOrderDetail(orderId: Int) {
        orderHistoryView.setProgressBar(true)
        dataRepository.getOrderDetailsByOrderID(orderId,  object : CallbackSubscriber<GetOrderDetailRs>() {
            override fun onSuccess(response: GetOrderDetailRs?) {

                orderHistoryView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {

                        response.order?.let {
                            if(it.message.isNullOrEmpty()) {

                                orderHistoryView.showOrderDetail(response.order)
                            } else {
                                orderHistoryView.getOrderDetailFail(it.message)
                            }
                        }

                    } else {
                        orderHistoryView.getOrderDetailFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                orderHistoryView.setProgressBar(false)
                orderHistoryView.processErrorWithToast(restError)
            }
        })
    }

    override fun getRiderRewardsPoint(): Float {
        getCustomerOrderHistoryRs?.let {
            return it.userRewardsPoint
        }
        return 0.0F
    }

    override fun search(text: String) {

        getCustomerOrderHistoryRs?.let {

            if(it.orderHList.isNotEmpty()) {


                val searchList = it.orderHList.filter {
                    it.formatedOrderID.contains(text,true) ||
                    it.deviceTypeName.contains(text,true)
                }

                if(!searchList.isNullOrEmpty()) {
                    Log.i("SEARCHTEST","Size: "+searchList.size)
                    orderHistoryView.showOrderHistory(searchList)
                } else {
                    Log.i("SEARCHTEST","Size: NULL")
                }
            }
        }
    }

    override fun reSendAttestation(equipmentOrderID: Int) {

        orderHistoryView.setProgressBar(true)

        val reSendAttestationRq = ReSendAttestationRq(equipmentOrderID,0)

        dataRepository.reSendAttestation(reSendAttestationRq,  object : CallbackSubscriber<String>() {
            override fun onSuccess(response: String) {

                orderHistoryView.setProgressBar(false)
                orderHistoryView.reSendAttestationSuccessfully(response)

            }

            override fun onFailure(restError: RestError?) {
                orderHistoryView.setProgressBar(false)
                orderHistoryView.processErrorWithToast(restError)
            }
        })
    }

    override fun cancelOrderByOrderId(order: OrderH) {

        orderHistoryView.setProgressBar(true)

        val cancelOrderByOrderIdRq = CancelOrderByOrderIdRq(order.equipmentOrderID, order.refundType,0)

        dataRepository.cancelOrderByOrderId(cancelOrderByOrderIdRq,  object : CallbackSubscriber<String>() {

            override fun onSuccess(response: String) {

                Log.e("Nilam 1111" ,response.toString())
                orderHistoryView.setProgressBar(false)
                orderHistoryView.cancelOrderByOrderIdSuccessfully(response)

                /*if(response.contains("successfully",true)) {
                    order.isRefundCredit = true
                }*/

            }

            override fun onFailure(restError: RestError?) {
                orderHistoryView.setProgressBar(false)
                orderHistoryView.processErrorWithToast(restError)
            }
        })

    }

    override fun sortBy(option: Int, isDescending: Boolean) {

        getCustomerOrderHistoryRs?.let {


            when (option) {
                0 ->  {
                    if(isDescending) {
                        orderHistoryView.showOrderHistory(it.orderHList.sortedByDescending {
                            it.equipmentOrderID }
                        )
                    } else {
                        orderHistoryView.showOrderHistory(it.orderHList.sortedBy { it.equipmentOrderID })
                    }

                }
                1 -> {
                    if(isDescending) {

                        orderHistoryView.showOrderHistory(it.orderHList.sortedByDescending
                        {
                            var timeInMill = 0L

                            if(it.arrivalDate.isNotEmpty()) {
                                timeInMill = DateFormatHelper.getCalendarFromStringDate(DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A, it.arrivalDate).timeInMillis
                            }
                            timeInMill
                        })

                    } else {

                        orderHistoryView.showOrderHistory(it.orderHList.sortedBy
                        {
                            var timeInMill = 0L

                            if(it.arrivalDate.isNotEmpty()) {
                                timeInMill = DateFormatHelper.getCalendarFromStringDate(DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A, it.arrivalDate).timeInMillis
                            }
                            timeInMill
                        })

                    }
                }
                2 -> {

                    if(isDescending) {

                        orderHistoryView.showOrderHistory(it.orderHList.sortedByDescending
                        {
                            it.locationName
                        })


                    } else {
                        orderHistoryView.showOrderHistory(it.orderHList.sortedBy
                        {
                            it.locationName
                        })
                    }

                }
                else -> { // Note the block
                    print("x is neither 1 nor 2")
                }
            }


        }

    }

    override fun getBadgeCount() {
        orderHistoryView.setProgressBar(true)
        dataRepository.getBadgeCount(
            object : CallbackSubscriber<GetBadgeCountRs>() {
                override fun onSuccess(response: GetBadgeCountRs?) {

                    orderHistoryView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            orderHistoryView.showBadgeNotificationCount(response)
                        } else {
                            orderHistoryView.getBadgeCountFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    orderHistoryView.setProgressBar(false)
                    orderHistoryView.processErrorWithToast(restError)
                }
            })
    }
}


