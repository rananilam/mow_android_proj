package com.data.model.user

import java.io.Serializable

class Human: Serializable {

    var name = ""
    var childList = listOf<Human>()
}