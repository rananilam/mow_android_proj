package com.ui.home.extendOrder

import com.data.model.destination.Location
import com.data.model.device.DeviceInfo
import com.data.model.device.DeviceOptionID
import com.data.model.device.DeviceProperty
import com.data.model.order.RewardPolicy
import com.data.model.user.User
import com.data.remote.response.order.BlackoutDatesRs
import com.data.remote.response.order.GetOrderBillingProfileRs
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface ExtendOrderFragmentContract {

    interface View : IBaseView {

        fun showOrderBillingProfile(getOrderBillingProfileRs: GetOrderBillingProfileRs)
        fun getOrderBillingProfileFail(errorMessage: String?)

        fun showLocation(location: Location)
        fun getLocationFail(errorMessage: String?)

        fun showDeviceInfo(deviceInfo: DeviceInfo)
        fun getDeviceInfoFail(errorMessage: String?)

        fun showRewardPolicy(rewardPolicy: RewardPolicy)
        fun getRewardPolicyFail(errorMessage: String?)

        fun showEntity(entity: String)

        fun showOperator(operator: User)
        fun getOperatorFail(errorMessage: String?)

        fun showOccupant(occupant: User)
        fun getOccupantFail(errorMessage: String?)

        fun showDevicePropertyOptions(deviceOptionID: DeviceOptionID, devicePropertyList: List<DeviceProperty>)
        fun getDevicePropertyOptionsFail(errorMessage: String?)
        fun showBlackDatesList(datesList: List<BlackoutDatesRs>)
        fun getBlackDatesListFail(errorMessage: String?)



    }

    interface Presenter : IBasePresenter<View> {


        fun getOrderBillingProfile(pickupDate: String,
                                   pickupTime: String,
                                   returnDate: String,
                                   returnTime: String,
                                   devicetypid: Int,
                                   locationId: Int,
                                   newReturnDate: String,
                                   newReturnTime:String,
                                   orderId: Int)

        fun getLocationById(locationId: Int)

        fun getDeviceTypeByID(deviceId: Int)

        fun getRewardPolicy()

        fun getEntityDefination(isOperatorEntity: Boolean)

        fun getOperatorByID(operatorID: Int)

        fun getOccupantByID(occupantID: Int)

        fun getDevicePropertyOptions(deviceOptionID: DeviceOptionID)
        fun getBlackoutDatesByLocationAndDevice(deviceTypeId: Int)
    }
}