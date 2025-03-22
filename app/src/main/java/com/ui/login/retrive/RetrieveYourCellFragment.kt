package com.ui.login.retrive

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import com.data.Injection
import com.ui.login.LoginActivity
import com.ui.login.login.LoginFragmentContract
import com.ui.login.login.LoginFragmentPresenter
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowRetrieveYourCellNumberBinding
import iCode.android.BaseFragment


class RetrieveYourCellFragment : BaseFragment(), RetrieveYourCellFragmentContract.View {


    private val binding: FragMowRetrieveYourCellNumberBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_retrieve_your_cell_number

    private var presenter: RetrieveYourCellFragmentContract.Presenter? = null


    private lateinit var loginActivity: LoginActivity
// private var presenter: LoginFragmentContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = activity as LoginActivity
        val dataRepository = Injection.provideDataRepository()
        presenter = RetrieveYourCellFragmentPresenter(this, dataRepository)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewCreateAccount.setText(HtmlCompat.fromHtml(getString(R.string.hint_create_account), HtmlCompat.FROM_HTML_MODE_LEGACY))
        binding.editTextEmailAddress.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        binding.rootConstraintLayout.setOnTouchListener { p0, p1 ->

            if(p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.rootConstraintLayout)
            }
            false
        }

        binding.rootNestedScrollView.setOnTouchListener { p0, p1 ->

            if(p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.rootNestedScrollView)
            }
            false
        }

        binding.textViewRetrieveCellNumber.setOnClickListener {
            presenter?.retrieveCellPhoneNumber(binding.editTextEmailAddress.text.toString().trim())
        }

        binding.textViewReturnLogin.setOnClickListener{
            loginActivity.showLoginFragment()
        }

        binding.textViewCreateAccount.setOnClickListener{
            loginActivity.showCreateAccountActivity(it.context)
        }

        binding.textViewLblHelp.setOnClickListener{
            loginActivity.showHelpActivity(it.context)
        }

    }

    override fun retrieveCellPhoneSuccessfully(message: String) {

        val builder = AlertDialog.Builder(loginActivity)
        builder.setMessage(message)
            .setPositiveButton(getString(R.string.ok)
            ) { _, _ ->
                loginActivity.showLoginFragment()
            }

        builder.show()
    }

    override fun retrieveCellPhoneFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    companion object {
        fun newInstance() = RetrieveYourCellFragment()
    }
}