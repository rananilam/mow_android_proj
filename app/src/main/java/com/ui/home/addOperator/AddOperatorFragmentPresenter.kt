package com.ui.home.addOperator

import android.util.Patterns
import com.mow.cash.android.R
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.user.User
import com.data.remote.request.user.AddAndEditOperatorRq
import com.data.remote.response.entityDefination.GetEntityDefinationRs
import com.data.remote.response.idanalyzer.GetDocumentRs
import com.data.remote.response.user.AddAndEditOperatorRs
import com.data.remote.response.user.AddOccupantRs
import com.ui.core.BasePresenter
import com.util.Utility
import libraries.image.helper.models.MediaResult
import libraries.image.helper.utils.StorageUtility
import libraries.retrofit.RestError


class AddOperatorFragmentPresenter(
    val addOperatorView: AddOperatorFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<AddOperatorFragmentContract.View>(), AddOperatorFragmentContract.Presenter {

    private var getEntityDefinationRs: GetEntityDefinationRs? = null

    override fun addEditNewOperator(
        operatorId: Int,
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
    ) {

        if (firstName.trim().isNotEmpty()) {

            if (lastName.trim().isNotEmpty()) {

                if (weight.trim().isNotEmpty()) {

                    if (heightFt.trim().isNotEmpty()) {

                        if (heightIn.trim().isNotEmpty()) {

                            if (cellNumber.length == 13) {

                                if (email.trim().isNotEmpty()) {

                                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                                        val operator = User()
                                        operator.operatorID = operatorId
                                        operator.customerId = dataRepository.user.id
                                        operator.firstName = firstName
                                        operator.middleName = middleName
                                        operator.lastName = lastName
                                        operator.weight = weight.toInt()
                                        operator.heightFeet = heightFt.toInt()
                                        operator.heightInch = heightIn.toInt()
                                        operator.cellNumber = cellNumber
                                        operator.homeNumber = homeNumber
                                        operator.licenseNo = licenseNumber
                                        operator.expiryDate = expiryDate
                                        operator.dateOfBirth = dateOfBirth
                                        operator.email = email
                                        operator.isDefault = isDefault

                                        if (mediaResult != null) {

                                            operator.fileContent =
                                                Utility.convertImageIntoBase64(mediaResult.path)
                                            operator.mimeType = mediaResult.mimeType
                                            operator.fileName = StorageUtility.getInstance().getFileNameWithExtenstion(mediaResult.path)
                                        }



                                        val addAndEditOperatorRq = AddAndEditOperatorRq(operator)

                                        addOperatorView.setProgressBar(true)
                                        dataRepository.addEditNewOperator(
                                            addAndEditOperatorRq,
                                            object : CallbackSubscriber<AddAndEditOperatorRs>() {
                                                override fun onSuccess(response: AddAndEditOperatorRs?) {

                                                    addOperatorView.setProgressBar(false)

                                                    response?.result?.isStatus?.let { isSuccess ->

                                                        if (isSuccess) {
                                                            addOperatorView.addEditNewOperatorSuccessfully(response.operator)
                                                        } else {
                                                            addOperatorView.addEditNewOperatorFail(response.result.message)
                                                        }
                                                    }
                                                }

                                                override fun onFailure(restError: RestError?) {
                                                    addOperatorView.setProgressBar(false)
                                                    addOperatorView.processErrorWithToast(restError)
                                                }
                                            })


                                    } else {
                                        addOperatorView.showToastMessage(
                                            addOperatorView.contextt.getString(
                                                R.string.validation_invalid_email_address
                                            )
                                        )
                                    }

                                } else {
                                    addOperatorView.showToastMessage(
                                        addOperatorView.contextt.getString(
                                            R.string.validation_email_is_required
                                        )
                                    )
                                }
                            } else {
                                addOperatorView.showToastMessage(
                                    addOperatorView.contextt.getString(
                                        R.string.validation_cell_number_is_required
                                    )
                                )
                            }

                        } else {
                            addOperatorView.showToastMessage(addOperatorView.contextt.getString(R.string.validation_please_select_height_in))
                        }

                    } else {
                        addOperatorView.showToastMessage(addOperatorView.contextt.getString(R.string.validation_please_select_height_ft))
                    }

                } else {
                    addOperatorView.showToastMessage(addOperatorView.contextt.getString(R.string.validation_weight_is_required))
                }

            } else {
                addOperatorView.showToastMessage(addOperatorView.contextt.getString(R.string.validation_operator_last_name_is_required))
            }

        } else {
            addOperatorView.showToastMessage(addOperatorView.contextt.getString(R.string.validation_operator_first_name_is_required))
        }

    }

    override fun getDocument(file: MediaResult, apiKey: String) {

        addOperatorView.setProgressBar(true)
        dataRepository.getDocument("https://api.idanalyzer.com", apiKey, true, "PD", false, file,
            object : CallbackSubscriber<GetDocumentRs>() {
                override fun onSuccess(response: GetDocumentRs?) {

                    addOperatorView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            if(response.document != null) {

                                if(response.document?.documentSide.equals("FRONT")) {

                                    addOperatorView.showDocument(response.document!!)

                                } else {
                                    addOperatorView.getDocumentFail(addOperatorView.contextt.getString(R.string.validation_request_you_to_scan_front_of_id))
                                }


                                /*if(response.document!!.score >= 0.1) {

                                    addOperatorView.showDocument(response.document!!)

                                } else {
                                    addOperatorView.getDocumentFail("Document is not authentic.")
                                }*/
                            }
                        } else {
                            addOperatorView.getDocumentFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addOperatorView.setProgressBar(false)
                    addOperatorView.processErrorWithToast(restError)
                }
            })
    }

    override fun getEntityDefination(isOperatorEntity: Boolean) {

        if (getEntityDefinationRs == null) {
            addOperatorView.setProgressBar(true)
            dataRepository.getEntityDefination(
                object : CallbackSubscriber<GetEntityDefinationRs>() {
                    override fun onSuccess(response: GetEntityDefinationRs?) {

                        addOperatorView.setProgressBar(false)

                        response?.result?.isStatus?.let { isSuccess ->

                            if (isSuccess) {
                                getEntityDefinationRs = response

                                if (isOperatorEntity) {
                                    addOperatorView.showEntity(response.defineOperator)
                                } else {
                                    addOperatorView.showEntity(response.defineOccupant)
                                }
                            }
                        }
                    }

                    override fun onFailure(restError: RestError?) {
                        addOperatorView.setProgressBar(false)
                        addOperatorView.processErrorWithToast(restError)
                    }
                })
        } else {
            getEntityDefinationRs?.let {
                if (isOperatorEntity) {
                    addOperatorView.showEntity(it.defineOperator)
                } else {
                    addOperatorView.showEntity(it.defineOccupant)
                }
            }
        }

    }
}


