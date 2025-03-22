package com.ui.home.reportBug

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.data.DataRepository
import com.data.Injection
import com.mow.cash.android.R
import com.ui.home.HomeActivity
import iCode.android.BaseActivity

class ReportBugActivity : BaseActivity()  {

    private lateinit var dataRepository: DataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.act_mow_report_bug)
        dataRepository = Injection.provideDataRepository()

        if (savedInstanceState == null) {
            showReportBugFragment()
        }

    }



    fun showReportBugFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_content,
                ReportBugFragment.newInstance(),
                ReportBugFragment::class.java.name
            )
            .commitNow()
    }



    fun showHomeActivity() {
        finish()
        HomeActivity.start(this)
    }


    companion object {

        fun start(context: Context) {
            val intent = Intent(context, ReportBugActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}