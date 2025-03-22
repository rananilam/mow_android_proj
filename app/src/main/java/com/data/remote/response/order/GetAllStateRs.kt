package com.data.remote.response.order

import com.data.model.order.State
import com.data.remote.response.BaseRs

class GetAllStateRs : BaseRs() {

    var stateList = listOf<State>()
}