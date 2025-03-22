package com.ui.login.register.operator

//import com.tbruyelle.rxpermissions2.RxPermissions
import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentManager
import com.data.DataRepository
import com.data.Injection
import com.data.model.idanalyzer.Document
import com.data.model.user.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowCreateAccountOperatorBinding
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


class OperatorInfoFragment : BaseFragment(), OperatorInfoFragmentContract.View {


    private val binding: FragMowCreateAccountOperatorBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_create_account_operator

    private lateinit var createAccountActivity: CreateAccountActivity
    private lateinit var dataRepository: DataRepository
    private lateinit var presenter: OperatorInfoFragmentContract.Presenter

    private var mediaResult: MediaResult? = null

    private lateinit var rxPermissions: RxPermissions




    private var editOperatorIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAccountActivity = activity as CreateAccountActivity
        rxPermissions = RxPermissions(createAccountActivity)
        dataRepository = Injection.provideDataRepository()
        presenter = OperatorInfoFragmentPresenter(this, dataRepository)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getInt("editOperatorIndex")?.let {
            editOperatorIndex = it
        }

        binding.textInputLayoutFirstName.setHintMsg(getString(R.string.hint_first_name))
        binding.textInputLayoutLastName.setHintMsg(getString(R.string.hint_last_name))
        binding.textInputLayoutCellNumber.setHintMsg(getString(R.string.hint_cell_phone_number))
        binding.textInputLayoutEmail.setHintMsg(getString(R.string.hint_email_address))
        binding.textInputLayoutSelectHeightFt.setHintMsg(getString(R.string.hint_height_ft))
        binding.textInputLayoutSelectHeightIn.setHintMsg(getString(R.string.hint_height_in))
        binding.textInputLayoutWeight.setHintMsg(getString(R.string.hint_weight_lbs))
        binding.textViewScan.setHint(HtmlCompat.fromHtml(getString(R.string.hint_scan_front_of_operator_id_now), HtmlCompat.FROM_HTML_MODE_LEGACY))
        binding.textInputLayoutHomeNumber.setHintMsg(getString(R.string.hint_home_phone_number_))
        binding.textInputLayoutLicenseNumber.setHintMsg(getString(R.string.hint_license_valid_id_number))
        binding.textInputLayoutExpirationDate.setHintMsg(getString(R.string.hint_expiration_date))
        binding.textInputLayoutDateOfBirth.setHintMsg(getString(R.string.hint_date_of_birth))


        binding.editTextFirstName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextMiddleName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLastName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextWeight.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextCellNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextHomeNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLicenseNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextEmail.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        binding.editTextCellNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextCellNumber))
        binding.editTextHomeNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextHomeNumber))


        binding.rootNestedScrollView.setOnTouchListener { p0, p1 ->

            if(p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.rootNestedScrollView)
            }
            false
        }


        if(editOperatorIndex != -1) {
            fillInputFromUser(createAccountActivity.operatorList[editOperatorIndex])
            binding.checkBoxIfSame.isChecked = createAccountActivity.operatorList[editOperatorIndex].isDefault
            binding.textViewDelete.visibility = View.VISIBLE
        } else {
            binding.textViewDelete.visibility = View.GONE
            createAccountActivity.operatorList.let { operatorList ->
                if(operatorList.isNotEmpty()) {
                    val isDefaultFilter = operatorList.filter { it.isDefault }

                    if(isDefaultFilter.isNotEmpty()) {
                        binding.checkBoxIfSame.isChecked = false
                        binding.checkBoxIfSame.visibility = View.GONE
                    }

                }
            }
        }

       /* binding.textViewScan.setOnClickListener {

            rxPermissions
                .request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .subscribe { granted: Boolean ->
                    if (granted) {
                        showBottomSheetDialog();
                        ImageHelper.getInstance().with(this@OperatorInfoFragment).setCrop(true)
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

                        /* ImageHelper.getInstance().with(this@OperatorInfoFragment).setCrop(true)
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


        binding.checkBoxIfSame.setOnClickListener {
            if(binding.checkBoxIfSame.isChecked) {
                createAccountActivity.payor?.let {
                    fillInputFromUser(it)
                }
            } else {
                clearInput()
            }
        }

        binding.textViewDelete.setOnClickListener {
            if(editOperatorIndex != -1) {
                createAccountActivity.operatorList.removeAt(editOperatorIndex)

                if(createAccountActivity.operatorList.isEmpty()) {
                    createAccountActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                } else {
                    createAccountActivity.onBackPressed()
                }
            }
        }
        binding.textViewSave.setOnClickListener {

            getUserFromInput()?.let {

                if(editOperatorIndex != -1) {
                    createAccountActivity.operatorList[editOperatorIndex] = it
                } else {
                    createAccountActivity.operatorList.add(it)
                }

                if(editOperatorIndex == -1 && createAccountActivity.operatorList.size == 1) {
                    createAccountActivity.supportFragmentManager.popBackStack()
                    createAccountActivity.showOperatorListFragment()
                } else {
                    createAccountActivity.onBackPressed()
                }
            }
        }

        binding.textViewBack.setOnClickListener {
            createAccountActivity.onBackPressed()
        }

        binding.editTextSelectHeightFt.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(createAccountActivity)
            builder.setTitle(R.string.select_height_ft)
                .setItems(R.array.height_ft) { dialog, which ->
                    binding.editTextSelectHeightFt.setText(createAccountActivity.resources.getStringArray(R.array.height_ft)[which])
                }
            builder.show()
        }

        binding.editTextSelectHeightIn.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(createAccountActivity)
            builder.setTitle(R.string.select_height_in)
                .setItems(R.array.height_in) { dialog, which ->
                    binding.editTextSelectHeightIn.setText(createAccountActivity.resources.getStringArray(R.array.height_in)[which])
                }
            builder.show()
        }


        binding.editTextExpirationDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                createAccountActivity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val cal = Calendar.getInstance()
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH,monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    binding.editTextExpirationDate.setText(DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_MM_DD_YYYY_S,cal))

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
                    cal.set(Calendar.MONTH,monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    binding.editTextDateOfBirth.setText(DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_MM_DD_YYYY_S,cal))

                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis();
            datePickerDialog.show()
        }

        binding.textViewLblOperatorInfo.setOnClickListener {
            presenter.getEntityDefination(true)
        }
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

        ImageHelper.getInstance().with(this@OperatorInfoFragment).setCrop(true)
            .setMaxResultSize(2048, 2048).setIImage {
                mediaResult = it
                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
            }.start(ImageHelper.Option.IMAGE_PICK)
    }
    private fun openCamera() {
        ImageHelper.getInstance().with(this@OperatorInfoFragment).setCrop(true)
            .setMaxResultSize(2048, 2048).setIImage {
                mediaResult = it
                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
            }.start(ImageHelper.Option.IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ImageHelper.getInstance().onActivityResult(requestCode, resultCode, data)
    }

    private fun getUserFromInput(): User? {
        return presenter.saveOperator(
            binding.editTextFirstName.text.toString(),
            binding.editTextMiddleName.text.toString(),
            binding.editTextLastName.text.toString(),
            binding.editTextWeight.text.toString(),
            binding.editTextSelectHeightFt.text.toString(),
            binding.editTextSelectHeightIn.text.toString(),
            binding.editTextCellNumber.text.toString(),
            binding.editTextHomeNumber.text.toString(),
            binding.editTextLicenseNumber.text.toString(),
            binding.editTextExpirationDate.text.toString(),
            binding.editTextDateOfBirth.text.toString(),
            binding.editTextEmail.text.toString(),
            binding.checkBoxIfSame.isChecked,
            mediaResult
        )
    }
    private fun clearInput() {
        binding.editTextFirstName.setText("")
        binding.editTextMiddleName.setText("")
        binding.editTextLastName.setText("")
        binding.editTextCellNumber.setText("")
        binding.editTextHomeNumber.setText("")
        binding.editTextLicenseNumber.setText("")
        binding.editTextExpirationDate.setText("")
        binding.editTextDateOfBirth.setText("")
        binding.editTextEmail.setText("")
        mediaResult = null
    }

    private fun fillInputFromUser(user: User) {
        binding.editTextFirstName.setText(user.firstName)
        binding.editTextMiddleName.setText(user.middleName)
        binding.editTextLastName.setText(user.lastName)
        binding.editTextCellNumber.setText(user.cellNumber)
        binding.editTextHomeNumber.setText(user.homeNumber)
        binding.editTextLicenseNumber.setText(user.licenseNo)
        binding.editTextExpirationDate.setText(user.expiryDate)
        binding.editTextDateOfBirth.setText(user.dateOfBirth)
        binding.editTextEmail.setText(user.email)
        if(user.weight != 0)
            binding.editTextWeight.setText(user.weight.toString())

        if(user.heightFeet != 0)
            binding.editTextSelectHeightFt.setText(user.heightFeet.toString())

        binding.editTextSelectHeightIn.setText(user.heightInch.toString())

        //binding.checkBoxIfSame.isChecked = user.isDefault
        mediaResult = user.mediaResult
    }


    override fun showDocument(document: Document) {

        binding.editTextFirstName.setText(document.firstName)
        binding.editTextMiddleName.setText(document.middleName)
        binding.editTextLastName.setText(document.lastName)
        binding.editTextLicenseNumber.setText(document.documentNumber)
        binding.editTextExpirationDate.setText(DateFormatHelper.getFormattedDate(document.expiry, DateFormatHelper.FORMAT_YYYY_MM_DD, DateFormatHelper.FORMAT_MM_DD_YYYY_S))
        binding.editTextDateOfBirth.setText(DateFormatHelper.getFormattedDate(document.dob, DateFormatHelper.FORMAT_YYYY_MM_DD, DateFormatHelper.FORMAT_MM_DD_YYYY_S))

        if(document.height.isNotEmpty() && document.height.contains("-")) {
            val documentSplit = document.height.split("-")

            if(documentSplit.size == 2) {
                val heightFt = documentSplit[0].filter { it.isDigit() }
                if(heightFt.isNotEmpty()) {
                    binding.editTextSelectHeightFt.setText(heightFt)
                }

                val heightIn = documentSplit[1].filter { it.isDigit() }
                if(heightIn.isNotEmpty()) {
                    binding.editTextSelectHeightIn.setText(heightIn)
                }
            }
        }

    }

    override fun getDocumentFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showEntity(entity: String) {
        createAccountActivity.showContentDialogFragment(entity)
    }

    companion object {

        fun newInstance(editOperatorIndex: Int): OperatorInfoFragment {

            val bundle = Bundle()
            bundle.putInt("editOperatorIndex", editOperatorIndex)
            val operatorInfoFragment = OperatorInfoFragment()
            operatorInfoFragment.arguments = bundle
            return operatorInfoFragment
        }
    }
}