package com.ui.home.addOccupant

import com.data.model.user.User
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface AddOccupantFragmentContract {

    interface View : IBaseView {
        fun addOccupantSuccessfully(occupant: User?)
        fun addOccupantFail(errorMessage: String?)

        fun showOperator(operator: User)
        fun getOperatorFail(errorMessage: String?)

        fun showEntity(entity: String)
    }

    interface Presenter : IBasePresenter<View> {

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

        fun getOperatorByID(operatorID: Int)

        fun getEntityDefination(isOperatorEntity: Boolean)
    }
}