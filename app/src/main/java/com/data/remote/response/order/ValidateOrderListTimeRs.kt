package com.data.remote.response.order

import com.data.model.order.ValidateOrderTime
import com.data.remote.response.BaseRs

class ValidateOrderListTimeRs : BaseRs() {

    var validateOrderTimeList = listOf<ValidateOrderTime>()
}