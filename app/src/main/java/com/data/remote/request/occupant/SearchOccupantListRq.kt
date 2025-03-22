package com.data.remote.request.occupant

import java.io.Serializable

data class SearchOccupantListRq(val SearchText: String,
                                val CustomerID: Int,
                                val OperatorID: Int) : Serializable {
}