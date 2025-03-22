package com.data.remote.response.notification

import com.data.model.notification.Notification
import com.data.remote.response.BaseRs

class NotificationListRs : BaseRs() {
    var notificationList = listOf<Notification>()
}