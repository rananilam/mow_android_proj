package com.ui.login.login

import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface LoginFragmentContract {

    interface View : IBaseView {
        fun generateOTPSuccessfully(otp: String)
        fun generateOTPFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {

        fun generateOTP(isAgree: Boolean, phoneNumber: String)
    }
}