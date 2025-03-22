package com.ui.home.orderView

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowOrderViewBinding
import com.data.model.order.OrderData
import com.ui.home.reservedItems.OrderAdapter
import com.ui.home.HomeActivity
import com.util.Utility
import com.util.Utility.Companion.roundUp
import iCode.android.BaseFragment

class OrderViewFragment : BaseFragment() {

    private val binding: FragMowOrderViewBinding by bindingLazy()
    private val reservedItemAdapter: OrderAdapter by lazy { OrderAdapter(true) }
    override val layoutResId = R.layout.frag_mow_order_view

    private lateinit var homeActivity: HomeActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeActivity = activity as HomeActivity


        binding.recycleViewItems.adapter = reservedItemAdapter
        reservedItemAdapter.submitList(OrderData.get())

        binding.textViewBack.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        setFooter()
    }

    private fun setFooter() {
        //$120.00(excl. tax)



        binding.textViewSubtotalAmount.text = String.format(getString(R.string.sub_total), Utility.convertUpTo2Decimal(OrderData.getSubTotal(true)))
        binding.textViewDeliveryFeeAmount.text = String.format(getString(R.string.sub_total), Utility.convertUpTo2Decimal(OrderData.getDeliveryFee()))
        binding.textViewtotalAmount.text = String.format(getString(R.string.total), Utility.convertUpTo2Decimal((OrderData.getSubTotal(true) + OrderData.getDeliveryFee() + OrderData.getTax())))
        //(Includes $2.65 Tax)
        binding.textViewIncludeText.text = String.format(getString(R.string.includes_tax), Utility.convertUpTo2Decimal(OrderData.getTax()))


        if(OrderData.isPromoCodeUsed()) {
            binding.textViewLblPromo.visibility = View.VISIBLE
            binding.textViewPromo.visibility = View.VISIBLE

            if(OrderData.getPromotionType()) {
                binding.textViewPromo.setText(String.format(getString(R.string.promo_value_off), Utility.convertUpTo2Decimal(OrderData.getPromotionFigureValue())))
            } else {
                binding.textViewPromo.text = "$${Utility.convertUpTo2Decimal(OrderData.getPromotionFigureValue())}% ${getString(R.string.off)}"
            }
        } else if(OrderData.isBonusBillingProfile()) {

            binding.textViewLblPromo.visibility = View.VISIBLE
            binding.textViewPromo.visibility = View.VISIBLE

            binding.textViewLblPromo.text = getString(R.string.price_adjustment_bonus_days)
            binding.textViewPromo.setText(String.format(getString(R.string.promo_inverse_value_off), Utility.convertUpTo2Decimal(OrderData.getPriceAdjustment())))
        }
    }

    companion object {
        fun newInstance() = OrderViewFragment()
    }
}