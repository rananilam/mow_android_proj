package com.ui.login.login

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.text.HtmlCompat
import com.data.Injection
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowLoginBinding
import com.ui.login.LoginActivity
import iCode.android.BaseFragment
import iCode.utility.PhoneNumberTextWatcher


class LoginFragment : BaseFragment(), LoginFragmentContract.View {


    private val binding: FragMowLoginBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_login


    private lateinit var loginActivity: LoginActivity
    private var presenter: LoginFragmentContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = activity as LoginActivity
        val dataRepository = Injection.provideDataRepository()
        presenter = LoginFragmentPresenter(this, dataRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkboxIAgreeToThe.setText(HtmlCompat.fromHtml(getString(R.string.hint_agree_terms), HtmlCompat.FROM_HTML_MODE_LEGACY))
        binding.textviewCreateAccount.setText(HtmlCompat.fromHtml(getString(R.string.hint_create_account), HtmlCompat.FROM_HTML_MODE_LEGACY))

        binding.textViewLogin.setOnClickListener {
            presenter?.generateOTP(true,
                binding.editTextPhoneNumer.text.toString())
        }

        binding.textViewLblRetrievNumber.setOnClickListener {
            loginActivity.showRetrieveYourCellFragment()
        }

        binding.textviewCreateAccount.setOnClickListener {
            loginActivity.showCreateAccountActivity(it.context)
        }
        binding.textViewReportBug.setOnClickListener {
            loginActivity.showReportBugActivity(it.context)
        }

        binding.textViewLblHelp.setOnClickListener {
            loginActivity.showHelpActivity(it.context)
        }

        val content = SpannableString(getString(R.string.terms_of_use))
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
/*        binding.textViewTermsOfUse.text = content

        binding.textViewTermsOfUse.setOnClickListener {
            WebViewActivity.start(
                requireActivity(), PestService.getTermsAndConditionURL()
                , "Accept Terms of Use"
            )
        }*/

        binding.editTextPhoneNumer.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextPhoneNumer))
    }



    override fun generateOTPSuccessfully(otp: String) {
        loginActivity.showPhoneNumberVerificationFragment(binding.editTextPhoneNumer.text.toString(), otp)
    }

    override fun generateOTPFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}