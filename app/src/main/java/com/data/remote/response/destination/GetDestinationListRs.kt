package com.data.remote.response.destination

import com.data.remote.response.BaseRs
import com.data.model.destination.Location

class GetDestinationListRs : BaseRs() {

    var destinationList = listOf<Location>()
}