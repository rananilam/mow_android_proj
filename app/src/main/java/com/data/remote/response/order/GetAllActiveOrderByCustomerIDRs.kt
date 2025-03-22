package com.data.remote.response.order

import com.data.remote.response.BaseRs
import com.data.model.order.OrderH

class GetAllActiveOrderByCustomerIDRs : BaseRs() {

    var activeOrderList = listOf<OrderH>()
}