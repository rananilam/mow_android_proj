package com.ui.home.addToReservation

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.device.DeviceOptionID
import com.data.remote.request.destination.GetPickupLocationInstructionRq
import com.data.remote.response.accessory.GetAccessoryByDeviceTypeIdListRs
import com.data.remote.response.destination.GetCustomerLocationListRs
import com.data.remote.response.destination.GetLocationByIdRs
import com.data.remote.response.destination.GetPickupLocationInstructionRs
import com.data.remote.response.device.GetDevicePropertyOptionRs
import com.data.remote.response.entityDefination.GetEntityDefinationRs
import com.data.remote.response.occupant.AddDefaultOccupantRs
import com.data.remote.response.occupant.GetOccupantListRs
import com.data.remote.response.operator.GetOperatorListRs
import com.data.remote.response.order.GetAllStateRs
import com.data.remote.response.order.GetBlackoutDatesByLocationAndDeviceRs
import com.data.remote.response.order.GetCustomerByIDRs
import com.data.remote.response.order.GetOrderBillingProfileRs
import com.data.remote.response.order.GetRewardPolicyRs
import com.data.remote.response.reservation.GetSameDayReservationRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddToReservationFragmentPresenter(
    val addToReservationView: AddToReservationFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<AddToReservationFragmentContract.View>(),
    AddToReservationFragmentContract.Presenter {


    private var getSameDayReservationRs: GetSameDayReservationRs? = null
    private var getBlackoutDatesByLocationAndDeviceRs: GetBlackoutDatesByLocationAndDeviceRs? = null
    private var getEntityDefinationRs: GetEntityDefinationRs? = null

    override fun getOperatorList() {

        addToReservationView.setProgressBar(true)
        dataRepository.getAllOperatorByCustomerID(dataRepository.user.id,
            object : CallbackSubscriber<GetOperatorListRs>() {
                override fun onSuccess(response: GetOperatorListRs?) {

                    addToReservationView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if (response.operatorList.isNotEmpty()) {
                                addToReservationView.showOperatorList(response.operatorList)
                            } else {
                                addToReservationView.getOperatorListFail(response.result.message)
                            }
                        } else {
                            addToReservationView.getOperatorListFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.setProgressBar(false)
                    addToReservationView.processErrorWithToast(restError)
                }
            })
    }

    override fun getOccupantList(operatorID: Int) {
        addToReservationView.setProgressBar(true)
        dataRepository.getOccupantListByOperatorID(operatorID,
            object : CallbackSubscriber<GetOccupantListRs>() {
                override fun onSuccess(response: GetOccupantListRs) {

                    addToReservationView.setProgressBar(false)

                    if (response.occupantList.isNotEmpty())
                        addToReservationView.showOccupantList(response.occupantList)
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.setProgressBar(false)
                    addToReservationView.processErrorWithToast(restError)
                }
            })
    }

    override fun getCustomerLocationList(deviceTypeID: Int) {

        addToReservationView.setProgressBar(true)
        dataRepository.getCustomerPickupLocationByDestination(dataRepository.locationId,
            deviceTypeID,
            object : CallbackSubscriber<GetCustomerLocationListRs>() {
                override fun onSuccess(response: GetCustomerLocationListRs?) {

                    addToReservationView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if (response.customerLocationList.isNotEmpty()) {
                                addToReservationView.showCustomerLocationList(response.customerLocationList)
                            } else {
                                addToReservationView.getCustomerLocationListFail(response.result.message)
                            }
                        } else {
                            addToReservationView.getCustomerLocationListFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.setProgressBar(false)
                    addToReservationView.processErrorWithToast(restError)
                }
            })
    }

    override fun getAccessoryList(deviceTypeID: Int) {
        addToReservationView.setProgressBar(true)
        dataRepository.getAccessoryByDeviceTypeIdList(deviceTypeID, dataRepository.locationId,
            object : CallbackSubscriber<GetAccessoryByDeviceTypeIdListRs>() {
                override fun onSuccess(response: GetAccessoryByDeviceTypeIdListRs?) {

                    addToReservationView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if (response.accessoryList.isNotEmpty()) {
                                addToReservationView.showAccessoryList(response.accessoryList)
                            } else {
                                addToReservationView.getAccessoryListFail(response.result.message)
                            }
                        } else {
                            addToReservationView.getAccessoryListFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.setProgressBar(false)
                    addToReservationView.processErrorWithToast(restError)
                }
            })
    }

    override fun getSameDayReservation(deviceTypeId: Int) {
        dataRepository.getSameDayReservation(getCurrentDay(),
            deviceTypeId,
            dataRepository.locationId,
            object : CallbackSubscriber<GetSameDayReservationRs>() {
                override fun onSuccess(response: GetSameDayReservationRs?) {


                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            getSameDayReservationRs = response
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.processErrorWithToast(restError)
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
                            addToReservationView.showBlackDatesList(response.blackoutDatesList);
                        }
                        else {
                            addToReservationView.getBlackDatesListFail(response.result.message)
                        }
                    }


                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.processErrorWithToast(restError)
                }
            })
    }


    override fun getOrderBillingProfile(
        pickupDate: String, pickupTime: String,
        returnDate: String, returnTime: String,
        devicetypid: Int, newReturnDate: String,
        newReturnTime: String, orderId: String
    ) {

        addToReservationView.setProgressBar(true)
        dataRepository.getOrderBillingProfile(
            pickupDate, pickupTime,
            returnDate, returnTime,
            devicetypid, dataRepository.locationId,
            dataRepository.user.id,
            newReturnDate, newReturnTime,
            orderId,
            object : CallbackSubscriber<GetOrderBillingProfileRs>() {
                override fun onSuccess(response: GetOrderBillingProfileRs?) {

                    addToReservationView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            addToReservationView.showOrderBillingProfile(response)
                        } else {
                            addToReservationView.getOrderBillingProfileFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.setProgressBar(false)
                    addToReservationView.processErrorWithToast(restError)
                }
            })

    }

    override fun getLocationById() {
        dataRepository.getLocationByID(
            dataRepository.locationId,
            object : CallbackSubscriber<GetLocationByIdRs>() {
                override fun onSuccess(response: GetLocationByIdRs?) {
                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            response.location?.let {
                                addToReservationView.showLocation(it)
                            }

                        } else {
                            addToReservationView.getLocationFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.processErrorWithToast(restError)
                }
            })
    }

    override fun getRewardPolicy() {
        addToReservationView.setProgressBar(true)
        dataRepository.getRewardPolicy(
            object : CallbackSubscriber<GetRewardPolicyRs>() {
                override fun onSuccess(response: GetRewardPolicyRs?) {

                    addToReservationView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            response.rewardPolicy?.let { addToReservationView.showRewardPolicy(it) }
                        } else {
                            addToReservationView.getRewardPolicyFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.setProgressBar(false)
                    addToReservationView.processErrorWithToast(restError)
                }
            })
    }

    override fun getEntityDefination(isOperatorEntity: Boolean) {

        if (getEntityDefinationRs == null) {
            addToReservationView.setProgressBar(true)
            dataRepository.getEntityDefination(
                object : CallbackSubscriber<GetEntityDefinationRs>() {
                    override fun onSuccess(response: GetEntityDefinationRs?) {

                        addToReservationView.setProgressBar(false)

                        response?.result?.isStatus?.let { isSuccess ->

                            if (isSuccess) {
                                getEntityDefinationRs = response

                                if (isOperatorEntity) {
                                    addToReservationView.showEntity(response.defineOperator)
                                } else {
                                    addToReservationView.showEntity(response.defineOccupant)
                                }
                            }
                        }
                    }

                    override fun onFailure(restError: RestError?) {
                        addToReservationView.setProgressBar(false)
                        addToReservationView.processErrorWithToast(restError)
                    }
                })
        } else {
            getEntityDefinationRs?.let {
                if (isOperatorEntity) {
                    addToReservationView.showEntity(it.defineOperator)
                } else {
                    addToReservationView.showEntity(it.defineOccupant)
                }
            }
        }

    }

    override fun getCustomer() {
        addToReservationView.setProgressBar(true)
        dataRepository.getCustomerByID(dataRepository.user.id,
            object : CallbackSubscriber<GetCustomerByIDRs>() {
                override fun onSuccess(response: GetCustomerByIDRs?) {

                    addToReservationView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            response.customer?.let {
                                addToReservationView.showCustomer(it)
                            }

                        } else {
                            addToReservationView.getCustomerFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.setProgressBar(false)
                    addToReservationView.processErrorWithToast(restError)
                }
            })
    }

    override fun addDefaultOccupant(operatorID: Int) {
        addToReservationView.setProgressBar(true)
        dataRepository.addDefaultOccupant(operatorID,
            object : CallbackSubscriber<AddDefaultOccupantRs>() {
                override fun onSuccess(response: AddDefaultOccupantRs?) {

                    addToReservationView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            addToReservationView.showDefaultOccupant(
                                response.selectedOccupantID,
                                response.occupantList
                            )
                        } else {
                            addToReservationView.getDefaultOccupantFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.setProgressBar(false)
                    addToReservationView.processErrorWithToast(restError)
                }
            })
    }

    override fun getPickupLocationInstruction(deviceTypeID: Int, pickupLocationID: Int) {


        val getPickupLocationInstructionRq = GetPickupLocationInstructionRq(
            deviceTypeID,
            dataRepository.locationId,
            pickupLocationID
        )
        addToReservationView.setProgressBar(true)
        dataRepository.getPickupLocationInstruction(getPickupLocationInstructionRq,
            object : CallbackSubscriber<GetPickupLocationInstructionRs>() {

                override fun onSuccess(response: GetPickupLocationInstructionRs?) {

                    addToReservationView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            response.pickupLocationInstruction?.let { pickupLocationInstruction ->
                                addToReservationView.showPickupLocationInstruction(
                                    pickupLocationInstruction
                                )
                            }

                        } else {
                            addToReservationView.getPickupLocationInstructionFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.setProgressBar(false)
                    addToReservationView.processErrorWithToast(restError)
                }
            })

    }

    override fun getDevicePropertyOptions(deviceOptionID: DeviceOptionID) {
        addToReservationView.setProgressBar(true)
        dataRepository.getDevicePropertyOptions(deviceOptionID.id,
            object : CallbackSubscriber<GetDevicePropertyOptionRs>() {
                override fun onSuccess(response: GetDevicePropertyOptionRs?) {

                    addToReservationView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if(response.devicePropertyList.isNotEmpty())
                                addToReservationView.showDevicePropertyOptions(deviceOptionID, response.devicePropertyList)
                        } else {
                            addToReservationView.getDevicePropertyOptionsFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.setProgressBar(false)
                    addToReservationView.processErrorWithToast(restError)
                }
            })
    }

    override fun getAllState() {

        addToReservationView.setProgressBar(true)
        dataRepository.getAllState(
            object : CallbackSubscriber<GetAllStateRs>() {
                override fun onSuccess(response: GetAllStateRs?) {

                    addToReservationView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            addToReservationView.showStateList(response.stateList)

                        } else {
                            addToReservationView.getStateListFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addToReservationView.setProgressBar(false)
                    addToReservationView.processErrorWithToast(restError)
                }
            })
    }
    override fun getMaxHour(arrivalDate: Calendar): Int {

        val currentDate = Calendar.getInstance()

        var maxHour = 23

        if (currentDate.get(Calendar.DAY_OF_YEAR) == arrivalDate.get(Calendar.DAY_OF_YEAR) &&
            currentDate.get(Calendar.YEAR) == arrivalDate.get(Calendar.YEAR)
        ) {

            getSameDayReservationRs?.let {
                // "EndTime": "16:00",

                val endTimeStr = it.endTime

                if (endTimeStr.isNotEmpty()) {
                    val endTimeStrSplit = endTimeStr.split(":")
                    if (endTimeStrSplit.isNotEmpty()) {
                        maxHour = endTimeStrSplit[0].toInt()
                    }
                }
            }
        }

        return maxHour

    }

    override fun getMaxMinute(arrivalDate: Calendar): Int {
        val currentDate = Calendar.getInstance()

        var maxMinute = 59

        if (currentDate.get(Calendar.DAY_OF_YEAR) == arrivalDate.get(Calendar.DAY_OF_YEAR) &&
            currentDate.get(Calendar.YEAR) == arrivalDate.get(Calendar.YEAR)
        ) {

            getSameDayReservationRs?.let {
                // "EndTime": "16:00",

                val endTimeStr = it.endTime

                if (endTimeStr.isNotEmpty()) {
                    val endTimeStrSplit = endTimeStr.split(":")
                    if (endTimeStrSplit.isNotEmpty()) {
                        maxMinute = endTimeStrSplit[1].toInt()
                    }
                }
            }
        }

        return maxMinute
    }

    override fun getMinHour(arrivalDate: Calendar): Int {
        val currentDate = Calendar.getInstance()

        var minHour = 0

        if (currentDate.get(Calendar.DAY_OF_YEAR) == arrivalDate.get(Calendar.DAY_OF_YEAR) &&
            currentDate.get(Calendar.YEAR) == arrivalDate.get(Calendar.YEAR)
        ) {

            getSameDayReservationRs?.let {
                // "EndTime": "16:00",

                val startTimeStr = it.startTime

                if (startTimeStr.isNotEmpty()) {
                    val startTimeStrSplit = startTimeStr.split(":")
                    if (startTimeStrSplit.isNotEmpty()) {
                        minHour = startTimeStrSplit[0].toInt()
                    }
                }
            }
        }

        return minHour
    }

    override fun getMinMinute(arrivalDate: Calendar): Int {
        val currentDate = Calendar.getInstance()

        var minMinute = 0

        if (currentDate.get(Calendar.DAY_OF_YEAR) == arrivalDate.get(Calendar.DAY_OF_YEAR) &&
            currentDate.get(Calendar.YEAR) == arrivalDate.get(Calendar.YEAR)
        ) {

            getSameDayReservationRs?.let {
                // "EndTime": "16:00",

                val startTimeStr = it.startTime

                if (startTimeStr.isNotEmpty()) {
                    val startTimeStrSplit = startTimeStr.split(":")
                    if (startTimeStrSplit.isNotEmpty()) {
                        minMinute = startTimeStrSplit[1].toInt()
                    }
                }
            }
        }

        return minMinute
    }


    private fun getCurrentDay(): String {
        val simpleDateFormat = SimpleDateFormat("EEEE")
        val d = Date()
        return simpleDateFormat.format(d)
    }


}


