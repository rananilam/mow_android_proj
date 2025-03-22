package com.ui.login.register

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.model.user.User
import com.data.remote.request.user.SaveCustomerRq
import com.data.remote.response.user.SaveCustomerRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class CreateAccountActivityPresenter(
    val createAccountActivityView: CreateAccountActivityContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<CreateAccountActivityContract.View>(), CreateAccountActivityContract.Presenter {


    override fun saveCustomer(
        payor: User,
        operatorList: ArrayList<User>
    ) {

        val saveCustomerRq = SaveCustomerRq()
        saveCustomerRq.payor = payor
        saveCustomerRq.operatorList = operatorList

        createAccountActivityView.setProgressBar(
            true
        )
        dataRepository.saveCustomer(
            saveCustomerRq,
            object :
                CallbackSubscriber<SaveCustomerRs>() {
                override fun onSuccess(
                    response: SaveCustomerRs?
                ) {

                    createAccountActivityView.setProgressBar(
                        false
                    )

                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {
                            createAccountActivityView.saveCustomerSuccessfully()
                        } else {
                            createAccountActivityView.saveCustomerFail(
                                response.result.message
                            )
                        }
                    }
                }

                override fun onFailure(
                    restError: RestError?
                ) {
                    createAccountActivityView.setProgressBar(
                        false
                    )
                    createAccountActivityView.processErrorWithToast(
                        restError
                    )
                }
            })

    }


}