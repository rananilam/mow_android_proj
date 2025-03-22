package com.ui.home.billingAddress

import com.data.model.order.State
import com.data.model.user.User
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface BillingAddressFragmentContract {

    interface View : IBaseView {

        fun showCustomer(customer: User)
        fun getCustomerFail(errorMessage: String?)


        fun showStateList(stateList: List<State>)
        fun getStateListFail(errorMessage: String?)

        fun saveCustomerSuccessfully()
        fun saveCustomerFail(errorMessage: String?)



    }

    interface Presenter : IBasePresenter<View> {
        fun getCustomer()
        fun getAllState()


        fun saveCustomer(
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
            email: String,
            payor: User?
        )


    }
}