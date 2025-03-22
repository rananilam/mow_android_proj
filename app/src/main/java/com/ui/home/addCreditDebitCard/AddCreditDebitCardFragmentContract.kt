package com.ui.home.addCreditDebitCard

import com.data.model.order.State
import com.data.model.user.User
import com.data.remote.response.order.CreditCardRs
import com.google.mlkit.vision.common.InputImage
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface AddCreditDebitCardFragmentContract {

    interface View : IBaseView {
        fun authorizePaymentSuccessfully()
        fun authorizePaymentFail(errorMessage: String?)

        fun showStateList(stateList: List<State>)
        fun getStateListFail(errorMessage: String?)

        fun showCustomer(customer: User)
        fun getCustomerFail(errorMessage: String?)

        fun showDocument(document: CreditCardRs)
        fun getDocumentFail(errorMessage: String?)

    }

    interface Presenter : IBasePresenter<View> {

        fun authorizePaymentRequest(
            firstName: String,
            middleName: String,
            lastName: String,
            email: String,
            phone: String,
            city: String,
            zip: String,
            state: String,
            stateID: Int,
            country: String,
            cardTypeID: Int,
            creditCardNumber: String,
            csv: String,
            address1: String,
            address2: String,
            expYear: Int,
            expMonth: Int
        )

        fun getAllState()

        fun getCustomer()
        fun getCreditCardInfo(request: InputImage)
        fun getCardType(cardNumber: String): String

    }
}