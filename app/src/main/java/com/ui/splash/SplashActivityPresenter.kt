package com.ui.splash

import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.destination.GetDestinationListRs
import com.data.remote.response.user.GetAppConfigurationRs
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class SplashActivityPresenter(
    val splashActivityView: SplashActivityContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<SplashActivityContract.View>(), SplashActivityContract.Presenter {




    override fun getDestinationList() {

        splashActivityView.setProgressBar(true)
        dataRepository.getDestinationList(object : CallbackSubscriber<GetDestinationListRs>() {
            override fun onSuccess(response: GetDestinationListRs?) {

                splashActivityView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {

                        if(response.destinationList.isNotEmpty()) {
                            splashActivityView.getDestinationListSuccessfully()
                        } else {
                            splashActivityView.getDestinationListFail(response.result.message)
                        }
                    } else {
                        splashActivityView.getDestinationListFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                splashActivityView.setProgressBar(false)
                splashActivityView.processErrorWithToast(restError)
            }
        })


    }

    override fun getAppConfig() {


        splashActivityView.setProgressBar(true)
        dataRepository.getAppConfiguration(object : CallbackSubscriber<GetAppConfigurationRs>() {
            override fun onSuccess(response: GetAppConfigurationRs?) {

                splashActivityView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {
                        splashActivityView.getAppConfigSuccessfully(response.config)
                    } else {
                        splashActivityView.getAppConfigFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                splashActivityView.setProgressBar(false)
                splashActivityView.processErrorWithToast(restError)
            }
        })

    }
}


