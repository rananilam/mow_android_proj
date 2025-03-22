package com.data.remote.response.order

import com.data.remote.response.BaseRs

class GetBlackoutDatesByLocationAndDeviceRs : BaseRs() {

    var blackoutDatesList = listOf<BlackoutDatesRs>()
}

class BlackoutDatesRs {

    var endDate = ""
    var startDate = ""

}