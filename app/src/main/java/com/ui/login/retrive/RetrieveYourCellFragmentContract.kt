package com.ui.login.retrive

import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface RetrieveYourCellFragmentContract {

    interface View : IBaseView {
        fun retrieveCellPhoneSuccessfully(message: String)
        fun retrieveCellPhoneFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {

        fun retrieveCellPhoneNumber(email: String)
    }
}