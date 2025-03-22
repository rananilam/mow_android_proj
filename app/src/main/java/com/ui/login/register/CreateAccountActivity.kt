package com.ui.login.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.data.DataRepository
import com.data.Injection
import com.data.model.user.User
import com.data.remote.request.user.SaveCustomerRq
import com.ui.home.contentDialog.ContentDialogFragment
import com.ui.login.register.operator.OperatorInfoFragment
import com.ui.login.register.operatorList.OperatorListFragment
import com.ui.login.register.payor.PayorInfoFragment
import com.ui.login.register.payor.PayorInfoFragmentContract
import com.ui.login.register.payor.PayorInfoFragmentPresenter
import com.mow.cash.android.R
import iCode.android.BaseActivity


class CreateAccountActivity : BaseActivity(), CreateAccountActivityContract.View {


    var payor: User? = null
    var operatorList = mutableListOf<User>()

    private lateinit var dataRepository: DataRepository
    private lateinit var presenter: CreateAccountActivityContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.act_mow_create_account)


        dataRepository = Injection.provideDataRepository()
        presenter = CreateAccountActivityPresenter(this, dataRepository)


        if (savedInstanceState == null) {

            showCreateAccountFragment()
        }
    }

    fun showCreateAccountFragment() {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_content, PayorInfoFragment.newInstance())
                .commitNow()
    }


    fun showOperatorListFragment() {

        supportFragmentManager.beginTransaction()
            .addToBackStack("OperatorListFragment")
            .replace(R.id.fragment_content, OperatorListFragment.newInstance())
            .commit()

    }


    fun showOperatorInfoFragment(editOperatorIndex: Int) {

        supportFragmentManager.beginTransaction()
            .addToBackStack("OperatorInfoFragment")
            .replace(R.id.fragment_content, OperatorInfoFragment.newInstance(editOperatorIndex))
            .commit()

    }

    fun showContentDialogFragment(content: String) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(
            ContentDialogFragment.newInstance(content),
            "ContentDialogFragment"
        )
        transaction.commitAllowingStateLoss()
    }

    fun saveCustomer() {
        payor?.let { presenter.saveCustomer(it, operatorList as ArrayList<User>) }
    }
    override fun saveCustomerSuccessfully() {
        Toast.makeText(this, getString(R.string.create_account_successfully), Toast.LENGTH_LONG)
            .show()
        finish()
    }

    override fun saveCustomerFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, CreateAccountActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}