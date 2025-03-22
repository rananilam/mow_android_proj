package com.ui.home.addOccupant

import com.mow.cash.android.R
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.user.User
import com.data.remote.request.user.AddOccupantRq
import com.data.remote.response.entityDefination.GetEntityDefinationRs
import com.data.remote.response.operator.GetOperatorByIDRs
import com.data.remote.response.user.AddOccupantRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError


class AddOccupantFragmentPresenter(
    val addOccupantView: AddOccupantFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<AddOccupantFragmentContract.View>(), AddOccupantFragmentContract.Presenter {

    private var getEntityDefinationRs: GetEntityDefinationRs? = null
    override fun addOccupant(
        id: Int,
        firstName: String,
        middleName: String,
        lastName: String,
        weight: String,
        heightFt: String,
        heightIn: String,
        operatorID: Int,
        isDefault: Boolean
    ) {
        if (firstName.trim().isNotEmpty()) {

            if (lastName.trim().isNotEmpty()) {

                if (weight.trim().isNotEmpty()) {

                    if (heightFt.trim().isNotEmpty()) {

                        if (heightIn.trim().isNotEmpty()) {

                            val occupant = User()
                            occupant.id = id
                            occupant.firstName = firstName
                            occupant.middleName = middleName
                            occupant.lastName = lastName
                            occupant.weight = weight.toInt()
                            occupant.heightFeet = heightFt.toInt()
                            occupant.heightInch = heightIn.toInt()
                            occupant.operatorID = operatorID
                            occupant.isDefault = isDefault

                            val addOccupantRq = AddOccupantRq(occupant)

                            addOccupantView.setProgressBar(true)
                            dataRepository.saveOccupant(
                                addOccupantRq,
                                object : CallbackSubscriber<AddOccupantRs>() {
                                    override fun onSuccess(response: AddOccupantRs?) {

                                        addOccupantView.setProgressBar(false)

                                        response?.result?.isStatus?.let { isSuccess ->

                                            if (isSuccess) {
                                                addOccupantView.addOccupantSuccessfully(response.occupant)
                                            } else {
                                                addOccupantView.addOccupantFail(response.result.message)
                                            }
                                        }
                                    }

                                    override fun onFailure(restError: RestError?) {
                                        addOccupantView.setProgressBar(false)
                                        addOccupantView.processErrorWithToast(restError)
                                    }
                                })

                        } else {
                            addOccupantView.showToastMessage(addOccupantView.contextt.getString(R.string.validation_please_select_height_in))
                        }

                    } else {
                        addOccupantView.showToastMessage(addOccupantView.contextt.getString(R.string.validation_please_select_height_ft))
                    }

                } else {
                    addOccupantView.showToastMessage(addOccupantView.contextt.getString(R.string.validation_weight_is_required))
                }

            } else {
                addOccupantView.showToastMessage(addOccupantView.contextt.getString(R.string.validation_last_name_is_required))
            }

        } else {
            addOccupantView.showToastMessage(addOccupantView.contextt.getString(R.string.validation_first_name_is_required))
        }
    }


    override fun getOperatorByID(operatorID: Int) {
        addOccupantView.setProgressBar(true)
        dataRepository.getOperatorByID(operatorID,
            object : CallbackSubscriber<GetOperatorByIDRs>() {
                override fun onSuccess(response: GetOperatorByIDRs?) {

                    addOccupantView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            response.operator?.let { operator ->
                                addOccupantView.showOperator(operator)
                            }
                        } else {
                            addOccupantView.getOperatorFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    addOccupantView.setProgressBar(false)
                    addOccupantView.processErrorWithToast(restError)
                }
            })
    }

    override fun getEntityDefination(isOperatorEntity: Boolean) {

        if (getEntityDefinationRs == null) {
            addOccupantView.setProgressBar(true)
            dataRepository.getEntityDefination(
                object : CallbackSubscriber<GetEntityDefinationRs>() {
                    override fun onSuccess(response: GetEntityDefinationRs?) {

                        addOccupantView.setProgressBar(false)

                        response?.result?.isStatus?.let { isSuccess ->

                            if (isSuccess) {
                                getEntityDefinationRs = response

                                if (isOperatorEntity) {
                                    addOccupantView.showEntity(response.defineOperator)
                                } else {
                                    addOccupantView.showEntity(response.defineOccupant)
                                }
                            }
                        }
                    }

                    override fun onFailure(restError: RestError?) {
                        addOccupantView.setProgressBar(false)
                        addOccupantView.processErrorWithToast(restError)
                    }
                })
        } else {
            getEntityDefinationRs?.let {
                if (isOperatorEntity) {
                    addOccupantView.showEntity(it.defineOperator)
                } else {
                    addOccupantView.showEntity(it.defineOccupant)
                }
            }
        }

    }

}


