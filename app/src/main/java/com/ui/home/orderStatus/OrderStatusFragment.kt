package com.ui.main.chats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.data.remote.response.order.SaveOrderListRs
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowOrderStatusBinding
import com.ui.home.HomeActivity
import com.util.Utility
import iCode.android.BaseFragment

class OrderStatusFragment : BaseFragment() {

    private val binding: FragMowOrderStatusBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_order_status

    private lateinit var homeActivity: HomeActivity

    private var saveOrderList: SaveOrderListRs? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        arguments?.getSerializable("saveOrderList")?.let {
            saveOrderList = it as SaveOrderListRs
        }

        binding.textViewBack.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            homeActivity.showActiveOrderFragment()
        }

        binding.imageViewHome.setOnClickListener {

            homeActivity.supportFragmentManager.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            homeActivity.showActiveOrderFragment()
        }
        saveOrderList?.let {

            if (it.result.isStatus) {

                binding.textViewOrderNumber.text =
                    String.format(getString(R.string.order_number), it.equipmentOrderIDWithFormat)
                binding.textViewDate.text = it.date
                binding.textViewTotal.text =
                    String.format(getString(R.string.total_), Utility.convertUpTo2Decimal(it.total))
                binding.textViewPaymentMethod.text =
                    String.format(getString(R.string.payment_method), it.paymentMethod)
                binding.textViewViewOrder.setOnClickListener {

                    homeActivity.supportFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    homeActivity.showActiveOrderFragment()
                }

            } else {

                binding.textViewLblOrderReceived.visibility = View.GONE
                binding.textViewOrderNumber.visibility = View.GONE
                binding.textViewDate.visibility = View.GONE
                binding.linearLayoutPaymentAndMode.visibility = View.GONE

                binding.imageViewOrderStatus.setImageResource(R.drawable.ic_mow_fail)
                // Nilam set error message when error occured while placing order
                //binding.textViewMessage.text = getString(R.string.something_wrong)
                 binding.textViewMessage.text = it.result.message;
                binding.textViewViewOrder.text = getString(R.string.return_to_main_menu)

                binding.textViewViewOrder.setOnClickListener {
                    homeActivity.supportFragmentManager.popBackStackImmediate(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
            }
        }


    }

    companion object {

        fun newInstance(saveOrderList: SaveOrderListRs): OrderStatusFragment {

            val bundle = Bundle()
            bundle.putSerializable("saveOrderList", saveOrderList)

            val thankYouFragment = OrderStatusFragment()
            thankYouFragment.arguments = bundle
            return thankYouFragment
        }
    }
}