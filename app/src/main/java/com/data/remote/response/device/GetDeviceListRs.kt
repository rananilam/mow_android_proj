package com.data.remote.response.device

import com.data.remote.response.BaseRs
import com.data.model.device.Device

class GetDeviceListRs : BaseRs() {

    var deviceList = listOf<Device>()
}