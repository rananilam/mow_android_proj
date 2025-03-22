package com.ui.login.verification

import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface PhoneNumberVerificationFragmentContract {

    interface View : IBaseView {
        fun generateOTPSuccessfully(otp: String)
        fun generateOTPFail(errorMessage: String?)

        fun verifyOTPSuccessfully()
        fun verifyOTPFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {

        fun generateOTP(phoneNumber: String)
        fun verifyOTP(phoneNumber: String, otp: String)
    }
}