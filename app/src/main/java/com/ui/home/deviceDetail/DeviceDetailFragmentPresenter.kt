package com.ui.home.deviceDetail

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.device.GetDeviceInfoRs
import com.data.remote.response.device.RentalPriceListRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class DeviceDetailFragmentPresenter(
    val deviceDetailView: DeviceDetailFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<DeviceDetailFragmentContract.View>(), DeviceDetailFragmentContract.Presenter {


    private var getDeviceInfoRs: GetDeviceInfoRs? = null
    private var rentalPriceListRs: RentalPriceListRs? = null
    override fun getDeviceTypeByID(deviceId: Int) {



        if(getDeviceInfoRs == null) {
            deviceDetailView.setProgressBar(true)
            dataRepository.getDeviceTypeByID(deviceId,  object : CallbackSubscriber<GetDeviceInfoRs>() {
                override fun onSuccess(response: GetDeviceInfoRs?) {

                    deviceDetailView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            response.deviceInfo?.let {
                                getDeviceInfoRs = response
                                deviceDetailView.showDeviceInfo(it)
                            }

                        } else {
                            deviceDetailView.getDeviceInfoFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    deviceDetailView.setProgressBar(false)
                    deviceDetailView.processErrorWithToast(restError)
                }
            })
        } else {
            deviceDetailView.showDeviceInfo(getDeviceInfoRs!!.deviceInfo!!)
        }


    }

    override fun getRentalRatesByDeviceID(deviceTypeID: Int) {


        if(rentalPriceListRs == null) {
            deviceDetailView.setProgressBar(true)

            dataRepository.getRentalRatesByDeviceID(deviceTypeID, dataRepository.locationId,
                    object : CallbackSubscriber<RentalPriceListRs>() {
                        override fun onSuccess(response: RentalPriceListRs?) {

                            deviceDetailView.setProgressBar(false)

                            response?.result?.isStatus?.let { isSuccess ->

                                if (isSuccess) {
                                    rentalPriceListRs = response
                                    deviceDetailView.showRentalRates(response.rentalPriceList)
                                } else {
                                    deviceDetailView.getDeviceInfoFail(response.result.message)
                                }
                            }
                        }

                        override fun onFailure(restError: RestError?) {
                            deviceDetailView.setProgressBar(false)
                            deviceDetailView.processErrorWithToast(restError)
                        }
                    })
        } else {
            deviceDetailView.showRentalRates(rentalPriceListRs!!.rentalPriceList)
        }
    }

    override fun isOperatorOccupantSame(): Boolean? {
        return getDeviceInfoRs?.deviceInfo?.operatorOccupantSame
    }
}


