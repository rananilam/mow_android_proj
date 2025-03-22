package com.ui.home.searchOccupant

import com.data.model.user.User
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface SearchOccupantFragmentContract {

    interface View : IBaseView {
        fun showOccupantList(occupantList: List<User>)

        fun addOccupantSuccessfully(occupant: User?)
        fun addOccupantFail(errorMessage: String?)

        fun showOccupant(occupant: User)
        fun getOccupantFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {
        fun searchOccupantList(searchText: String, operatorID: Int)
        fun addOccupant(
            id: Int,
            firstName: String,
            middleName: String,
            lastName: String,
            weight: String,
            heightFt : String,
            heightIn: String,
            operatorID: Int,
            isDefault: Boolean
        )

        fun getOccupantByID(occupantID: Int)
    }
}