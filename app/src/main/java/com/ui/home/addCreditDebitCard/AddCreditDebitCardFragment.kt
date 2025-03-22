package com.ui.home.addCreditDebitCard


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import com.data.DataRepository
import com.data.Injection
import com.data.model.order.State
import com.data.model.user.User
import com.data.remote.response.order.CreditCardRs
import com.google.android.gms.wallet.PaymentCardRecognitionResult
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.mlkit.vision.common.InputImage
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowCreditCardDetailsBinding
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import com.ui.home.HomeActivity
import com.util.PaymentsUtil
import com.util.setHintMsg
import iCode.android.BaseFragment
import iCode.utility.PhoneNumberTextWatcher
import libraries.image.helper.ImageHelper
import libraries.image.helper.models.MediaResult
import java.io.File
import java.util.Calendar
import java.util.Locale

class AddCreditDebitCardFragment : BaseFragment(), AddCreditDebitCardFragmentContract.View {

    private val binding: FragMowCreditCardDetailsBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_credit_card_details

    private lateinit var homeActivity: HomeActivity
    private lateinit var presenter: AddCreditDebitCardFragmentContract.Presenter

    private var state = ""
    private var expMonth = 0
    private var expYear = 0
    private var cardId = 0
    private val bundle = Bundle()

    private var selectedStateId = 0

    private var stateList = listOf<State>()
    private lateinit var rxPermissions: RxPermissions
    private var mediaResult: MediaResult? = null
    private lateinit var dataRepository: DataRepository

    private lateinit var paymentsClient: PaymentsClient




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        rxPermissions = RxPermissions(homeActivity)
        dataRepository = Injection.provideDataRepository()
        presenter = AddCreditDebitCardFragmentPresenter(this, dataRepository)
        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        paymentsClient = PaymentsUtil.createPaymentsClient(homeActivity)

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CheckResult", "SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bundle.putBoolean("CreditCard", false)
        parentFragmentManager.setFragmentResult("requestKeyCreditCard", bundle)
        // Nilam add html logo in credit card page start
        binding.webViewLogo.setOnTouchListener { _, _ -> true }
        binding.webViewLogo.settings.javaScriptEnabled = true
        binding.webViewLogo.isVerticalScrollBarEnabled = false
        binding.webViewLogo.isHorizontalScrollBarEnabled = false
        binding.webViewLogo.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        binding.webViewLogo.webViewClient = WebViewClient()
        val htmlTextLogo = homeActivity.getString(R.string.html_text_logo)
        binding.webViewLogo.loadDataWithBaseURL(null, htmlTextLogo, "text/html", "UTF-8", null)
        // Nilam add html logo in credit card page end


        binding.textInputLayoutFirstName.setHintMsg(getString(R.string.hint_first_name))
        binding.textInputLayoutLastName.setHintMsg(getString(R.string.hint_last_name))
        binding.textInputLayoutStreetAddress.setHintMsg(getString(R.string.hint_street_address))
        binding.textInputLayoutTownCity.setHintMsg(getString(R.string.hint_town_city))
        binding.textInputLayoutState.setHintMsg(getString(R.string.hint_state))
        binding.textInputLayoutZip.setHintMsg(getString(R.string.hint_zip))
        binding.textInputLayoutMobileCellPhone.setHintMsg(getString(R.string.hint_cell_number))
        binding.textInputLayoutPhone.setHintMsg(getString(R.string.hint_home_number))

        binding.textInputLayoutEmailAddress.setHintMsg(getString(R.string.hint_email))
        binding.textInputLayoutCreditCardType.setHintMsg(getString(R.string.hint_credit_card_type))
        binding.textInputLayoutCardNumber.setHintMsg(getString(R.string.hint_card_number))
        binding.textInputLayoutMonth.setHintMsg(getString(R.string.hint_expiration_month))
        binding.textInputLayoutYear.setHintMsg(getString(R.string.hint_expiration_year))
        binding.textInputLayoutCVV.setHintMsg(getString(R.string.hint_cvv))
        binding.textViewScan.hint = HtmlCompat.fromHtml(
            getString(R.string.hint_scan_front_of_credit_card_now),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )



        binding.editTextFirstName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextMiddleName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLastName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextStreetAddress.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextApartment.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextTownCity.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextEmailAddress.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextCardNumber.filters = arrayOf<InputFilter>(LengthFilter(20))


        binding.editTextState.setOnClickListener {
            showStateDialog()

        }
        binding.editTextMonth.setOnClickListener {
            showMonthDialog()
        }
        binding.editTextViewYear.setOnClickListener {
            showYearDialog()
        }

        binding.editTextCreditCardType.setOnClickListener {
            showCardDialog()
        }

        binding.textViewSubmit.setOnClickListener {
            presenter.authorizePaymentRequest(

                binding.editTextFirstName.text.toString(),
                binding.editTextMiddleName.text.toString(),
                binding.editTextLastName.text.toString(),
                binding.editTextEmailAddress.text.toString(),
                binding.editTextMobileCellPhone.text.toString(),
                binding.editTextTownCity.text.toString(),
                binding.editTextZip.text.toString(),
                state, selectedStateId, "", cardId,
                binding.editTextCardNumber.text.toString(),
                binding.editTextCVV.text.toString(),
                binding.editTextStreetAddress.text.toString(),
                binding.editTextApartment.text.toString(),
                expYear, expMonth

            )
        }
        binding.textViewReturnReservation.setOnClickListener {
            bundle.putBoolean("CreditCard", false)
            homeActivity.onBackPressed()
        }
        binding.textViewBack.setOnClickListener {

            bundle.putBoolean("CreditCard", false)
            homeActivity.onBackPressed()
        }


        binding.rootConstraintLayout.setOnTouchListener { p0, p1 ->

            if (p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.rootConstraintLayout)
            }
            false
        }

        binding.rootNestedScrollView.setOnTouchListener { p0, p1 ->

            if (p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.rootNestedScrollView)
            }
            false
        }


        binding.textViewScan.setOnClickListener {
            rxPermissions
                .requestEach(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
                .subscribe({ permission ->
                    if (permission.granted) {
                        Log.d("Hello World", "Granted permisison");
                        showBottomSheetDialog(permission)

                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Permission denied without 'Never Ask Again'
                        Log.d(
                            "Hello World",
                            permission.name + " permission denied, should show rationale"
                        )
                    } else {
                        // Permission denied with 'Never Ask Again'
                        Log.d("Hello World", permission.name + " permission denied permanently")
                    }
                }
                ) { Log.e("Hello World", "Error requesting permissions") }


        }





        binding.editTextCardNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (s.isNotEmpty()) {
                        val cardType = presenter.getCardType(it.toString())
                        var  cardId_ = when (cardType) {
                            "Visa" -> 1
                            "American Express" -> 2
                            "MasterCard" -> 3
                            "Discover" -> 4
                            else -> 0
                        }
                        Log.e("Nilam", "cardId --> " + cardId_)
                        if(cardId_ != 0 ){
                            cardId = cardId_
                            binding.editTextCreditCardType.setText(cardType)

                        }else{
                            cardId = 0
                            binding.editTextCreditCardType.setText("")
                        }

                    }else{
                        Log.e("Nilam", "else portion call " )

                        cardId = 0
                        binding.editTextCreditCardType.setText("")
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }
        })


        binding.editTextMobileCellPhone.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextMobileCellPhone))
        binding.editTextPhone.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextPhone))
        presenter.getCustomer()
    }


    private fun showBottomSheetDialog(permission: Permission) {
        val bottomSheetDialog = BottomSheetDialog(homeActivity)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_galary_capture, null)

        val openGallery = view.findViewById<TextView>(R.id.openGallery)
        val capturePhoto = view.findViewById<TextView>(R.id.capturePhoto)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
        openGallery.setOnClickListener {
            bottomSheetDialog.dismiss()

            if (permission.granted) {
                openGallery()
            }


        }

        capturePhoto.setOnClickListener {
            openCamera()
            bottomSheetDialog.dismiss()

        }


    }

    private fun openGallery() {

        ImageHelper.getInstance().with(this@AddCreditDebitCardFragment).setCrop(true)
            .setMaxResultSize(2048, 2048).setIImage {
                mediaResult = it
                val imageFile = File(mediaResult!!.path)
                val bytes = imageFile.readBytes()
                val imageBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)
                val bm = BitmapFactory.decodeFile(mediaResult!!.path)
                val image = InputImage.fromBitmap(bm, 0)
                presenter.getCreditCardInfo(image)
            }.start(ImageHelper.Option.IMAGE_PICK)
    }


    private fun openCamera() {
        ImageHelper.getInstance().with(this@AddCreditDebitCardFragment).setCrop(true)
            .setMaxResultSize(2048, 2048).setIImage {
                mediaResult = it
                val bm = BitmapFactory.decodeFile(mediaResult!!.path)
                val image = InputImage.fromBitmap(bm, 0)
                presenter.getCreditCardInfo(image)
            }.start(ImageHelper.Option.IMAGE_CAPTURE)
    }


    private fun showStateDialog() {

        if (stateList.isNotEmpty()) {
            val list = arrayOfNulls<String>(stateList.size)

            for ((index, value) in stateList.withIndex()) {
                list[index] = value.stateName
            }


            val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_state)
                .setItems(list) { dialog, which ->
                    selectedStateId = stateList[which].id
                    binding.editTextState.setText(stateList[which].stateName)
                }
            builder.show()
        }

    }

    private fun showMonthDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
        builder.setTitle(R.string.select_month)
            .setItems(R.array.month_options) { dialog, which ->
                expMonth = which + 1
                binding.editTextMonth.setText(homeActivity.resources.getStringArray(R.array.month_options)[which])

            }
        builder.show()
    }

    private fun showYearDialog() {

        val list = arrayOfNulls<String>(20)

        val startYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 0..19) {
            list[i] = (startYear + i).toString()
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
        builder.setTitle(R.string.select_year)
            .setItems(list) { dialog, which ->
                expYear = list[which]!!.toInt()
                binding.editTextViewYear.setText(list[which])
            }
        builder.show()
    }


    private fun showCardDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
        builder.setTitle(R.string.select_card)
            .setItems(R.array.card_type_options) { dialog, which ->

                binding.editTextCardNumber.setText("")
                binding.editTextCVV.setText("")
                binding.editTextMonth.setText("")
                binding.editTextViewYear.setText("")



                cardId = which + 1
                binding.editTextCreditCardType.setText(homeActivity.resources.getStringArray(R.array.card_type_options)[which])

                if (which == 1) {
                    binding.editTextCardNumber.filters = arrayOf<InputFilter>(LengthFilter(20))
                    binding.editTextCVV.filters = arrayOf<InputFilter>(LengthFilter(4))

                } else {
                    binding.editTextCardNumber.filters = arrayOf<InputFilter>(LengthFilter(20))
                    binding.editTextCVV.filters = arrayOf<InputFilter>(LengthFilter(3))
                }
            }
        builder.show()
    }

    override fun authorizePaymentSuccessfully() {
        bundle.putBoolean("CreditCard", true)
        homeActivity.onBackPressed()
    }

    override fun authorizePaymentFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showStateList(stateList: List<State>) {
        if (stateList.isNotEmpty()) {
            this.stateList = stateList

            if (selectedStateId == 0) {
                if (binding.editTextState.text.toString().trim().isNotEmpty()) {
                    val selectedState = stateList.filter {
                        it.stateName.equals(binding.editTextState.text.toString(), true)
                    }

                    if (selectedState.isNotEmpty()) {
                        selectedStateId = selectedState[0].id
                    }
                }
            }
        }
    }

    override fun getStateListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showCustomer(customer: User) {

        binding.editTextFirstName.setText(customer.firstName.toUpperCase(Locale.ROOT))
        binding.editTextMiddleName.setText(customer.middleName.toUpperCase(Locale.ROOT))
        binding.editTextLastName.setText(customer.lastName.toUpperCase(Locale.ROOT))
        binding.editTextStreetAddress.setText(customer.billAddress1.toUpperCase(Locale.ROOT))
        binding.editTextApartment.setText(customer.billAddress2.toUpperCase(Locale.ROOT))
        binding.editTextTownCity.setText(customer.billCity.toUpperCase(Locale.ROOT))
        binding.editTextState.setText(customer.stateName.toUpperCase(Locale.ROOT))
        binding.editTextZip.setText(customer.billZip)
        binding.editTextMobileCellPhone.setText(customer.cellNumber)
        binding.editTextPhone.setText(customer.homeNumber)
        binding.editTextEmailAddress.setText(customer.email.toUpperCase(Locale.ROOT))
        selectedStateId = customer.billStateID
        if (customer.firstName.isNotEmpty()) {
            binding.editTextFirstName.isFocusable = false;
            binding.editTextFirstName.isClickable = false;
        }
        if (customer.middleName.isNotEmpty()) {
            binding.editTextMiddleName.isFocusable = false;
            binding.editTextMiddleName.isClickable = false;
        }
        if (customer.lastName.isNotEmpty()) {
            binding.editTextLastName.isFocusable = false;
            binding.editTextLastName.isClickable = false;
        }

        presenter.getAllState()

    }

    override fun getCustomerFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showDocument(document: CreditCardRs) {

        /*<item>Visa</item>
        <item>American Express</item>
        <item>MasterCard</item>
        <item>Discover</item>*/

        val cardType = document.card_type
        cardId = when (cardType) {
            "Visa" -> 1
            "American Express" -> 2
            "MasterCard" -> 3
            "Discover" -> 4
            else -> 0
        }
        Log.e("Nilam", "cardId --> " + cardId)
        binding.editTextCreditCardType.setText(document.card_type)
        binding.editTextCardNumber.setText(document.card_number)
        Log.e("Nilam", "expiry_date --> " + document.expiry_date)
        Log.e("Nilam", "card number --> " + document.card_number.toString())

        try {
            val documentSplit = document.expiry_date.trim().split("/")
            if (documentSplit.size == 2) {
                val month = documentSplit[0]
                if (month.isNotEmpty()) {
                    expMonth = month.toInt() - 1
                    binding.editTextMonth.setText(homeActivity.resources.getStringArray(R.array.month_options)[expMonth])
                    Log.e("Nilam", "month --> " + month.toString())
                }
                val year = documentSplit[1]
                if (year.isNotEmpty()) {
                    val list = arrayOfNulls<String>(20)
                    val startYear = Calendar.getInstance().get(Calendar.YEAR)
                    val starty = startYear - 1
                    for (i in 0..19) {
                        list[i] = (starty + i).toString()
                    }
                    val year_ = "20$year"
                    expYear = year_.toInt()
                    val ind = list.indexOf(expYear.toString());
                    binding.editTextViewYear.setText(list[ind])
                    Log.e("Nilam", "expYear --> " + expYear.toString())

                }
            } else {
                binding.editTextMonth.setText("")
                binding.editTextViewYear.setText("")
                expYear = 0
                expMonth = 0
            }


        } catch (e: Exception) {
            Log.e("Nilam", "errrorrrrr --> " + e.toString())
            binding.editTextMonth.setText("")
            binding.editTextViewYear.setText("")

        }


    }


    override fun getDocumentFail(errorMessage: String?) {
        binding.editTextMonth.setText("")
        binding.editTextViewYear.setText("")
        binding.editTextCreditCardType.setText("")
        binding.editTextCardNumber.setText("")
        expYear = 0
        expMonth = 0
        cardId = 0
        showToastMessage(errorMessage)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        ImageHelper.getInstance().onActivityResult(requestCode, resultCode, data)

    }







    private fun handlePaymentCardRecognitionSuccess(
        cardRecognitionResult: PaymentCardRecognitionResult,
    ) {
        Log.e("Nilam", "cardResult text---> ")

        val creditCardExpirationDate = cardRecognitionResult.creditCardExpirationDate
        val expirationDate = creditCardExpirationDate?.let { "%02d/%d".format(it.month, it.year) }
        val cardResultText = "PAN: ${cardRecognitionResult.pan}\nExpiration date: $expirationDate"

        Log.e("Nilam", "cardResult text---> ${cardResultText}")
    }


    companion object {
        fun newInstance() =
            AddCreditDebitCardFragment()

    }
}