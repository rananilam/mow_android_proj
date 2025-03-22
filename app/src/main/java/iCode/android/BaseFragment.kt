package iCode.android

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.data.Injection
import com.google.android.material.snackbar.Snackbar
import com.mow.cash.android.R
import com.ui.core.IBaseView
import com.ui.login.LoginActivity
import iCode.view.ProgressDialogFragment
import libraries.retrofit.RestError
import libraries.retrofit.enums.RestErrorType


abstract class BaseFragment : Fragment(), IBaseView {

    private lateinit var fragmentBinding: ViewDataBinding
    private var progressDialogFragment: ProgressDialogFragment? = null

    abstract val layoutResId: Int

    protected inline fun <reified T : ViewDataBinding> bindingLazy(): BindingLazy<T> = BindingLazy()

    private var snackbar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return if (layoutResId == 0) {
            throw IllegalArgumentException("Please setup layoutResId")
        } else {
            fragmentBinding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
            fragmentBinding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    /*open fun onHandledException(exception: HandledException) {
        val context = requireContext()
        Dialogs.show(
            context = context,
            title = exception.getStringTitle(context),
            message = exception.getText(context)
        )
    }*/

    override fun setProgressBar(show: Boolean) {
        if (progressDialogFragment != null) {
            progressDialogFragment!!.dismissAllowingStateLoss()
            progressDialogFragment = null
        }

        if (show) {
            progressDialogFragment = ProgressDialogFragment.newInstance()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.add(progressDialogFragment!!, "ProgressDialogFragment")
            transaction.commitAllowingStateLoss()
        }
    }

    override fun processErrorWithToast(restError: RestError?) {

        restError?.restErrorType?.let {

            when (it) {

                RestErrorType.TOKEN_EXPIRED -> {
                    val dataRepository = Injection.provideDataRepository()
                    dataRepository.expiredToken()

                    activity?.finish()
                    LoginActivity.start(requireContext())
                }
                RestErrorType.INTERNET_NOT_AVAILABLE -> {
                    showToastMessage(context?.getString(R.string.check_your_connection_and_try_again))
                }
                RestErrorType.NETWORK_NOT_AVAILABLE -> {
                    showToastMessage(context?.getString(R.string.check_your_connection_and_try_again))
                }
                RestErrorType.REST_API_ERROR -> {
                    showToastMessage(context?.getString(R.string.something_went_wrong_please_try_again))
                }
                RestErrorType.UNKNOWN -> {
                    showToastMessage(context?.getString(R.string.something_went_wrong_please_try_again))
                }

                RestErrorType.NONE -> {
                    showToastMessage(context?.getString(R.string.something_went_wrong_please_try_again))
                }
            }
        }

    }



    override fun showToastMessage(message: String?) {

        Log.e("Nilam","Fragment error toast message")
        if(isAdded) {
            Log.e("Nilam","Fragment error toast message 111")

            snackbar?.let {
                Log.e("Nilam","Fragment error toast message 2222")

                if(it.isShown)
                    return
            }
            Log.e("Nilam","Fragment error toast message 44444")

            message?.let {

                snackbar = Snackbar.make(fragmentBinding.root, it, Snackbar.LENGTH_LONG)



                /* val view = snackbar?.view
                val params = view?.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP
                view.layoutParams = params*/

                val snackbarView = snackbar?.view

                // Set background color to red
                snackbarView?.setBackgroundColor(Color.RED)

                // Get the Snackbar's text view and set multiple lines
                val textView = snackbarView?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                textView?.maxLines = 4 // Allow up to 4 lines
                textView?.setTextColor(Color.WHITE) // Optional: Set text color


                snackbar?.show()
            }
        }

    }

    override fun processErrorWithDialog(restError: RestError?) {
        Toast.makeText(context,restError?.message,Toast.LENGTH_LONG).show()
    }

    override fun getContextt(): Context? {
        return context
    }

    fun showKeyBoard() {

        val inputMethodManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

    }

    fun closeKeyBoard(view: View) {
        val imm =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @Suppress("UNCHECKED_CAST")
    protected inner class BindingLazy<out T : ViewDataBinding> : Lazy<T> {

        override val value: T
            get() = fragmentBinding as T

        override fun isInitialized() = ::fragmentBinding.isInitialized
    }
}