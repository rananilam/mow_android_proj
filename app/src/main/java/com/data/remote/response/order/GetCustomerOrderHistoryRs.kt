package com.data.remote.response.order

import com.data.remote.response.BaseRs
import com.data.model.order.OrderH

class GetCustomerOrderHistoryRs : BaseRs() {

    var userRewardsPoint = 0.0F
    var orderHList = listOf<OrderH>()
}