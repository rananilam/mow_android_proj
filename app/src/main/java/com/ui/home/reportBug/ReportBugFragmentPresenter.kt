package com.ui.home.reportBug

import android.graphics.Color
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import com.data.CallbackSubscriber
import com.data.DataRepository
import com.data.remote.response.order.GetCustomerByIDRs
import com.data.remote.response.order.SentBugReportRs
import com.google.android.material.snackbar.Snackbar
import com.mow.cash.android.R
import com.ui.core.BasePresenter
import libraries.retrofit.RestError

class ReportBugFragmentPresenter(
    val reportBugView: ReportBugFragmentContract.View,
    private val dataRepository: DataRepository,
) : BasePresenter<ReportBugFragmentContract.View>(), ReportBugFragmentContract.Presenter {

    override fun getCustomer() {
        reportBugView.setProgressBar(true)
        dataRepository.getCustomerByID(dataRepository.user.id,
            object : CallbackSubscriber<GetCustomerByIDRs>() {
                override fun onSuccess(response: GetCustomerByIDRs?) {

                    reportBugView.setProgressBar(false)
                    response?.result?.isStatus?.let { isSuccess ->

                        if (isSuccess) {

                            response.customer?.let {
                                reportBugView.showCustomer(it)

                            }

                        } else {
                            reportBugView.getCustomerFail(response.result.message)
                        }
                    }
                }

                override fun onFailure(restError: RestError?) {
                    reportBugView.setProgressBar(false)
                    reportBugView.processErrorWithToast(restError)
                }
            })
    }

    override fun sentBugReport(
        Description: String,
        DeviceModel: String,
        Email: String,
        AppVersion: String,
        documentData: Array<Array<String>>,
    ) {
        if (Description.isNotEmpty()) {
            if (DeviceModel.isNotEmpty()) {
                if (dataRepository.isLogin || (Email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(
                        Email
                    ).matches())
                ) {

                    reportBugView.setProgressBar(true)
                    dataRepository.sentBugReport(Description,
                        DeviceModel,
                        Email,
                        AppVersion,
                        documentData,
                        object : CallbackSubscriber<SentBugReportRs>() {
                            override fun onSuccess(response: SentBugReportRs?) {
                                reportBugView.setProgressBar(false)

                                response?.result?.isStatus?.let { isSuccess ->

                                    if (isSuccess) {
                                        reportBugView.sentBugReportSuccessfully(response.result.message);
                                    } else {
                                        reportBugView.sentBugReportFail(response.result.errorMessage)
                                    }
                                }


                            }

                            override fun onFailure(restError: RestError?) {
                                reportBugView.setProgressBar(false)
                                reportBugView.processErrorWithToast(restError)
                            }
                        })
                } else {
                    if (Email.isEmpty()) {

                        reportBugView.showToastMessage(reportBugView.contextt.getString(R.string.validation_please_enter_email))

                    } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                        reportBugView.showToastMessage(reportBugView.contextt.getString(R.string.validation_please_enter_valid_email))
                    }


                }
            } else {
                reportBugView.showToastMessage(reportBugView.contextt.getString(R.string.validation_please_enter_deviceModel))
            }
        } else {
            reportBugView.showToastMessage(reportBugView.contextt.getString(R.string.validation_please_enter_description))
        }

    }

    fun showSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)

        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.RED) // Set background color to red

        val params = snackbarView.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT // Full-width
        snackbarView.layoutParams = params

        snackbar.show()
    }

}


