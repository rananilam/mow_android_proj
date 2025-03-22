package com.ui.splash

//import com.check.rent.R

import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.data.DataRepository
import com.data.Injection
import com.data.model.user.Config
import com.data.objectforupdate.DialogUtils
import com.mow.cash.android.BuildConfig
import com.mow.cash.android.R
import com.ui.home.HomeActivity
import com.ui.login.LoginActivity
import com.util.AppSignatureHelper
import iCode.android.BaseActivity


class SplashActivity : BaseActivity(), SplashActivityContract.View {

    private lateinit var dataRepository: DataRepository
    private lateinit var presenter: SplashActivityPresenter

    private val startHomeRunnable = Runnable {

        dataRepository = Injection.provideDataRepository()
        presenter = SplashActivityPresenter(this, dataRepository)
        presenter.getAppConfig()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_mow_splash)

        val appSignatureHelper = AppSignatureHelper(this)
        val codes = appSignatureHelper.appSignatures

        Log.i("SMSTEST", "codes: "+codes.toString())
    }

    private val delayedStartHandler = Handler()

    override fun onStart() {
        super.onStart()
        delayedStartHandler.postDelayed(startHomeRunnable, SPLASH_DISPLAY_TIME_MS)
    }

    override fun onStop() {
        super.onStop()
        delayedStartHandler.removeCallbacks(startHomeRunnable)
    }

    override fun getDestinationListSuccessfully() {
        finish()
        if(dataRepository.isLogin) {
            HomeActivity.start(this)
        } else {
            LoginActivity.start(this)
        }
    }

    override fun getDestinationListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun getAppConfigSuccessfully(config: Config?) {


        config?.let {
            val packageManager = packageManager
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionNameFirst = (packageInfo.versionName)?.toFloat()
            val versionName = BuildConfig.VERSION_NAME
            Log.d("AppVersion", "Version Name: SplashActivity $versionName")


            if(it.minimumAndroidVersion <= versionNameFirst!!) {
                if(dataRepository.locationId == 0) {
                    presenter.getDestinationList()
                } else {
                    finish()
                    if(dataRepository.isLogin) {
                        HomeActivity.start(this)
                    } else {
                        LoginActivity.start(this)
                    }
                }
            }
            else {
                DialogUtils.showAlertDialog(context = this, activity = this)




            }
        }
    }

    override fun getAppConfigFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    companion object {
        private const val SPLASH_DISPLAY_TIME_MS: Long = 2000
    }


}