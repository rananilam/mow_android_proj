package com.ui.home

import com.data.model.order.OrderH
import com.data.remote.response.notification.GetBadgeCountRs
import com.data.remote.response.user.ValidateLicenseRs
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface HomeActivityContract {

    interface View : IBaseView {
        fun showRiderRewardsPoint(riderRewardPoint: Float)
        fun getRiderRewardsPointFail(errorMessage: String?)

        fun showValidateLicense(validateLicenseRs: ValidateLicenseRs)
        fun getValidateLicenseFail(errorMessage: String?)


    }

    interface Presenter : IBasePresenter<View> {
        fun getRiderRewardsPoint()
        fun validateLicense()
        fun addUpdateFCMToken()
    }
}