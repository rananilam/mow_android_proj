package com.ui.home.searchOccupant

import com.mow.cash.android.R
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.user.User
import com.data.remote.request.occupant.SearchOccupantListRq
import com.data.remote.request.user.AddOccupantRq
import com.data.remote.response.occupant.GetOccupantByIDRs
import com.data.remote.response.occupant.SearchOccupantListRs
import com.data.remote.response.user.AddOccupantRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError


class SearchOccupantFragmentPresenter(
    val searchOccupantView: SearchOccupantFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<SearchOccupantFragmentContract.View>(), SearchOccupantFragmentContract.Presenter {


    override fun searchOccupantList(searchText: String, operatorID: Int) {

        val searchOccupantListRq = SearchOccupantListRq(searchText,dataRepository.user.id,operatorID)
        searchOccupantView.setProgressBar(true)
        dataRepository.getExistingOccupant(
            searchOccupantListRq,
            object : CallbackSubscriber<SearchOccupantListRs>() {
                override fun onSuccess(response: SearchOccupantListRs?) {

                    searchOccupantView.setProgressBar(false)


                    response?.occupantList?.let {
                        searchOccupantView.showOccupantList(it)
                    }
                }

                override fun onFailure(restError: RestError?) {
                    searchOccupantView.setProgressBar(false)
                    searchOccupantView.processErrorWithToast(restError)
                }
            })


    }

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

                            searchOccupantView.setProgressBar(true)
                            dataRepository.saveOccupant(
                                addOccupantRq,
                                object : CallbackSubscriber<AddOccupantRs>() {
                                    override fun onSuccess(response: AddOccupantRs?) {

                                        searchOccupantView.setProgressBar(false)

                                        response?.result?.isStatus?.let { isSuccess ->

                                            if (isSuccess) {
                                                searchOccupantView.addOccupantSuccessfully(response.occupant)
                                            } else {
                                                searchOccupantView.addOccupantFail(response.result.message)
                                            }
                                        }
                                    }

                                    override fun onFailure(restError: RestError?) {
                                        searchOccupantView.setProgressBar(false)
                                        searchOccupantView.processErrorWithToast(restError)
                                    }
                                })

                        } else {
                            searchOccupantView.showToastMessage(searchOccupantView.contextt.getString(R.string.validation_please_select_height_in))
                        }

                    } else {
                        searchOccupantView.showToastMessage(searchOccupantView.contextt.getString(R.string.validation_please_select_height_ft))
                    }

                } else {
                    searchOccupantView.showToastMessage(searchOccupantView.contextt.getString(R.string.validation_weight_is_required))
                }

            } else {
                searchOccupantView.showToastMessage(searchOccupantView.contextt.getString(R.string.validation_last_name_is_required))
            }

        } else {
            searchOccupantView.showToastMessage(searchOccupantView.contextt.getString(R.string.validation_first_name_is_required))
        }
    }

    override fun getOccupantByID(occupantID: Int) {
        searchOccupantView.setProgressBar(true)
        dataRepository.getOccupantByID(occupantID,
            object : CallbackSubscriber<GetOccupantByIDRs>() {
                override fun onSuccess(response: GetOccupantByIDRs?) {

                    searchOccupantView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            response.occupant?.let { occupant ->
                                searchOccupantView.showOccupant(occupant)
                            }
                        } else {
                            searchOccupantView.getOccupantFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    searchOccupantView.setProgressBar(false)
                    searchOccupantView.processErrorWithToast(restError)
                }
            })
    }


}


