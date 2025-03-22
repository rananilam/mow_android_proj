package com.ui.login.login

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.user.GenerateOTPRs
import com.ui.core.BasePresenter
import com.mow.cash.android.R
import libraries.retrofit.RestError

class LoginFragmentPresenter(
    val loginView: LoginFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<LoginFragmentContract.View>(), LoginFragmentContract.Presenter {


    override fun generateOTP(isAgree: Boolean, phoneNumber: String) {


        if (phoneNumber.isNotEmpty()) {

            if (phoneNumber.length == 13) {


                if (isAgree) {
                    loginView.setProgressBar(true)
                    dataRepository.generateOTP(
                        phoneNumber,
                        object : CallbackSubscriber<GenerateOTPRs>() {
                            override fun onSuccess(response: GenerateOTPRs?) {

                                loginView.setProgressBar(false)

                                response?.result?.isStatus?.let { isSuccess ->

                                    if (isSuccess) {

                                        if (response.otp.isNotEmpty()) {
                                            loginView.generateOTPSuccessfully(response.otp)
                                        } else {
                                            loginView.generateOTPFail(response.result.message)
                                        }
                                    } else {
                                        loginView.generateOTPFail(response.result.message)
                                    }
                                }
                            }

                            override fun onFailure(restError: RestError?) {
                                loginView.setProgressBar(false)
                                loginView.processErrorWithToast(restError)
                            }
                        })
                } else {
                    loginView.showToastMessage(loginView.contextt.getString(R.string.validation_please_accept_terms_of_use))
                }

            } else {
                loginView.showToastMessage(loginView.contextt.getString(R.string.validation_please_enter_valid_phone_number))
            }
        } else {
            loginView.showToastMessage(loginView.contextt.getString(R.string.validation_please_enter_phone_number))
        }

    }
}


