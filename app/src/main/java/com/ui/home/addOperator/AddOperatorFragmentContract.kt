package com.ui.home.addOperator

import com.data.model.idanalyzer.Document
import com.data.model.user.User
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView
import libraries.image.helper.models.MediaResult


interface AddOperatorFragmentContract {

    interface View : IBaseView {

        fun addEditNewOperatorSuccessfully(operator: User?)
        fun addEditNewOperatorFail(errorMessage: String?)

        fun showDocument(document: Document)
        fun getDocumentFail(errorMessage: String?)

        fun showEntity(entity: String)
    }

    interface Presenter : IBasePresenter<View> {

        fun addEditNewOperator(
            operatorId: Int,
            firstName: String,
            middleName: String,
            lastName: String,
            weight: String,
            heightFt : String,
            heightIn: String,
            cellNumber: String,
            homeNumber: String,
            licenseNumber: String,
            expiryDate: String,
            dateOfBirth: String,
            email: String,
            isDefault: Boolean,
            mediaResult: MediaResult?
        )

        fun getDocument(file: MediaResult, apiKey: String)

        fun getEntityDefination(isOperatorEntity: Boolean)

    }
}