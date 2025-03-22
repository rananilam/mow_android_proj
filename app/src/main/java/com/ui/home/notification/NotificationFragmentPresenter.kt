package com.ui.home.notification

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.notification.MarkNotificationAsReadRs
import com.data.remote.response.notification.NotificationListRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class NotificationFragmentPresenter(
    val notificationView: NotificationFragmentContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<NotificationFragmentContract.View>(), NotificationFragmentContract.Presenter {




    override fun getNotificationList() {
        notificationView.setProgressBar(true)
        dataRepository.getNotificationList(
            object : CallbackSubscriber<NotificationListRs>() {
                override fun onSuccess(response: NotificationListRs?) {

                    notificationView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {

                            notificationView.showNotificationList(response.notificationList)
                        } else {
                            notificationView.getNotificationFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    notificationView.setProgressBar(false)
                    notificationView.processErrorWithToast(restError)
                }
            })
    }

    override fun markNotificationAsRead() {
        notificationView.setProgressBar(true)
        dataRepository.markNotificationAsRead(
            object : CallbackSubscriber<MarkNotificationAsReadRs>() {
                override fun onSuccess(response: MarkNotificationAsReadRs?) {

                    notificationView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {

                            notificationView.markNotificationAsReadSuccessfully()
                        } else {
                            notificationView.markNotificationAsReadFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    notificationView.setProgressBar(false)
                    notificationView.processErrorWithToast(restError)
                }
            })
    }


}


