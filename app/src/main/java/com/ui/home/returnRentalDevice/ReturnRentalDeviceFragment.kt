package com.ui.home.returnRentalDevice

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowReturnRentalDeviceBinding
import com.data.Injection
import com.squareup.picasso.Picasso
//import com.tbruyelle.rxpermissions2.RxPermissions
import com.ui.home.HomeActivity
import com.ui.home.deviceList.ActiveOrderInfoFragmentContract
import com.ui.home.deviceList.ActiveOrderInfoFragmentPresenter
import com.ui.login.LoginActivity

import iCode.android.BaseFragment
import libraries.image.helper.models.MediaResult
import java.io.File

class ReturnRentalDeviceFragment : BaseFragment(), ReturnRentalDeviceFragmentContract.View {

    private val binding: FragMowReturnRentalDeviceBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_return_rental_device

    private lateinit var homeActivity: HomeActivity
    private lateinit var presenter: ReturnRentalDeviceFragmentContract.Presenter

    var orderId = 0
    lateinit var mediaResult: MediaResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeActivity = activity as HomeActivity
        val dataRepository = Injection.provideDataRepository()
        presenter = ReturnRentalDeviceFragmentPresenter(this, dataRepository)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.containsKey("mediaResult")?.let {

            if(it) {
                mediaResult = arguments?.getSerializable("mediaResult") as MediaResult
            } else {
                homeActivity.onBackPressed()
            }
        }

        arguments?.containsKey("orderId")?.let {

            if(it) {
                orderId = arguments?.getInt("orderId",0)!!
            } else {
                homeActivity.onBackPressed()
            }
        }

        Picasso.get().load(Uri.fromFile(File(mediaResult.path)))
            .into(binding.imageView)

        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }

        binding.imageViewHome.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        binding.textViewSend.setOnClickListener {
            presenter.imageUploadedForReturnDevice(orderId, mediaResult)
        }


    }

    override fun imageUploadedForReturnDeviceSuccessfully() {

        val builder = AlertDialog.Builder(homeActivity).setTitle(getString(R.string.alert))
            .setMessage(getString(R.string.awesome_file_uploaded_successfully))
        builder.apply {
            setCancelable(false)
            setPositiveButton(
                getString(R.string.ok)
            ) { _, _ ->
                homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }

        builder.setOnCancelListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        builder.show()
    }

    override fun imageUploadedForReturnDeviceFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    companion object {

        fun newInstance(orderId: Int, mediaResult: MediaResult): ReturnRentalDeviceFragment {

            val bundle = Bundle()
            bundle.putInt("orderId", orderId)
            bundle.putSerializable("mediaResult", mediaResult)
            val returnRentalDeviceFragment = ReturnRentalDeviceFragment()
            returnRentalDeviceFragment.arguments = bundle
            return returnRentalDeviceFragment
        }
    }
}