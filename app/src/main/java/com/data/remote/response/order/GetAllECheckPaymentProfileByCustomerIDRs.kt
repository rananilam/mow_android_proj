package com.data.remote.response.order

import com.data.model.order.ECheckCard
import com.data.remote.response.BaseRs

class GetAllECheckPaymentProfileByCustomerIDRs : BaseRs() {
    var cardList = listOf<ECheckCard>()
}