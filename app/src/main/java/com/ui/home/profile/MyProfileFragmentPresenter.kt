package com.ui.home.profile

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.user.User
import com.data.remote.request.user.SaveCustomerRq
import com.data.remote.response.idanalyzer.GetDocumentRs
import com.data.remote.response.notification.GetBadgeCountRs
import com.data.remote.response.operator.GetOperatorListRs
import com.data.remote.response.order.GetAllStateRs
import com.data.remote.response.order.GetCustomerByIDRs
import com.data.remote.response.user.SaveCustomerRs
import com.data.remote.response.user.ValidateLicenseRs
import com.mow.cash.android.R
import com.ui.core.BasePresenter
import com.util.Utility
import libraries.image.helper.models.MediaResult
import libraries.image.helper.utils.StorageUtility
import libraries.retrofit.RestError

class MyProfileFragmentPresenter(
    val myProfileView: MyProfileFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<MyProfileFragmentContract.View>(), MyProfileFragmentContract.Presenter {

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


    override fun getOperatorList() {


        myProfileView.setProgressBar(true)
        dataRepository.getAllOperatorByCustomerID(dataRepository.user.id,
            object : CallbackSubscriber<GetOperatorListRs>() {
                override fun onSuccess(response: GetOperatorListRs?) {

                    myProfileView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if (response.operatorList.isNotEmpty()) {
                                myProfileView.showOperatorList(response.operatorList)
                            } else {
                                myProfileView.getOperatorListFail(response.result.message)
                            }
                        } else {
                            myProfileView.getOperatorListFail(response.result.message)
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
        cellNumber: String,
        homeNumber: String,
        licenseNumber: String,
        expiryDate: String,
        dateOfBirth: String,
        email: String,
        mediaResult: MediaResult?
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

                                    if (cellNumber.length == 13) {

                                        if (email.trim().isNotEmpty()) {

                                            val payor = User()

                                            payor.id = dataRepository.user.id
                                            payor.firstName = firstName
                                            payor.middleName = middleName
                                            payor.lastName = lastName
                                            payor.country = country
                                            payor.billAddress1 = billAddress1
                                            payor.billAddress2 = billAddress2
                                            payor.billCity = townOrCity
                                            payor.billStateID = stateId
                                            if (otherBillStateName != null)
                                                payor.otherBillStateName = otherBillStateName
                                            payor.billZip = zip
                                            payor.cellNumber = cellNumber
                                            payor.homeNumber = homeNumber
                                            payor.licenseNo = licenseNumber
                                            payor.expiryDate = expiryDate
                                            payor.dateOfBirth = dateOfBirth
                                            payor.email = email


                                            mediaResult?.let {
                                                payor.fileContent =
                                                    Utility.convertImageIntoBase64(it.path)
                                                payor.mimeType = it.mimeType
                                                payor.fileName = StorageUtility.getInstance()
                                                    .getFileNameWithExtenstion(it.path)
                                            }

                                            val saveCustomerRq = SaveCustomerRq()
                                            saveCustomerRq.payor = payor

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
                                                    R.string.validation_email_is_required
                                                )
                                            )
                                        }

                                    } else {
                                        myProfileView.showToastMessage(
                                            myProfileView.contextt.getString(
                                                R.string.validation_cell_number_is_required
                                            )
                                        )
                                    }

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

    override fun getDocument(file: MediaResult, apiKey: String) {

        myProfileView.setProgressBar(true)
        dataRepository.getDocument("https://api.idanalyzer.com", apiKey, true, "PD", false, file,
            object : CallbackSubscriber<GetDocumentRs>() {
                override fun onSuccess(response: GetDocumentRs?) {

                    myProfileView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if (response.document != null) {

                                if(response.document?.documentSide.equals("FRONT")) {

                                    myProfileView.showDocument(response.document!!)

                                } else {
                                    myProfileView.getDocumentFail(myProfileView.contextt.getString(R.string.validation_request_you_to_scan_front_of_id))
                                }

                                /*if (response.document!!.score >= 0.1) {

                                    myProfileView.showDocument(response.document!!)

                                } else {
                                    myProfileView.getDocumentFail("Document is not authentic.")
                                }*/
                            }
                        } else {
                            myProfileView.getDocumentFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    myProfileView.setProgressBar(false)
                    myProfileView.processErrorWithToast(restError)
                }
            })

    }
    override fun validateLicense() {

        myProfileView.setProgressBar(true)
        dataRepository.validateLicense(
            object : CallbackSubscriber<ValidateLicenseRs>() {
                override fun onSuccess(response: ValidateLicenseRs?) {

                    myProfileView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            myProfileView.showValidateLicense(response)
                        } else {
                            myProfileView.getValidateLicenseFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    myProfileView.setProgressBar(false)
                    myProfileView.processErrorWithToast(restError)
                }
            })
    }

    override fun getBadgeCount() {
        myProfileView.setProgressBar(true)
        dataRepository.getBadgeCount(
            object : CallbackSubscriber<GetBadgeCountRs>() {
                override fun onSuccess(response: GetBadgeCountRs?) {

                    myProfileView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            myProfileView.showBadgeNotificationCount(response)
                        } else {
                            myProfileView.getBadgeCountFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    myProfileView.setProgressBar(false)
                    myProfileView.processErrorWithToast(restError)
                }
            })
    }
}


