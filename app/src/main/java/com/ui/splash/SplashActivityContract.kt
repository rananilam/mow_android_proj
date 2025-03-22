package com.ui.splash

import com.data.model.user.Config
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface SplashActivityContract {

    interface View : IBaseView {
        fun getDestinationListSuccessfully()
        fun getDestinationListFail(errorMessage: String?)

        fun getAppConfigSuccessfully(config: Config?)
        fun getAppConfigFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {
        fun getDestinationList()
        fun getAppConfig()
    }
}