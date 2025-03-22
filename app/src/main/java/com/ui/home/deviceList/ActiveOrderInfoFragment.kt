package com.ui.home.deviceList

//import com.tbruyelle.rxpermissions2.RxPermissions
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.application.BaseApplication
import com.data.Injection
import com.data.model.order.Order
import com.data.model.order.OrderH
import com.data.remote.PestService
import com.mow.cash.android.R
import com.mow.cash.android.databinding.ItemMowActiveOrderBinding
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions3.RxPermissions
import com.ui.home.HomeActivity
import com.util.Utility
import iCode.android.BaseFragment
import iCode.utility.DateFormatHelper
import iCode.utility.ScreenUtility
import libraries.image.helper.ImageHelper


class ActiveOrderInfoFragment : BaseFragment(), ActiveOrderInfoFragmentContract.View{

    private val binding: ItemMowActiveOrderBinding by bindingLazy()
    override val layoutResId = R.layout.item_mow_active_order


    private lateinit var homeActivity: HomeActivity
    private lateinit var presenter: ActiveOrderInfoFragmentContract.Presenter
    private lateinit var order: OrderH

    private lateinit var rxPermissions: RxPermissions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeActivity = activity as HomeActivity
        rxPermissions = RxPermissions(homeActivity)
        val dataRepository = Injection.provideDataRepository()
        presenter = ActiveOrderInfoFragmentPresenter(this, dataRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        if(arguments!=null && arguments!!.containsKey("order")) {
        if(arguments!=null && requireArguments().containsKey("order")) {

            arguments?.getSerializable("order")?.let {
                order = it as OrderH
            }

        } else {
            homeActivity.onBackPressed()
        }


        if (order.deviceImagePath.isNotEmpty()) {
            Picasso.get().load(PestService.getBaseURLForImage() + order.deviceImagePath)
                .resize(
                    ScreenUtility.dpToPx(56, BaseApplication.appContext),
                    ScreenUtility.dpToPx(56, BaseApplication.appContext)
                )
                .centerInside()
                .into(binding.imageViewProduct)
        }

        else {
            Picasso.get().load(R.drawable.bg_mow_rounded_corner_rectangle_white)
                .into(binding.imageViewProduct)
        }

        binding.textViewTimeRemaining.text = order.remainingTime
        binding.textViewName.text = order.deviceTypeName

        if (order.arrivalDate.isNotEmpty() && order.departureDate.isNotEmpty()) {

            binding.textViewDate.text = DateFormatHelper.getFormattedDate(order.arrivalDate,
                    DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A,
                    DateFormatHelper.FORMAT_MM_DD_YYYY_S) + " to " +
                        DateFormatHelper.getFormattedDate(order.departureDate,
                            DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A,
                            DateFormatHelper.FORMAT_MM_DD_YYYY_S)

        }
        binding.textViewOrderNo.text = String.format(getString(R.string.order_number), order.formatedOrderID)
        binding.textViewLocation.text = order.locationName

        binding.textViewPrice.text = String.format(getString(R.string.price), Utility.convertUpTo2Decimal(order.profilePrice))
        binding.textViewDays.text = order.rateSelected

        binding.textViewExtendMyRental.setOnClickListener {
            presenter.getOrderDetail(order.orderID)
        }

        binding.textViewReturnMyRental.setOnClickListener {
            presenter.checkForReturnDeviceImage(order.orderID)
        }

        binding.textView19.setOnClickListener {
            homeActivity.showEnterEquipmentDialogFragment(order.orderID, this@ActiveOrderInfoFragment, 852)
        }

    }

    override fun showOrderDetail(order: Order?) {

        order?.let {
            homeActivity.showExtendOrderFragment(it)
        }
    }

    override fun getOrderDetailFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    @SuppressLint("CheckResult")
    override fun isReturnDeviceImage(isReturn: Boolean) {
        if(!isReturn) {
            rxPermissions
                .requestEach(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
                .subscribe({ permission ->
                    if (permission.granted) {
                        Log.d("Permission","Granted permisison");
                          ImageHelper.getInstance().with(this@ActiveOrderInfoFragment).setCrop(true)
                            .setMaxResultSize(1024, 1024).setIImage {
                                homeActivity.showReturnRentalDeviceFragment(order.orderID, it)
                            }.start(ImageHelper.Option.IMAGE_CAPTURE)
                    } else {
                        // Permission denied with 'Never Ask Again'
                        Log.d("Permission", permission.name + " permission denied permanently")
                    }
                }
                ) { Log.e("Permission", "Error requesting permissions") }

//            rxPermissions
//                .request(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                .subscribe { granted: Boolean ->
//                    if (granted) {
//                        ImageHelper.getInstance().with(this@ActiveOrderInfoFragment).setCrop(true)
//                            .setMaxResultSize(1024, 1024).setIImage {
//
//                                homeActivity.showReturnRentalDeviceFragment(order.orderID, it)
//
//                            }.start(ImageHelper.Option.IMAGE_CAPTURE)
//                    }
//                }
        } else {
            showToastMessage(getString(R.string.validation_return_rental_device_link_is_not_available_for_this_order))
        }
    }

    override fun checkForReturnDeviceImageFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ImageHelper.getInstance().onActivityResult(requestCode, resultCode, data)
    }

    companion object {

        fun newInstance(order: OrderH): ActiveOrderInfoFragment {

            val bundle = Bundle()
            bundle.putSerializable("order", order)
            val activeOrderInfoFragment = ActiveOrderInfoFragment()
            activeOrderInfoFragment.arguments = bundle
            return activeOrderInfoFragment
        }

    }
}