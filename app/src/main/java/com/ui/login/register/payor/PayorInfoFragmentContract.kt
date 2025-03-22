package com.ui.login.register.payor

import com.data.model.idanalyzer.Document
import com.data.model.order.State
import com.data.model.user.User
import com.data.remote.request.user.SaveCustomerRq
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView
import libraries.image.helper.models.MediaResult


interface PayorInfoFragmentContract {

    interface View : IBaseView {
       //
       //

        fun showDocument(document: Document)
        fun getDocumentFail(errorMessage: String?)

        fun showStateList(stateList: List<State>)
        fun getStateListFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {

        /**/

        fun savePayor(
            firstName: String,
            middleName: String,
            lastName: String,
            country: String,
            billAddress1: String,
            billAddress2: String,
            townOrCity: String,
            stateId: Int,
            otherBillStateName: String?,
            zip: String,
            cellNumber: String,
            homeNumber: String,
            licenseNumber: String,
            expiryDate: String,
            dateOfBirth: String,
            email: String,
            password: String,
            confirmPassword: String,
            notes: String,
            mediaResult: MediaResult?
        ): User?

      //  fun getSaveCustomerRq(): SaveCustomerRq

        fun getDocument(file: MediaResult, apiKey: String)

        fun getAllState()
    }
}