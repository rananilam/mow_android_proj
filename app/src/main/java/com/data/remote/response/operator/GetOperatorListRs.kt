package com.data.remote.response.operator

import com.data.remote.response.BaseRs
import com.data.model.user.User

class GetOperatorListRs : BaseRs() {
    var operatorList = listOf<User>()
}