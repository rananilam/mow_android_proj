package com.ui.home.addCustomerECheck

import com.data.model.order.State
import com.data.model.user.User
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface AddCustomerECheckFragmentContract {

    interface View : IBaseView {
        fun showCustomer(customer: User)
        fun showStateList(stateList: List<State>)
        fun showAllBankAccType(stateList: MutableMap<Int, String>)
        fun showAccountMatchOrNotToast(isAccountNumberMatched: Boolean)
        fun showRoutingMatchOrNotToast(isRoutingNumberMatched: Boolean)
        fun getStateListFail(errorMessage: String?)
        fun getCustomerFail(errorMessage: String?)
        fun authorizeECheckSuccessfully()
        fun authorizeECheckFail(errorMessage: String?)

    }

    interface Presenter : IBasePresenter<View> {



        fun getAllState()
        fun getCustomer()
        fun matchAccountNumber(confirmAccNo: String,AccNo: String)
        fun matchRoutingNumber(confirmRoutingNo: String,routingNo: String)

        fun getAllBankAccType()

       fun authorizeECheckRequest(
           firstName: String,
           middleName: String,
           lastName: String,
           address1: String,
           address2: String,
           country: String,
           state: String,
           stateID: Int,
           city: String,
           zip: String,
           phone: String,
           email: String,
           bankName: String,
           bankAccountType: String,
           accountNumber: String,
           routingNumber: String,
           confirmAccountNumber: String,
           isAccountNumberMatched:Boolean,
           confirmRoutingNumber: String,
           isRoutingNumberMatched:Boolean,
        )


    }
}