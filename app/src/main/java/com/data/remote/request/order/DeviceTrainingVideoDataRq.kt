package com.data.remote.request.order

import java.io.Serializable

data class DeviceTrainingVideoDataRq(val DeviceTypeID: Int,
                                     val LocationID: Int,
                                     val CustomerID: Int,
                                     val AttestationID: Int,
                                     val OperatorID: Int,
                                     val IsDefaultOperator: Boolean,
                                     val RemovedTermsAndConditionCheckbox: Boolean) : Serializable {



}