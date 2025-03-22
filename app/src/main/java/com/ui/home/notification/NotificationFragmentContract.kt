package com.ui.home.notification

import com.data.model.notification.Notification
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView

interface NotificationFragmentContract {

    interface View : IBaseView {
        fun showNotificationList(notificationList: List<Notification>)
        fun getNotificationFail(errorMessage: String?)

        fun markNotificationAsReadSuccessfully()
        fun markNotificationAsReadFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {
        fun getNotificationList()

        fun markNotificationAsRead()
    }
}