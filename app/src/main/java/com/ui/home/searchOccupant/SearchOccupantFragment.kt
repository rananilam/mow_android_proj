package com.ui.home.searchOccupant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.FragmentManager
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowSearchOccupantBinding
import com.data.Injection
import com.data.model.user.User
import com.ui.home.HomeActivity
import iCode.android.BaseFragment


class SearchOccupantFragment : BaseFragment(), SearchOccupantFragmentContract.View {


    private val binding: FragMowSearchOccupantBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_search_occupant

    private val occupantAdapter: SearchOccupantAdapter by lazy { SearchOccupantAdapter() }

    private lateinit var homeActivity: HomeActivity
    private lateinit var presenter: SearchOccupantFragmentContract.Presenter

    private var operatorId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        val dataRepository = Injection.provideDataRepository()
        presenter = SearchOccupantFragmentPresenter(this, dataRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleViewItems.adapter = occupantAdapter
        arguments?.getInt("operatorId")?.let {
            operatorId = it
        }


        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }

        binding.imageViewHome.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        binding.editTextSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if(binding.editTextSearch.text.toString().isNotEmpty())
                    presenter.searchOccupantList(binding.editTextSearch.text.toString(), operatorId)

                return@OnEditorActionListener true
            }
            false
        })

        occupantAdapter.actionCallback = {
            presenter.getOccupantByID(it.occupantID)
        }

    }



    override fun showOccupantList(occupantList: List<User>) {

        if(occupantList.isNotEmpty()) {
            binding.linearLayoutHeader.visibility = View.VISIBLE
            binding.recycleViewItems.visibility = View.VISIBLE
            occupantAdapter.submitList(occupantList)
        } else {
            binding.linearLayoutHeader.visibility = View.GONE
            binding.recycleViewItems.visibility = View.GONE
        }

    }

    override fun addOccupantSuccessfully(occupant: User?) {
        occupant?.let {

            presenter.getOccupantByID(it.occupantID)
            val intent = Intent()
            intent.putExtra("occupant",occupant)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        }
        homeActivity.onBackPressed()
    }

    override fun addOccupantFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showOccupant(occupant: User) {
        presenter.addOccupant(0, occupant.firstName, occupant.middleName, occupant.lastName,
            occupant.weight.toString(), occupant.heightFeet.toString(), occupant.heightInch.toString(),operatorId,false)
    }

    override fun getOccupantFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    companion object {

        fun newInstance(operatorId: Int): SearchOccupantFragment {

            val bundle = Bundle()
            bundle.putInt("operatorId", operatorId)
            val searchOccupantFragment = SearchOccupantFragment()
            searchOccupantFragment.arguments = bundle
            return searchOccupantFragment
        }
    }

}