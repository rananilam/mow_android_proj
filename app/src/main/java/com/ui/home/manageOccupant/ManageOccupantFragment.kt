package com.ui.home.manageOccupant

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowManageOccupantBinding
import com.data.Injection
import com.data.model.user.User
import com.ui.home.HomeActivity
import com.ui.home.addOccupant.AddOccupantFragment
import com.ui.home.searchOccupant.SearchOccupantFragment
import iCode.android.BaseFragment


class ManageOccupantFragment : BaseFragment(), ManageOccupantFragmentContract.View {


    private final val REQUEST_CODE_ADD_OCCUPANT = 654
    private final val REQUEST_CODE_EDIT_OCCUPANT = 789
    private final val REQUEST_CODE_EXISTING_ADD_OCCUPANT = 693

    private val binding: FragMowManageOccupantBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_manage_occupant

    private lateinit var homeActivity: HomeActivity
    private lateinit var presenter: ManageOccupantFragmentContract.Presenter

    private val occupantAdapter: OccupantAdapter by lazy { OccupantAdapter() }

    private lateinit var operator: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        val dataRepository = Injection.provideDataRepository()
        presenter = ManageOccupantFragmentPresenter(this, dataRepository)

        arguments?.getSerializable("operator")?.let {
            operator = it as User
        }
        presenter.getOccupantList(operator.operatorID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recycleViewItems.adapter = occupantAdapter
        occupantAdapter.actionCallbackEditOccupant = {

            val addOccupantFragment = AddOccupantFragment.newInstance(operator.operatorID,it)
            addOccupantFragment.setTargetFragment(this, REQUEST_CODE_EDIT_OCCUPANT)


            homeActivity.supportFragmentManager.beginTransaction()
                .addToBackStack("AddOccupantFragment")
                .replace(R.id.fragment_content, addOccupantFragment)
                .commit()

        }
        occupantAdapter.actionCallbackDeleteOccupant = {

            showRemoveOccupantDialog(it)
        }


        binding.textViewAddNewOccupant.setOnClickListener {

            val addOccupantFragment = AddOccupantFragment.newInstance(operator.operatorID,null)
            addOccupantFragment.setTargetFragment(this, REQUEST_CODE_ADD_OCCUPANT)


            homeActivity.supportFragmentManager.beginTransaction()
                .addToBackStack("AddOccupantFragment")
                .replace(R.id.fragment_content, addOccupantFragment)
                .commit()

        }

        binding.textViewAddExistingOccupant.setOnClickListener {


            val searchOccupantFragment = SearchOccupantFragment.newInstance(operator.operatorID)
            searchOccupantFragment.setTargetFragment(this, REQUEST_CODE_EXISTING_ADD_OCCUPANT)

            homeActivity.supportFragmentManager.beginTransaction()
                .addToBackStack("SearchOccupantFragment")
                .replace(R.id.fragment_content, searchOccupantFragment)
                .commit()


        }
        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }

        binding.imageViewHome.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        binding.textViewLblManageOccupant.setOnClickListener {
            presenter.getEntityDefination(false)
        }

    }

    private fun showRemoveOccupantDialog(occupant: User) {
        val builder = AlertDialog.Builder(homeActivity)
        builder.setMessage(getString(R.string.alert_are_you_sure_wants_to_remove_this_occupant))
            .setPositiveButton(getString(R.string.ok),
                DialogInterface.OnClickListener { dialog, id ->
                    presenter.removeOccupantById(occupant.id)
                })
            .setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                })
        // Create the AlertDialog object and return it
        builder.show()
    }
    override fun showOccupantList(occupantList: List<User>) {
        occupantAdapter.submitList(occupantList)
    }

    override fun getOccupantListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {

            if(requestCode == REQUEST_CODE_ADD_OCCUPANT || requestCode == REQUEST_CODE_EDIT_OCCUPANT || requestCode == REQUEST_CODE_EXISTING_ADD_OCCUPANT) {
                presenter.getOccupantList(operator.operatorID)
            }
        }
    }

    override fun showEntity(entity: String) {
        homeActivity.showContentDialogFragment(entity)
    }

    override fun removeOccupantSuccessfully() {
        presenter.getOccupantList(operator.operatorID)
    }

    override fun removeOccupantFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    companion object {


        fun newInstance(operator: User): ManageOccupantFragment {

            val bundle = Bundle()
            bundle.putSerializable("operator", operator)

            val manageOccupantFragment = ManageOccupantFragment()
            manageOccupantFragment.arguments = bundle
            return manageOccupantFragment
        }

    }
}