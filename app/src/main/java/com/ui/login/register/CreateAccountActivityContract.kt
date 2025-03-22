package com.ui.login.register

import com.data.model.idanalyzer.Document
import com.data.model.user.User
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView
import libraries.image.helper.models.MediaResult


interface CreateAccountActivityContract {

    interface View : IBaseView {
        fun saveCustomerSuccessfully()
        fun saveCustomerFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {

        fun saveCustomer(
            payor: User, operatorList: ArrayList<User>
        )

    }
}