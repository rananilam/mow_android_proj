package com.ui.home.addOperator

//import io.reactivex.disposables.Disposable
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentManager
import com.data.DataRepository
import com.data.Injection
import com.data.model.idanalyzer.Document
import com.data.model.user.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowAddOperatorBinding
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import com.ui.home.HomeActivity
import com.util.setHintMsg
import iCode.android.BaseFragment
import iCode.utility.DateFormatHelper
import iCode.utility.PhoneNumberTextWatcher
import libraries.image.helper.ImageHelper
import libraries.image.helper.models.MediaResult
import java.util.Calendar


class AddOperatorFragment : BaseFragment(), AddOperatorFragmentContract.View {


    private val binding: FragMowAddOperatorBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_add_operator

    private lateinit var homeActivity: HomeActivity
    private lateinit var dataRepository: DataRepository
    private lateinit var presenter: AddOperatorFragmentContract.Presenter

    private var mediaResult: MediaResult? = null

    private lateinit var rxPermissions: RxPermissions

    private lateinit var payor: User
    private var operator: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        rxPermissions = RxPermissions(homeActivity)
        dataRepository = Injection.provideDataRepository()
        presenter = AddOperatorFragmentPresenter(this, dataRepository)
    }

    @SuppressLint("CheckResult", "ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.getSerializable("payor")?.let {
            payor = it as User
        }

        arguments?.getSerializable("operator")?.let {
            operator = it as User
        }

        binding.textInputLayoutFirstName.setHintMsg(getString(R.string.hint_first_name))
        binding.textInputLayoutLastName.setHintMsg(getString(R.string.hint_last_name))
        binding.textInputLayoutCellNumber.setHintMsg(getString(R.string.hint_cell_number))
        binding.textInputLayoutEmail.setHintMsg(getString(R.string.hint_email_address))
        binding.textInputLayoutSelectHeightFt.setHintMsg(getString(R.string.hint_height_ft))
        binding.textInputLayoutSelectHeightIn.setHintMsg(getString(R.string.hint_height_in))
        binding.textInputLayoutWeight.setHintMsg(getString(R.string.hint_weight_lbs))
        binding.textViewScan.setHint(HtmlCompat.fromHtml(getString(R.string.hint_scan_front_of_operators_id_now), HtmlCompat.FROM_HTML_MODE_LEGACY))


        binding.textInputLayoutExpirationDate.setHintMsg(getString(R.string.hint_expiration_date_))
        binding.textInputLayoutDateOfBirth.setHintMsg(getString(R.string.hint_date_of_birth_))



        binding.editTextFirstName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextMiddleName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLastName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextWeight.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
//        binding.editTextCellNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
//        binding.editTextHomeNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLicenseNumber.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextEmail.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        binding.editTextCellNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextCellNumber))
        binding.editTextHomeNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextHomeNumber))


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

        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }

        binding.imageViewHome.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
      /*  binding.textViewScan.setOnClickListener {
            Log.d("Hello World","calickkkkk");
//            Log.d("Hello World", permission.name + " permission granted")

            rxPermissions
                .requestEach(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
                .subscribe({ permission ->
                    if (permission.granted) {
                        showBottomSheetDialog(permission);
                        Log.d("Permission","Granted permisison");

        ImageHelper.getInstance().with(this@AddOperatorFragment).setCrop(true)
                           .setMaxResultSize(2048, 2048).setIImage {
                               mediaResult = it
                               presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
                           }.start(ImageHelper.Option.IMAGE_CAPTURE)
                    } else {
                        // Permission denied with 'Never Ask Again'
                        Log.d("Permission", permission.name + " permission denied permanently")
                    }
                }
                ) { Log.e("Permission", "Error requesting permissions") }


//
//            rxPermissions
//                .request(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_MEDIA_IMAGES
//                )
//                .subscribe { granted: Boolean ->
//                    if (granted) {
//                        Log.d("Hello World","Granted permisison");
//                        ImageHelper.getInstance().with(this@AddOperatorFragment).setCrop(true)
//                            .setMaxResultSize(2048, 2048).setIImage {
//                                mediaResult = it
//                                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
//                            }.start(ImageHelper.Option.IMAGE_CAPTURE)
//                    }
//                }

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
                       /* ImageHelper.getInstance().with(this@AddOperatorFragment).setCrop(true)
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


        binding.checkBoxIfSame.setOnCheckedChangeListener { p0, isChecked ->
            binding.checkBoxIfSame.isChecked = isChecked

            if(binding.checkBoxIfSame.isChecked)
                copyPayorInfo()
            else
                clearInput()
        }
        binding.textViewSubmit.setOnClickListener {

            presenter.addEditNewOperator(
                if(operator == null) 0 else operator!!.operatorID,
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


        binding.editTextSelectHeightFt.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_height_ft)
                .setItems(R.array.height_ft) { dialog, which ->
                    binding.editTextSelectHeightFt.setText(homeActivity.resources.getStringArray(R.array.height_ft)[which])
                }
            builder.show()
        }

        binding.editTextSelectHeightIn.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_height_in)
                .setItems(R.array.height_in) { dialog, which ->
                    binding.editTextSelectHeightIn.setText(homeActivity.resources.getStringArray(R.array.height_in)[which])
                }
            builder.show()
        }


        binding.editTextExpirationDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                homeActivity,
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
                homeActivity,
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
        binding.editTextCellNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextCellNumber))
        binding.editTextHomeNumber.addTextChangedListener(PhoneNumberTextWatcher(binding.editTextHomeNumber))

        showOperator()
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

        ImageHelper.getInstance().with(this@AddOperatorFragment).setCrop(true)
            .setMaxResultSize(2048, 2048).setIImage {
                mediaResult = it
                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
            }.start(ImageHelper.Option.IMAGE_PICK)
    }
    private fun openCamera() {
        ImageHelper.getInstance().with(this@AddOperatorFragment).setCrop(true)
            .setMaxResultSize(2048, 2048).setIImage {
                mediaResult = it
                presenter.getDocument(it, dataRepository.appConfig.idanalyzerApiKey)
            }.start(ImageHelper.Option.IMAGE_CAPTURE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ImageHelper.getInstance().onActivityResult(requestCode, resultCode, data)
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

    override fun addEditNewOperatorSuccessfully(operator: User?) {
        operator?.let {
            val intent = Intent()
            intent.putExtra("operator",operator)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        }
        homeActivity.onBackPressed()
    }

    override fun addEditNewOperatorFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    private fun showOperator() {
        operator?.let {


            binding.editTextFirstName.setText(it.firstName)
            binding.editTextMiddleName.setText(it.middleName)
            binding.editTextLastName.setText(it.lastName)
            binding.editTextWeight.setText(it.weight.toString())
            binding.editTextSelectHeightFt.setText(it.heightFeet.toString())
            binding.editTextSelectHeightIn.setText(it.heightInch.toString())
            binding.editTextCellNumber.setText(it.cellNumber)
            binding.editTextHomeNumber.setText(it.homeNumber)
            binding.editTextLicenseNumber.setText(it.licenseNo)
            binding.editTextEmail.setText(it.email)
            binding.checkBoxIfSame.isChecked = it.isDefault


            if(it.expiryDate.isNotEmpty()) {

                binding.editTextExpirationDate.setText(DateFormatHelper.getFormattedDate(
                    it.expiryDate,
                    DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A,
                    DateFormatHelper.FORMAT_MM_DD_YYYY_S
                ))
            }

            if(it.dateOfBirth.isNotEmpty()) {

                binding.editTextDateOfBirth.setText(DateFormatHelper.getFormattedDate(
                    it.dateOfBirth,
                    DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A,
                    DateFormatHelper.FORMAT_MM_DD_YYYY_S
                ))
            }

        }
    }

    private fun copyPayorInfo() {

        binding.editTextFirstName.setText(payor.firstName)
        binding.editTextMiddleName.setText(payor.middleName)
        binding.editTextLastName.setText(payor.lastName)
        binding.editTextCellNumber.setText(payor.cellNumber)
        binding.editTextHomeNumber.setText(payor.homeNumber)
        binding.editTextLicenseNumber.setText(payor.licenseNo)
        binding.editTextEmail.setText(payor.email)
       // binding.checkBoxIfSame.isChecked = payor.isDefault


        if(payor.expiryDate.isNotEmpty()) {

            binding.editTextExpirationDate.setText(DateFormatHelper.getFormattedDate(
                payor.expiryDate,
                DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A,
                DateFormatHelper.FORMAT_MM_DD_YYYY_S
            ))
        }

        if(payor.dateOfBirth.isNotEmpty()) {

            binding.editTextDateOfBirth.setText(DateFormatHelper.getFormattedDate(
                payor.dateOfBirth,
                DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A,
                DateFormatHelper.FORMAT_MM_DD_YYYY_S
            ))
        }
    }

    private fun clearInput() {
        binding.editTextFirstName.setText("")
        binding.editTextMiddleName.setText("")
        binding.editTextLastName.setText("")
        binding.editTextCellNumber.setText("")
        binding.editTextHomeNumber.setText("")
        binding.editTextLicenseNumber.setText("")
        binding.editTextEmail.setText("")
        binding.editTextExpirationDate.setText("")
        binding.editTextDateOfBirth.setText("")
    }

    override fun showEntity(entity: String) {
        homeActivity.showContentDialogFragment(entity)
    }
    companion object {

        fun newInstance(payor: User, operator: User?): AddOperatorFragment {

            val bundle = Bundle()
            bundle.putSerializable("payor", payor)
            operator?.let {
                bundle.putSerializable("operator", it)
            }

            val addOperatorFragment = AddOperatorFragment()
            addOperatorFragment.arguments = bundle
            return addOperatorFragment
        }
    }
}