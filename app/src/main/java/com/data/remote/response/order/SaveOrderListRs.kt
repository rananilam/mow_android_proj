package com.data.remote.response.order

import com.data.remote.response.BaseRs
import java.io.Serializable

class SaveOrderListRs : BaseRs(), Serializable {

    /*


   {
    "ErrorMessage": null,
    "Message": "Record inserted successfully.",
    "StatusCode": "OK",
    "CheckCreditCardBalance": true,
    "EquipmentOrderID": 0,
    "EquipmentOrderIDWithFormat": "W 120698,",
    "PaymentMethod": null
}
     */

    var checkCreditCardBalance = false
    var equipmentOrderID = 0
    var equipmentOrderIDWithFormat = ""
    var paymentMethod = ""
    var total = 0.0F
    var date = ""
}