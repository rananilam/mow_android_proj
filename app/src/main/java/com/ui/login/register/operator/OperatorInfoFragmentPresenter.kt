package com.ui.login.register.operator

import android.util.Patterns
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.user.User
import com.data.remote.response.entityDefination.GetEntityDefinationRs
import com.data.remote.response.idanalyzer.GetDocumentRs
import com.data.remote.response.user.SaveCustomerRs
import com.ui.core.BasePresenter
import com.util.Utility
import com.mow.cash.android.R
import libraries.image.helper.models.MediaResult
import libraries.image.helper.utils.StorageUtility
import libraries.retrofit.RestError


class OperatorInfoFragmentPresenter(
    val operatorInfoView: OperatorInfoFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<OperatorInfoFragmentContract.View>(), OperatorInfoFragmentContract.Presenter {

    private var getEntityDefinationRs: GetEntityDefinationRs? = null

    override fun saveOperator(
        firstName: String,
        middleName: String,
        lastName: String,
        weight: String,
        heightFt: String,
        heightIn: String,
        cellNumber: String,
        homeNumber: String,
        licenseNumber: String,
        expiryDate: String,
        dateOfBirth: String,
        email: String,
        isDefault: Boolean,
        mediaResult: MediaResult?
    ): User? {

        if (firstName.trim().isNotEmpty()) {

            if (lastName.trim().isNotEmpty()) {

                if (weight.trim().isNotEmpty()) {

                    if (heightFt.trim().isNotEmpty()) {

                        if (heightIn.trim().isNotEmpty()) {

                            if (cellNumber.length == 13) {

                                if (licenseNumber.trim().isNotEmpty()) {

                                    if (expiryDate.trim().isNotEmpty()) {

                                        if (dateOfBirth.trim().isNotEmpty()) {
                                            if (email.trim().isNotEmpty()) {

                                                if (Patterns.EMAIL_ADDRESS.matcher(email)
                                                        .matches()
                                                ) {


                                                    if (mediaResult != null) {

                                                        val user = User()
                                                        user.firstName = firstName
                                                        user.middleName = middleName
                                                        user.lastName = lastName
                                                        user.weight = weight.toInt()
                                                        user.heightFeet = heightFt.toInt()
                                                        user.heightInch = heightIn.toInt()
                                                        user.cellNumber = cellNumber
                                                        user.homeNumber = homeNumber
                                                        user.licenseNo = licenseNumber
                                                        user.expiryDate = expiryDate
                                                        user.dateOfBirth = dateOfBirth
                                                        user.email = email
                                                        user.isDefault = isDefault

                                                        user.fileContent =
                                                            Utility.convertImageIntoBase64(
                                                                mediaResult.path
                                                            )
                                                        user.mimeType = mediaResult.mimeType
                                                        user.fileName =
                                                            StorageUtility.getInstance()
                                                                .getFileNameWithExtenstion(
                                                                    mediaResult.path
                                                                )

                                                        user.mediaResult = mediaResult
                                                        return user

                                                    } else {
                                                        operatorInfoView.showToastMessage(
                                                            operatorInfoView.contextt.getString(
                                                                R.string.validation_scan_valid_id_required
                                                            )
                                                        )
                                                    }


                                                } else {
                                                    operatorInfoView.showToastMessage(
                                                        operatorInfoView.contextt.getString(
                                                            R.string.validation_invalid_email_address
                                                        )
                                                    )
                                                }

                                            } else {
                                                operatorInfoView.showToastMessage(
                                                    operatorInfoView.contextt.getString(
                                                        R.string.validation_email_is_required
                                                    )
                                                )
                                            }


                                        } else {
                                            operatorInfoView.showToastMessage(
                                                operatorInfoView.contextt.getString(
                                                    R.string.validation_date_of_birth_is_required
                                                )
                                            )
                                        }

                                    } else {
                                        operatorInfoView.showToastMessage(
                                            operatorInfoView.contextt.getString(
                                                R.string.validation_expiration_date_is_required
                                            )
                                        )
                                    }

                                } else {
                                    operatorInfoView.showToastMessage(
                                        operatorInfoView.contextt.getString(
                                            R.string.validation_license_number_is_required
                                        )
                                    )
                                }

                            } else {
                                operatorInfoView.showToastMessage(
                                    operatorInfoView.contextt.getString(
                                        R.string.validation_cell_number_is_required
                                    )
                                )
                            }

                        } else {
                            operatorInfoView.showToastMessage(operatorInfoView.contextt.getString(R.string.validation_please_select_height_in))
                        }

                    } else {
                        operatorInfoView.showToastMessage(operatorInfoView.contextt.getString(R.string.validation_please_select_height_ft))
                    }

                } else {
                    operatorInfoView.showToastMessage(operatorInfoView.contextt.getString(R.string.validation_weight_is_required))
                }

            } else {
                operatorInfoView.showToastMessage(operatorInfoView.contextt.getString(R.string.validation_operator_last_name_is_required))
            }

        } else {
            operatorInfoView.showToastMessage(operatorInfoView.contextt.getString(R.string.validation_operator_first_name_is_required))
        }

        return null
    }

    override fun getDocument(file: MediaResult, apiKey: String) {

        operatorInfoView.setProgressBar(true)
        dataRepository.getDocument("https://api.idanalyzer.com", apiKey, true, "PD", false, file,
            object : CallbackSubscriber<GetDocumentRs>() {
                override fun onSuccess(response: GetDocumentRs?) {

                    operatorInfoView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if (response.document != null) {


                                if(response.document?.documentSide.equals("FRONT")) {

                                    operatorInfoView.showDocument(response.document!!)

                                } else {
                                    operatorInfoView.getDocumentFail("Request you to scan front of ID")
                                }



                                /*if (response.document!!.score >= 0.1) {

                                    operatorInfoView.showDocument(response.document!!)

                                } else {
                                    operatorInfoView.getDocumentFail("Document is not authentic.")
                                }*/
                            }
                        } else {
                            operatorInfoView.getDocumentFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    operatorInfoView.setProgressBar(false)
                    operatorInfoView.processErrorWithToast(restError)
                }
            })

    }

    override fun getEntityDefination(isOperatorEntity: Boolean) {

        if (getEntityDefinationRs == null) {
            operatorInfoView.setProgressBar(true)
            dataRepository.getEntityDefination(
                object : CallbackSubscriber<GetEntityDefinationRs>() {
                    override fun onSuccess(response: GetEntityDefinationRs?) {

                        operatorInfoView.setProgressBar(false)

                        response?.result?.isStatus?.let { isSuccess ->

                            if (isSuccess) {
                                getEntityDefinationRs = response

                                if (isOperatorEntity) {
                                    operatorInfoView.showEntity(response.defineOperator)
                                } else {
                                    operatorInfoView.showEntity(response.defineOccupant)
                                }
                            }
                        }
                    }

                    override fun onFailure(restError: RestError?) {
                        operatorInfoView.setProgressBar(false)
                        operatorInfoView.processErrorWithToast(restError)
                    }
                })
        } else {
            getEntityDefinationRs?.let {
                if (isOperatorEntity) {
                    operatorInfoView.showEntity(it.defineOperator)
                } else {
                    operatorInfoView.showEntity(it.defineOccupant)
                }
            }
        }

    }
}


