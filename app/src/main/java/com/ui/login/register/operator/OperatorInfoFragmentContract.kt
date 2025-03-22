package com.ui.login.register.operator

import com.data.model.idanalyzer.Document
import com.data.model.user.User
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView
import libraries.image.helper.models.MediaResult


interface OperatorInfoFragmentContract {

    interface View : IBaseView {
        fun showDocument(document: Document)
        fun getDocumentFail(errorMessage: String?)

        fun showEntity(entity: String)
    }

    interface Presenter : IBasePresenter<View> {

        fun saveOperator(
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
        ): User?

        fun getDocument(file: MediaResult, apiKey: String)

        fun getEntityDefination(isOperatorEntity: Boolean)

    }
}