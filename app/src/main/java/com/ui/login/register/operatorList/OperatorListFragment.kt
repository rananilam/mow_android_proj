package com.ui.login.register.operatorList

import android.os.Bundle
import android.view.View
import com.ui.login.register.CreateAccountActivity
import com.ui.login.register.operator.OperatorAdapter
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowCreateAccountOperatorListBinding
import iCode.android.BaseFragment


class OperatorListFragment : BaseFragment(){


    private val binding: FragMowCreateAccountOperatorListBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_create_account_operator_list
    private val operatorAdapter: OperatorAdapter by lazy { OperatorAdapter(false) }

    private lateinit var createAccountActivity: CreateAccountActivity



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAccountActivity = activity as CreateAccountActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {

            binding.recycleViewOperatorList.adapter = operatorAdapter
        }




        operatorAdapter.actionCallbackEditUser = {

            val editIndex = createAccountActivity.operatorList.indexOf(it)
            createAccountActivity.showOperatorInfoFragment(editIndex)
            //createAccountActivity.showOperatorInfoFragment(presenter.getSaveCustomerRq(), it)
        }

        binding.imageViewAddOprator.setOnClickListener {
            createAccountActivity.showOperatorInfoFragment(-1)
        }

        binding.textViewBack.setOnClickListener {
            createAccountActivity.onBackPressed()
        }

        binding.textViewCreateAccount.setOnClickListener {
            createAccountActivity.saveCustomer()
        }
        operatorAdapter.submitList(createAccountActivity.operatorList)

    }




    companion object {
        fun newInstance() = OperatorListFragment()
    }

}