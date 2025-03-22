package com.ui.home.activeOrder

//import com.tbruyelle.rxpermissions2.RxPermissions
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import com.data.DataRepository
import com.data.Injection
import com.data.model.order.Order
import com.data.model.order.OrderData
import com.data.model.order.OrderH
import com.data.model.user.Config
import com.data.objectforupdate.DialogUtils
import com.data.objectforupdate.UpdateAppContract
import com.data.objectforupdate.updateAppPresenter
import com.data.remote.response.notification.GetBadgeCountRs
import com.mow.cash.android.BuildConfig
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowActiveOrderBinding
import com.tbruyelle.rxpermissions3.RxPermissions
import com.ui.home.HomeActivity
import iCode.android.BaseFragment
import libraries.image.helper.ImageHelper

class ActiveOrdersFragment : BaseFragment(), ActiveOrdersFragmentContract.View , UpdateAppContract.View {

    private val binding: FragMowActiveOrderBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_active_order
    private val activeOrderAdapter: ActiveOrderAdapter by lazy { ActiveOrderAdapter() }

    private lateinit var homeActivity: HomeActivity
    private lateinit var presenter: ActiveOrdersFragmentContract.Presenter
    private lateinit var rxPermissions: RxPermissions
    private var selectedOrder: OrderH? = null
    private lateinit var presenter_updateApp: UpdateAppContract.Presenter

    private lateinit var timer: CountDownTimer
    private lateinit var dataRepository: DataRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        rxPermissions = RxPermissions(homeActivity)
        dataRepository = Injection.provideDataRepository()

        presenter_updateApp = updateAppPresenter(this,dataRepository)
        presenter = ActiveOrdersFragmentPresenter(this,dataRepository)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleViewOptions.adapter = activeOrderAdapter

        binding.imageViewMenu.setOnClickListener {
            homeActivity.toggleDrawer()
        }

        binding.frameLayoutCart.setOnClickListener {
            homeActivity.showReservedItemFragment(false)
        }

        binding.frameLayoutNotification.setOnClickListener {
            homeActivity.showNotificationsFragment()
        }

        binding.textViewSelectADestinationAndPlaceOrder.setOnClickListener {
            homeActivity.showHomeFragment(isShowDestinationList = true)
        }

        activeOrderAdapter.setOrderListener(object :
            ActiveOrderAdapter.OrderListener {
//            @SuppressLint("CheckResult")
            override fun onReturnClick(order: OrderH) {
                selectedOrder = order
                presenter.checkForReturnDeviceImage(order.orderID)
            }

            override fun onExtendClick(order: OrderH) {
                selectedOrder = order
                presenter.getOrderDetail(order.orderID)
            }

            override fun onEquipmentClick(orderId: Int) {
                homeActivity.showEnterEquipmentDialogFragment(orderId, this@ActiveOrdersFragment,852)
            }
        })




            timer = object: CountDownTimer(Long.MAX_VALUE, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    presenter.updateTimer()
                }

                override fun onFinish() {
                }
            }

        // nilam add below line for force update app
        presenter_updateApp.updateAppConfig()



    }

    override fun onResume() {
        super.onResume()
        timer.start()
        if(OrderData.isCartEmpty()) {
            binding.frameLayoutCart.visibility = View.GONE
        } else {
            binding.frameLayoutCart.visibility = View.VISIBLE
            binding.textViewNumberOfItems.text = OrderData.getCartSize().toString()
        }

        
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }
    override fun showActiveOrders(activeOrders: List<OrderH>) {
        if (activeOrders.isNotEmpty()) {


            activeOrderAdapter.submitList(activeOrders)
            activeOrderAdapter.notifyDataSetChanged()
            binding.linearLayoutNoActiveOrders.visibility = View.GONE
            binding.recycleViewOptions.visibility = View.VISIBLE
        } else {
            binding.linearLayoutNoActiveOrders.visibility = View.VISIBLE
            binding.recycleViewOptions.visibility = View.GONE
        }
    }

    override fun getActiveOrdersFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showOrderDetail(order: Order?) {
        // Nilam make 0 price in extend my order page
        order?.price =0F
        Log.e("Nilam","price make zero here in ActiveOrdersFragment -- > ${order?.price}")

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
                        ImageHelper.getInstance().with(this@ActiveOrdersFragment).setCrop(true)
                            .setMaxResultSize(1024, 1024).setIImage {mediaResult ->
                                selectedOrder?.let {
                                    homeActivity.showReturnRentalDeviceFragment(it.orderID, mediaResult)
                                }
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
//                        ImageHelper.getInstance().with(this@ActiveOrdersFragment).setCrop(true)
//                            .setMaxResultSize(1024, 1024).setIImage {mediaResult ->
//
//                                selectedOrder?.let {
//                                    homeActivity.showReturnRentalDeviceFragment(it.orderID, mediaResult)
//                                }
//
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

    @SuppressLint("SetTextI18n")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == 852 && resultCode == Activity.RESULT_OK) {
            presenter.getActiveOrders()
        } else {
            ImageHelper.getInstance().onActivityResult(requestCode, resultCode, data)
        }

    }


    companion object {
        fun newInstance() = ActiveOrdersFragment()
    }

    override fun getAppConfigSuccessfully(config: Config?) {
        config?.let {
            val packageManager = homeActivity.packageManager
            val packageInfo = packageManager.getPackageInfo(homeActivity.packageName, 0)
            val versionNameFirst = (packageInfo.versionName)?.toFloat()
            val versionName = BuildConfig.VERSION_NAME
            Log.d("AppVersion", "Version Name: ActiveOrderFragment $versionName")


            Log.e("Nilam","versionNameFirst -----> ActiveOrdersFragment $versionNameFirst")

            if(it.minimumAndroidVersion <= versionNameFirst!!) {

                presenter.getBadgeCount()
                presenter.getActiveOrders()

            }
            else {
            //    DialogUtils.showAlertDialog(context = requireActivity(),)
                DialogUtils.showAlertDialog(context = homeActivity,homeActivity)

            }
        }
    }

    override fun getAppConfigFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }


    /* override fun showUpdateApp(updateData: UpdateAppRs) {

         updateData.let {
             Log.e("Nilam","androidAppVersionMessage----->>>>"+it.androidAppVersionMessage)
             Log.e("Nilam","androidAppVersionRequired----->>>>"+it.androidAppVersionRequired)
             if(it.androidAppVersionRequired.toFloat() > 1.5)
                 DialogUtils.showAlertDialog(context = requireActivity(),)

         }


     }*/
}