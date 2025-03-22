package com.ui.home.checkOut

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.order.OrderData
import com.data.remote.GsonInterface
import com.data.remote.request.order.ApplyBonusRq
import com.data.remote.request.order.DeleteAuthorizedPaymentProfileRq
import com.data.remote.request.order.DeviceTrainingVideoDataRq
import com.data.remote.request.order.GetBonusDayBillingProfileInfoRq
import com.data.remote.request.order.GetPromotionDetailByLocationIDNCodeRq
import com.data.remote.response.order.ApplyBonusRs
import com.data.remote.response.order.DeleteAuthorizedPaymentProfileRs
import com.data.remote.response.order.GetAllECheckPaymentProfileByCustomerIDRs
import com.data.remote.response.order.GetAllPaymentProfileByCustomerIDRs
import com.data.remote.response.order.GetBonusDayBillingProfileInfoListRs
import com.data.remote.response.order.GetBonusDayBillingProfileInfoRs
import com.data.remote.response.order.GetCustomerByIDRs
import com.data.remote.response.order.GetDeviceTrainingVideoAndDataRs
import com.data.remote.response.order.GetDisclaimerOfPaymentMethodRs
import com.data.remote.response.order.GetProcessingFeeRs
import com.data.remote.response.order.GetPromotionDetailByLocationIDNCodeRs
import com.data.remote.response.order.SaveOrderListRs
import com.data.remote.response.order.ValidateOrderListTimeRs
import com.data.remote.response.user.ValidateLicenseRs
import com.google.gson.JsonArray
import com.mow.cash.android.R
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class CheckOutFragmentPresenter(
    val checkOutView: CheckOutFragmentContract.View,
    private val dataRepository: DataRepository,
) : BasePresenter<CheckOutFragmentContract.View>(), CheckOutFragmentContract.Presenter {

    private var getDeviceTrainingVideoAndDataRs: GetDeviceTrainingVideoAndDataRs? = null
    private var getDeviceTrainingVideoAndDataRsStr: String? = null
    override fun getCardList() {
        checkOutView.setProgressBar(true)
        dataRepository.getAllPaymentProfileByCustomerID(dataRepository.user.id,
            object : CallbackSubscriber<GetAllPaymentProfileByCustomerIDRs>() {
                override fun onSuccess(response: GetAllPaymentProfileByCustomerIDRs?) {

                    checkOutView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if (response.cardList.isNotEmpty()) {
                                checkOutView.showCardList(response.cardList)
                            } else {
                                checkOutView.getCardListFail(response.result.message)
                            }
                        } else {
                            checkOutView.showCardList(response.cardList)

                            //   checkOutView.getCardListFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    checkOutView.setProgressBar(false)
                    checkOutView.processErrorWithToast(restError)
                }
            })
    }

    override fun getECheckList() {
        checkOutView.setProgressBar(true)
        dataRepository.getAllECheckPaymentProfileByCustomerID(dataRepository.user.id,
            object : CallbackSubscriber<GetAllECheckPaymentProfileByCustomerIDRs>() {
                override fun onSuccess(response: GetAllECheckPaymentProfileByCustomerIDRs?) {

                    checkOutView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if (response.cardList.isNotEmpty()) {
                                checkOutView.showECheckList(response.cardList)
                            } else {
                                checkOutView.getECheckListFail(response.result.message)
                            }
                        } else {
                            checkOutView.showECheckList(response.cardList)
                            //   checkOutView.getCardListFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    checkOutView.setProgressBar(false)
                    checkOutView.processErrorWithToast(restError)
                }
            })
    }

    override fun getCustomer() {
        checkOutView.setProgressBar(true)
        dataRepository.getCustomerByID(dataRepository.user.id,
            object : CallbackSubscriber<GetCustomerByIDRs>() {
                override fun onSuccess(response: GetCustomerByIDRs?) {

                    checkOutView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            response.customer?.let {
                                checkOutView.showCustomer(it)
                            }

                        } else {
                            checkOutView.getCustomerFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    checkOutView.setProgressBar(false)
                    checkOutView.processErrorWithToast(restError)
                }
            })
    }

    override fun saveOrderList(isAcceptTerms: Boolean, isCreditCardRadioButtonSelected: Boolean?) {

        if (isAcceptTerms) {
            checkOutView.setProgressBar(true)
            dataRepository.saveOrderList(
                OrderData.get(),
                isCreditCardRadioButtonSelected,
                object : CallbackSubscriber<SaveOrderListRs>() {
                    override fun onSuccess(response: SaveOrderListRs?) {

                        checkOutView.setProgressBar(false)
                        checkOutView.saveOrderListSuccessfully(response)
                    }

                    override fun onFailure(restError: RestError?) {
                        checkOutView.setProgressBar(false)
                        checkOutView.processErrorWithToast(restError)
                    }
                })

        } else {
            checkOutView.showToastMessage(checkOutView.contextt.getString(R.string.validation_please_accept_terms_conditions))
        }

    }

    override fun getDeviceTrainingVideoAndData(isViewDialog: Boolean) {

        if (getDeviceTrainingVideoAndDataRs == null) {
            val saveOrderRqList = OrderData.get()
            val deviceTrainingVideoDataRqList = mutableListOf<DeviceTrainingVideoDataRq>()
            if (!saveOrderRqList.isNullOrEmpty()) {

                for (item in saveOrderRqList) {

                    deviceTrainingVideoDataRqList.add(
                        DeviceTrainingVideoDataRq(
                            item.deviceTypeID,
                            dataRepository.locationId,
                            dataRepository.user.id,
                            0,
                            item.operatorID,
                            item.isDefault,
                            false
                        )

                    )

                }
                //GetDeviceTrainingVideoAndDataRs
                checkOutView.setProgressBar(true)
                dataRepository.getDeviceTrainingVideoAndData(deviceTrainingVideoDataRqList,
                    object : CallbackSubscriber<JsonArray>() {
                        override fun onSuccess(response: JsonArray?) {

                            checkOutView.setProgressBar(false)

                            response?.let {
                                val responseObj = GsonInterface.getInstance().gson.fromJson(
                                    it,
                                    GetDeviceTrainingVideoAndDataRs::class.java
                                )



                                responseObj?.result?.isStatus?.let { isSuccess ->

                                    if (isSuccess) {

                                        if (responseObj.deviceTrainingVideoDataList.isNotEmpty()) {
                                            checkOutView.showDeviceTrainingVideoAndData(
                                                isViewDialog,
                                                responseObj
                                            )
                                            getDeviceTrainingVideoAndDataRs = responseObj
                                            getDeviceTrainingVideoAndDataRsStr = it.toString()
                                        }

                                    } else {
                                        checkOutView.getCustomerFail(responseObj.result.message)
                                    }
                                }
                            }


                        }

                        override fun onFailure(restError: RestError?) {
                            checkOutView.setProgressBar(false)
                            checkOutView.processErrorWithToast(restError)
                        }
                    })
            }
        } else {
            getDeviceTrainingVideoAndDataRs?.let {
                checkOutView.showDeviceTrainingVideoAndData(isViewDialog, it)
            }
        }

    }

    override fun getDeviceTrainingVideoAndDataStr() = getDeviceTrainingVideoAndDataRsStr

    override fun applyPromoCode(promoCode: String) {
        checkOutView.setProgressBar(true)

        val getPromotionDetailByLocationIDNCodeRq =
            GetPromotionDetailByLocationIDNCodeRq(promoCode, dataRepository.locationId)

        dataRepository.getPromotionDetailByLocationIDNCode(
            getPromotionDetailByLocationIDNCodeRq,
            object : CallbackSubscriber<GetPromotionDetailByLocationIDNCodeRs>() {
                override fun onSuccess(response: GetPromotionDetailByLocationIDNCodeRs?) {

                    checkOutView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            checkOutView.applyPromoCodeSuccessfully(promoCode, response)
                        } else {
                            checkOutView.applyPromoCodeFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    checkOutView.setProgressBar(false)
                    checkOutView.processErrorWithToast(restError)
                }
            })
    }

    override fun getBonusDayBillingProfileInfo(
        deviceTypeID: Int,
        rentalHours: Int,
        locationID: Int,
    ) {
        checkOutView.setProgressBar(true)


        val getBonusDayBillingProfileInfoRq =
            GetBonusDayBillingProfileInfoRq(deviceTypeID, rentalHours, 0, locationID, 0, 0, 0)

        dataRepository.getBonusDayBillingProfileInfo(
            getBonusDayBillingProfileInfoRq,
            object : CallbackSubscriber<GetBonusDayBillingProfileInfoRs>() {
                override fun onSuccess(response: GetBonusDayBillingProfileInfoRs?) {

                    checkOutView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            getBonusDayBillingProfileInfoList(
                                deviceTypeID,
                                rentalHours,
                                locationID
                            )

                        } else {
                            checkOutView.getBonusDayBillingProfileInfoFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    checkOutView.setProgressBar(false)
                    checkOutView.processErrorWithToast(restError)
                }
            })
    }

    override fun getBonusDayBillingProfileInfoList(
        deviceTypeID: Int,
        rentalHours: Int,
        locationID: Int,
    ) {
        checkOutView.setProgressBar(true)


        val getBonusDayBillingProfileInfoRq =
            GetBonusDayBillingProfileInfoRq(deviceTypeID, rentalHours, 0, locationID, 0, 0, 0)

        dataRepository.getBonusDayBillingProfileInfoList(
            getBonusDayBillingProfileInfoRq,
            object : CallbackSubscriber<GetBonusDayBillingProfileInfoListRs>() {
                override fun onSuccess(response: GetBonusDayBillingProfileInfoListRs?) {

                    checkOutView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            checkOutView.showBonusDayBillingProfileInfoList(response.bonusDayBillingProfileInfoList)
                        } else {
                            checkOutView.getBonusDayBillingProfileInfoListFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    checkOutView.setProgressBar(false)
                    checkOutView.processErrorWithToast(restError)
                }
            })
    }

    override fun applyBonus(
        chairPadPrice: Float,
        locationID: Int,
        deviceTypeID: Int,
        pickUpDate: String,
        pickUpTime: String,
        returnDate: String,
        returnTime: String,
        rewardPoint: Int,
        deliveryFee: Float,
        pickupLocationTaxRate: Float,
        orderRegularPrice: Float,
        isBonusDayCheck:Boolean
    ) {
        checkOutView.setProgressBar(true)


        val applyBonusRq =
            ApplyBonusRq(
                chairPadPrice,
                locationID,
                deviceTypeID,
                pickUpDate,
                pickUpTime,
                returnDate,
                returnTime,
                rewardPoint,
                deliveryFee,
                pickupLocationTaxRate,
                orderRegularPrice,
                isBonusDayCheck
            )

        dataRepository.applyBonus(
            applyBonusRq,
            object : CallbackSubscriber<ApplyBonusRs>() {
                override fun onSuccess(response: ApplyBonusRs?) {

                    checkOutView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            checkOutView.applyBonusSuccessfully(response)
                        } else {
                            checkOutView.applyBonusFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    checkOutView.setProgressBar(false)
                    checkOutView.processErrorWithToast(restError)
                }
            })
    }


    override fun deleteAuthorizedPaymentProfile(
        authorizedPaymentProfileID: Int,
        isEcheck: Boolean,
    ) {


        checkOutView.setProgressBar(true)
        val request =
            DeleteAuthorizedPaymentProfileRq(authorizedPaymentProfileID, dataRepository.user.id)
        dataRepository.deleteAuthorizedPaymentProfile(request,
            object : CallbackSubscriber<DeleteAuthorizedPaymentProfileRs>() {
                override fun onSuccess(response: DeleteAuthorizedPaymentProfileRs?) {

                    checkOutView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            checkOutView.deleteAuthorizedPaymentProfileSuccessfully(isEcheck)
                        } else {
                            checkOutView.deleteAuthorizedPaymentProfileFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    checkOutView.setProgressBar(false)
                    checkOutView.processErrorWithToast(restError)
                }
            })
    }


    override fun validateOrderListTime() {


        if (OrderData.isExtendOrder()) {
            checkOutView.validateOrderListTimeSuccessfully()
        } else {
            checkOutView.setProgressBar(true)
            val request = OrderData.getValidateOrderTimeRq()
            dataRepository.validateOrderListTime(request,
                object : CallbackSubscriber<ValidateOrderListTimeRs>() {
                    override fun onSuccess(response: ValidateOrderListTimeRs?) {

                        checkOutView.setProgressBar(false)
                        response?.let {

                            var errorMessage = ""
                            response.validateOrderTimeList.forEach {
                                if (!it.message.isNullOrEmpty()) {
                                    errorMessage = errorMessage + it.message + "\n"
                                }
                            }

                            if (errorMessage.isEmpty()) {
                                checkOutView.validateOrderListTimeSuccessfully()
                            } else {
                                checkOutView.validateOrderListTimeFail(errorMessage)
                            }
                        }
                    }

                    override fun onFailure(restError: RestError?) {
                        checkOutView.setProgressBar(false)
                        checkOutView.processErrorWithToast(restError)
                    }
                })
        }

    }

    override fun validateLicense() {

        checkOutView.setProgressBar(true)
        dataRepository.validateLicense(
            object : CallbackSubscriber<ValidateLicenseRs>() {
                override fun onSuccess(response: ValidateLicenseRs?) {

                    checkOutView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            checkOutView.showValidateLicense(response)
                        } else {
                            checkOutView.getValidateLicenseFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    checkOutView.setProgressBar(false)
                    checkOutView.processErrorWithToast(restError)
                }
            })

    }

    override fun getDisclaimerOfPaymentMethod(id: String) {

        checkOutView.setProgressBar(true)
        dataRepository.getDisclaimerOfPaymentMethod(id,
            object : CallbackSubscriber<GetDisclaimerOfPaymentMethodRs>() {
                override fun onSuccess(response: GetDisclaimerOfPaymentMethodRs?) {

                    checkOutView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            checkOutView.showDisclaimerOfPaymentMethodResponse(response)
                        } else {
                            checkOutView.showDisclaimerOfPaymentMethodResponseError(response.result.message)
                        }
                    }

//

                }

                override fun onFailure(restError: RestError?) {
                    checkOutView.setProgressBar(false)
                    checkOutView.processErrorWithToast(restError)
                }
            })

    }

    override fun getProcessingFee(
        IsCardChecked: Boolean,
        IsEcheckChecked: Boolean,
        priceWithTaxTotal: String,
    ) {

        checkOutView.setProgressBar(true)

        dataRepository.getProcessingFee(IsCardChecked, IsEcheckChecked, priceWithTaxTotal,
            object : CallbackSubscriber<GetProcessingFeeRs>() {
                override fun onSuccess(response: GetProcessingFeeRs?) {

                    checkOutView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            checkOutView.setProcessFeeData(response)
                        } else {
                            checkOutView.setProcessFeeData(response)

                          //  checkOutView.getValidateLicenseFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    checkOutView.setProgressBar(false)
                    checkOutView.processErrorWithToast(restError)
                }
            })

    }

}


