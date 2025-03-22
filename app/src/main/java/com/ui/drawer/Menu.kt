package com.ui.drawer


data class Menu(

    var name: String = "",
    var resId: Int = 0,
    var subMenu: MutableList<SubMenu> = mutableListOf()
)