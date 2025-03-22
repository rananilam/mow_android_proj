package com.ui.home.deviceList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.data.Injection
import com.data.model.destination.Location
import com.data.model.device.Device
import com.data.model.order.OrderData
import com.data.model.order.OrderH
import com.data.remote.response.notification.GetBadgeCountRs
import com.ui.home.HomeActivity
import com.ui.home.destination.DestinationFragment
import com.ui.home.deviceDetail.DeviceDetailFragment
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragmentMowDeviceListBinding
import iCode.android.BaseFragment

private const val DESTINATION_REQUEST_CODE = 123
private const val DEVICE_DETAIL_AND_ADD_TO_RESERVATION_REQUEST_CODE = 321

class DeviceListFragment : BaseFragment(), DeviceListFragmentContract.View {


    private val binding: FragmentMowDeviceListBinding by bindingLazy()
    private val scheduledAppointmentAdapter: DeviceAdapter by lazy { DeviceAdapter() }
    override val layoutResId = R.layout.fragment_mow_device_list

    //var arrayList: ArrayList<Int>? = null
    //private var dot: Array<ImageView?>? = null

    private lateinit var homeActivity: HomeActivity
    private lateinit var presenter: DeviceListFragmentContract.Presenter

    private val dataRepository = Injection.provideDataRepository()

    private var isShowDestinationList = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeActivity = activity as HomeActivity
        presenter = DeviceListFragmentPresenter(this, dataRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getBoolean("isShowDestinationList")?.let {
            isShowDestinationList = it

            arguments?.putBoolean("isShowDestinationList",false)
        }

        binding.recycleViewItems.adapter = scheduledAppointmentAdapter

        binding.imageViewMenu.setOnClickListener {
            homeActivity.toggleDrawer()
        }
        binding.frameLayoutDestination.setOnClickListener {
            homeActivity.showDestinationListFragment(this,DESTINATION_REQUEST_CODE)
        }

        binding.frameLayoutCart.setOnClickListener {
            homeActivity.showReservedItemFragment(false)
        }

        binding.frameLayoutNotification.setOnClickListener {
            homeActivity.showNotificationsFragment()
        }
        scheduledAppointmentAdapter.actionCallback = {
            homeActivity.showProductDetailsFragment(this,
                DEVICE_DETAIL_AND_ADD_TO_RESERVATION_REQUEST_CODE,
                it.deviceTypeId, it)
        }

        if(isShowDestinationList) {
            homeActivity.showDestinationListFragment(this,DESTINATION_REQUEST_CODE)
        }

        if(dataRepository.locationId != 0)
            presenter.getDeviceListByLocationId()
        /*else
            homeActivity.showDestinationFragment()*/

        presenter.getBadgeCount()
    }


    override fun onResume() {
        super.onResume()
        //binding.textViewLocationCategory.text = dataRepository.locationCategory
        binding.textViewLocationName.text = dataRepository.locationName

        if(OrderData.isCartEmpty()) {
            binding.frameLayoutCart.visibility = View.GONE
        } else {
            binding.frameLayoutCart.visibility = View.VISIBLE
            binding.textViewNumberOfItems.text = OrderData.getCartSize().toString()
        }
    }
    override fun showDeviceList(deviceList: List<Device>) {
        scheduledAppointmentAdapter.submitList(deviceList)
    }

    override fun getDeviceListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showActiveOrders(activeOrders: List<OrderH>) {
    }

    override fun getActiveOrdersFail(errorMessage: String?) {
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {

            if(requestCode == DESTINATION_REQUEST_CODE) {
                binding.textViewLocationName.text = dataRepository.locationName
                presenter.getDeviceListByLocationId()
            } else if(requestCode == DEVICE_DETAIL_AND_ADD_TO_RESERVATION_REQUEST_CODE) {
                homeActivity.showDestinationListFragment(this,DESTINATION_REQUEST_CODE)
            }
        }

    }
    companion object {

        fun newInstance(isShowDestinationList: Boolean): DeviceListFragment {

            val bundle = Bundle()
            bundle.putBoolean("isShowDestinationList", isShowDestinationList)
            val deviceListFragment = DeviceListFragment()
            deviceListFragment.arguments = bundle
            return deviceListFragment
        }

    }


}