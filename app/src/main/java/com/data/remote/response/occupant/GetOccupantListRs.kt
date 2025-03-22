package com.data.remote.response.occupant

import com.data.remote.response.BaseRs
import com.data.model.user.User

class GetOccupantListRs : BaseRs() {

    var occupantList = listOf<User>()
}