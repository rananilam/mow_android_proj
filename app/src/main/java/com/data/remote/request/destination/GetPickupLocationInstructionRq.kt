package com.data.remote.request.destination

import java.io.Serializable

data class GetPickupLocationInstructionRq(val DeviceTypeID: Int,
                                          val LocationID: Int,
                                          val PickupLocationID: Int) : Serializable {



}