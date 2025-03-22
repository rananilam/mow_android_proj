package com.data.model.order

import java.io.Serializable

class Card : Serializable{

    /*
       "ErrorMessage": null,
            "Message": null,
            "StatusCode": null,
            "Address1": "PrahladNagar",
            "Address2": "Venus Atlantis",
            "CardExpDate": "/Date(2235234600000+0530)/",
            "CardNumber": "XXXX2222",
            "ExtDate": "Oct\/2038",
            "CardType": "American Express",
            "FirstName": "Rutvij",
            "ID": 32300,
            "LastName": "Bhatt",
            "MiddleName": "H",
            "Phone": null,
            "ddlItem": "XXXX2222 (American Express - Oct/2040)"
     */

    var address1 = ""
    var address2 = ""
    var cardExpDate = ""
    var cardNumber = ""
    var cardType = CardType.NONE
    var firstName = ""
    var id = 0
    var lastName = ""
    var middleName = ""
    var phone = ""
    var ddlItem = ""
    var extDate = ""
}