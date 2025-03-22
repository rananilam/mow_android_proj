package com.ui.home.notification

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.data.Injection
import com.data.model.notification.Notification
import com.data.model.order.OrderData
import com.ui.home.HomeActivity
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowNotificationBinding
import iCode.android.BaseFragment

class NotificationFragment : BaseFragment(), NotificationFragmentContract.View {

    private val binding: FragMowNotificationBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_notification
    private val notificationAdapter: NotificationAdapter by lazy { NotificationAdapter() }

    private lateinit var homeActivity: HomeActivity
    private lateinit var presenter: NotificationFragmentContract.Presenter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        presenter = NotificationFragmentPresenter(this, Injection.provideDataRepository())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleViewItems.adapter = notificationAdapter

        binding.imageViewMenu.setOnClickListener {
            homeActivity.toggleDrawer()
        }

        binding.frameLayoutCart.setOnClickListener {
            homeActivity.showReservedItemFragment(false)
        }
        presenter.getNotificationList()
        presenter.markNotificationAsRead()
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

    override fun showNotificationList(notificationList: List<Notification>) {
        if (notificationList.isNotEmpty()) {


            notificationAdapter.submitList(notificationList)
            binding.textViewLblNoNotifications.visibility = View.GONE
            binding.recycleViewItems.visibility = View.VISIBLE
        } else {
            binding.textViewLblNoNotifications.visibility = View.VISIBLE
            binding.recycleViewItems.visibility = View.GONE
        }
    }

    override fun getNotificationFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun markNotificationAsReadSuccessfully() {
    }

    override fun markNotificationAsReadFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    companion object {
        fun newInstance() = NotificationFragment()
    }


}