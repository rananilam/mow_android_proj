package com.ui.login.retrive

import android.util.Patterns
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.user.RetrieveCellPhoneNumberRs
import com.ui.core.BasePresenter
import com.mow.cash.android.R
import libraries.retrofit.RestError

class RetrieveYourCellFragmentPresenter(
    val retrieveYourCellView: RetrieveYourCellFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<RetrieveYourCellFragmentContract.View>(), RetrieveYourCellFragmentContract.Presenter {


    override fun retrieveCellPhoneNumber(email: String) {

        if(email.isNotEmpty()) {

            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {


                retrieveYourCellView.setProgressBar(true)
                dataRepository.retrieveCellPhoneNumber(
                    email,
                    object : CallbackSubscriber<RetrieveCellPhoneNumberRs>() {
                        override fun onSuccess(response: RetrieveCellPhoneNumberRs?) {

                            retrieveYourCellView.setProgressBar(false)

                            response?.result?.isStatus?.let { isSuccess ->

                                if (isSuccess) {
                                    retrieveYourCellView.retrieveCellPhoneSuccessfully(response.result.message)
                                } else {
                                    retrieveYourCellView.retrieveCellPhoneFail(response.result.message)
                                }
                            }
                        }

                        override fun onFailure(restError: RestError?) {
                            retrieveYourCellView.setProgressBar(false)
                            retrieveYourCellView.processErrorWithToast(restError)
                        }
                    })



            } else {
                retrieveYourCellView.showToastMessage(retrieveYourCellView.contextt.getString(R.string.validation_please_enter_valid_email))
            }

        } else {
            retrieveYourCellView.showToastMessage(retrieveYourCellView.contextt.getString(R.string.validation_please_enter_email))
        }

    }
}


