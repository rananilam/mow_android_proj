package com.ui.home.addCustomerECheck

import android.util.Log
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.order.AuthorizeECheckRequestRs
import com.data.remote.response.order.GetAllStateRs
import com.data.remote.response.order.GetCustomerByIDRs
import com.mow.cash.android.R
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class AddCustomerECheckFragmentPresenter(
    val addCustomerECheckView: AddCustomerECheckFragmentContract.View,
    private val dataRepository: DataRepository,
) : BasePresenter<AddCustomerECheckFragmentContract.View>(),
    AddCustomerECheckFragmentContract.Presenter {


    override fun getAllState() {

        addCustomerECheckView.setProgressBar(true)
        dataRepository.getAllState(object : CallbackSubscriber<GetAllStateRs>() {
            override fun onSuccess(response: GetAllStateRs?) {

                addCustomerECheckView.setProgressBar(false)
                response?.result?.isStatus?.let { isSuccess ->
                    if (isSuccess) {
                        addCustomerECheckView.showStateList(response.stateList)
                    } else {
                        addCustomerECheckView.getStateListFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                addCustomerECheckView.setProgressBar(false)
                addCustomerECheckView.processErrorWithToast(restError)
            }
        })
    }

    override fun getCustomer() {
        addCustomerECheckView.setProgressBar(true)
        dataRepository.getCustomerByID(
            dataRepository.user.id,
            object : CallbackSubscriber<GetCustomerByIDRs>() {
                override fun onSuccess(response: GetCustomerByIDRs?) {

                    addCustomerECheckView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            response.customer?.let {
                                addCustomerECheckView.showCustomer(it)
                            }

                        } else {
                            addCustomerECheckView.getCustomerFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addCustomerECheckView.setProgressBar(false)
                    addCustomerECheckView.processErrorWithToast(restError)
                }
            })
    }

    override fun matchAccountNumber(confirmAccNo: String, AccNo: String) {
        if (AccNo == confirmAccNo) {
            Log.i("NILAM", "Account numbers match --->ConfirmAccNo: ${confirmAccNo}")
            addCustomerECheckView.showAccountMatchOrNotToast(true)


        } else {
            Log.i("NILAM", "Account numbers do not match --->ConfirmAccNo: ${confirmAccNo}")
            addCustomerECheckView.showAccountMatchOrNotToast(false)

        }

    }

    override fun matchRoutingNumber(confirmRoutingNo: String, routingNo: String) {
        if (routingNo == confirmRoutingNo) {
            Log.i("NILAM", "Routing numbers match --->ConfirmAccNo: ${confirmRoutingNo}")
            addCustomerECheckView.showRoutingMatchOrNotToast(true)


        } else {
            Log.i("NILAM", "Routing numbers do not match --->ConfirmAccNo: ${confirmRoutingNo}")
            addCustomerECheckView.showRoutingMatchOrNotToast(false)

        }

    }

    override fun getAllBankAccType() {

        val stringList: MutableMap<Int, String> = mutableMapOf(
            0 to "Checking",
            1 to "Savings",

            )
        addCustomerECheckView.showAllBankAccType(stringList)
    }

    override fun authorizeECheckRequest(
        firstName: String,
        middleName: String,
        lastName: String,
        address1: String,
        address2: String,
        country: String,
        state: String,
        stateID: Int,
        city: String,
        zip: String,
        phone: String,
        email: String,
        bankName: String,
        bankAccountType: String,
        accountNumber: String,
        routingNumber: String,
        confirmAccountNumber: String,
        isAccountNumberMatched: Boolean,
        confirmRoutingNumber: String,
        isRoutingNumberMatched: Boolean,

        ) {


        if (firstName.trim().isNotEmpty()) {

            if (lastName.trim().isNotEmpty()) {

                if (address1.trim().isNotEmpty()) {


                    if (city.trim().isNotEmpty()) {


                        if (stateID != 0) {


                            if (zip.trim().isNotEmpty()) {


                                if (phone.trim().isNotEmpty()) {

                                    if (email.trim().isNotEmpty()) {


                                        if (bankAccountType.trim().isNotEmpty()) {

                                            if (accountNumber.trim().isNotEmpty()) {

                                                if (confirmAccountNumber.trim().isNotEmpty()) {
                                                    if (isAccountNumberMatched) {

                                                        if (routingNumber.trim().isNotEmpty()) {
                                                            if (confirmRoutingNumber.trim()
                                                                    .isNotEmpty()
                                                            ) {
                                                                if (isRoutingNumberMatched) {
                                                                    addCustomerECheckView.setProgressBar(
                                                                        true
                                                                    )
                                                                    dataRepository.authorizeECheckRequest(
                                                                        dataRepository.user.id,
                                                                        firstName,
                                                                        middleName,
                                                                        lastName,
                                                                        address1,
                                                                        address2,
                                                                        country,
                                                                        state,
                                                                        stateID,
                                                                        city,
                                                                        zip,
                                                                        phone,
                                                                        email,
                                                                        bankName,
                                                                        bankAccountType,
                                                                        accountNumber,
                                                                        routingNumber,
                                                                        object :
                                                                            CallbackSubscriber<AuthorizeECheckRequestRs>() {
                                                                            override fun onSuccess(
                                                                                response: AuthorizeECheckRequestRs?,
                                                                            ) {

                                                                                addCustomerECheckView.setProgressBar(
                                                                                    false
                                                                                )

                                                                                response?.result?.isStatus?.let { isSuccess ->
                                                                                    if (isSuccess) {
                                                                                        addCustomerECheckView.authorizeECheckSuccessfully()

                                                                                    } else {
                                                                                        addCustomerECheckView.authorizeECheckFail(
                                                                                            response.result.errorMessage
                                                                                        )
                                                                                    }
                                                                                }

                                                                            }

                                                                            override fun onFailure(
                                                                                restError: RestError?,
                                                                            ) {
                                                                                addCustomerECheckView.setProgressBar(
                                                                                    false
                                                                                )
                                                                                addCustomerECheckView.processErrorWithToast(
                                                                                    restError
                                                                                )
                                                                            }
                                                                        })
                                                                } else {
                                                                    addCustomerECheckView.showToastMessage(
                                                                        addCustomerECheckView.contextt.getString(
                                                                            R.string.validation_confirm_routing_number_doesnt_match
                                                                        )
                                                                    )
                                                                }

                                                            } else {
                                                                addCustomerECheckView.showToastMessage(
                                                                    addCustomerECheckView.contextt.getString(
                                                                        R.string.validation_please_enter_confirm_routing_number
                                                                    )
                                                                )
                                                            }


                                                        } else {
                                                            addCustomerECheckView.showToastMessage(
                                                                addCustomerECheckView.contextt.getString(
                                                                    R.string.validation_please_enter_routing_number
                                                                )
                                                            )
                                                        }

                                                    } else {

                                                        addCustomerECheckView.showToastMessage(
                                                            addCustomerECheckView.contextt.getString(
                                                                R.string.validation_confirm_account_number_doesnt_match
                                                            )
                                                        )

                                                    }

                                                } else {
                                                    addCustomerECheckView.showToastMessage(
                                                        addCustomerECheckView.contextt.getString(
                                                            R.string.validation_please_enter_confirm_account_number
                                                        )
                                                    )

                                                }


                                            } else {
                                                addCustomerECheckView.showToastMessage(
                                                    addCustomerECheckView.contextt.getString(
                                                        R.string.validation_please_enter_account_number
                                                    )
                                                )
                                            }

                                        } else {
                                            addCustomerECheckView.showToastMessage(
                                                addCustomerECheckView.contextt.getString(
                                                    R.string.validation_please_select_bank_account_type
                                                )
                                            )
                                        }


                                    } else {
                                        addCustomerECheckView.showToastMessage(
                                            addCustomerECheckView.contextt.getString(
                                                R.string.validation_please_enter_email_address
                                            )
                                        )
                                    }

                                } else {
                                    addCustomerECheckView.showToastMessage(
                                        addCustomerECheckView.contextt.getString(
                                            R.string.validation_please_enter_phone_number
                                        )
                                    )
                                }

                            } else {
                                addCustomerECheckView.showToastMessage(
                                    addCustomerECheckView.contextt.getString(
                                        R.string.validation_please_enter_zip_code
                                    )
                                )
                            }

                        } else {
                            addCustomerECheckView.showToastMessage(
                                addCustomerECheckView.contextt.getString(
                                    R.string.validation_please_select_state
                                )
                            )
                        }

                    } else {
                        addCustomerECheckView.showToastMessage(
                            addCustomerECheckView.contextt.getString(
                                R.string.validation_please_enter_town_city
                            )
                        )
                    }

                } else {
                    addCustomerECheckView.showToastMessage(
                        addCustomerECheckView.contextt.getString(
                            R.string.validation_please_enter_street_address
                        )
                    )
                }
            } else {
                addCustomerECheckView.showToastMessage(
                    addCustomerECheckView.contextt.getString(
                        R.string.validation_please_enter_last_name
                    )
                )
            }

        } else {
            addCustomerECheckView.showToastMessage(addCustomerECheckView.contextt.getString(R.string.validation_please_enter_first_name))
        }

    }
}





