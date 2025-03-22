package com.ui.home.billingAddress

import android.util.Log
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.user.User
import com.data.remote.request.user.SaveCustomerRq
import com.data.remote.response.order.GetAllStateRs
import com.data.remote.response.order.GetCustomerByIDRs
import com.data.remote.response.user.SaveCustomerRs
import com.mow.cash.android.R
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class BillingAddressFragmentPresenter(
    val myProfileView: BillingAddressFragmentContract.View,
    private val dataRepository: DataRepository

) : BasePresenter<BillingAddressFragmentContract.View>(), BillingAddressFragmentContract.Presenter {

    override fun getCustomer() {
        myProfileView.setProgressBar(true)
        dataRepository.getCustomerByID(dataRepository.user.id,
            object : CallbackSubscriber<GetCustomerByIDRs>() {
                override fun onSuccess(response: GetCustomerByIDRs?) {

                    myProfileView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            response.customer?.let {
                                myProfileView.showCustomer(it)
                            }

                        } else {
                            myProfileView.getCustomerFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    myProfileView.setProgressBar(false)
                    myProfileView.processErrorWithToast(restError)
                }
            })
    }



    override fun getAllState() {

        myProfileView.setProgressBar(true)
        dataRepository.getAllState(
            object : CallbackSubscriber<GetAllStateRs>() {
                override fun onSuccess(response: GetAllStateRs?) {

                    myProfileView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            myProfileView.showStateList(response.stateList)

                        } else {
                            myProfileView.getStateListFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    myProfileView.setProgressBar(false)
                    myProfileView.processErrorWithToast(restError)
                }
            })
    }


    override fun saveCustomer(
        firstName: String,
        middleName: String,
        lastName: String,
        country: String,
        billAddress1: String,
        billAddress2: String,
        townOrCity: String,
        stateId: Int,
        otherBillStateName: String?,
        zip: String,
        email: String,
        payor: User?
    ) {

        if (firstName.trim().isNotEmpty()) {

            if (lastName.trim().isNotEmpty()) {

                if (country.trim().isNotEmpty()) {

                    if (billAddress1.trim().isNotEmpty() || billAddress2.trim().isNotEmpty()) {

                        if (townOrCity.trim().isNotEmpty()) {

                            var isStateValid = true
                            if (country.equals("USA", true)) {

                                if (stateId == 0) {
                                    isStateValid = false
                                    myProfileView.showToastMessage(
                                        myProfileView.contextt.getString(
                                            R.string.validation_please_select_state
                                        )
                                    )
                                }

                            } else {
                                if (otherBillStateName.isNullOrEmpty()) {
                                    isStateValid = false
                                    myProfileView.showToastMessage(
                                        myProfileView.contextt.getString(
                                            R.string.validation_other_state_is_required
                                        )
                                    )
                                }
                            }

                            if (isStateValid) {

                                if (zip.trim().isNotEmpty()) {

                                    val payor_demo = User()

                                    payor_demo.id = dataRepository.user.id
                                    payor_demo.firstName = firstName
                                    payor_demo.middleName = middleName
                                    payor_demo.lastName = lastName
                                    payor_demo.country = country
                                    payor_demo.billAddress1 = billAddress1
                                    payor_demo.billAddress2 = billAddress2
                                    payor_demo.billCity = townOrCity
                                    payor_demo.billStateID = stateId
                                    // nilam update mobile number in billing address
                                    Log.e("Nilam","Update mobile number in billing address ")
                                    payor_demo.cellNumber = payor?.cellNumber ?: ""
                                    payor_demo.homeNumber = payor?.homeNumber ?: ""
                                    payor_demo.licenseNo = payor?.licenseNo ?: ""
                                    payor_demo.expiryDate = payor?.expiryDate ?: ""
                                    payor_demo.dateOfBirth = payor?.dateOfBirth ?: ""
                                    if (otherBillStateName != null)
                                        payor_demo.otherBillStateName = otherBillStateName
                                    payor_demo.billZip = zip
                                    payor_demo.email = email


                                    val saveCustomerRq = SaveCustomerRq()
                                    saveCustomerRq.payor = payor_demo

                                    myProfileView.setProgressBar(true)
                                    dataRepository.updateProfile(
                                        saveCustomerRq,
                                        object :
                                            CallbackSubscriber<SaveCustomerRs>() {
                                            override fun onSuccess(
                                                response: SaveCustomerRs?
                                            ) {

                                                myProfileView.setProgressBar(
                                                    false
                                                )

                                                response?.result?.isStatus?.let { isSuccess ->

                                                    if (isSuccess) {
                                                        myProfileView.saveCustomerSuccessfully()
                                                    } else {
                                                        myProfileView.saveCustomerFail(
                                                            response.result.message
                                                        )
                                                    }
                                                }
                                            }

                                            override fun onFailure(
                                                restError: RestError?
                                            ) {
                                                myProfileView.setProgressBar(
                                                    false
                                                )
                                                myProfileView.processErrorWithToast(
                                                    restError
                                                )
                                            }
                                        })

                                } else {
                                    myProfileView.showToastMessage(
                                        myProfileView.contextt.getString(
                                            R.string.validation_zip_is_required
                                        )
                                    )
                                }
                            }

                        } else {
                            myProfileView.showToastMessage(myProfileView.contextt.getString(R.string.validation_town_city_is_required))
                        }

                    } else {
                        myProfileView.showToastMessage(myProfileView.contextt.getString(R.string.validation_address_is_required))
                    }

                } else {
                    myProfileView.showToastMessage(myProfileView.contextt.getString(R.string.validation_country_is_required))
                }

            } else {
                myProfileView.showToastMessage(myProfileView.contextt.getString(R.string.validation_last_name_is_required))
            }

        } else {
            myProfileView.showToastMessage(myProfileView.contextt.getString(R.string.validation_first_name_is_required))
        }

    }

}


