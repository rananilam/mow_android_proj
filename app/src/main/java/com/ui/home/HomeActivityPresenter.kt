package com.ui.home

import android.util.Log
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.request.notification.AddUpdateFCMTokenRq
import com.data.remote.response.notification.AddUpdateFCMTokenRs
import com.data.remote.response.order.GetCustomerOrderHistoryRs
import com.data.remote.response.user.ValidateLicenseRs
import com.ui.core.BasePresenter
import iCode.utility.SystemUtility
import libraries.retrofit.RestError

class HomeActivityPresenter(
    val homeActivityView: HomeActivityContract.View,
    private val dataRepository: DataRepository
) : BasePresenter<HomeActivityContract.View>(), HomeActivityContract.Presenter {
    
    override fun getRiderRewardsPoint() {

        homeActivityView.setProgressBar(true)
        dataRepository.getCustomerOrderHistory(dataRepository.user.id,  object : CallbackSubscriber<GetCustomerOrderHistoryRs>() {
            override fun onSuccess(response: GetCustomerOrderHistoryRs?) {

                homeActivityView.setProgressBar(false)

                response?.result?.isStatus?.let { isSuccess ->

                    if (isSuccess) {

                        homeActivityView.showRiderRewardsPoint(response.userRewardsPoint)

                    } else {
                        homeActivityView.getRiderRewardsPointFail(response.result.message)
                    }
                }
            }

            override fun onFailure(restError: RestError?) {
                homeActivityView.setProgressBar(false)
                homeActivityView.processErrorWithToast(restError)
            }
        })
    }

    override fun validateLicense() {

        homeActivityView.setProgressBar(true)
        dataRepository.validateLicense(
            object : CallbackSubscriber<ValidateLicenseRs>() {
                override fun onSuccess(response: ValidateLicenseRs?) {

                    homeActivityView.setProgressBar(false)

                    response?.result?.isStatus?.let { isSuccess ->
                        if (isSuccess) {
                            homeActivityView.showValidateLicense(response)
                        } else {
                            homeActivityView.getValidateLicenseFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    homeActivityView.setProgressBar(false)
                    homeActivityView.processErrorWithToast(restError)
                }
            })

    }

    override fun addUpdateFCMToken() {

        /*
         class AddUpdateFCMTokenRq(
    val UserID: Int,
    val LoggedOn: Int,
    val DeviceToken: String,
    val HardwareNo: String
) : Serializable
         */

        Log.i("TOKENTEST", "1. Token process start...")
        dataRepository.getToken(object : CallbackSubscriber<String>() {
            override fun onSuccess(fcmToken: String) {

                Log.i("TOKENTEST", "2. onSuccess: "+fcmToken)
                val localFCMToken = dataRepository.firebaseToken

                if(localFCMToken == null || !localFCMToken.equals(fcmToken,true)) {

                    Log.i("TOKENTEST", "3. localFCMToken == null || !localFCMToken.equals(fcmToken,true)")
                    val addUpdateFCMTokenRq = AddUpdateFCMTokenRq(dataRepository.user.id,
                        2,
                        fcmToken,
                        SystemUtility.getInstance().deviceId
                        )

                    dataRepository.addUpdateFCMToken(addUpdateFCMTokenRq,
                        object : CallbackSubscriber<AddUpdateFCMTokenRs>() {
                            override fun onSuccess(response: AddUpdateFCMTokenRs?) {

                                Log.i("TOKENTEST", "4. onSuccess: addUpdateFCMToken")
                                response?.result?.isStatus?.let { isSuccess ->
                                    if (isSuccess) {
                                        Log.i("TOKENTEST", "5. isSuccess: tokenset")
                                        dataRepository.firebaseToken = fcmToken
                                    }
                                }
                            }

                            override fun onFailure(restError: RestError?) {
                                homeActivityView.setProgressBar(false)
                                homeActivityView.processErrorWithToast(restError)
                                Log.i("TOKENTEST", "6. onFailure: "+restError?.message)
                            }
                        })


                }
                //
            }

            override fun onFailure(restError: RestError?) {
               // Log.i("TOKENTEST", "Token fail: "+restError?.message)
                Log.i("TOKENTEST", "7. onFailure1: "+restError?.message)
            }

        })

    }
}


