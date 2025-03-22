package com.data.remote.response.device

import com.data.remote.response.BaseRs
import com.data.model.device.Device
import com.data.model.device.DeviceProperty

class GetDevicePropertyOptionRs : BaseRs() {

    var devicePropertyList = listOf<DeviceProperty>()
}