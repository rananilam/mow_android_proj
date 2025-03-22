package com.data.objectforupdate

import com.data.model.user.Config
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface UpdateAppContract {

    interface View: IBaseView {
        fun getAppConfigSuccessfully(config: Config?)
        fun getAppConfigFail(errorMessage: String?)

    }

    interface Presenter : IBasePresenter<View> {
        fun updateAppConfig()
    }
}