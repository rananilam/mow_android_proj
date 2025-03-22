package com.ui.home.deviceList

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.order.GetOrderDetailRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class ActiveOrderInfoFragmentPresenter(
    val activeOrderInfoView: ActiveOrderInfoFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<ActiveOrderInfoFragmentContract.View>(), ActiveOrderInfoFragmentContract.Presenter {

    override fun getOrderDetail(orderId: Int) {
        activeOrderInfoView.setProgressBar(true)
        dataRepository.getOrderDetailsByOrderID(orderId,  object : CallbackSubscriber<GetOrderDetailRs>() {
            override fun onSuccess(response: GetOrderDetailRs?) {

                activeOrderInfoView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {

                        response.order?.let {
                            if(it.message.isNullOrEmpty()) {
                                activeOrderInfoView.showOrderDetail(response.order)
                            } else {
                                activeOrderInfoView.getOrderDetailFail(it.message)
                            }
                        }

                    } else {
                        activeOrderInfoView.getOrderDetailFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                activeOrderInfoView.setProgressBar(false)
                activeOrderInfoView.processErrorWithToast(restError)
            }
        })
    }

    override fun checkForReturnDeviceImage(orderId: Int) {

        activeOrderInfoView.setProgressBar(true)
        dataRepository.checkForReturnDeviceImage(orderId, object : CallbackSubscriber<String>() {
            override fun onSuccess(response: String?) {

                activeOrderInfoView.setProgressBar(false)

                response?.let {
                    if(it.equals("true",true)) {
                        activeOrderInfoView.isReturnDeviceImage(true)
                    } else {
                        activeOrderInfoView.isReturnDeviceImage(false)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                activeOrderInfoView.setProgressBar(false)
                activeOrderInfoView.processErrorWithToast(restError)
            }
        })

    }
}


