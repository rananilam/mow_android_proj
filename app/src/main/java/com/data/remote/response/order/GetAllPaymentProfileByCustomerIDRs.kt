package com.data.remote.response.order

import com.data.remote.response.BaseRs
import com.data.model.order.Card

class GetAllPaymentProfileByCustomerIDRs : BaseRs() {
    var cardList = listOf<Card>()
}