package com.ui.login.register.payor

import android.util.Patterns
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.user.User
import com.data.remote.request.user.SaveCustomerRq
import com.data.remote.response.idanalyzer.GetDocumentRs
import com.data.remote.response.order.GetAllStateRs
import com.data.remote.response.user.SaveCustomerRs
import com.ui.core.BasePresenter
import com.util.Utility
import com.mow.cash.android.R
import libraries.image.helper.models.MediaResult
import libraries.image.helper.utils.StorageUtility
import libraries.retrofit.RestError

class PayorInfoFragmentPresenter(
    val payorInfoView: PayorInfoFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<PayorInfoFragmentContract.View>(), PayorInfoFragmentContract.Presenter {

    override fun getDocument(file: MediaResult, apiKey: String) {

        payorInfoView.setProgressBar(true)
        dataRepository.getDocument("https://api.idanalyzer.com", apiKey, true, "PD", false, file,
            object : CallbackSubscriber<GetDocumentRs>() {
                override fun onSuccess(response: GetDocumentRs?) {

                    payorInfoView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if (response.document != null) {


                                if(response.document?.documentSide.equals("FRONT")) {

                                    payorInfoView.showDocument(response.document!!)

                                } else {
                                    payorInfoView.getDocumentFail(payorInfoView.contextt.getString(R.string.validation_request_you_to_scan_front_of_id))
                                }



                                /*if (response.document!!.score >= 0.1) {

                                    payorInfoView.showDocument(response.document!!)

                                } else {
                                    payorInfoView.getDocumentFail("Document is not authentic.")
                                }*/
                            }
                        } else {
                            payorInfoView.getDocumentFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    payorInfoView.setProgressBar(false)
                    payorInfoView.processErrorWithToast(restError)
                }
            })

    }

    /*override fun saveCustomer(
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
        password: String,
        confirmPassword: String,
        notes: String,
        mediaResult: MediaResult?
    ) {

        if (firstName.trim().isNotEmpty()) {

            if (lastName.trim().isNotEmpty()) {

                if (country.trim().isNotEmpty()) {

                    if (billAddress1.trim().isNotEmpty() || billAddress2.trim().isNotEmpty()) {


                        if(licenseNumber.trim().isNotEmpty()) {

                            if(expiryDate.trim().isNotEmpty()) {

                                if(dateOfBirth.trim().isNotEmpty()) {

                                    if (townOrCity.trim().isNotEmpty()) {



                                        var isStateValid = true
                                        if (country.equals("USA", true)) {

                                            if (stateId == 0) {
                                                isStateValid = false
                                                payorInfoView.showToastMessage(
                                                    payorInfoView.contextt.getString(
                                                        R.string.validation_please_select_state
                                                    )
                                                )
                                            }

                                        } else {
                                            if (otherBillStateName.isNullOrEmpty()) {
                                                isStateValid = false
                                                payorInfoView.showToastMessage(
                                                    payorInfoView.contextt.getString(
                                                        R.string.validation_other_state_is_required
                                                    )
                                                )
                                            }
                                        }

                                        if (isStateValid) {

                                            if (zip.trim().isNotEmpty()) {

                                                if (cellNumber.length == 13) {

                                                    if (mediaResult != null) {

                                                        if (email.trim().isNotEmpty()) {

                                                            if (password.trim().isNotEmpty()) {

                                                                if (confirmPassword.trim().isNotEmpty()) {

                                                                    if (Patterns.EMAIL_ADDRESS.matcher(email)
                                                                            .matches()
                                                                    ) {

                                                                        if (password.equals(confirmPassword)) {

                                                                            val payor = User()

                                                                            payor.firstName = firstName
                                                                            payor.middleName = middleName
                                                                            payor.lastName = lastName
                                                                            payor.country = country
                                                                            payor.billAddress1 =
                                                                                billAddress1
                                                                            payor.billAddress2 =
                                                                                billAddress2
                                                                            payor.billCity = townOrCity
                                                                            payor.billStateID = stateId
                                                                            if (otherBillStateName != null)
                                                                                payor.otherBillStateName =
                                                                                    otherBillStateName
                                                                            payor.billZip = zip
                                                                            payor.cellNumber =
                                                                                cellNumber
                                                                            payor.homeNumber =
                                                                                homeNumber
                                                                            payor.licenseNo = licenseNumber
                                                                            payor.expiryDate =
                                                                                expiryDate
                                                                            payor.dateOfBirth = dateOfBirth
                                                                            payor.email = email
                                                                            payor.password = password
                                                                            payor.notes = notes

                                                                            payor.fileContent =
                                                                                Utility.convertImageIntoBase64(
                                                                                    mediaResult.path
                                                                                )
                                                                            payor.mimeType =
                                                                                mediaResult.mimeType

                                                                            payor.fileName =
                                                                                StorageUtility.getInstance()
                                                                                    .getFileNameWithExtenstion(
                                                                                        mediaResult.path
                                                                                    )

                                                                            saveCustomerRq.payor = payor

                                                                            payorInfoView.setProgressBar(
                                                                                true
                                                                            )
                                                                            dataRepository.saveCustomer(
                                                                                saveCustomerRq,
                                                                                object :
                                                                                    CallbackSubscriber<SaveCustomerRs>() {
                                                                                    override fun onSuccess(
                                                                                        response: SaveCustomerRs?
                                                                                    ) {

                                                                                        payorInfoView.setProgressBar(
                                                                                            false
                                                                                        )

                                                                                        response?.result?.isStatus?.let { isSuccess ->

                                                                                            if (isSuccess) {
                                                                                                payorInfoView.saveCustomerSuccessfully()
                                                                                            } else {
                                                                                                payorInfoView.saveCustomerFail(
                                                                                                    response.result.message
                                                                                                )
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                    override fun onFailure(
                                                                                        restError: RestError?
                                                                                    ) {
                                                                                        payorInfoView.setProgressBar(
                                                                                            false
                                                                                        )
                                                                                        payorInfoView.processErrorWithToast(
                                                                                            restError
                                                                                        )
                                                                                    }
                                                                                })

                                                                        } else {
                                                                            payorInfoView.showToastMessage(
                                                                                payorInfoView.contextt.getString(
                                                                                    R.string.validation_password_and_confirm_password_does_not_match
                                                                                )
                                                                            )
                                                                        }

                                                                    } else {
                                                                        payorInfoView.showToastMessage(
                                                                            payorInfoView.contextt.getString(
                                                                                R.string.validation_invalid_email_address
                                                                            )
                                                                        )
                                                                    }

                                                                } else {
                                                                    payorInfoView.showToastMessage(
                                                                        payorInfoView.contextt.getString(R.string.validation_confirm_password_is_required)
                                                                    )
                                                                }

                                                            } else {
                                                                payorInfoView.showToastMessage(
                                                                    payorInfoView.contextt.getString(
                                                                        R.string.validation_password_is_required
                                                                    )
                                                                )
                                                            }

                                                        } else {
                                                            payorInfoView.showToastMessage(
                                                                payorInfoView.contextt.getString(
                                                                    R.string.validation_email_is_required
                                                                )
                                                            )
                                                        }

                                                    } else {
                                                        payorInfoView.showToastMessage(
                                                            payorInfoView.contextt.getString(
                                                                R.string.validation_scan_valid_id_required
                                                            )
                                                        )
                                                    }

                                                } else {
                                                    payorInfoView.showToastMessage(
                                                        payorInfoView.contextt.getString(
                                                            R.string.validation_cell_number_is_required
                                                        )
                                                    )
                                                }

                                            } else {
                                                payorInfoView.showToastMessage(
                                                    payorInfoView.contextt.getString(
                                                        R.string.validation_zip_is_required
                                                    )
                                                )
                                            }
                                        }

                                    } else {
                                        payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_town_city_is_required))
                                    }

                                } else {
                                    payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_date_of_birth_is_required))
                                }

                            } else {
                                payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_expiration_date_is_required))
                            }

                        } else {
                            payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_license_number_is_required))
                        }

                    } else {
                        payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_address_is_required))
                    }

                } else {
                    payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_country_is_required))
                }

            } else {
                payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_last_name_is_required))
            }

        } else {
            payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_first_name_is_required))
        }

    }*/

    override fun savePayor(
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
        password: String,
        confirmPassword: String,
        notes: String,
        mediaResult: MediaResult?
    ): User? {



        if (firstName.trim().isNotEmpty()) {

            if (lastName.trim().isNotEmpty()) {

                if (country.trim().isNotEmpty()) {

                    if (billAddress1.trim().isNotEmpty() || billAddress2.trim().isNotEmpty()) {


                        if(licenseNumber.trim().isNotEmpty()) {

                            if(expiryDate.trim().isNotEmpty()) {

                                if(dateOfBirth.trim().isNotEmpty()) {

                                    if (townOrCity.trim().isNotEmpty()) {



                                        var isStateValid = true
                                        if (country.equals("USA", true)) {

                                            if (stateId == 0) {
                                                isStateValid = false
                                                payorInfoView.showToastMessage(
                                                    payorInfoView.contextt.getString(
                                                        R.string.validation_please_select_state
                                                    )
                                                )
                                            }

                                        } else {
                                            if (otherBillStateName.isNullOrEmpty()) {
                                                isStateValid = false
                                                payorInfoView.showToastMessage(
                                                    payorInfoView.contextt.getString(
                                                        R.string.validation_other_state_is_required
                                                    )
                                                )
                                            }
                                        }

                                        if (isStateValid) {

                                            if (zip.trim().isNotEmpty()) {

                                                if (cellNumber.length == 13) {

                                                    if (mediaResult != null) {

                                                        if (email.trim().isNotEmpty()) {

                                                            if (password.trim().isNotEmpty()) {

                                                                if (confirmPassword.trim().isNotEmpty()) {

                                                                    if (Patterns.EMAIL_ADDRESS.matcher(email)
                                                                            .matches()
                                                                    ) {

                                                                        if (password.equals(confirmPassword)) {

                                                                            val payor = User()

                                                                            payor.firstName = firstName
                                                                            payor.middleName = middleName
                                                                            payor.lastName = lastName
                                                                            payor.country = country
                                                                            payor.billAddress1 =
                                                                                billAddress1
                                                                            payor.billAddress2 =
                                                                                billAddress2
                                                                            payor.billCity = townOrCity
                                                                            payor.billStateID = stateId
                                                                            if (otherBillStateName != null)
                                                                                payor.otherBillStateName =
                                                                                    otherBillStateName
                                                                            payor.billZip = zip
                                                                            payor.cellNumber =
                                                                                cellNumber
                                                                            payor.homeNumber =
                                                                                homeNumber
                                                                            payor.licenseNo = licenseNumber
                                                                            payor.expiryDate =
                                                                                expiryDate
                                                                            payor.dateOfBirth = dateOfBirth
                                                                            payor.email = email
                                                                            payor.password = password
                                                                            payor.notes = notes

                                                                            payor.fileContent =
                                                                                Utility.convertImageIntoBase64(
                                                                                    mediaResult.path
                                                                                )
                                                                            payor.mimeType =
                                                                                mediaResult.mimeType

                                                                            payor.fileName =
                                                                                StorageUtility.getInstance()
                                                                                    .getFileNameWithExtenstion(
                                                                                        mediaResult.path
                                                                                    )

                                                                            return payor


                                                                        } else {
                                                                            payorInfoView.showToastMessage(
                                                                                payorInfoView.contextt.getString(
                                                                                    R.string.validation_password_and_confirm_password_does_not_match
                                                                                )
                                                                            )
                                                                        }

                                                                    } else {
                                                                        payorInfoView.showToastMessage(
                                                                            payorInfoView.contextt.getString(
                                                                                R.string.validation_invalid_email_address
                                                                            )
                                                                        )
                                                                    }

                                                                } else {
                                                                    payorInfoView.showToastMessage(
                                                                        payorInfoView.contextt.getString(R.string.validation_confirm_password_is_required)
                                                                    )
                                                                }

                                                            } else {
                                                                payorInfoView.showToastMessage(
                                                                    payorInfoView.contextt.getString(
                                                                        R.string.validation_password_is_required
                                                                    )
                                                                )
                                                            }

                                                        } else {
                                                            payorInfoView.showToastMessage(
                                                                payorInfoView.contextt.getString(
                                                                    R.string.validation_email_is_required
                                                                )
                                                            )
                                                        }

                                                    } else {
                                                        payorInfoView.showToastMessage(
                                                            payorInfoView.contextt.getString(
                                                                R.string.validation_scan_valid_id_required
                                                            )
                                                        )
                                                    }

                                                } else {
                                                    payorInfoView.showToastMessage(
                                                        payorInfoView.contextt.getString(
                                                            R.string.validation_cell_number_is_required
                                                        )
                                                    )
                                                }

                                            } else {
                                                payorInfoView.showToastMessage(
                                                    payorInfoView.contextt.getString(
                                                        R.string.validation_zip_is_required
                                                    )
                                                )
                                            }
                                        }

                                    } else {
                                        payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_town_city_is_required))
                                    }

                                } else {
                                    payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_date_of_birth_is_required))
                                }

                            } else {
                                payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_expiration_date_is_required))
                            }

                        } else {
                            payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_license_number_is_required))
                        }

                    } else {
                        payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_address_is_required))
                    }

                } else {
                    payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_country_is_required))
                }

            } else {
                payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_last_name_is_required))
            }

        } else {
            payorInfoView.showToastMessage(payorInfoView.contextt.getString(R.string.validation_first_name_is_required))
        }

        return null
    }

    /*override fun getSaveCustomerRq(): SaveCustomerRq {
        return saveCustomerRq
    }*/

    override fun getAllState() {

        payorInfoView.setProgressBar(true)
        dataRepository.getAllState(
            object : CallbackSubscriber<GetAllStateRs>() {
                override fun onSuccess(response: GetAllStateRs?) {

                    payorInfoView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            payorInfoView.showStateList(response.stateList)

                        } else {
                            payorInfoView.getStateListFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    payorInfoView.setProgressBar(false)
                    payorInfoView.processErrorWithToast(restError)
                }
            })
    }
}