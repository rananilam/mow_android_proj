package com.ui.home.manageOccupant

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.entityDefination.GetEntityDefinationRs
import com.data.remote.response.occupant.GetOccupantListRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError


class ManageOccupantFragmentPresenter(
    val manageOccupantView: ManageOccupantFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<ManageOccupantFragmentContract.View>(), ManageOccupantFragmentContract.Presenter {


    private var getEntityDefinationRs: GetEntityDefinationRs? = null
    override fun getOccupantList(operatorID: Int) {
        manageOccupantView.setProgressBar(true)
        dataRepository.getOccupantListByOperatorID(operatorID,
            object : CallbackSubscriber<GetOccupantListRs>() {
                override fun onSuccess(response: GetOccupantListRs) {

                    manageOccupantView.setProgressBar(false)
                    manageOccupantView.showOccupantList(response.occupantList)
                }

                override fun onFailure(restError: RestError?) {
                    manageOccupantView.setProgressBar(false)
                    manageOccupantView.processErrorWithToast(restError)
                }
            })
    }

    override fun getEntityDefination(isOperatorEntity: Boolean) {

        if (getEntityDefinationRs == null) {
            manageOccupantView.setProgressBar(true)
            dataRepository.getEntityDefination(
                object : CallbackSubscriber<GetEntityDefinationRs>() {
                    override fun onSuccess(response: GetEntityDefinationRs?) {

                        manageOccupantView.setProgressBar(false)

                        response?.result?.isStatus?.let { isSuccess ->

                            if (isSuccess) {
                                getEntityDefinationRs = response

                                if (isOperatorEntity) {
                                    manageOccupantView.showEntity(response.defineOperator)
                                } else {
                                    manageOccupantView.showEntity(response.defineOccupant)
                                }
                            }
                        }
                    }

                    override fun onFailure(restError: RestError?) {
                        manageOccupantView.setProgressBar(false)
                        manageOccupantView.processErrorWithToast(restError)
                    }
                })
        } else {
            getEntityDefinationRs?.let {
                if (isOperatorEntity) {
                    manageOccupantView.showEntity(it.defineOperator)
                } else {
                    manageOccupantView.showEntity(it.defineOccupant)
                }
            }
        }

    }

    override fun removeOccupantById(occupantID: Int) {
        manageOccupantView.setProgressBar(true)
        dataRepository.removeOccupantById(occupantID,
            object : CallbackSubscriber<String>() {
                override fun onSuccess(response: String) {

                    manageOccupantView.setProgressBar(false)
                    manageOccupantView.removeOccupantSuccessfully()
                }

                override fun onFailure(restError: RestError?) {
                    manageOccupantView.setProgressBar(false)
                    manageOccupantView.processErrorWithToast(restError)
                   // manageOccupantView.removeOccupantSuccessfully()
                }
            })
    }

    /* override fun addOccupant(
         firstName: String,
         middleName: String,
         lastName: String,
         weight: String,
         heightFt: String,
         heightIn: String,
         operatorID: Int
     ) {
         if (firstName.trim().isNotEmpty()) {

             if (lastName.trim().isNotEmpty()) {

                 if (weight.trim().isNotEmpty()) {

                     if (heightFt.trim().isNotEmpty()) {

                         if (heightIn.trim().isNotEmpty()) {

                             val occupant = User()
                             occupant.firstName = firstName
                             occupant.middleName = middleName
                             occupant.lastName = lastName
                             occupant.weight = weight.toInt()
                             occupant.heightFeet = heightFt.toInt()
                             occupant.heightInch = heightIn.toInt()
                             occupant.operatorID = operatorID

                             val addOccupantRq = AddOccupantRq(occupant)

                             manageOccupantView.setProgressBar(true)
                             dataRepository.saveOccupant(
                                 addOccupantRq,
                                 object : CallbackSubscriber<AddOccupantRs>() {
                                     override fun onSuccess(response: AddOccupantRs?) {

                                         manageOccupantView.setProgressBar(false)

                                         response?.result?.isStatus?.let { isSuccess ->

                                             if (isSuccess) {
                                                 manageOccupantView.addOccupantSuccessfully(response.occupant)
                                             } else {
                                                 manageOccupantView.addOccupantFail(response.result.message)
                                             }
                                         }
                                     }

                                     override fun onFailure(restError: RestError?) {
                                         manageOccupantView.setProgressBar(false)
                                         manageOccupantView.processErrorWithToast(restError)
                                     }
                                 })



                         } else {
                             manageOccupantView.showToastMessage(manageOccupantView.contextt.getString(R.string.please_select_height_in))
                         }

                     } else {
                         manageOccupantView.showToastMessage(manageOccupantView.contextt.getString(R.string.please_select_height_ft))
                     }

                 } else {
                     manageOccupantView.showToastMessage(manageOccupantView.contextt.getString(R.string.weight_is_required))
                 }

             } else {
                 manageOccupantView.showToastMessage(manageOccupantView.contextt.getString(R.string.operator_last_name_is_required))
             }

         } else {
             manageOccupantView.showToastMessage(manageOccupantView.contextt.getString(R.string.operator_first_name_is_required))
         }
     }*/
}


