package com.ui.login.verification

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.user.GenerateOTPRs
import com.data.remote.response.user.VerifyOTPRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class PhoneNumberVerificationFragmentPresenter(
    val phoneNumberVerificationView: PhoneNumberVerificationFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<PhoneNumberVerificationFragmentContract.View>(), PhoneNumberVerificationFragmentContract.Presenter {


    override fun generateOTP(phoneNumber: String) {

        phoneNumberVerificationView.setProgressBar(true)


        dataRepository.generateOTP(phoneNumber,  object : CallbackSubscriber<GenerateOTPRs>() {
            override fun onSuccess(response: GenerateOTPRs?) {

                phoneNumberVerificationView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {

                        if(response.otp.isNotEmpty()) {
                            phoneNumberVerificationView.generateOTPSuccessfully(response.otp)
                        } else {
                            phoneNumberVerificationView.generateOTPFail(response.result.message)
                        }
                    } else {
                        phoneNumberVerificationView.generateOTPFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                phoneNumberVerificationView.setProgressBar(false)
                phoneNumberVerificationView.processErrorWithToast(restError)
            }
        })

    }

    override fun verifyOTP(phoneNumber: String, otp: String) {
        phoneNumberVerificationView.setProgressBar(true)


        dataRepository.verifyOTP(phoneNumber,otp,  object : CallbackSubscriber<VerifyOTPRs>() {
            override fun onSuccess(response: VerifyOTPRs?) {

                phoneNumberVerificationView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {

                        response.user?.token?.isNotEmpty()?.let {
                            if(it) {
                                phoneNumberVerificationView.verifyOTPSuccessfully()
                            } else {
                                phoneNumberVerificationView.verifyOTPFail(response.result.message)
                            }
                        }

                    } else {
                        phoneNumberVerificationView.generateOTPFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                phoneNumberVerificationView.setProgressBar(false)
                phoneNumberVerificationView.processErrorWithToast(restError)
            }
        })
    }
}


