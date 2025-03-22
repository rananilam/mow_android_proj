package com.data.remote.response.user

import com.data.model.user.User
import com.data.remote.response.BaseRs

class ValidateLicenseRs : BaseRs() {

    var isActiveOrderAval = false


    var isLicenseAvailableForPayor = false
    var isValidLicenseForPayor = false


    var notValidLicenseForOperator = mutableListOf<Int>()
    var lstLicenseNotAvailableForOperator = mutableListOf<Int>()

}