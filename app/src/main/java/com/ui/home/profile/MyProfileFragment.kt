package com.ui.home.profile

//import com.tbruyelle.rxpermissions2.RxPermissions
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import com.data.DataRepository
import com.data.Injection
import com.data.model.idanalyzer.Document
import com.data.model.order.OrderData
import com.data.model.order.State
import com.data.model.user.User
import com.data.remote.response.notification.GetBadgeCountRs
import com.data.remote.response.user.ValidateLicenseRs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowProfileBinding
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import com.ui.home.HomeActivity
import com.ui.home.addOperator.AddOperatorFragment
import com.ui.login.register.operator.OperatorAdapter
import com.util.setHintMsg
import iCode.android.BaseFragment
import iCode.utility.DateFormatHelper
import iCode.utility.PhoneNumberTextWatcher
import libraries.image.helper.ImageHelper
import libraries.image.helper.models.MediaResult
import java.util.Calendar


class MyProfileFragment : BaseFragment(), MyProfileFragmentContract.View {

    private final val REQUEST_CODE_EDIT_OPERATOR = 312
    private final val REQUEST_CODE_ADD_OPERATOR = 456

    private val binding: FragMowProfileBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_profile
    private val operatorAdapter: OperatorAdapter by lazy { OperatorAdapter(true) }

    private lateinit var homeActivity: HomeActivity
    private lateinit var dataRepository: DataRepository
    private lateinit var presenter: MyProfileFragmentContract.Presenter

    private var payor: User? = null
    private var stateList = listOf<State>()
    private var selectedStateId = 0
//    private var ExpirationDateAndTime = 0L

    private lateinit var rxPermissions: RxPermissions
    private var mediaResult: MediaResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        rxPermissions = RxPermissions(homeActivity)
        dataRepository = Injection.provideDataRepository()
        presenter = MyProfileFragmentPresenter(this, dataRepository)

        presenter.getCustomer()
        presenter.getOperatorList()
        presenter.getAllState()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.textInputLayoutFirstName.setHintMsg(getString(R.string.hint_first_name))
        binding.textInputLayoutLastName.setHintMsg(getString(R.string.hint_last_name))
        binding.textInputLayoutSelectCountry.setHintMsg(getString(R.string.hint_select_country))
        binding.textInputLayoutStreetAddress.setHintMsg(getString(R.string.hint_street_address))
        binding.textInputLayoutTownCity.setHintMsg(getString(R.string.hint_town_city))
        binding.textInputLayoutState.setHintMsg(getString(R.string.hint_state))
        binding.textInputLayoutZip.setHintMsg(getString(R.string.hint_zip))
        binding.textInputLayoutCellNumber.setHintMsg(getString(R.string.hint_cell_number))
        binding.textInputLayoutEmail.setHintMsg(getString(R.string.hint_email))
        binding.textInputLayoutCountry.setHintMsg((getString(R.string.hint_country)))
        binding.textViewScan.hint = HtmlCompat.fromHtml(getString(R.string.hint_scan_front_of_payor_id_now), HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.textInputLayoutOtherState.setHintMsg(getString(R.string.hint_other_state))
        binding.textInputLayoutExpirationDate.setHintMsg(getString(R.string.hint_expiration_date_))

        binding.textInputLayoutDateOfBirth.setHintMsg(getString(R.string.hint_date_of_birth_))

        binding.recycleViewItems.adapter = operatorAdapter


        binding.editTextCellNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextCellNumber))
        binding.editTextHomeNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextHomeNumber))

        binding.imageViewMenu.setOnClickListener {
            homeActivity.toggleDrawer()
        }
        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }

        operatorAdapter.actionCallbackEditUser = { operator ->

            payor?.let {

                val addOperatorFragment = AddOperatorFragment.newInstance(it,operator)
                addOperatorFragment.setTargetFragment(this, REQUEST_CODE_EDIT_OPERATOR)

                homeActivity.supportFragmentManager.beginTransaction()
                    .addToBackStack("AddOperatorFragment")
                    .replace(R.id.fragment_content, addOperatorFragment)
                    .commit()

            }
        }

        operatorAdapter.actionCallbackManageOccupant = {
            homeActivity.showManageOccupantFragment(it)
        }

        binding.textViewScan.setOnClickListener {
            rxPermissions
                .requestEach(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
                .subscribe({ permission ->
                    if (permission.granted) {
                        Log.d("Hello World","Granted permisison");
                        showBottomSheetDialog(permission)

                       /* ImageHelper.getInstance().with(this@MyProfileFragment).setCrop(true)
                            .setMaxResultSize(2048, 2048).setIImage {
                                mediaResult = it
                                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
                            }.start(ImageHelper.Option.IMAGE_CAPTURE)*/
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

        binding.textviewOprator.setOnClickListener {

            payor?.let {

                val addOperatorFragment = AddOperatorFragment.newInstance(it,null)
                addOperatorFragment.setTargetFragment(this, REQUEST_CODE_ADD_OPERATOR)

                homeActivity.supportFragmentManager.beginTransaction()
                    .addToBackStack("AddOperatorFragment")
                    .replace(R.id.fragment_content, addOperatorFragment)
                    .commit()
            }
        }

        binding.editTextSelectCountry.setOnClickListener {
            val builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_country)
                .setItems(R.array.country_options,
                    DialogInterface.OnClickListener { _, which ->
                        binding.editTextSelectCountry.setText(homeActivity.resources.getStringArray(R.array.country_options)[which])

                        if (which == 0) {
                            binding.textInputLayoutState.visibility = View.VISIBLE
                            binding.textInputLayoutOtherState.visibility = View.GONE
                            binding.textInputLayoutCountry.visibility = View.GONE
                        } else {
                            binding.textInputLayoutState.visibility = View.GONE
                            binding.textInputLayoutOtherState.visibility = View.VISIBLE
                            binding.textInputLayoutCountry.visibility = View.VISIBLE

                            selectedStateId = 0
                            binding.editTextState.setText("")
                        }
                    })
            builder.show()
        }

        binding.editTextState.setOnClickListener {
            showStateDialog()
        }

        binding.editTextExpirationDate.setOnClickListener {
          val datePickerDialog = DatePickerDialog(
                homeActivity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val cal = Calendar.getInstance()
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    binding.editTextExpirationDate.setText(DateFormatHelper.getStringFromCalendar(
                        DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                        cal
                    ))
                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis();
            datePickerDialog.show()



            /*  val ExpirationDate = Calendar.getInstance()

          if (ExpirationDateAndTime != 0L)
                ExpirationDate.timeInMillis = ExpirationDateAndTime
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val todayDate = calendar.timeInMillis

            CalendarViewOpen(parentFragmentManager,todayDate,ExpirationDate, emptyList()) { selection ->
                val selectedDate = Calendar.getInstance().apply { timeInMillis = selection }
                ExpirationDate.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR))
                ExpirationDate.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH))
                ExpirationDate.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH))

                ExpirationDateAndTime = ExpirationDate.timeInMillis
                binding.editTextExpirationDate.setText(DateFormatHelper.getStringFromCalendar(
                    DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                    ExpirationDate
                ))
            }.showDatePicker()*/
        }

        binding.editTextDateOfBirth.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                homeActivity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val cal = Calendar.getInstance()
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    binding.editTextDateOfBirth.setText(DateFormatHelper.getStringFromCalendar(
                        DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                        cal
                    ))

                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis();
            datePickerDialog.show()
        }

        binding.textViewSaveChanges.setOnClickListener {

            var country = binding.editTextSelectCountry.text.toString()

            if(binding.editTextSelectCountry.text.toString().trim().equals("Other"))
                country = binding.editTextCountry.text.toString()

            presenter.saveCustomer(
                binding.editTextFirstName.text.toString(),
                binding.editTextMiddleName.text.toString(),
                binding.editTextLastName.text.toString(),
                country,
                binding.editTextStreetAddress.text.toString(),
                binding.editTextApartment.text.toString(),
                binding.editTextTownCity.text.toString(),
                selectedStateId,
                binding.editTextOtherState.text.toString(),
                binding.editTextZip.text.toString(),
                binding.editTextCellNumber.text.toString(),
                binding.editTextHomeNumber.text.toString(),
                binding.editTextLicenseNumber.text.toString(),
                binding.editTextExpirationDate.text.toString(),
                binding.editTextDateOfBirth.text.toString(),
                binding.editTextEmail.text.toString(),
                mediaResult
            )
        }

       // binding.editTextCellNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextCellNumber))
      //  binding.editTextHomeNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextHomeNumber))

        binding.editTextFirstName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextMiddleName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLastName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextCountry.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextStreetAddress.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextApartment.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextTownCity.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextOtherState.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextZip.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
      //  binding.editTextCellNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
      //  binding.editTextHomeNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLicenseNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextEmail.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

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

        binding.frameLayoutCart.setOnClickListener {
            homeActivity.showReservedItemFragment(false)
        }

        binding.frameLayoutNotification.setOnClickListener {
            homeActivity.showNotificationsFragment()
        }

        payor?.let {
            showCustomer(it)
        }

        presenter.getBadgeCount()
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

            if(permission.granted){
                openGallery()
            }



        }

        capturePhoto.setOnClickListener {
            openCamera()
            bottomSheetDialog.dismiss()

        }


    }
    private fun openGallery() {

        ImageHelper.getInstance().with(this@MyProfileFragment).setCrop(true)
            .setMaxResultSize(2048, 2048).setIImage {

                mediaResult = it
                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
            }.start(ImageHelper.Option.IMAGE_PICK)
    }
    private fun openCamera() {
        ImageHelper.getInstance().with(this@MyProfileFragment).setCrop(true)
            .setMaxResultSize(2048, 2048).setIImage {
                mediaResult = it
                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
            }.start(ImageHelper.Option.IMAGE_CAPTURE)
    }

    override fun onResume() {
        super.onResume()

        if(OrderData.isCartEmpty()) {
            binding.frameLayoutCart.visibility = View.GONE
        } else {
            binding.frameLayoutCart.visibility = View.VISIBLE
            binding.textViewNumberOfItems.text = OrderData.getCartSize().toString()
        }

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
        binding.editTextCellNumber.setText(customer.cellNumber)
        binding.editTextHomeNumber.setText(customer.homeNumber)
        binding.editTextLicenseNumber.setText(customer.licenseNo)
        binding.editTextEmail.setText(customer.email)

        if (customer.country.equals("USA", true)) {
            binding.editTextSelectCountry.setText(customer.country)
            binding.textInputLayoutState.visibility = View.VISIBLE
            binding.textInputLayoutOtherState.visibility = View.GONE
            binding.textInputLayoutCountry.visibility = View.GONE
            selectedStateId = customer.billStateID
            binding.editTextState.setText(customer.stateName)
        } else {
            binding.editTextSelectCountry.setText(getString(R.string.other))
            binding.editTextCountry.setText(customer.country)
            binding.textInputLayoutCountry.visibility = View.VISIBLE
            binding.textInputLayoutState.visibility = View.GONE
            binding.textInputLayoutOtherState.visibility = View.VISIBLE
            selectedStateId = 0
            binding.editTextOtherState.setText(customer.otherBillStateName)
            binding.editTextState.setText("")
        }

        if(customer.expiryDate.isNotEmpty()) {

            binding.editTextExpirationDate.setText(DateFormatHelper.getFormattedDate(
                customer.expiryDate,
                DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A,
                DateFormatHelper.FORMAT_MM_DD_YYYY_S
                ))
        }

        if(customer.dateOfBirth.isNotEmpty()) {

            binding.editTextDateOfBirth.setText(DateFormatHelper.getFormattedDate(
                customer.dateOfBirth,
                DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A,
                DateFormatHelper.FORMAT_MM_DD_YYYY_S
            ))
        }
    }

    override fun getCustomerFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showOperatorList(operatorList: List<User>) {
        operatorAdapter.submitList(operatorList)
        presenter.validateLicense()
    }

    override fun getOperatorListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showStateList(stateList: List<State>) {
        if (stateList.isNotEmpty()) {
            this.stateList = stateList
        }
    }

    override fun getStateListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun saveCustomerSuccessfully() {
        Toast.makeText(homeActivity, getString(R.string.update_successfully), Toast.LENGTH_LONG)
            .show()

    }

    override fun saveCustomerFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showDocument(document: Document) {
        binding.editTextLicenseNumber.setText(document.documentNumber)
        binding.editTextFirstName.setText(document.firstName)
        binding.editTextMiddleName.setText(document.middleName)
        binding.editTextLastName.setText(document.lastName)

        if (document.nationality_iso3.equals("USA", true)) {
            binding.editTextSelectCountry.setText(document.nationality_iso3)
            binding.textInputLayoutCountry.visibility = View.GONE
            binding.textInputLayoutState.visibility = View.VISIBLE
            binding.textInputLayoutOtherState.visibility = View.GONE

            val filterState = stateList.filter { it.abbreviation == document.issuerOrgRegionAbbr }

            if (filterState.isNotEmpty()) {
                selectedStateId = filterState[0].id
                binding.editTextState.setText(filterState[0].stateName)
            }
        } else {
            binding.textInputLayoutCountry.visibility = View.VISIBLE
            binding.editTextCountry.setText(document.nationality_iso3)
            binding.editTextSelectCountry.setText(getString(R.string.other))
            binding.textInputLayoutState.visibility = View.GONE
            binding.textInputLayoutOtherState.visibility = View.VISIBLE
            binding.editTextOtherState.setText(document.issuerOrgRegionFull)
            selectedStateId = 0
            binding.editTextState.setText("")
        }

        binding.editTextStreetAddress.setText(document.address1)
        binding.editTextApartment.setText(document.address2)
        binding.editTextTownCity.setText(document.issueAuthority)
        binding.editTextZip.setText(document.postcode)
        binding.editTextExpirationDate.setText(
            DateFormatHelper.getFormattedDate(
                document.expiry,
                DateFormatHelper.FORMAT_YYYY_MM_DD,
                DateFormatHelper.FORMAT_MM_DD_YYYY_S
            )
        )
        binding.editTextDateOfBirth.setText(
            DateFormatHelper.getFormattedDate(
                document.dob,
                DateFormatHelper.FORMAT_YYYY_MM_DD,
                DateFormatHelper.FORMAT_MM_DD_YYYY_S
            )
        )
    }

    override fun getDocumentFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ImageHelper.getInstance().onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {

            if(requestCode == REQUEST_CODE_EDIT_OPERATOR || requestCode == REQUEST_CODE_ADD_OPERATOR) {
                presenter.getOperatorList()
            }
        }

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

    override fun showValidateLicense(validateLicenseRs: ValidateLicenseRs) {


        if(!validateLicenseRs.isLicenseAvailableForPayor) {

            showUpdateLicenceDialog(getString(R.string.validation_please_scan_a_valid_payor_id),null)

        } else if(validateLicenseRs.lstLicenseNotAvailableForOperator.isNotEmpty()) {

            val filterOperatorList = operatorAdapter.currentList.filter {
                it.operatorID == validateLicenseRs.lstLicenseNotAvailableForOperator[0]
            }

            if(filterOperatorList.isNotEmpty()) {

                val message =
                    String.format(getString(R.string.validation_please_can_a_operator_valid_id),
                        filterOperatorList[0].firstName,
                        filterOperatorList[0].lastName)
                showUpdateLicenceDialog(message, filterOperatorList[0])
            }


        } else if(!validateLicenseRs.isValidLicenseForPayor) {

            showUpdateLicenceDialog(getString(R.string.alert_payor_license_expired),null)

        } else if(validateLicenseRs.notValidLicenseForOperator.isNotEmpty()) {

            val filterOperatorList = operatorAdapter.currentList.filter {
                it.operatorID == validateLicenseRs.notValidLicenseForOperator[0]
            }

            if(filterOperatorList.isNotEmpty()) {

                val message =
                    String.format(getString(R.string.alert_license_expired),
                        filterOperatorList[0].firstName,
                        filterOperatorList[0].lastName)

                showUpdateLicenceDialog(message, filterOperatorList[0])
            }
        }



    }

    private fun showUpdateLicenceDialog(message: String, operator: User?) {

        val builder = AlertDialog.Builder(homeActivity).setTitle(getString(R.string.alert))
            .setMessage(message)
        builder.apply {
            setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->

                if(operator != null) {

                    payor?.let {

                        val addOperatorFragment = AddOperatorFragment.newInstance(it,operator)
                        addOperatorFragment.setTargetFragment(this@MyProfileFragment, REQUEST_CODE_EDIT_OPERATOR)

                        homeActivity.supportFragmentManager.beginTransaction()
                            .addToBackStack("AddOperatorFragment")
                            .replace(R.id.fragment_content, addOperatorFragment)
                            .commit()

                    }

                }
            }
            setNegativeButton(
                getString(R.string.cancel)
            ) { _, _ ->

            }
        }
        builder.show()

    }
    override fun getValidateLicenseFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showBadgeNotificationCount(getBadgeCount: GetBadgeCountRs) {
        if(getBadgeCount.badgeCount >= 1) {
            binding.textViewNumberOfNotifications.visibility = View.VISIBLE
            binding.textViewNumberOfNotifications.text = getBadgeCount.badgeCount.toString()
        } else {
            binding.textViewNumberOfNotifications.visibility = View.GONE
        }
    }

    override fun getBadgeCountFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }
    companion object {

            fun newInstance() =  MyProfileFragment()
    }
}