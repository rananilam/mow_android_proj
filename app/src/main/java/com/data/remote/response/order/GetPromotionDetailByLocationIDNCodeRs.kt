package com.data.remote.response.order

import com.data.remote.response.BaseRs

class GetPromotionDetailByLocationIDNCodeRs : BaseRs() {

    /*
{
  "ErrorMessage": null,
  "Message": null,
  "StatusCode": "OK",
  "PromotionFigure": 10,
  "PromotionID": 18,
  "PromotionType": true
}
     */

    var promotionFigure = 0.0F
    var promotionID = 0
    var promotionType = false
}