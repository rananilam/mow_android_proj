package com.ui.home.profile

import com.data.model.idanalyzer.Document
import com.data.model.order.State
import com.data.model.user.User
import com.data.remote.response.notification.GetBadgeCountRs
import com.data.remote.response.user.ValidateLicenseRs
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView
import libraries.image.helper.models.MediaResult


interface MyProfileFragmentContract {

    interface View : IBaseView {

        fun showCustomer(customer: User)
        fun getCustomerFail(errorMessage: String?)

        fun showOperatorList(operatorList: List<User>)
        fun getOperatorListFail(errorMessage: String?)

        fun showStateList(stateList: List<State>)
        fun getStateListFail(errorMessage: String?)

        fun saveCustomerSuccessfully()
        fun saveCustomerFail(errorMessage: String?)

        fun showDocument(document: Document)
        fun getDocumentFail(errorMessage: String?)

        fun showValidateLicense(validateLicenseRs: ValidateLicenseRs)
        fun getValidateLicenseFail(errorMessage: String?)

        fun showBadgeNotificationCount(getBadgeCount: GetBadgeCountRs)
        fun getBadgeCountFail(errorMessage: String?)

    }

    interface Presenter : IBasePresenter<View> {
        fun getCustomer()
        fun getOperatorList()
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
            cellNumber: String,
            homeNumber: String,
            licenseNumber: String,
            expiryDate: String,
            dateOfBirth: String,
            email: String,
            mediaResult: MediaResult?
        )

        fun getDocument(file: MediaResult, apiKey: String)

        fun validateLicense()

        fun getBadgeCount()
    }
}