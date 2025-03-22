package com.ui.login.register.payor

//import com.tbruyelle.rxpermissions2.RxPermissions
import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import com.data.DataRepository
import com.data.Injection
import com.data.model.idanalyzer.Document
import com.data.model.order.State
import com.data.model.user.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowCreateAccountPayerBinding
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import com.ui.login.register.CreateAccountActivity
import com.util.setHintMsg
import iCode.android.BaseFragment
import iCode.utility.DateFormatHelper
import iCode.utility.PhoneNumberTextWatcher
import libraries.image.helper.ImageHelper
import libraries.image.helper.models.MediaResult
import java.util.Calendar


class PayorInfoFragment : BaseFragment(), PayorInfoFragmentContract.View {


    private val binding: FragMowCreateAccountPayerBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_create_account_payer

    private lateinit var createAccountActivity: CreateAccountActivity
    private lateinit var dataRepository: DataRepository
    private lateinit var presenter: PayorInfoFragmentContract.Presenter

    private var stateList = listOf<State>()
    private var selectedStateId = 0
    private var mediaResult: MediaResult? = null

    private lateinit var rxPermissions: RxPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAccountActivity = activity as CreateAccountActivity
        rxPermissions = RxPermissions(createAccountActivity)
        dataRepository = Injection.provideDataRepository()
        presenter = PayorInfoFragmentPresenter(this, dataRepository)
    }

    @SuppressLint("CheckResult", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {


            binding.textInputLayoutFirstName.setHintMsg(getString(R.string.hint_first_name))



            binding.TextInputLayoutLastName.setHintMsg(
                getString(R.string.hint_last_name))

            binding.textInputLayoutSelectCountry.setHintMsg(
                    getString(R.string.hint_select_country)
                )

            binding.textInputLayoutStreetAddress.setHintMsg(getString(R.string.hint_street_address))
            binding.textInputLayoutTownCity.setHintMsg(getString(R.string.hint_town_city))
            binding.textInputLayoutState.setHintMsg(
                    getString(R.string.hint_state)
                )
            binding.textInputLayoutOtherState.setHintMsg(getString(R.string.hint_other_state))
            binding.textInputLayoutZip.setHintMsg(getString(R.string.hint_zip))
            binding.textInputLayoutCellNumber.setHintMsg(getString(R.string.hint_cell_phone_number))
            binding.textInputLayoutEmail.setHintMsg(
                getString(R.string.hint_email))
            binding.textInputLayoutPassword.setHintMsg(
                    getString(R.string.hint_password)
                )
            binding.textInputLayoutConfirmPassword.setHintMsg(
                    getString(R.string.hint_confirm_password)
                )
            binding.textViewLogin.text =
                HtmlCompat.fromHtml(
                    getString(R.string.hint_already_have_an_account),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            binding.textInputLayoutCountry.setHintMsg(
                getString(R.string.hint_country)
            )

            binding.textViewScan.hint = HtmlCompat.fromHtml(
                getString(R.string.hint_scan_front_of_payor_id_now),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            binding.textInputLayoutLicenseNumber.setHintMsg(
                getString(R.string.hint_license_valid_id_number)
            )
            binding.textInputLayoutExpirationDate.setHintMsg(getString(R.string.hint_expiration_date))

            binding.textInputLayoutDateOfBirth.setHintMsg(
                getString(R.string.hint_date_of_birth)
            )
        }

        binding.editTextFirstName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextMiddleName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLastName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextCountry.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextStreetAddress.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextApartment.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextTownCity.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextOtherState.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextZip.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextCellNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextHomeNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLicenseNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextEmail.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextNote.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        binding.editTextCellNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextCellNumber))
        binding.editTextHomeNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextHomeNumber))



        binding.rootNestedScrollView.setOnTouchListener { p0, p1 ->

            if(p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.rootNestedScrollView)
            }
            false
        }

        binding.textViewLogin.setOnClickListener {
            createAccountActivity.finish()
        }

        binding.textViewSubmit.setOnClickListener {

            var country = binding.textViewSelectCountry.text.toString()

            if(binding.textViewSelectCountry.text.toString().trim().equals("Other"))
                country = binding.editTextCountry.text.toString()

            val payer = presenter.savePayor(
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
                binding.editTextPassword.text.toString(),
                binding.editTextConfirmPassword.text.toString(),
                binding.editTextNote.text.toString(),
                mediaResult
            )

            payer?.let {
                createAccountActivity.payor = it
                createAccountActivity.payor?.mediaResult = mediaResult
                if(createAccountActivity.operatorList.isNotEmpty()) {
                    createAccountActivity.showOperatorListFragment()
                } else {
                    createAccountActivity.showOperatorInfoFragment(-1)
                }
            }
            //createAccountActivity.showOperatorInfoFragment(presenter.getSaveCustomerRq(), null)
        }

       /* binding.textViewScan.setOnClickListener {
            rxPermissions
                .request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .subscribe { granted: Boolean ->
                    if (granted) {
                        ImageHelper.getInstance().with(this@PayorInfoFragment).setCrop(true)
                            .setMaxResultSize(2048, 2048).setIImage {
                                mediaResult = it
                                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
                            }.start(ImageHelper.Option.IMAGE_CAPTURE)
                    }
                }
        }*/
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
                        /*ImageHelper.getInstance().with(this@PayorInfoFragment).setCrop(true)
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

//            rxPermissions
//                .request(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                .subscribe { granted: Boolean ->
//                    if (granted) {
//                        ImageHelper.getInstance().with(this@MyProfileFragment).setCrop(true)
//                            .setMaxResultSize(2048, 2048).setIImage {
//                                mediaResult = it
//                                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
//                            }.start(ImageHelper.Option.IMAGE_CAPTURE)
//                    }
//                }
        }


        binding.textViewSelectCountry.setOnClickListener {
            val builder = AlertDialog.Builder(createAccountActivity)
            builder.setTitle(R.string.select_country)
                .setItems(R.array.country_options,
                    DialogInterface.OnClickListener { _, which ->
                        binding.textViewSelectCountry.setText(
                            createAccountActivity.resources.getStringArray(R.array.country_options)[which])

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
                createAccountActivity,
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
        }

        binding.editTextDateOfBirth.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                createAccountActivity,
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



        presenter.getAllState()

        createAccountActivity.payor?.let {
            showInformationFromUser(it)
        }
        // *** Nilam *** //
        binding.editTextCellNumber.setFilters(arrayOf<InputFilter>(LengthFilter(13)))
        binding.editTextHomeNumber.setFilters(arrayOf<InputFilter>(LengthFilter(13)))
        // *** Nilam *** //

        binding.editTextCellNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextCellNumber))
        binding.editTextHomeNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextHomeNumber))
    }

    private fun showBottomSheetDialog(permission: Permission) {
        val bottomSheetDialog = BottomSheetDialog(createAccountActivity)
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

        ImageHelper.getInstance().with(this@PayorInfoFragment).setCrop(true)
            .setMaxResultSize(2048, 2048).setIImage {
                mediaResult = it
                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
            }.start(ImageHelper.Option.IMAGE_PICK)
    }
    private fun openCamera() {
        ImageHelper.getInstance().with(this@PayorInfoFragment).setCrop(true)
            .setMaxResultSize(2048, 2048).setIImage {
                mediaResult = it
                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
            }.start(ImageHelper.Option.IMAGE_CAPTURE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ImageHelper.getInstance().onActivityResult(requestCode, resultCode, data)
    }

    /*override fun saveCustomerSuccessfully() {
        Toast.makeText(createAccountActivity, getString(R.string.create_account_successfully), Toast.LENGTH_LONG)
            .show()
        createAccountActivity.finish()
    }

    override fun saveCustomerFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }*/

    override fun showDocument(document: Document) {

        binding.editTextLicenseNumber.setText(document.documentNumber)
        binding.editTextFirstName.setText(document.firstName)
        binding.editTextMiddleName.setText(document.middleName)
        binding.editTextLastName.setText(document.lastName)

        if (document.nationality_iso3.equals("USA", true)) {
            binding.textViewSelectCountry.setText(document.nationality_iso3)
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
            binding.textViewSelectCountry.setText(getString(R.string.other))
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

    override fun showStateList(stateList: List<State>) {
        if (stateList.isNotEmpty()) {
            this.stateList = stateList
        }
    }

    override fun getStateListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    private fun showInformationFromUser(user: User) {

        binding.editTextExpirationDate.setText(user.expiryDate)
        binding.editTextDateOfBirth.setText(user.dateOfBirth)

        if (user.country.equals("USA", true)) {
            binding.textViewSelectCountry.setText(user.country)
            binding.textInputLayoutState.visibility = View.VISIBLE
            binding.textInputLayoutOtherState.visibility = View.GONE
            binding.textInputLayoutCountry.visibility = View.GONE

            val filterState = stateList.filter { it.id == selectedStateId }

            if (filterState.isNotEmpty()) {
                selectedStateId = filterState[0].id
                binding.editTextState.setText(filterState[0].stateName)
            }
        } else {
            binding.textViewSelectCountry.setText(getString(R.string.other))
            binding.editTextCountry.setText(user.country)
            binding.textInputLayoutCountry.visibility = View.VISIBLE
            binding.textInputLayoutState.visibility = View.GONE
            binding.textInputLayoutOtherState.visibility = View.VISIBLE
            selectedStateId = 0
            binding.editTextState.setText("")
        }


    }

    private fun showStateDialog() {

        if (stateList.isNotEmpty()) {
            val list = arrayOfNulls<String>(stateList.size)

            for ((index, value) in stateList.withIndex()) {
                list[index] = value.stateName
            }


            val builder: AlertDialog.Builder = AlertDialog.Builder(createAccountActivity)
            builder.setTitle(R.string.select_state)
                .setItems(list) { dialog, which ->
                    selectedStateId = stateList[which].id
                    binding.editTextState.setText(stateList[which].stateName)
                }
            builder.show()
        }

    }

    companion object {
        fun newInstance() = PayorInfoFragment()
    }

}