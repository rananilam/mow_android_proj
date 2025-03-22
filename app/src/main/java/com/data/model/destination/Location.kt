package com.data.model.destination

class Location {

    /*

      "DeliveryFee": 0,
      "ID": 3,
      "IsDistinctLocationCategory": true,
      "IsGenerateAcceptBonusDay": false,
      "IsTwentyFourBySevenAvailability": false,
      "LocationCategoryName": "Atlantic City Casinos",
      "LocationName": "Bally's AC",
      "TaxRate": 0

       "CustomerPickupLocationID": 3,
        "CustomerPickuplocationName": "Bus Lobby Information Desk",
        "IsAvailableOrNot": false,
        "IsShippingAddress": false

    "DeliveryFee": 10.00,
    "ID": 3,
    "IsDistinctLocationCategory": false,
    "IsGenerateAcceptBonusDay": true,
    "IsTwentyFourBySevenAvailability": false,
    "LocationCategoryName": null,
    "LocationName": "Bally's AC",
    "TaxRate": 6.63
     */

    var id = 0
    var locationCategoryName = ""
    var locationName = ""

    var customerPickupLocationID = 0
    var customerPickuplocationName = ""

    var isAvailableOrNot = false
    var isShippingAddress = false

    var deliveryFee = 0.0F
    var isDistinctLocationCategory = false

    var isGenerateAcceptBonusDay = false
    var isTwentyFourBySevenAvailability = false
    var taxRate = 0.0F
}