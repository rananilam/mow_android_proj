package com.ui.home.addOccupant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentManager
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowAddOccupantBinding
import com.data.Injection
import com.data.model.user.User
import com.ui.home.HomeActivity
import com.util.setHintMsg
import iCode.android.BaseFragment


class AddOccupantFragment : BaseFragment(), AddOccupantFragmentContract.View {


    private val binding: FragMowAddOccupantBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_add_occupant

    private lateinit var homeActivity: HomeActivity
    private lateinit var presenter: AddOccupantFragmentContract.Presenter

    private var operator: User? = null
    private var occupant: User? = null

    private var operatorID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        val dataRepository = Injection.provideDataRepository()
        presenter = AddOccupantFragmentPresenter(this, dataRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getInt("operatorID")?.let {
            operatorID = it
        }

        arguments?.getSerializable("occupant")?.let {
            occupant = it as User
        }

        binding.textInputLayoutFirstName.setHintMsg(getString(R.string.hint_first_name))
        binding.textInputLayoutLastName.setHintMsg(getString(R.string.hint_last_name))
        binding.textInputLayoutSelectHeightFt.setHintMsg(getString(R.string.hint_height_ft))
        binding.textInputLayoutSelectHeightIn.setHintMsg(getString(R.string.hint_height_in))
        binding.textInputLayoutWeight.setHintMsg(getString(R.string.hint_weight_lbs))


        binding.editTextFirstName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextMiddleName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLastName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        binding.textViewSubmit.setOnClickListener {

            presenter.addOccupant(
                if(occupant == null) 0 else occupant!!.id,
                binding.editTextFirstName.text.toString(),
                binding.editTextMiddleName.text.toString(),
                binding.editTextLastName.text.toString(),
                binding.editTextWeight.text.toString(),
                binding.editTextSelectHeightFt.text.toString(),
                binding.editTextSelectHeightIn.text.toString(),
                operatorID,
                binding.checkBoxIfSame.isChecked
                )

        }


        binding.editTextSelectHeightFt.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_height_ft)
                .setItems(R.array.height_ft) { dialog, which ->
                    binding.editTextSelectHeightFt.setText(homeActivity.resources.getStringArray(R.array.height_ft)[which])
                }
            builder.show()
        }

        binding.editTextSelectHeightIn.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_height_in)
                .setItems(R.array.height_in) { dialog, which ->
                    binding.editTextSelectHeightIn.setText(homeActivity.resources.getStringArray(R.array.height_in)[which])
                }
            builder.show()
        }
        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }

        binding.imageViewHome.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }


        binding.checkBoxIfSame.setOnCheckedChangeListener { p0, isChecked ->
            binding.checkBoxIfSame.isChecked = isChecked

            if(binding.checkBoxIfSame.isChecked)
                copyOperatorInfo()
            else
                clearInput()
        }

        binding.textViewLblAddOccupant.setOnClickListener {
            presenter.getEntityDefination(false)
        }


        binding.rootConstraintLayout.setOnTouchListener { p0, p1 ->

            if(p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.rootConstraintLayout)
            }
            false
        }

        binding.rootNestedScrollView.setOnTouchListener { p0, p1 ->

            if(p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.rootNestedScrollView)
            }
            false
        }

        if(operatorID != 0)
            presenter.getOperatorByID(operatorID)


        showOccupant()

    }


    override fun addOccupantSuccessfully(occupant: User?) {

        occupant?.let {
            val intent = Intent()
            intent.putExtra("occupant",occupant)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        }
        homeActivity.onBackPressed()
    }

    override fun addOccupantFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showOperator(operator: User) {
        this.operator = operator
    }

    override fun getOperatorFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    private fun showOccupant() {
        occupant?.let {

            binding.editTextFirstName.setText(it.firstName)
            binding.editTextMiddleName.setText(it.middleName)
            binding.editTextLastName.setText(it.lastName)
            binding.editTextWeight.setText(it.weight.toString())
            binding.editTextSelectHeightFt.setText(it.heightFeet.toString())
            binding.editTextSelectHeightIn.setText(it.heightInch.toString())
            binding.checkBoxIfSame.isChecked = it.isDefault

        }
    }

    private fun copyOperatorInfo() {

        operator?.let {

            binding.editTextFirstName.setText(it.firstName)
            binding.editTextMiddleName.setText(it.middleName)
            binding.editTextLastName.setText(it.lastName)
            binding.editTextWeight.setText(it.weight.toString())
            binding.editTextSelectHeightFt.setText(it.heightFeet.toString())
            binding.editTextSelectHeightIn.setText(it.heightInch.toString())

        }

        //binding.checkBoxIfSame.isChecked = operator.isDefault
    }

    private fun clearInput() {

        binding.editTextFirstName.setText("")
        binding.editTextMiddleName.setText("")
        binding.editTextLastName.setText("")
        binding.editTextWeight.setText("")
        binding.editTextSelectHeightFt.setText("")
        binding.editTextSelectHeightIn.setText("")

    }

    override fun showEntity(entity: String) {
        homeActivity.showContentDialogFragment(entity)
    }
    companion object {

        fun newInstance(operatorID: Int, occupant: User?): AddOccupantFragment {

            val bundle = Bundle()
            bundle.putInt("operatorID", operatorID)
            occupant?.let {
                bundle.putSerializable("occupant", it)
            }

            val addOccupantFragment = AddOccupantFragment()
            addOccupantFragment.arguments = bundle
            return addOccupantFragment
        }
    }


}