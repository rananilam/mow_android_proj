package com.data.remote.response.order

import com.data.remote.response.BaseRs

class CreditCardRs : BaseRs() {

    var card_number= ""
    var expiry_date= ""
    var cardholder_name = ""
    var issuer = ""        // Issuer name (e.g., Chase, CitiBank)
    var card_type = ""     // Card type (e.g., Visa, MasterCard)
    var country = ""       // Country of issuance


}
