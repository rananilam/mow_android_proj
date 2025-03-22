package com.ui.home.deviceDetail

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.application.BaseApplication
import com.data.DataRepository
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowProductDetailBinding
import com.data.Injection
import com.data.remote.PestService
import com.data.model.device.Device
import com.data.model.device.DeviceInfo
import com.data.model.order.OrderData
import com.squareup.picasso.Picasso
import com.ui.home.HomeActivity
import com.ui.home.contentDialog.ContentDialogFragment
import com.ui.productDetails.RentalRatesAdapter
import com.ui.splash.SplashActivity
import com.ui.splash.SplashActivityPresenter
import com.util.safeLet
import com.util.showHtmlText

import iCode.android.BaseFragment
import iCode.utility.ScreenUtility

class DeviceDetailFragment : BaseFragment(), DeviceDetailFragmentContract.View {

    private val binding: FragMowProductDetailBinding by bindingLazy()
    private val rentalRatesAdapter: RentalRatesAdapter by lazy { RentalRatesAdapter() }
    override val layoutResId = R.layout.frag_mow_product_detail

    private lateinit var homeActivity: HomeActivity
    private lateinit var dataRepository: DataRepository
    private var presenter: DeviceDetailFragmentContract.Presenter? = null

    private var isDescriptionTab = true
    private var deviceId = 0
    private var device: Device? = null
    private var deviceInfo: DeviceInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeActivity = activity as HomeActivity
        dataRepository = Injection.provideDataRepository()
        presenter = DeviceDetailFragmentPresenter(this, dataRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.getInt("deviceId")?.let {
            deviceId = it
        }

        arguments?.getSerializable("device")?.let {
            device = it as Device
        }

        binding.recycleViewItems.adapter = rentalRatesAdapter


        binding.textViewLblDescription.setOnClickListener {
            isDescriptionTab = true
            setTabView()
        }

        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }
        binding.textViewLblRentalRates.setOnClickListener {
            isDescriptionTab = false
            setTabView()
        }

        binding.textViewAddToReservation.setOnClickListener {


            safeLet(device, deviceInfo) { device, deviceInfo ->
                homeActivity.showAddToReservation(targetFragment, targetRequestCode, device, deviceInfo)
            }
        }

        binding.frameLayoutDestination.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
        }

        binding.imageViewHome.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        //binding.textViewLocationCategory.text = dataRepository.locationCategory
        binding.textViewLocationName.text = dataRepository.locationName

        setTabView()
    }


    private fun setTabView() {

        binding.textViewLblDescription.setBackgroundResource(R.drawable.bg_mow_rounded_white_button)
        binding.textViewLblDescription.setTextColor(
            ContextCompat.getColor(
                binding.textViewLblDescription.context,
                (R.color.black)
            )
        )
        binding.textViewLblRentalRates.setBackgroundResource(R.drawable.bg_mow_rounded_white_button)
        binding.textViewLblRentalRates.setTextColor(
            ContextCompat.getColor(
                binding.textViewLblRentalRates.context,
                (R.color.black)
            )
        )



        if (isDescriptionTab) {

            binding.recycleViewItems.visibility = View.GONE
            binding.webViewLongDescription.visibility = View.VISIBLE
            binding.textViewLblPropertyDescription.text = getString(R.string.product_description)
            binding.textViewLblDescription.setBackgroundResource(R.drawable.bg_mow_rounded_blue_button)
            binding.textViewLblDescription.setTextColor(
                ContextCompat.getColor(
                    binding.textViewLblDescription.context,
                    (R.color.white)
                )
            )

            presenter?.getDeviceTypeByID(deviceId)
        } else {
            binding.recycleViewItems.visibility = View.VISIBLE
            binding.webViewLongDescription.visibility = View.GONE
            binding.textViewLblPropertyDescription.text = getString(R.string.rates_do_not_include_tax)
            binding.textViewLblRentalRates.setBackgroundResource(R.drawable.bg_mow_rounded_blue_button)
            binding.textViewLblRentalRates.setTextColor(
                ContextCompat.getColor(
                    binding.textViewLblRentalRates.context,
                    (R.color.white)
                )
            )

            presenter?.getRentalRatesByDeviceID(deviceId)
        }
    }

    override fun showDeviceInfo(deviceInfo: DeviceInfo) {

        this.deviceInfo = deviceInfo
        if (deviceInfo.itemImagePath.isNotEmpty())
            Picasso.get().load(PestService.getBaseURLForImage() + deviceInfo.itemImagePath)
                .resize(
                    ScreenUtility.dpToPx(512, BaseApplication.appContext),
                    ScreenUtility.dpToPx(256, BaseApplication.appContext)
                )
                .centerInside()
                .into(binding.imageViewProduct)

        binding.textViewName.text = deviceInfo.deviceTypeName


        binding.webViewShortDescription.showHtmlText(deviceInfo.itemShortDescription)
        binding.webViewLongDescription.showHtmlText(deviceInfo.itemFullDescription)

    }

    override fun getDeviceInfoFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showRentalRates(rateList: List<String>) {
        rentalRatesAdapter.submitList(rateList)
    }

    override fun getRentalRatesFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    companion object {


        fun newInstance(deviceId: Int, device: Device): DeviceDetailFragment {

            val bundle = Bundle()
            bundle.putInt("deviceId", deviceId)
            bundle.putSerializable("device", device)
            val productDetailsFragment = DeviceDetailFragment()
            productDetailsFragment.arguments = bundle
            return productDetailsFragment
        }
    }


}