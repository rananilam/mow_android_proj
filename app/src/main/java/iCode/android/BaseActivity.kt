package iCode.android

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentTransaction
import com.data.Injection
import com.mow.cash.android.R
import com.ui.core.IBaseView
import com.ui.login.LoginActivity
import iCode.view.ProgressDialogFragment
import libraries.retrofit.RestError
import libraries.retrofit.enums.RestErrorType

abstract class BaseActivity : AppCompatActivity(), IBaseView {

    private var progressDialogFragment: ProgressDialogFragment? = null

    /**
     * Binds layout to [ViewDataBinding]
     */
    fun <T : ViewDataBinding> bindContentView(@LayoutRes layoutRes: Int): T =
        DataBindingUtil.setContentView(this, layoutRes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun setProgressBar(show: Boolean) {
        if (progressDialogFragment != null) {
            progressDialogFragment!!.dismissAllowingStateLoss()
            progressDialogFragment = null
        }

        if (show) {
            progressDialogFragment = ProgressDialogFragment.newInstance()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
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

                    finish()
                    LoginActivity.start(this)
                }
                RestErrorType.INTERNET_NOT_AVAILABLE -> {
                    Toast.makeText(this,getString(R.string.check_your_connection_and_try_again),Toast.LENGTH_LONG).show()
                }
                RestErrorType.NETWORK_NOT_AVAILABLE -> {
                    Toast.makeText(this,getString(R.string.check_your_connection_and_try_again),Toast.LENGTH_LONG).show()
                }
                RestErrorType.REST_API_ERROR -> {
                    Toast.makeText(this,getString(R.string.something_went_wrong_please_try_again),Toast.LENGTH_LONG).show()
                }
                RestErrorType.UNKNOWN -> {
                    Toast.makeText(this,getString(R.string.something_went_wrong_please_try_again),Toast.LENGTH_LONG).show()
                }

                RestErrorType.NONE -> {
                    Toast.makeText(this,getString(R.string.something_went_wrong_please_try_again),Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    override fun showToastMessage(message: String?) {
        Log.e("Nilam","Activity---- error toast message")
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    override fun processErrorWithDialog(restError: RestError?) {
        Toast.makeText(this,restError?.message, Toast.LENGTH_LONG).show()
    }

    override fun getContextt(): Context? {
        return this
    }


    fun showKeyBoard() {

        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

    }

    fun closeKeyBoard(view: View) {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    /*open fun onHandledException(exception: HandledException) {
        Dialogs.show(
            context = this,
            title = exception.getStringTitle(this),
            message = exception.getText(this)
        )
    }*/


}