package com.ui.home.addToReservation

import com.data.model.accessory.Accessory
import com.data.model.destination.Location
import com.data.model.destination.PickupLocationInstruction
import com.data.model.device.DeviceOptionID
import com.data.model.device.DeviceProperty
import com.data.model.order.RewardPolicy
import com.data.model.order.State
import com.data.model.user.User
import com.data.remote.response.order.BlackoutDatesRs
import com.data.remote.response.order.GetOrderBillingProfileRs
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView
import java.util.Calendar


interface AddToReservationFragmentContract {

    interface View : IBaseView {
        fun showOperatorList(operatorList: List<User>)
        fun getOperatorListFail(errorMessage: String?)

        fun showOccupantList(occupantList: List<User>)
        fun getOccupantListFail(errorMessage: String?)

        fun showCustomerLocationList(customerLocationList: List<Location>)
        fun getCustomerLocationListFail(errorMessage: String?)

        fun showAccessoryList(accessoryList: List<Accessory>)
        fun getAccessoryListFail(errorMessage: String?)

        fun showOrderBillingProfile(getOrderBillingProfileRs: GetOrderBillingProfileRs)
        fun getOrderBillingProfileFail(errorMessage: String?)

        fun showLocation(location: Location)
        fun getLocationFail(errorMessage: String?)

        fun showRewardPolicy(rewardPolicy: RewardPolicy)
        fun getRewardPolicyFail(errorMessage: String?)

        fun showEntity(entity: String)

        fun showCustomer(customer: User)
        fun getCustomerFail(errorMessage: String?)

        fun showDefaultOccupant(selectedOccupantID: Int, occupantList: List<User>)
        fun getDefaultOccupantFail(errorMessage: String?)

        fun showPickupLocationInstruction(pickupLocationInstruction: PickupLocationInstruction)
        fun getPickupLocationInstructionFail(errorMessage: String?)

        fun showDevicePropertyOptions(deviceOptionID: DeviceOptionID, devicePropertyList: List<DeviceProperty>)
        fun getDevicePropertyOptionsFail(errorMessage: String?)

        fun showStateList(stateList: List<State>)
        fun getStateListFail(errorMessage: String?)

        fun showBlackDatesList(datesList: List<BlackoutDatesRs>)
        fun getBlackDatesListFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {

        fun getOperatorList()
        fun getOccupantList(operatorID: Int)
        fun getCustomerLocationList(deviceTypeID: Int)
        fun getAccessoryList(deviceTypeID: Int)

        fun getSameDayReservation(deviceTypeId: Int)
        fun getBlackoutDatesByLocationAndDevice(deviceTypeId: Int)

        fun getMaxHour(arrivalDate: Calendar): Int
        fun getMaxMinute(arrivalDate: Calendar): Int

        fun getMinHour(arrivalDate: Calendar): Int
        fun getMinMinute(arrivalDate: Calendar): Int

        fun getOrderBillingProfile(pickupDate: String,
                                   pickupTime: String,
                                   returnDate: String,
                                   returnTime: String,
                                   devicetypid: Int,
                                   newReturnDate: String,
                                   newReturnTime:String,
                                   orderId: String)

        fun getLocationById()

        fun getRewardPolicy()

        fun getEntityDefination(isOperatorEntity: Boolean)

        fun getCustomer()

        fun addDefaultOccupant(operatorID: Int)

        fun getPickupLocationInstruction(deviceTypeID: Int,
                                         pickupLocationID: Int)

        fun getDevicePropertyOptions(deviceOptionID: DeviceOptionID)

        fun getAllState()
    }
}