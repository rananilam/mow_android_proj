package com.ui.login.verification


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.data.Injection
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.tasks.Task
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowPhoneNumberVerificationBinding
import com.ui.login.LoginActivity
import iCode.android.BaseFragment
import java.util.concurrent.TimeUnit


class PhoneNumberVerificationFragment : BaseFragment(),
    View.OnClickListener, PhoneNumberVerificationFragmentContract.View {


    private val binding: FragMowPhoneNumberVerificationBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_phone_number_verification

    var time: kotlin.String = "05:00"

    private lateinit var loginActivity: LoginActivity

    private var presenter: PhoneNumberVerificationFragmentContract.Presenter? = null

    private var phoneNumber = ""

    private lateinit var timer: CountDownTimer

    private lateinit var client: SmsRetrieverClient

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val code = intent.getStringExtra("code")

            if (code!= null && code.length == 4) {
                binding.editTextOTP1.setText(code[0].toString())
                binding.editTextOTP2.setText(code[1].toString())
                binding.editTextOTP3.setText(code[2].toString())
                binding.editTextOTP4.setText(code[3].toString())
            }

            Log.i("SMSTEST", "onReceive: "+code)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = activity as LoginActivity
        val dataRepository = Injection.provideDataRepository()
        presenter = PhoneNumberVerificationFragmentPresenter(this, dataRepository)

        client = SmsRetriever.getClient(loginActivity)

        listenCode()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textviewnotReceiveCode.visibility = View.INVISIBLE
        binding.textviewResend.visibility = View.INVISIBLE
        //start timer
        timerStart()


        arguments?.getString("otp")?.let {

            /*if (it.length == 4) {
                binding.editTextOTP1.setText(it[0].toString())
                binding.editTextOTP2.setText(it[1].toString())
                binding.editTextOTP3.setText(it[2].toString())
                binding.editTextOTP4.setText(it[3].toString())
            }*/
        }

        arguments?.getString("phoneNumber")?.let {
            phoneNumber = it
        }



        binding.textviewResend.setOnClickListener(this)


        // binding.textViewEnterOtp.setText(HtmlCompat.fromHtml(textCreateAC, HtmlCompat.FROM_HTML_MODE_LEGACY))
        binding.editTextOTP1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

                if (s.length == 1) {

                    binding.editTextOTP2.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length != 0) {
                    binding.editTextOTP1.setBackgroundResource(R.drawable.bg_mow_edittext_rounded_fill_border)
                } else {
                    binding.editTextOTP1.setBackgroundResource(R.drawable.bg_mow_edittext_rounded_fill_gray)
                }
            }
        })
        binding.editTextOTP2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {

                    binding.editTextOTP3.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length != 0) {
                    binding.editTextOTP2.setBackgroundResource(R.drawable.bg_mow_edittext_rounded_fill_border)
                } else {
                    binding.editTextOTP2.setBackgroundResource(R.drawable.bg_mow_edittext_rounded_fill_gray)
                }

            }
        })
        binding.editTextOTP3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

                if (s.length == 1) {

                    binding.editTextOTP4.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length != 0) {
                    binding.editTextOTP3.setBackgroundResource(R.drawable.bg_mow_edittext_rounded_fill_border)
                } else {
                    binding.editTextOTP3.setBackgroundResource(R.drawable.bg_mow_edittext_rounded_fill_gray)
                }

            }
        })
        binding.editTextOTP4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {


                if (s.length == 1) {

                    //binding.editTextOTP4.requestFocus()
                }

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {


            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length != 0) {
                    binding.editTextOTP4.setBackgroundResource(R.drawable.bg_mow_edittext_rounded_fill_border)
                } else {
                    binding.editTextOTP4.setBackgroundResource(R.drawable.bg_mow_edittext_rounded_fill_gray)
                }

            }
        })


        //GenericKeyEvent here works for deleting the element and to switch back to previous EditText
//first parameter is the current EditText and second parameter is previous EditText
        binding.editTextOTP1.setOnKeyListener(GenericKeyEvent(binding.editTextOTP1, null))
        binding.editTextOTP2.setOnKeyListener(
            GenericKeyEvent(
                binding.editTextOTP2,
                binding.editTextOTP1
            )
        )
        binding.editTextOTP3.setOnKeyListener(
            GenericKeyEvent(
                binding.editTextOTP3,
                binding.editTextOTP2
            )
        )
        binding.editTextOTP4.setOnKeyListener(
            GenericKeyEvent(
                binding.editTextOTP4,
                binding.editTextOTP3
            )
        )



        binding.textViewSubmit.setOnClickListener {
            val otp =
                binding.editTextOTP1.text.toString() + "" + binding.editTextOTP2.text.toString() + "" + binding.editTextOTP3.text.toString() + "" + binding.editTextOTP4.text.toString()

            presenter?.verifyOTP(phoneNumber, otp)
        }

    }

    fun timerStart() {

        timer = object : CountDownTimer(300000, 1000) {
            // adjust the milli seconds here

            override fun onTick(millisUntilFinished: Long) {
                /*               binding.textViewEnterOtp.setText(
                                   HtmlCompat.fromHtml(  textCreateAC +  String.format(
                                       "%2d:%2d",
                                       TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                       TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                               TimeUnit.MINUTES.toSeconds(
                                                   TimeUnit.MILLISECONDS.toMinutes(
                                                       millisUntilFinished
                                                   )
                                               )
                                   )+min, HtmlCompat.FROM_HTML_MODE_LEGACY)
                                 )*/


                val text2: String =
                    (getString(R.string.enter_your_4_digit_code_within) + String.format(
                        "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(
                                        millisUntilFinished
                                    )
                                )
                    ) + getString(R.string.min))

                val spannable: Spannable = SpannableString(text2)

                spannable.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            binding.textViewEnterOtp.context,
                            R.color.dark_blue
                        )
                    ),
                    getString(R.string.enter_your_4_digit_code_within).length,
                    (getString(R.string.enter_your_4_digit_code_within) + String.format(
                        "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(
                                        millisUntilFinished
                                    )
                                )
                    )).length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.textViewEnterOtp.setText(spannable, TextView.BufferType.SPANNABLE)

            }

            override fun onFinish() {

                binding.textviewnotReceiveCode.visibility = View.VISIBLE
                binding.textviewResend.visibility = View.VISIBLE

            }
        }.start()
    }

    override fun onClick(v: View?) {
        if (v!!.equals(binding.textviewResend)) {
            timerStart()
            binding.textviewnotReceiveCode.visibility = View.INVISIBLE
            binding.textviewResend.visibility = View.INVISIBLE
            presenter?.generateOTP(phoneNumber)

            listenCode()

        }
    }

    override fun generateOTPSuccessfully(otp: String) {
        /*if (otp.length == 4) {
            binding.editTextOTP1.setText(otp[0].toString())
            binding.editTextOTP2.setText(otp[1].toString())
            binding.editTextOTP3.setText(otp[2].toString())
            binding.editTextOTP4.setText(otp[3].toString())
        }*/

    }

    override fun generateOTPFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun verifyOTPSuccessfully() {
        loginActivity.showHomeActivity()
    }

    override fun verifyOTPFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun onDestroyView() {
        super.onDestroyView()


        if (this::timer.isInitialized) {
            timer.cancel()
        }
    }


    private fun listenCode() {
        val task: Task<Void> = client.startSmsRetriever()

        task.addOnSuccessListener {
            Log.i("SMSTEST", "addOnSuccessListener")
        }

        task.addOnFailureListener {
            Log.i("SMSTEST", "addOnFailureListener")
        }
    }
    override fun onResume() {
        super.onResume()

        LocalBroadcastManager.getInstance(loginActivity).registerReceiver(mMessageReceiver,
            IntentFilter("codeEvent")
        );
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(loginActivity).unregisterReceiver(mMessageReceiver);
    }
    companion object {

        fun newInstance(phoneNumber: String, otp: String): PhoneNumberVerificationFragment {

            val bundle = Bundle()
            bundle.putString("otp", otp)
            bundle.putString("phoneNumber", phoneNumber)
            val phoneNumberVerificationFragment = PhoneNumberVerificationFragment()
            phoneNumberVerificationFragment.arguments = bundle
            return phoneNumberVerificationFragment
        }
    }


}


class GenericKeyEvent internal constructor(
    private val currentView: EditText,
    private val previousView: EditText?
) : View.OnKeyListener {
    override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.editTextOTP1 && currentView.text.isEmpty()) {
            //If current is empty then previous EditText's number will also be deleted
            previousView!!.text = null
            previousView.requestFocus()
            return true
        }
        return false
    }
}


