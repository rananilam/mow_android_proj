package com.ui.home.addCustomerECheck

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import com.data.Injection
import com.data.model.order.State
import com.data.model.user.User
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowAddCustomerECheckBinding
import com.ui.home.HomeActivity
import com.util.setHintMsg
import iCode.android.BaseFragment
import iCode.utility.PhoneNumberTextWatcher


class AddCustomerECheckFragment : BaseFragment(),AddCustomerECheckFragmentContract.View {

    private val binding: FragMowAddCustomerECheckBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_add_customer_e_check
    private lateinit var homeActivity: HomeActivity

    private var selectedStateId = 0
    private var selectedState = State()
    private var stateList = listOf<State>()

    private lateinit var accTypeList: MutableMap<Int, String>
    private var selectedAccType = Pair(0, "Alice")
    private var selectOther = false
    private var isAccountNumberMatched = false
    private var isRoutingNumberMatched = false
    private var payor: User? = null

    private lateinit var presenter: AddCustomerECheckFragmentContract.Presenter
    private  val bundle = Bundle()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        presenter = AddCustomerECheckFragmentPresenter(this, Injection.provideDataRepository())
        presenter.getCustomer()
        presenter.getAllBankAccType()
    }

    @SuppressLint("ClickableViewAccessibility", "SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isAccountNumberMatched = false;
        isRoutingNumberMatched = false;

        bundle.putBoolean("eCheckCard", false)
        parentFragmentManager.setFragmentResult("requestKeyEcheck", bundle)



        // Nilam add html logo in e-check card page start
        binding.webViewLogo.setOnTouchListener { _, _ -> true }
        binding.webViewLogo.settings.javaScriptEnabled = true
        binding.webViewLogo.isVerticalScrollBarEnabled = false
        binding.webViewLogo.isHorizontalScrollBarEnabled = false
        binding.webViewLogo.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        binding.webViewLogo.webViewClient = WebViewClient()
        val htmlTextLogo = homeActivity.getString(R.string.html_text_logo)
        binding.webViewLogo.loadDataWithBaseURL(null, htmlTextLogo, "text/html", "UTF-8", null)
        // Nilam add html logo in e-check card page end


        binding.textInputLayoutFirstName.setHintMsg(getString(R.string.hint_first_name))
        binding.textInputLayoutMiddleName.setHintMsg(getString(R.string.hint_middle_name))
        binding.textInputLayoutLastName.setHintMsg(getString(R.string.hint_last_name))
        binding.textInputLayoutStreetAddress.setHintMsg(getString(R.string.hint_street_address))
        binding.textInputLayoutTownCity.setHintMsg(getString(R.string.hint_town_city))
        binding.textInputLayoutState.setHintMsg(getString(R.string.hint_state))
        binding.textInputLayoutZip.setHintMsg(getString(R.string.hint_zip))
        binding.textInputLayoutEmailAddress.setHintMsg(getString(R.string.hint_email))
        binding.textInputLayoutSelectCountry.setHintMsg(getString(R.string.hint_select_country))
        binding.textInputLayoutMobileCellPhone.setHintMsg(getString(R.string.hint_cell_number))
        binding.textInputLayoutPhone.setHintMsg(getString(R.string.hint_home_number))
        binding.textInputLayoutOtherState.setHintMsg(getString(R.string.hint_other_state))
      //  binding.textInputLayoutBankName.setHintMsg(getString(R.string.bank_name))
        binding.textInputLayoutBankAccountType.setHintMsg(getString(R.string.bank_account_type))
        binding.textInputLayoutAccountNumber.setHintMsg(getString(R.string.account_number))
        binding.textInputLayoutConfirmAccountNumber.setHintMsg(getString(R.string.confirm_account_number))
        binding.textInputLayoutRoutingNumber.setHintMsg(getString(R.string.routing_number))
        binding.textInputLayoutConfirmRoutingNumber.setHintMsg(getString(R.string.confirm_routing_number))
        selectOther=false


        binding.editTextFirstName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextMiddleName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLastName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextStreetAddress.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextApartment.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextTownCity.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextEmailAddress.filters = arrayOf<InputFilter>(InputFilter.AllCaps())


        binding.editTextConfirmAccountNumber.filters = arrayOf<InputFilter>(LengthFilter(16))
        binding.editTextAccountNumber.filters = arrayOf<InputFilter>(LengthFilter(16))
        binding.editTextRoutingNumber.filters = arrayOf<InputFilter>(LengthFilter(10))
        binding.editTextConfirmRoutingNumber.filters = arrayOf<InputFilter>(LengthFilter(10))

        // confirm account number start
        binding.editTextConfirmAccountNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    if (it.isNotEmpty() && binding.editTextAccountNumber.text.toString().isNotEmpty())
                        presenter.matchAccountNumber(it.toString(),binding.editTextAccountNumber.text.toString())

                }
            }
        })

        binding.editTextAccountNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    if (it.isNotEmpty() && binding.editTextConfirmAccountNumber.text.toString().isNotEmpty())
                        presenter.matchAccountNumber(it.toString(),binding.editTextConfirmAccountNumber.text.toString())

                }
            }
        })
        // confirm account number end


        // confirm rouing number start
        binding.editTextConfirmRoutingNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    if (it.isNotEmpty() && binding.editTextRoutingNumber.text.toString().isNotEmpty())
                        presenter.matchRoutingNumber(it.toString(),binding.editTextRoutingNumber.text.toString())

                }
            }
        })

        binding.editTextRoutingNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    if (it.isNotEmpty() && binding.editTextConfirmRoutingNumber.text.toString().isNotEmpty())
                        presenter.matchRoutingNumber(it.toString(),binding.editTextConfirmRoutingNumber.text.toString())

                }
            }
        })
        // confirm rouing number end


        binding.editTextState.setOnClickListener {
            showStateDialog()
        }

        binding.editTextBankAccountType.setOnClickListener {
            showBankAccTypeDialog()
        }

        binding.editTextSelectCountry.setOnClickListener {
            val builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_country)
                .setItems(R.array.country_options_with_one_country,
                    DialogInterface.OnClickListener { _, which ->
                        binding.editTextSelectCountry.setText(homeActivity.resources.getStringArray(R.array.country_options)[which])

                        if (which == 0) {
                            selectOther=false
                            binding.textInputLayoutState.visibility = View.VISIBLE
                            binding.textInputLayoutOtherState.visibility = View.GONE
                        } else {
                            binding.textInputLayoutState.visibility = View.GONE
                            binding.textInputLayoutOtherState.visibility = View.VISIBLE
                            selectOther=true
                            selectedStateId = 0
                            selectedState=State()
                            selectedState.id=0
                            binding.editTextState.setText("")
                        }
                    })
            builder.show()
        }



      /*  binding.textViewSubmit.setOnClickListener {
           presenter.authorizePaymentRequest(

                binding.editTextFirstName.text.toString(),
                binding.editTextMiddleName.text.toString(),
                binding.editTextLastName.text.toString(),
                binding.editTextEmailAddress.text.toString(),
                binding.editTextMobileCellPhone.text.toString(),
                binding.editTextTownCity.text.toString(),
                binding.editTextZip.text.toString(),
                state,selectedStateId,"",cardId,
                binding.editTextCardNumber.text.toString(),
                binding.editTextCVV.text.toString(),
                binding.editTextStreetAddress.text.toString(),
                binding.editTextApartment.text.toString(),
                expYear,expMonth

            )
        }*/
        binding.textViewSubmit.setOnClickListener {
            payor.let {
                it!!.firstName = binding.editTextFirstName.text.toString()
                it!!.middleName = binding.editTextMiddleName.text.toString()
                it!!.lastName = binding.editTextLastName.text.toString()
                it!!.billAddress1 = binding.editTextStreetAddress.text.toString()
                it!!.billAddress2 = binding.editTextApartment.text.toString()
                it!!.country = binding.editTextSelectCountry.text.toString()
                it!!.billStateID = selectedStateId
                it!!.billCity = binding.editTextTownCity.text.toString()
                it!!.billZip = binding.editTextZip.text.toString()
                it!!.homeNumber = binding.editTextPhone.text.toString()
                it!!.cellNumber = binding.editTextMobileCellPhone.text.toString()
                it!!.email = binding.editTextEmailAddress.text.toString()
            }
            var  selectedcountry  = ""
            var  selectedState_1  = ""
            if(selectOther && binding.editTextOtherState.text.toString().isNotEmpty()){
                selectedcountry ="Other"
                selectedState_1 = binding.editTextOtherState.text.toString()
            }else{
                selectedcountry =binding.editTextSelectCountry.text.toString()
                selectedState_1 =  selectedState.stateName
            }
            presenter.authorizeECheckRequest(
                binding.editTextFirstName.text.toString(),
                binding.editTextMiddleName.text.toString(),
                binding.editTextLastName.text.toString(),
                binding.editTextStreetAddress.text.toString(),
                binding.editTextApartment.text.toString(),
                selectedcountry,
                selectedState.stateName,
                selectedStateId,
                binding.editTextTownCity.text.toString(),
                binding.editTextZip.text.toString(),
                binding.editTextMobileCellPhone.text.toString(),
                binding.editTextEmailAddress.text.toString(),
              //  binding.editTextBankName.text.toString(),
                "",
                binding.editTextBankAccountType.text.toString(),
                binding.editTextAccountNumber.text.toString(),
                binding.editTextRoutingNumber.text.toString(),
                binding.editTextConfirmAccountNumber.text.toString(),
                isAccountNumberMatched,
                binding.editTextConfirmRoutingNumber.text.toString(),
                isRoutingNumberMatched

                )
        }

        binding.textViewReturnReservation.setOnClickListener {
            bundle.putBoolean("eCheckCard", false)
            homeActivity.onBackPressed()
        }
        binding.textViewBack.setOnClickListener {
            bundle.putBoolean("eCheckCard", false)
            homeActivity.onBackPressed()
        }


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





        binding.editTextMobileCellPhone.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextMobileCellPhone))
        binding.editTextPhone.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextPhone))
        payor?.let {
            showCustomer(it)
        }
    }


    companion object {
        fun newInstance() =
            AddCustomerECheckFragment()
    }



    override fun showCustomer(customer: User) {

        payor = customer
        binding.editTextFirstName.setText(customer.firstName)
        binding.editTextMiddleName.setText(customer.middleName)
        binding.editTextLastName.setText(customer.lastName)

        binding.editTextStreetAddress.setText(customer.billAddress1)
        binding.editTextApartment.setText(customer.billAddress2)
        binding.editTextTownCity.setText(customer.billCity)
        binding.editTextZip.setText(customer.billZip)
        binding.editTextPhone.setText(customer.homeNumber)
        binding.editTextMobileCellPhone.setText(customer.cellNumber)
        binding.editTextEmailAddress.setText(customer.email)

        if (customer.country.equals("USA", true)) {
            binding.editTextSelectCountry.setText(customer.country)
            binding.textInputLayoutState.visibility = View.VISIBLE
            binding.textInputLayoutOtherState.visibility = View.GONE
            selectedStateId = customer.billStateID
            selectedState.id=customer.billStateID
            selectedState.stateName=customer.stateName
            binding.editTextState.setText(customer.stateName)
        } else {
            binding.editTextSelectCountry.setText(getString(R.string.other))
            binding.textInputLayoutState.visibility = View.GONE
            binding.textInputLayoutOtherState.visibility = View.VISIBLE
           // binding.editTextOtherState.setText(customer.otherBillStateName)
            selectedStateId = 0
            selectedState.id=0
            binding.editTextOtherState.setText(customer.otherBillStateName)
            binding.editTextState.setText("")
        }




    presenter.getAllState()
    }

    override fun showStateList(stateList: List<State>) {
        if(stateList.isNotEmpty()) {
            this.stateList = stateList

            if(selectedStateId == 0) {
                if(binding.editTextState.text.toString().trim().isNotEmpty()) {
                    val selectedState = stateList.filter {
                        it.stateName.equals(binding.editTextState.text.toString(),true)
                    }

                    if(selectedState.isNotEmpty()) {
                        selectedStateId = selectedState[0].id
                        this.selectedState = selectedState[0]
                    }
                }
            }
        }
    }
    override fun showAllBankAccType(accTypeList: MutableMap<Int, String>) {
        if(accTypeList.isNotEmpty()) {
            this.accTypeList = accTypeList

        }
    }



    private fun showStateDialog() {

        if(stateList.isNotEmpty()) {
            val list = arrayOfNulls<String>(stateList.size)

            for ((index, value) in stateList.withIndex()) {
                list[index] = value.stateName
            }



            val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_state)
                .setItems(list) { dialog, which ->
                    this.selectedStateId = stateList[which].id
                    binding.editTextState.setText(stateList[which].stateName)
                }
            builder.show()
        }

    }

    private fun showBankAccTypeDialog() {

        if(accTypeList.isNotEmpty()) {

            val list = arrayOfNulls<String>(accTypeList.size)


            for ((index, entry) in accTypeList.entries.withIndex()) {
                println("Index: $index, Key: ${entry.key}, Value: ${entry.value}")
                list[index] = entry.value
            }
            val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_bank_account_type)
                .setItems(list) { dialog, which ->

                    accTypeList.entries.forEachIndexed { index, entry ->
                        if (index == which) {
                            binding.editTextBankAccountType.setText(entry.value)
                            this.selectedAccType = entry.toPair()


                        }
                    }

                }
            builder.show()
        }

    }



    override fun getStateListFail(errorMessage: String?) {
        showToastMessage(errorMessage)

    }

    override fun getCustomerFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun authorizeECheckSuccessfully() {
        bundle.putBoolean("eCheckCard", true)
        homeActivity.onBackPressed()
    }

    override fun authorizeECheckFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showAccountMatchOrNotToast(isAccountNumberMatched: Boolean) {
        this.isAccountNumberMatched = isAccountNumberMatched

    }
    override fun showRoutingMatchOrNotToast(isRoutingNumberMatched: Boolean) {
        this.isRoutingNumberMatched = isRoutingNumberMatched

    }
}

