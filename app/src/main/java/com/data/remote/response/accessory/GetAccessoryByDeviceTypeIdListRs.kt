package com.data.remote.response.accessory

import com.data.remote.response.BaseRs
import com.data.model.accessory.Accessory

class GetAccessoryByDeviceTypeIdListRs : BaseRs() {

    var accessoryList = listOf<Accessory>()
}