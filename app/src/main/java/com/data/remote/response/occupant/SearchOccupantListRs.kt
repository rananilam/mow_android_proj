package com.data.remote.response.occupant

import com.data.model.user.User
import com.data.remote.response.BaseRs

class SearchOccupantListRs : BaseRs() {

    var occupantList = listOf<User>()
}