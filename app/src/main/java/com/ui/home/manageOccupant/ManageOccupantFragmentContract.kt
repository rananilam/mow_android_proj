package com.ui.home.manageOccupant

import com.data.model.user.User
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface ManageOccupantFragmentContract {

    interface View : IBaseView {
        /*fun addOccupantSuccessfully(occupant: User?)
        fun addOccupantFail(errorMessage: String?)*/

        fun showOccupantList(occupantList: List<User>)
        fun getOccupantListFail(errorMessage: String?)

        fun showEntity(entity: String)

        fun removeOccupantSuccessfully()
        fun removeOccupantFail(errorMessage: String?)
    }

    interface Presenter : IBasePresenter<View> {

        /*fun addOccupant(
            firstName: String,
            middleName: String,
            lastName: String,
            weight: String,
            heightFt : String,
            heightIn: String,
            operatorID: Int
        )*/

        fun getOccupantList(operatorID: Int)

        fun getEntityDefination(isOperatorEntity: Boolean)

        fun removeOccupantById(occupantID: Int)

    }
}