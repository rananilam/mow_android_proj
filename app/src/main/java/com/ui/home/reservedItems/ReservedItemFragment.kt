package com.ui.home.reservedItems

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowReservedItemBinding
import com.data.model.order.OrderData
import com.ui.home.HomeActivity
import com.ui.home.deviceDetail.DeviceDetailFragment
import com.util.Utility

import iCode.android.BaseFragment

class ReservedItemFragment : BaseFragment() {

    private val binding: FragMowReservedItemBinding by bindingLazy()
    private val orderAdapter: OrderAdapter by lazy { OrderAdapter(false) }
    override val layoutResId = R.layout.frag_mow_reserved_item

    private lateinit var homeActivity: HomeActivity
    private var isOpenFromCheckOut = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isOpenFromCheckOut = OrderData.isExtendOrder()
        /*arguments?.getBoolean("isOpenFromCheckOut")?.let {
            isOpenFromCheckOut = it
        }*/

        binding.recycleViewItems.adapter = orderAdapter

        orderAdapter.submitList(OrderData.get())
        orderAdapter.actionDeleteCallback = {

            OrderData.delete(it)
            orderAdapter.notifyDataSetChanged()
            setFooter()
        }


        orderAdapter.actionEditCallback = {
            if(isOpenFromCheckOut) {

                //homeActivity.supportFragmentManager.popBackStackImmediate("ReservedItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                homeActivity.onBackPressed()
            } else {
                homeActivity.showAddToReservation(it)
            }

        }

        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }

        binding.imageViewHome.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        binding.textViewAddAdditionalOrder.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        binding.textViewProceedToCheckout.setOnClickListener {
            homeActivity.showCheckOutFragment()
        }

        binding.textViewReturnMainMenu.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        setFooter()

    }

    @SuppressLint("StringFormatMatches")
    private fun setFooter() {
        //$120.00(excl. tax)


        if(OrderData.isCartEmpty()) {
            binding.textViewReturnMainMenu.visibility = View.VISIBLE
            binding.constraintView.visibility = View.GONE
            binding.textViewAddAdditionalOrder.visibility = View.GONE
            binding.textViewProceedToCheckout.visibility = View.GONE


        } else {

            binding.textViewReturnMainMenu.visibility = View.GONE
            binding.constraintView.visibility = View.VISIBLE
            binding.textViewAddAdditionalOrder.visibility = View.VISIBLE
            binding.textViewProceedToCheckout.visibility = View.VISIBLE

            binding.textViewSubTotal.text = String.format(getString(R.string.sub_total,Utility.convertUpTo2Decimal(OrderData.getSubTotal(false))))
            binding.textViewDeliveryFees.text = String.format(getString(R.string.sub_total,Utility.convertUpTo2Decimal(OrderData.getDeliveryFee())))
            binding.textViewTotal.text = String.format(getString(R.string.total,Utility.convertUpTo2Decimal((OrderData.getSubTotal(false) + OrderData.getDeliveryFee() + OrderData.getTax()))))
            //(Includes $2.65 Tax)
            binding.textViewIncludeText.text = String.format(getString(R.string.includes_tax,Utility.convertUpTo2Decimal(OrderData.getTax())))

            if(isOpenFromCheckOut)
                binding.textViewAddAdditionalOrder.visibility = View.GONE
            else
                binding.textViewAddAdditionalOrder.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(isOpenFromCheckOut: Boolean): ReservedItemFragment {

            val bundle = Bundle()
            bundle.putBoolean("isOpenFromCheckOut", isOpenFromCheckOut)
            val reservedItemFragment = ReservedItemFragment()
            reservedItemFragment.arguments = bundle
            return reservedItemFragment
        }
    }
}