package com.data.remote.response.order

import com.data.remote.response.BaseRs
import com.data.model.order.DeviceTrainingVideoData
import java.io.Serializable

class GetDeviceTrainingVideoAndDataRs : BaseRs() {

    var deviceTrainingVideoDataList = listOf<DeviceTrainingVideoData>()
}