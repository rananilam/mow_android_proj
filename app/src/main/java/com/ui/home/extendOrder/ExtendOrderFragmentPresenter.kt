package com.ui.home.extendOrder

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.device.DeviceOptionID
import com.data.remote.response.destination.GetLocationByIdRs
import com.data.remote.response.device.GetDeviceInfoRs
import com.data.remote.response.device.GetDevicePropertyOptionRs
import com.data.remote.response.entityDefination.GetEntityDefinationRs
import com.data.remote.response.occupant.GetOccupantByIDRs
import com.data.remote.response.occupant.GetOccupantListRs
import com.data.remote.response.operator.GetOperatorByIDRs
import com.data.remote.response.operator.GetOperatorListRs
import com.data.remote.response.order.GetBlackoutDatesByLocationAndDeviceRs
import com.data.remote.response.order.GetOrderBillingProfileRs
import com.data.remote.response.order.GetRewardPolicyRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class ExtendOrderFragmentPresenter(
    val extendOrderView: ExtendOrderFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<ExtendOrderFragmentContract.View>(),
    ExtendOrderFragmentContract.Presenter {


    private var getOperatorListRs: GetOperatorListRs? = null
    private var getEntityDefinationRs: GetEntityDefinationRs? = null
    private var getBlackoutDatesByLocationAndDeviceRs: GetBlackoutDatesByLocationAndDeviceRs? = null


    /*override fun getOperatorList() {


        if (getOperatorListRs == null) {
            extendOrderView.setProgressBar(true)
            dataRepository.getAllOperatorByCustomerID(dataRepository.user.id,
                object : CallbackSubscriber<GetOperatorListRs>() {
                    override fun onSuccess(response: GetOperatorListRs?) {

                        extendOrderView.setProgressBar(false)

                        response?.result?.isStatus?.let { isSuccess ->

                            if (isSuccess) {

                                if (response.operatorList.isNotEmpty()) {
                                    getOperatorListRs = response
                                    extendOrderView.showOperatorList(response.operatorList)
                                } else {
                                    extendOrderView.getOperatorListFail(response.result.message)
                                }
                            } else {
                                extendOrderView.getOperatorListFail(response.result.message)
                            }
                        }
                    }

                    override fun onFailure(restError: RestError?) {
                        extendOrderView.setProgressBar(false)
                        extendOrderView.processErrorWithToast(restError)
                    }
                })
        } else {
            extendOrderView.showOperatorList(getOperatorListRs!!.operatorList)
        }


    }*/

    /*override fun getOccupantList(operatorID: Int) {
        extendOrderView.setProgressBar(true)
        dataRepository.getOccupantListByOperatorID(operatorID,
            object : CallbackSubscriber<GetOccupantListRs>() {
                override fun onSuccess(response: GetOccupantListRs?) {

                    extendOrderView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if (response.occupantList.isNotEmpty()) {
                                extendOrderView.showOccupantList(response.occupantList)
                            } else {
                                extendOrderView.getOccupantListFail(response.result.message)
                            }
                        } else {
                            extendOrderView.getOccupantListFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    extendOrderView.setProgressBar(false)
                    extendOrderView.processErrorWithToast(restError)
                }
            })
    }*/

    override fun getOrderBillingProfile(
        pickupDate: String,
        pickupTime: String,
        returnDate: String,
        returnTime: String,
        devicetypid: Int,
        locationId: Int,
        newReturnDate: String,
        newReturnTime: String,
        orderId: Int
    ) {
        extendOrderView.setProgressBar(true)
        dataRepository.getOrderBillingProfile(
            pickupDate, pickupTime,
            returnDate, returnTime,
            devicetypid, locationId,
            dataRepository.user.id,
            newReturnDate, newReturnTime,
            orderId.toString(),
            object : CallbackSubscriber<GetOrderBillingProfileRs>() {
                override fun onSuccess(response: GetOrderBillingProfileRs?) {

                    extendOrderView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            extendOrderView.showOrderBillingProfile(response)
                        } else {
                            extendOrderView.getOrderBillingProfileFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    extendOrderView.setProgressBar(false)
                    extendOrderView.processErrorWithToast(restError)
                }
            })
    }


    override fun getLocationById(locationId: Int) {
        dataRepository.getLocationByID(
            locationId,
            object : CallbackSubscriber<GetLocationByIdRs>() {
                override fun onSuccess(response: GetLocationByIdRs?) {
                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            response.location?.let {
                                extendOrderView.showLocation(it)
                            }

                        } else {
                            extendOrderView.getLocationFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    extendOrderView.processErrorWithToast(restError)
                }
            })
    }

    override fun getDeviceTypeByID(deviceId: Int) {


        extendOrderView.setProgressBar(true)
        dataRepository.getDeviceTypeByID(deviceId, object : CallbackSubscriber<GetDeviceInfoRs>() {
            override fun onSuccess(response: GetDeviceInfoRs?) {

                extendOrderView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {

                        response.deviceInfo?.let {
                            extendOrderView.showDeviceInfo(it)
                        }

                    } else {
                        extendOrderView.getDeviceInfoFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                extendOrderView.setProgressBar(false)
                extendOrderView.processErrorWithToast(restError)
            }
        })


    }

    override fun getRewardPolicy() {
        extendOrderView.setProgressBar(true)
        dataRepository.getRewardPolicy(
            object : CallbackSubscriber<GetRewardPolicyRs>() {
                override fun onSuccess(response: GetRewardPolicyRs?) {

                    extendOrderView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            response.rewardPolicy?.let { extendOrderView.showRewardPolicy(it) }
                        } else {
                            extendOrderView.getRewardPolicyFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    extendOrderView.setProgressBar(false)
                    extendOrderView.processErrorWithToast(restError)
                }
            })
    }

    override fun getEntityDefination(isOperatorEntity: Boolean) {

        if (getEntityDefinationRs == null) {
            extendOrderView.setProgressBar(true)
            dataRepository.getEntityDefination(
                object : CallbackSubscriber<GetEntityDefinationRs>() {
                    override fun onSuccess(response: GetEntityDefinationRs?) {

                        extendOrderView.setProgressBar(false)

                        response?.result?.isStatus?.let { isSuccess ->

                            if (isSuccess) {
                                getEntityDefinationRs = response

                                if (isOperatorEntity) {
                                    extendOrderView.showEntity(response.defineOperator)
                                } else {
                                    extendOrderView.showEntity(response.defineOccupant)
                                }
                            }
                        }
                    }

                    override fun onFailure(restError: RestError?) {
                        extendOrderView.setProgressBar(false)
                        extendOrderView.processErrorWithToast(restError)
                    }
                })
        } else {
            getEntityDefinationRs?.let {
                if (isOperatorEntity) {
                    extendOrderView.showEntity(it.defineOperator)
                } else {
                    extendOrderView.showEntity(it.defineOccupant)
                }
            }
        }

    }

    override fun getOperatorByID(operatorID: Int) {
        extendOrderView.setProgressBar(true)
        dataRepository.getOperatorByID(operatorID,
            object : CallbackSubscriber<GetOperatorByIDRs>() {
                override fun onSuccess(response: GetOperatorByIDRs?) {

                    extendOrderView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            response.operator?.let { operator ->
                                extendOrderView.showOperator(operator)
                            }
                        } else {
                            extendOrderView.getOperatorFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    extendOrderView.setProgressBar(false)
                    extendOrderView.processErrorWithToast(restError)
                }
            })
    }

    override fun getOccupantByID(occupantID: Int) {
        extendOrderView.setProgressBar(true)
        dataRepository.getOccupantByID(occupantID,
            object : CallbackSubscriber<GetOccupantByIDRs>() {
                override fun onSuccess(response: GetOccupantByIDRs?) {

                    extendOrderView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            response.occupant?.let { occupant ->
                                extendOrderView.showOccupant(occupant)
                            }
                        } else {
                            extendOrderView.getOccupantFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    extendOrderView.setProgressBar(false)
                    extendOrderView.processErrorWithToast(restError)
                }
            })
    }

    override fun getDevicePropertyOptions(deviceOptionID: DeviceOptionID) {
        extendOrderView.setProgressBar(true)
        dataRepository.getDevicePropertyOptions(deviceOptionID.id,
            object : CallbackSubscriber<GetDevicePropertyOptionRs>() {
                override fun onSuccess(response: GetDevicePropertyOptionRs?) {

                    extendOrderView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if(response.devicePropertyList.isNotEmpty())
                                extendOrderView.showDevicePropertyOptions(deviceOptionID, response.devicePropertyList)
                        } else {
                            extendOrderView.getDevicePropertyOptionsFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    extendOrderView.setProgressBar(false)
                    extendOrderView.processErrorWithToast(restError)
                }
            })
    }

    override fun getBlackoutDatesByLocationAndDevice(deviceTypeId: Int) {
        dataRepository.getBlackoutDatesByLocationAndDevice(deviceTypeId,
            dataRepository.locationId,
            object : CallbackSubscriber<GetBlackoutDatesByLocationAndDeviceRs>() {
                override fun onSuccess(response: GetBlackoutDatesByLocationAndDeviceRs?) {


                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            getBlackoutDatesByLocationAndDeviceRs = response
                            extendOrderView.showBlackDatesList(response.blackoutDatesList);
                        }
                        else {
                            extendOrderView.getBlackDatesListFail(response.result.message)
                        }
                    }


                }

                override fun onFailure(restError: RestError?) {
                    extendOrderView.processErrorWithToast(restError)
                }
            })
    }


}


