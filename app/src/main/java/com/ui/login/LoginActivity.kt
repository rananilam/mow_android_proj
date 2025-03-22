package com.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.data.DataRepository
import com.data.Injection
import com.data.model.user.Config
import com.data.objectforupdate.DialogUtils
import com.data.objectforupdate.UpdateAppContract
import com.data.objectforupdate.updateAppPresenter
import com.mow.cash.android.BuildConfig
import com.mow.cash.android.R
import com.ui.help.HelpActivity
import com.ui.home.HomeActivity
import com.ui.home.reportBug.ReportBugActivity
import com.ui.login.login.LoginFragment
import com.ui.login.register.CreateAccountActivity
import com.ui.login.retrive.RetrieveYourCellFragment
import com.ui.login.verification.PhoneNumberVerificationFragment
import iCode.android.BaseActivity


class LoginActivity : BaseActivity() , UpdateAppContract.View{

    private lateinit var presenter_updateApp: UpdateAppContract.Presenter
    private lateinit var dataRepository: DataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.act_mow_login)
        dataRepository = Injection.provideDataRepository()
        presenter_updateApp = updateAppPresenter(this,dataRepository)

        if (savedInstanceState == null) {
            // nilam add below line for force update app
            presenter_updateApp.updateAppConfig()
        }

    }



    fun showLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_content,
                LoginFragment.newInstance(),
                LoginFragment::class.java.name
            )
            .commitNow()
    }


    fun showCreateAccountActivity(context: Context) {

        val intent = Intent(context, CreateAccountActivity::class.java)
        context.startActivity(intent)
    }
  fun showReportBugActivity(context: Context) {

  /*    supportFragmentManager.beginTransaction()
          .addToBackStack("RetrieveYourCellFragment")
          .replace(R.id.fragment_content, ReportBugFragment.newInstance())
          .commit()*/
      val intent = Intent(context, ReportBugActivity::class.java)
      context.startActivity(intent)

  }


    fun showRetrieveYourCellFragment() {

        supportFragmentManager.beginTransaction()
                .addToBackStack("RetrieveYourCellFragment")
                .replace(R.id.fragment_content, RetrieveYourCellFragment.newInstance())
                .commit()

    }

    fun showPhoneNumberVerificationFragment(phoneNumber: String, otp: String) {

        supportFragmentManager.beginTransaction()
                .addToBackStack("PhoneNumberVerificationFragment")
                .replace(R.id.fragment_content, PhoneNumberVerificationFragment.newInstance(phoneNumber,otp))
                .commit()

    }

    fun showHelpActivity(context: Context) {

/*        supportFragmentManager.beginTransaction()
            .addToBackStack("helpFragment")
            .replace(R.id.fragment_content, helpFragment.newInstance())
            .commit()*/


        val intent = Intent(context, HelpActivity::class.java)
        context.startActivity(intent)


    }


    fun showHomeActivity() {
        finish()
        HomeActivity.start(this)
    }

    override fun getAppConfigSuccessfully(config: Config?) {
        config?.let {
            val packageManager = packageManager
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionNameFirst = (packageInfo.versionName)?.toFloat()
            val versionName = BuildConfig.VERSION_NAME
            Log.d("AppVersion", "Version Name: LoginActivity $versionName")


            if(it.minimumAndroidVersion <= versionNameFirst!!) {
                showLoginFragment()
            }
            else {
                //    DialogUtils.showAlertDialog(context = requireActivity(),)
                DialogUtils.showAlertDialog(context = this, activity = this)


            }
        }
    }

    override fun getAppConfigFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }


}