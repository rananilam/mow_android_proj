package com.ui.home.reportBug

import com.data.model.user.User
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface ReportBugFragmentContract {

    interface View : IBaseView {
        fun showCustomer(customer: User)
        fun getCustomerFail(errorMessage: String?)
        fun sentBugReportSuccessfully(successMessage: String?)
        fun sentBugReportFail(errorMessage: String?)

    }

    interface Presenter : IBasePresenter<View> {
        fun getCustomer()
        fun sentBugReport(
            Description: String,
            DeviceModel: String,
            Email: String,
            AppVersion: String,
            documentData: Array<Array<String>>,
        )

    }
}