package com.data.remote.response.order

import com.data.model.order.BonusDayBillingProfileInfo
import com.data.remote.response.BaseRs
import com.data.model.order.OrderH

class GetBonusDayBillingProfileInfoListRs : BaseRs() {

    var bonusDayBillingProfileInfoList = listOf<BonusDayBillingProfileInfo>()
}