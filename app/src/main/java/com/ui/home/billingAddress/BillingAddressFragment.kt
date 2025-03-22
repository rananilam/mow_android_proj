package com.ui.home.billingAddress

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.data.DataRepository
import com.data.Injection
import com.data.model.order.State
import com.data.model.user.User
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowBillingAddressBinding
import com.ui.home.HomeActivity
import com.util.setHintMsg
import iCode.android.BaseFragment


class BillingAddressFragment : BaseFragment(), BillingAddressFragmentContract.View {


    private val binding: FragMowBillingAddressBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_billing_address

    private lateinit var homeActivity: HomeActivity
    private lateinit var dataRepository: DataRepository
    private lateinit var presenter: BillingAddressFragmentContract.Presenter

    private var payor: User? = null
    private var stateList = listOf<State>()
    private var selectedStateId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        dataRepository = Injection.provideDataRepository()
        presenter = BillingAddressFragmentPresenter(this, dataRepository)

        presenter.getCustomer()
        presenter.getAllState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textInputLayoutFirstName.setHintMsg(getString(R.string.hint_first_name))
        binding.textInputLayoutLastName.setHintMsg(getString(R.string.hint_last_name))
        binding.textInputLayoutSelectCountry.setHintMsg(getString(R.string.hint_select_country))
        binding.textInputLayoutStreetAddress.setHintMsg(getString(R.string.hint_street_address))
        binding.textInputLayoutTownCity.setHintMsg(getString(R.string.hint_town_city))
        binding.textInputLayoutState.setHintMsg(getString(R.string.hint_state))
        binding.textInputLayoutZip.setHintMsg(getString(R.string.hint_zip))
        binding.textInputLayoutCountry.setHintMsg(getString(R.string.hint_country))
        binding.textInputLayoutOtherState.setHintMsg(getString(R.string.hint_other_state))


        binding.editTextFirstName.isFocusable = false
        binding.editTextFirstName.isClickable = false

        binding.editTextMiddleName.isFocusable = false
        binding.editTextMiddleName.isClickable = false

        binding.editTextLastName.isFocusable = false
        binding.editTextLastName.isClickable = false

        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }




        binding.editTextSelectCountry.setOnClickListener {
            val builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_country)
                .setItems(R.array.country_options,
                    DialogInterface.OnClickListener { _, which ->
                        binding.editTextSelectCountry.setText(homeActivity.resources.getStringArray(R.array.country_options)[which])

                        if (which == 0) {
                            binding.textInputLayoutState.visibility = View.VISIBLE
                            binding.textInputLayoutOtherState.visibility = View.GONE
                            binding.textInputLayoutCountry.visibility = View.GONE
                        } else {
                            binding.textInputLayoutState.visibility = View.GONE
                            binding.textInputLayoutOtherState.visibility = View.VISIBLE
                            binding.textInputLayoutCountry.visibility = View.VISIBLE

                            selectedStateId = 0
                            binding.editTextState.setText("")
                        }
                    })
            builder.show()
        }

        binding.editTextState.setOnClickListener {
            showStateDialog()
        }



        binding.textViewSaveChanges.setOnClickListener {

            var country = binding.editTextSelectCountry.text.toString()

            if(binding.editTextSelectCountry.text.toString().trim().equals("Other"))
                country = binding.editTextCountry.text.toString()

            presenter.saveCustomer(
                binding.editTextFirstName.text.toString(),
                binding.editTextMiddleName.text.toString(),
                binding.editTextLastName.text.toString(),
                country,
                binding.editTextStreetAddress.text.toString(),
                binding.editTextApartment.text.toString(),
                binding.editTextTownCity.text.toString(),
                selectedStateId,
                binding.editTextOtherState.text.toString(),
                binding.editTextZip.text.toString(),
                payor?.email?:"",
                payor
            )
        }



        binding.editTextFirstName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextMiddleName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextLastName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextCountry.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextStreetAddress.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextApartment.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextTownCity.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextOtherState.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextZip.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

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

        payor?.let {
            showCustomer(it)
        }
    }


    override fun showCustomer(customer: User) {

        payor = customer
        binding.editTextFirstName.setText(customer.firstName)
        binding.editTextMiddleName.setText(customer.middleName)
        binding.editTextLastName.setText(customer.lastName)

        binding.editTextStreetAddress.setText(customer.billAddress1)
        binding.editTextApartment.setText(customer.billAddress2)
        binding.editTextTownCity.setText(customer.billCity)
        binding.editTextZip.setText(customer.billZip)

        if (customer.country.equals("USA", true)) {
            binding.editTextSelectCountry.setText(customer.country)
            binding.textInputLayoutState.visibility = View.VISIBLE
            binding.textInputLayoutOtherState.visibility = View.GONE
            binding.textInputLayoutCountry.visibility = View.GONE
            selectedStateId = customer.billStateID
            binding.editTextState.setText(customer.stateName)
        } else {
            binding.editTextSelectCountry.setText(getString(R.string.other))
            binding.editTextCountry.setText(customer.country)
            binding.textInputLayoutCountry.visibility = View.VISIBLE
            binding.textInputLayoutState.visibility = View.GONE
            binding.textInputLayoutOtherState.visibility = View.VISIBLE
            selectedStateId = 0
            binding.editTextOtherState.setText(customer.otherBillStateName)
            binding.editTextState.setText("")
        }

    }

    override fun getCustomerFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showStateList(stateList: List<State>) {
        if (stateList.isNotEmpty()) {
            this.stateList = stateList
        }
    }

    override fun getStateListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun saveCustomerSuccessfully() {
        Toast.makeText(homeActivity, getString(R.string.update_successfully), Toast.LENGTH_LONG)
            .show()

        homeActivity.onBackPressed()

    }

    override fun saveCustomerFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }


    private fun showStateDialog() {

        if (stateList.isNotEmpty()) {
            val list = arrayOfNulls<String>(stateList.size)

            for ((index, value) in stateList.withIndex()) {
                list[index] = value.stateName
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_state)
                .setItems(list) { dialog, which ->
                    selectedStateId = stateList[which].id
                    binding.editTextState.setText(stateList[which].stateName)
                }
            builder.show()
        }
    }

    companion object {

        fun newInstance() = BillingAddressFragment()
    }
}