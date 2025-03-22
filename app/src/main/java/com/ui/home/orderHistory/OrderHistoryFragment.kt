package com.ui.home.orderHistory

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.data.Injection.provideDataRepository
import com.data.model.order.Order
import com.data.model.order.OrderData
import com.data.model.order.OrderH
import com.data.remote.response.notification.GetBadgeCountRs
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowOrderHistoryBinding
import com.ui.home.HomeActivity
import iCode.android.BaseFragment


class OrderHistoryFragment : BaseFragment(), OrderHistoryFragmentContract.View {

    private val binding: FragMowOrderHistoryBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_order_history

    private lateinit var homeActivity: HomeActivity
    private lateinit var presenter: OrderHistoryFragmentContract.Presenter

    //private lateinit var orderHistoryAdapter: OrderHistoryAdapter

    private var sortByOrderNumberIsDescending = true
    private var sortByArrivalDateIsDescending = false
    private var sortByDestinationIsDescending = false

    private var sortBy = "Sort by"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        presenter = OrderHistoryFragmentPresenter(this, provideDataRepository())

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.imageViewMenu.setOnClickListener {
            homeActivity.toggleDrawer()
        }

        binding.frameLayoutCart.setOnClickListener {
            homeActivity.showReservedItemFragment(false)
        }

        binding.frameLayoutNotification.setOnClickListener {
            homeActivity.showNotificationsFragment()
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    if (it.isNotEmpty())
                        presenter.search(it.toString())
                    else
                        presenter.getOrderHistory(false)
                }
            }
        })

        binding.textViewRiderRewardPoints.setOnClickListener {
            homeActivity.showRiderRewardPointDialogFragment(presenter.getRiderRewardsPoint())
        }

        binding.editTextSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (binding.editTextSearch.text.toString().trim().isNotEmpty())
                    presenter.search(binding.editTextSearch.text.toString().trim())
                else
                    presenter.getOrderHistory(false)

                val manager = homeActivity.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                manager
                    .hideSoftInputFromWindow(
                        view.windowToken, 0
                    )

                return@OnEditorActionListener true
            }
            false
        })

        binding.textViewSortOrder.setOnClickListener {

            closeKeyBoard(binding.textViewSortOrder)
            val builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(sortBy)
                .setItems(
                    R.array.sort_by_options
                ) { dialog, which ->

                    when (which) {
                        0 -> {
                            sortByOrderNumberIsDescending = !sortByOrderNumberIsDescending
                            presenter.sortBy(which, sortByOrderNumberIsDescending)

                            sortBy = "Sort by - Order number "+(if (sortByOrderNumberIsDescending) "▼" else "▲")
                        }
                        1 -> {
                            presenter.sortBy(which, sortByArrivalDateIsDescending)
                            sortByArrivalDateIsDescending = !sortByArrivalDateIsDescending

                            sortBy = "Sort by - Arrival date "+(if (sortByArrivalDateIsDescending) "▼" else "▲")
                        }
                        2 -> {
                            presenter.sortBy(which, sortByDestinationIsDescending)
                            sortByDestinationIsDescending = !sortByDestinationIsDescending

                            sortBy = "Sort by - Destination "+(if (sortByDestinationIsDescending) "▼" else "▲")
                        }
                        else -> {
                        }
                    }

                    // The 'which' argument contains the index position
                    // of the selected item
                }
            builder.show()
        }

        binding.rootConstraintLayout.setOnTouchListener { p0, p1 ->

            if(p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.rootConstraintLayout)
            }
            false
        };

        binding.recycleViewItems.setOnTouchListener { p0, p1 ->

            if(p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.recycleViewItems)
            }
            false
        };


        presenter.getBadgeCount()
//        presenter.getOrderHistory(true)
    }

    override fun showOrderHistory(orderList: List<OrderH>) {

        val orderHistoryAdapter = OrderHistoryAdapter(
            homeActivity,
            provideDataRepository().appConfig.timeZoneOffset
        )

        binding.recycleViewItems.adapter = orderHistoryAdapter

        orderHistoryAdapter.orderActionInterface = object :
            OrderHistoryAdapter.OrderActionInterface {
            override fun onExtendClicked(order: OrderH) {
                presenter.getOrderDetail(order.equipmentOrderID)
            }

            override fun onRefundTypeClicked(order: OrderH) {
               openRefundDialogFragment(order)
            }

            override fun onResendAttestationClicked(order: OrderH) {
                presenter.reSendAttestation(order.equipmentOrderID)
            }

            override fun onCompleteAttestationTwoClicked(order: OrderH) {
                //presenter.reSendAttestation(order.equipmentOrderID)
                homeActivity.showWebFragment(order.payorEleAttestationUrl);

            }

            override fun onCompleteAttestationOneClicked(order: OrderH) {
                //presenter.reSendAttestation(order.equipmentOrderID)
                homeActivity.showWebFragment(order.operatorEleAttestationUrl);

            }
        }

        orderHistoryAdapter.submitList(orderList)





       // binding.recycleViewItems.recycledViewPool.clear()
     //   orderHistoryAdapter.notifyDataSetChanged()

      //  orderHistoryAdapter.submitList(orderList)

     //   orderHistoryAdapter.notifyDataSetChanged()

        /*GlobalScope.launch(Dispatchers.Main) {
            delay(200)
            binding.recycleViewItems.layoutManager?.scrollToPosition(0)
        }*/
    }


    private fun openRefundDialogFragment(order: OrderH) {
        val builder = AlertDialog.Builder(homeActivity).setTitle(getString(R.string.alert))
            .setMessage(getString(R.string.alert_are_you_sure))
        builder.apply {
            setPositiveButton(
                getString(R.string.ok)
            ) { _, _ ->
                presenter.cancelOrderByOrderId(order)
            }
            setNegativeButton(
                getString(R.string.cancel)
            ) { _, _ ->

            }
        }
        builder.show()
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
    override fun getOrderHistoryFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showOrderDetail(order: Order?) {
        order?.let {
            homeActivity.showExtendOrderFragment(it)
        }
    }

    override fun getOrderDetailFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun reSendAttestationSuccessfully(response: String) {
        Toast.makeText(homeActivity, response, Toast.LENGTH_LONG).show()
    }

    override fun reSendAttestationFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun cancelOrderByOrderIdSuccessfully(response: String) {
        Toast.makeText(homeActivity, response, Toast.LENGTH_LONG).show()
        Log.e("Nilam 2222" ,response.toString())

        presenter.getOrderHistory(true)

       // orderHistoryAdapter.notifyDataSetChanged()
    }

    override fun cancelOrderByOrderIdFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showBadgeNotificationCount(getBadgeCount: GetBadgeCountRs) {
        if(getBadgeCount.badgeCount >= 1) {
            binding.textViewNumberOfNotifications.visibility = View.VISIBLE
            binding.textViewNumberOfNotifications.text = getBadgeCount.badgeCount.toString()
        } else {
            binding.textViewNumberOfNotifications.visibility = View.GONE
        }
        presenter.getOrderHistory(true)

    }

    override fun getBadgeCountFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    companion object {
        fun newInstance() = OrderHistoryFragment()
    }
}