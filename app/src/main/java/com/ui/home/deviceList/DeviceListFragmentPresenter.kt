package com.ui.home.deviceList

import android.util.Log
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.device.GetDeviceListRs
import com.data.remote.response.notification.GetBadgeCountRs
import com.data.remote.response.order.GetAllActiveOrderByCustomerIDRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class DeviceListFragmentPresenter(
    val deviceListView: DeviceListFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<DeviceListFragmentContract.View>(), DeviceListFragmentContract.Presenter {

    override fun getDeviceListByLocationId() {

        deviceListView.setProgressBar(true)

        dataRepository.getDeviceListByLocationId(dataRepository.locationId,  object : CallbackSubscriber<GetDeviceListRs>() {

            override fun onSuccess(response: GetDeviceListRs?) {

               deviceListView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->
                    if (isSuccess) {
                        Log.d("Nilam","successfully run -11-- >");
                        deviceListView.showDeviceList(response.deviceList)
                    } else {
                        Log.d("Nilam","successfully run -22-- >");
                        deviceListView.getDeviceListFail(response.result.message)
                    }
                }
                Log.d("Nilam","successfully run --- >");
            }

            override fun onFailure(restError: RestError?) {
                deviceListView.setProgressBar(false)
                deviceListView.processErrorWithToast(restError)
            }
        })
    }

    override fun getActiveOrders() {
        deviceListView.setProgressBar(true)
        dataRepository.getAllActiveOrderByCustomerID(
            dataRepository.user.id,
            object : CallbackSubscriber<GetAllActiveOrderByCustomerIDRs>() {
                override fun onSuccess(response: GetAllActiveOrderByCustomerIDRs?) {
                    deviceListView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            deviceListView.showActiveOrders(response.activeOrderList)
                        } else {
                            deviceListView.getActiveOrdersFail(response.result.message)
                        }
                    }
                }
                override fun onFailure(restError: RestError?) {
                    deviceListView.setProgressBar(false)
                    deviceListView.processErrorWithToast(restError)
                }
            })
    }

    override fun getBadgeCount() {
        deviceListView.setProgressBar(true)
        dataRepository.getBadgeCount(
            object : CallbackSubscriber<GetBadgeCountRs>() {
                override fun onSuccess(response: GetBadgeCountRs?) {

                    deviceListView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            deviceListView.showBadgeNotificationCount(response)
                        } else {
                            deviceListView.getBadgeCountFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    deviceListView.setProgressBar(false)
                    deviceListView.processErrorWithToast(restError)
                }
            })
    }

}


