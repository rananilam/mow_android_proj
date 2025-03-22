package com.ui.home.reportBug

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.data.DataRepository
import com.data.Injection
import com.data.model.user.User
import com.mow.cash.android.BuildConfig
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowReportBugBinding
import com.tbruyelle.rxpermissions3.RxPermissions
import com.util.Utility
import com.util.setHintMsg
import iCode.android.BaseFragment
import libraries.image.helper.ImageHelper
import libraries.image.helper.models.MediaResult
import okhttp3.MultipartBody
import java.io.File


class ReportBugFragment : BaseFragment(), ReportBugFragmentContract.View {
    private val binding: FragMowReportBugBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_report_bug

    private lateinit var reportBugActivity: ReportBugActivity
    private lateinit var dataRepository: DataRepository
    private lateinit var presenter: ReportBugFragmentContract.Presenter

    private var payor: User? = null


    private lateinit var rxPermissions: RxPermissions
    private var mediaResult: MediaResult? = null
    private val fileList = mutableListOf<MediaResult>()
    private val fileNameListAdapter: FileNameListAdapter by lazy { FileNameListAdapter() }
    private var selectedFile: MediaResult? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportBugActivity = activity as ReportBugActivity
        rxPermissions = RxPermissions(reportBugActivity)
        dataRepository = Injection.provideDataRepository()
        presenter = ReportBugFragmentPresenter(this, dataRepository)
        if (dataRepository.isLogin)
            presenter.getCustomer()


    }

    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textInputLayoutIssueDescription.setHintMsg(getString(R.string.hint_issue_description))
        binding.textInputLayoutAppVersion.setHintMsg(getString(R.string.hint_app_version))
        binding.textInputLayoutModelOfPhone.setHintMsg(getString(R.string.hint_model_of_phone))
        binding.textInputLayoutEmail.setHintMsg(getString(R.string.hint_reported_by_email_address))

        binding.editTextAppVersion.setText(BuildConfig.VERSION_NAME)
        binding.editTextAppVersion.isEnabled = false
        binding.editTextIssueDescription.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextModelOfPhone.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        binding.editTextIssueDescription.movementMethod =
            ScrollingMovementMethod() // Enable scrolling
        binding.editTextIssueDescription.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true) // Fix nested scrolling
            false
        }



        binding.flAttachment.setOnClickListener {
            openGallery()
        }

        binding.textViewCancel.setOnClickListener {
            reportBugActivity.finish()  // Closes SecondActivity and returns to FirstActivity
        }
        binding.textViewBack.setOnClickListener {
            reportBugActivity.finish()  // Closes SecondActivity and returns to FirstActivity

        }
        payor?.let {
            showCustomer(it)
        }

        binding.recycleViewFileList.adapter = fileNameListAdapter
        fileNameListAdapter.actionCallback = {
            selectedFile = it
        }
        fileNameListAdapter.actionDeleteCallback = {

            val builder = AlertDialog.Builder(reportBugActivity)
            builder.setMessage(getString(R.string.alert_are_you_sure_you_want_to_delete_file))
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    fileList.removeIf { media: MediaResult ->
                        Log.d("FileList", "media file: " + media.name)
                        Log.d("FileList", "it file: " + it.name)
                        val shouldRemove = media.name == it.name
                        if (shouldRemove) {
                            Log.d("FileList", "Removing file: " + media.name)
                        }
                        shouldRemove
                    }
                    fileNameListAdapter.notifyDataSetChanged()


                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
            builder.show()
        }

        binding.textViewSubmit.setOnClickListener {

            val documentData: Array<Array<String>> = fileList.map { media ->
                arrayOf(media.mimeType, media.base64String)
            }.toTypedArray()
            presenter.sentBugReport(
                binding.editTextIssueDescription.text.toString(),
                binding.editTextModelOfPhone.text.toString(),
                binding.editTextEmail.text.toString(),
                binding.editTextAppVersion.text.toString(),
                documentData
            )
        }
        if (dataRepository.isLogin) {
            binding.textViewRportedBy.visibility = View.VISIBLE
        } else {
            binding.textViewRportedBy.visibility = View.GONE
            binding.editTextEmail.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
            binding.editTextEmail.visibility = View.VISIBLE
            binding.textInputLayoutEmail.visibility = View.VISIBLE
        }


        fileNameListAdapter.submitList(fileList)
        fileNameListAdapter.notifyDataSetChanged()


    }

    // Call this function when the user clicks a button
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    private fun openGallery() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ requires specific media permissions
            arrayOf(
//                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            // Older versions require storage permissions
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        rxPermissions
            .requestEach(*permissions)
            .subscribe({ permission ->
                if (permission.granted) {
                    Log.e("Nilam", "open gallary start --->")

                    ImageHelper.getInstance().with(this@ReportBugFragment).setCrop(false)
                        .setMaxResultSize(1024, 1024).setIImage {


                            try {
                                mediaResult = it
                                println("File Path: ${it.path}")

                                mediaResult?.let { itt ->
                                    val filePath = itt.path
                                    println("File Path: $filePath")
                                    val imageFile = File(filePath)
                                    if (!imageFile.exists() || imageFile.length() == 0L) {
                                        println("Error: File does not exist or is empty.")

                                    } else {
                                        val isDuplicate =
                                            fileList.any { existingFile -> existingFile.path == filePath }
                                        if (isDuplicate) {


                                            println("Error: Duplicate file detected.")
                                            Toast.makeText(
                                                context,
                                                "Selected file already exits.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            val imageBase64 = Utility.encodeFileToBase64(imageFile)
                                            println("File imageBase64 ---> $imageBase64")

                                            itt.base64String = imageBase64
                                            itt.name = imageFile.name
                                            fileList.add(itt)
                                            Log.e("Nilam file name added", fileList.toString())
                                            fileNameListAdapter.notifyDataSetChanged()

                                        }
                                        fileNameListAdapter.notifyDataSetChanged()
                                    }
                                }


                            } catch (e: Exception) {
                                Log.e("Nilam Exception", e.toString())
                            }


                        }.start(ImageHelper.Option.OTHER)


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

    fun multipartToString(part: MultipartBody.Part): String {
        return "MultipartBody -> Name: ${part.headers}, File: ${part.body}"
    }


    @SuppressLint("SetTextI18n")
    override fun showCustomer(customer: User) {

        payor = customer
        binding.textViewRportedBy.text = getString(R.string.reported_by) + " " + customer.email
        // Submit the list, not a single item


    }

    override fun getCustomerFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun sentBugReportFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun sentBugReportSuccessfully(successMessage: String?) {

        Toast.makeText(
            context,
            successMessage,
            Toast.LENGTH_LONG
        ).show()
        reportBugActivity.finish()


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this@ReportBugFragment.setProgressBar(true)

        ImageHelper.getInstance().onActivityResult(requestCode, resultCode, data)
        this@ReportBugFragment.setProgressBar(false)

    }

    companion object {

        fun newInstance() = ReportBugFragment()

    }


}

