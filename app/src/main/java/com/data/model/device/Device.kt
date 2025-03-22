package com.data.model.device

import java.io.Serializable

class Device : Serializable{

    /*
     "CompPriceDescription": "-",
      "DeviceTypeID": 54,
      "InventoryID": 0,
      "ItemImagePath": "ItemImage\/20171110102515.png",
      "ItemName": "BARIATRIC POWER WHEELCHAIR",
      "RegularPriceDescription": "-"
     */

    var compPriceDescription = ""
    var deviceTypeId = 0
    var inventoryId = 0
    var itemImagePath = ""
    var itemName = ""
    var regularPriceDescription = ""
}