package com.data.remote.response.occupant

import com.data.model.user.User
import com.data.remote.response.BaseRs

class AddDefaultOccupantRs : BaseRs() {

    var selectedOccupantID = 0
    var occupantList = listOf<User>()
}