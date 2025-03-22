package com.data.objectforupdate
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.user.GetAppConfigurationRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError


class updateAppPresenter(val myUpdateAppView: UpdateAppContract.View,private val dataRepository: DataRepository) : BasePresenter<UpdateAppContract.View>(), UpdateAppContract.Presenter {

    override fun updateAppConfig() {


        myUpdateAppView.setProgressBar(true)
        dataRepository.getAppConfiguration(object : CallbackSubscriber<GetAppConfigurationRs>() {
            override fun onSuccess(response: GetAppConfigurationRs?) {

                myUpdateAppView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {
                        myUpdateAppView.getAppConfigSuccessfully(response.config)

                    } else {
                        myUpdateAppView.getAppConfigFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                myUpdateAppView.setProgressBar(false)
                myUpdateAppView.processErrorWithToast(restError)
            }
        })

    }
}