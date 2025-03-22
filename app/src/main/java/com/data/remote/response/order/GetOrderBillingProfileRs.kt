package com.data.remote.response.order

import com.data.remote.response.BaseRs

class GetOrderBillingProfileRs : BaseRs() {

    /*


    {
  "BillingProfileID": 751,
  "Description": "*1 DAY",
  "Message": null,
  "RegularPrice": "40.00",
  "Result": true,
  "RewardPoint": 1.00,
  "SecondNdTripPrice": "0.0",
  "TotalPrice": "118.00"
}
     */

    var billingProfileID = 0
    var description = ""


    var regularPrice = ""
    var rewardPoint = 0.0F
    var secondNdTripPrice = ""
  //  var totalPrice = ""


}