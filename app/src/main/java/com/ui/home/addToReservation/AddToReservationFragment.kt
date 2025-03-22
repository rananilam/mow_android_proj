package com.ui.home.addToReservation

import android.app.Activity
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentManager
import com.data.DataRepository
import com.data.Injection
import com.data.model.accessory.Accessory
import com.data.model.destination.Location
import com.data.model.destination.PickupLocationInstruction
import com.data.model.device.DeviceOptionID
import com.data.model.device.DeviceProperty
import com.data.model.order.OrderData
import com.data.model.order.RewardPolicy
import com.data.model.order.State
import com.data.model.user.User
import com.data.remote.request.order.SaveOrderRq
import com.data.remote.response.order.BlackoutDatesRs
import com.data.remote.response.order.GetOrderBillingProfileRs
import com.google.gson.Gson
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowAddToReservationBinding
import com.ui.home.HomeActivity
import com.ui.home.addOccupant.AddOccupantFragment
import com.ui.home.addOperator.AddOperatorFragment
import com.util.CalendarViewOpen
import com.util.CustomTimePickerDialog
import com.util.Utility
import com.util.setHintMsg
import com.util.showHtmlText
import iCode.android.BaseFragment
import iCode.utility.DateFormatHelper
import java.util.Calendar
import kotlin.random.Random


class AddToReservationFragment : BaseFragment(), AddToReservationFragmentContract.View {

    private val REQUEST_CODE_ADD_OCCUPANT = 123
    private val REQUEST_CODE_ADD_OPERATOR = 321
    private val binding: FragMowAddToReservationBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_add_to_reservation


    private val customerLocationAdapter: CustomerLocationAdapter by lazy { CustomerLocationAdapter() }


    private lateinit var homeActivity: HomeActivity
    private lateinit var dataRepository: DataRepository
    private lateinit var presenter: AddToReservationFragmentContract.Presenter

    private var payor: User? = null


    private lateinit var saveOrderRq: SaveOrderRq

    private var stateList = listOf<State>()
    private var blackoutDatesList = listOf<BlackoutDatesRs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        dataRepository = Injection.provideDataRepository()
        presenter = AddToReservationFragmentPresenter(this, dataRepository)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.getSerializable("saveOrderRq")?.let {
            saveOrderRq = it as SaveOrderRq
        }

        binding.textInputLayoutStreetAddress.setHintMsg(getString(R.string.hint_building_name_and_or_building_no))
        binding.textInputLayoutCity.setHintMsg(getString(R.string.hint_city))
        binding.textInputState.setHintMsg(getString(R.string.hint_state))
        binding.textInputLayoutZip.setHintMsg(getString(R.string.hint_zip))

        binding.textInputLayoutSelectOperatorOccupant.setHintMsg(getString(R.string.select_operator))
        binding.textInputLayoutSelectAccessoryType.setHintMsg(getString(R.string.select_accessory_type))

        binding.textInputLayoutSelectOccupant.setHintMsg(getString(R.string.select_occupant))




        binding.textViewLblOperator.setText(
            HtmlCompat.fromHtml(
                getString(R.string.hint_operator),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        binding.textViewLblPickupLocation.setText(
            HtmlCompat.fromHtml(
                getString(R.string.hint_pickup_location),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        binding.textViewLblArrivalDate.setText(
            HtmlCompat.fromHtml(
                getString(R.string.hint_arrival_date),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        binding.textViewLblArrivalTime.setText(
            HtmlCompat.fromHtml(
                getString(R.string.hint_arrival_time),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        binding.textViewLblDepartureDate.setText(
            HtmlCompat.fromHtml(
                getString(R.string.hint_departure_date),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        binding.textViewLblDepartureTime.setText(
            HtmlCompat.fromHtml(
                getString(R.string.hint_departure_time),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )


        //binding.textViewLocationCategory.text = dataRepository.locationCategory
        binding.textViewLocationName.text = dataRepository.locationName

        binding.recycleViewCustomerPickupLocation.adapter = customerLocationAdapter


        binding.editTextStreetAddress.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextApartment.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextCity.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextZip.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        binding.editTextContactPerson.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        binding.editTextSelectOccupant.setOnClickListener {


            if (saveOrderRq.operatorID != 0)
                presenter.getOccupantList(saveOrderRq.operatorID)

        }
        binding.editTextSelectOperatorOccupant.setOnClickListener {
            presenter.getOperatorList()
        }

        customerLocationAdapter.actionCallback = {

            saveOrderRq.pickUpLocationID = it.customerPickupLocationID
            saveOrderRq.pickUpLocationName = it.customerPickuplocationName
            customerLocationAdapter.notifyDataSetChanged()

            checkButtonStatus()


            if (it.customerPickuplocationName.equals(
                    "OTHER: DIFFERS FROM BILLING ADDRESS AS FOLLOWS:",
                    true
                )
            ) {
                saveOrderRq.isShippingAddress = true
                binding.linearLayoutOtherPickupLocation.visibility = View.VISIBLE
            } else {
                saveOrderRq.isShippingAddress = false
                binding.linearLayoutOtherPickupLocation.visibility = View.GONE
            }
        }

        customerLocationAdapter.actionLocationInstruction = {
            presenter.getPickupLocationInstruction(
                saveOrderRq.deviceTypeID,
                it.customerPickupLocationID
            )
        }
        binding.editTextSelectAccessoryType.setOnClickListener {
            presenter.getAccessoryList(saveOrderRq.deviceTypeID)
        }

        binding.frameLayoutDestination.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
        }

        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }


        binding.textViewArrivalDate.setOnClickListener {

           /* val arrivalDate = Calendar.getInstance()
            if (saveOrderRq.arrivalDateAndTime != 0L)
                arrivalDate.timeInMillis = saveOrderRq.arrivalDateAndTime

*/

            val arrivalDate = Calendar.getInstance()
            arrivalDate.timeInMillis = System.currentTimeMillis() - 1000
            if (saveOrderRq.arrivalDateAndTime != 0L)
                arrivalDate.timeInMillis = saveOrderRq.arrivalDateAndTime - 1000


            val todayDate = System.currentTimeMillis() - 1000
            CalendarViewOpen(parentFragmentManager,todayDate,arrivalDate,blackoutDatesList) { selection ->
                val selectedDate = Calendar.getInstance().apply { timeInMillis = selection }
                arrivalDate.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR))
                arrivalDate.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH))
                arrivalDate.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH))

                saveOrderRq.arrivalDateAndTime = arrivalDate.timeInMillis

                binding.textViewArrivalDate.setText(
                    DateFormatHelper.getStringFromCalendar(
                        DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                        arrivalDate
                    )
                )


                getOrderBillingProfile()
            }.showDatePicker()


        }

        binding.textViewArrivalTime.setOnClickListener {


            if (saveOrderRq.arrivalDateAndTime != 0L) {

                val arrivalDate = Calendar.getInstance()
                arrivalDate.timeInMillis = saveOrderRq.arrivalDateAndTime

                arrivalDate.set(Calendar.SECOND, 0)
                arrivalDate.set(Calendar.MILLISECOND, 0)


                val mTimePicker = CustomTimePickerDialog(
                    homeActivity, object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            arrivalDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            arrivalDate.set(Calendar.MINUTE, minute)

                            saveOrderRq.arrivalDateAndTime = arrivalDate.timeInMillis

                            binding.textViewArrivalTime.setText(
                                DateFormatHelper.getStringFromCalendar(
                                    DateFormatHelper.FORMAT_S_HH_MM_A,
                                    arrivalDate
                                ).toUpperCase()
                            )
                            getOrderBillingProfile()
                        }
                    }, arrivalDate.get(Calendar.HOUR_OF_DAY),
                    arrivalDate.get(Calendar.MINUTE), false,
                    presenter.getMinHour(arrivalDate),
                    presenter.getMinMinute(arrivalDate),
                    presenter.getMaxHour(arrivalDate),
                    presenter.getMaxMinute(arrivalDate),
                    CustomTimePickerDialog.ITimeBound {
                        showToastMessage(it)
                    }
                )


                mTimePicker.show()

            }
        }

        binding.textViewDepartureDate.setOnClickListener {


            val departureDate = Calendar.getInstance()
            departureDate.timeInMillis = System.currentTimeMillis() - 1000
            if (saveOrderRq.departureDateAndTime != 0L)
                departureDate.timeInMillis = saveOrderRq.departureDateAndTime - 1000

//            val calendar = Calendar.getInstance().apply {
//                set(Calendar.MINUTE, 0)
//                set(Calendar.SECOND, 0)
//                set(Calendar.MILLISECOND, 0)
//            }

            val todayDate =  if (saveOrderRq.arrivalDateAndTime != 0L) saveOrderRq.arrivalDateAndTime - 1000 else System.currentTimeMillis() - 1000

            CalendarViewOpen(parentFragmentManager,todayDate,departureDate,blackoutDatesList) { selection ->
                val selectedDate = Calendar.getInstance().apply { timeInMillis = selection }
                departureDate.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR))
                departureDate.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH))
                departureDate.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH))

                saveOrderRq.departureDateAndTime = departureDate.timeInMillis
                binding.textViewDepartureDate.setText(
                    DateFormatHelper.getStringFromCalendar(
                        DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                        departureDate
                    )
                )
                getOrderBillingProfile()
            }.showDatePicker()


        }

        binding.textViewDepartureTime.setOnClickListener {

            val departureDate = Calendar.getInstance()

            if (saveOrderRq.departureDateAndTime != 0L)
                departureDate.timeInMillis = saveOrderRq.departureDateAndTime

            departureDate.set(Calendar.SECOND, 0)
            departureDate.set(Calendar.MILLISECOND, 0)


            /*val mTimePicker = TimePickerDialog(homeActivity, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    departureDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    departureDate.set(Calendar.MINUTE, minute)

                    saveOrderRq.departureDateAndTime = departureDate.timeInMillis
                    binding.textViewDepartureTime.setText(
                        DateFormatHelper.getStringFromCalendar(
                            DateFormatHelper.FORMAT_S_HH_MM_A,
                            departureDate
                        ).toUpperCase()
                    )

                    getOrderBillingProfile()
                }
            }, departureDate.get(Calendar.HOUR_OF_DAY),
                departureDate.get(Calendar.MINUTE), false)

            mTimePicker.show()*/


            val mTimePicker = CustomTimePickerDialog(
                homeActivity, object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        departureDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        departureDate.set(Calendar.MINUTE, minute)

                        saveOrderRq.departureDateAndTime = departureDate.timeInMillis
                        binding.textViewDepartureTime.setText(
                            DateFormatHelper.getStringFromCalendar(
                                DateFormatHelper.FORMAT_S_HH_MM_A,
                                departureDate
                            ).toUpperCase()
                        )

                        getOrderBillingProfile()
                    }
                }, departureDate.get(Calendar.HOUR_OF_DAY),
                departureDate.get(Calendar.MINUTE), false,
                CustomTimePickerDialog.ITimeBound {
                    showToastMessage(it)
                }
            )


            mTimePicker.show()
            /*val tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                { view, hourOfDay, minute, _ ->

                    departureDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    departureDate.set(Calendar.MINUTE, minute)

                    saveOrderRq.departureDateAndTime = departureDate.timeInMillis
                    binding.textViewDepartureTime.setText(
                        DateFormatHelper.getStringFromCalendar(
                            DateFormatHelper.FORMAT_S_HH_MM_A,
                            departureDate
                        ).toUpperCase()
                    )

                    getOrderBillingProfile()

                },
                departureDate.get(Calendar.HOUR_OF_DAY),
                departureDate.get(Calendar.MINUTE),
                false
            )
            tpd.version = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_1
            tpd.setTimeInterval(1, 5)
            tpd.show(requireFragmentManager(), "Timepickerdialog")*/
        }

        binding.textViewSaveAndContinue.setOnClickListener {


            if (saveOrderRq.locationID != 0 && saveOrderRq.pickUpLocationID != 0 && saveOrderRq.billingProfileID != 0 && saveOrderRq.operatorID != 0) {

                if (!saveOrderRq.operatorOccupantSame && saveOrderRq.occupantID == 0)
                    return@setOnClickListener

                if (checkValidationOfShippingAddress()) {
                    if (saveOrderRq.localOrderId == 0) {

                        saveOrderRq.orderId = 0
                        saveOrderRq.isPrimaryOrder = true
                        saveOrderRq.primaryOrderId = 0
                        saveOrderRq.customerID = dataRepository.user.id

                        saveOrderRq.localOrderId = Random.nextInt()
                        OrderData.add(saveOrderRq)
                        OrderData.removeBonusAndPromo()
                    } else {
                        OrderData.update(saveOrderRq)
                        OrderData.removeBonusAndPromo()
                    }

                    homeActivity.showReservedItemFragment(false)
                }

            }

        }

        binding.textViewSaveAndAddAdditional.setOnClickListener {


            if (saveOrderRq.locationID != 0 && saveOrderRq.pickUpLocationID != 0 && saveOrderRq.billingProfileID != 0 && saveOrderRq.operatorID != 0) {

                if (!saveOrderRq.operatorOccupantSame && saveOrderRq.occupantID == 0)
                    return@setOnClickListener

                if (checkValidationOfShippingAddress()) {
                    if (saveOrderRq.localOrderId == 0) {

                        saveOrderRq.orderId = 0
                        saveOrderRq.isPrimaryOrder = true
                        saveOrderRq.primaryOrderId = 0
                        saveOrderRq.customerID = dataRepository.user.id

                        saveOrderRq.localOrderId = Random.nextInt()
                        OrderData.add(saveOrderRq)
                        OrderData.removeBonusAndPromo()
                    } else {
                        OrderData.update(saveOrderRq)
                        OrderData.removeBonusAndPromo()
                    }

                    homeActivity.supportFragmentManager.popBackStackImmediate(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }


            }
        }

        binding.textViewLblNewOccupant.setOnClickListener {

            if (saveOrderRq.operatorID != 0) {

                val addOccupantFragment =
                    AddOccupantFragment.newInstance(saveOrderRq.operatorID, null)
                addOccupantFragment.setTargetFragment(this, REQUEST_CODE_ADD_OCCUPANT)

                homeActivity.supportFragmentManager.beginTransaction()
                    .addToBackStack("AddOccupantFragment")
                    .replace(R.id.fragment_content, addOccupantFragment)
                    .commit()

            } else {
                showToastMessage(getString(R.string.validation_please_complete_operator_detail_first_to_create_new_occupant))
            }
        }

        binding.textViewLblNewOperator.setOnClickListener {

            payor?.let {
                val addOperatorFragment = AddOperatorFragment.newInstance(it, null)
                addOperatorFragment.setTargetFragment(this, REQUEST_CODE_ADD_OPERATOR)


                homeActivity.supportFragmentManager.beginTransaction()
                    .addToBackStack("AddOperatorFragment")
                    .replace(R.id.fragment_content, addOperatorFragment)
                    .commit()
            }

        }

        binding.textViewLblRiderRewardsPolicy.setOnClickListener {
            presenter.getRewardPolicy()
        }

        binding.textViewLblOperator.setOnClickListener {
            presenter.getEntityDefination(true)
        }

        binding.textViewLblOccupant.isFocusable = false
        binding.textViewLblOccupant.isFocusableInTouchMode = false

        binding.textViewLblOccupant.setOnClickListener {
            presenter.getEntityDefination(false)
        }


        binding.checkBoxOperatorOccupantSame.setOnCheckedChangeListener { p0, isChecked ->
            binding.checkBoxOperatorOccupantSame.isChecked = isChecked


            if (saveOrderRq.operatorID != 0) {

                if (isChecked) {
                    presenter.addDefaultOccupant(saveOrderRq.operatorID)
                } else {
                    saveOrderRq.occupantID = 0
                    saveOrderRq.occupantName = ""
                    binding.editTextSelectOccupant.setText("")
                }

            }

        }

        binding.editTextState.setOnClickListener {
            if (stateList.isNotEmpty()) {
                val list = arrayOfNulls<String>(stateList.size)

                for ((index, value) in stateList.withIndex()) {
                    list[index] = value.stateName
                }


                val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
                builder.setTitle(R.string.select_state)
                    .setItems(list) { dialog, which ->

                        saveOrderRq.shippingStateID = stateList[which].id
                        saveOrderRq.shippingStateName = stateList[which].stateName
                        binding.editTextState.setText(stateList[which].stateName)
                    }
                builder.show()
            } else {
                presenter.getAllState()
            }
        }

        binding.checkBoxIsSameBillingAddress.setOnCheckedChangeListener { p0, isChecked ->
            binding.checkBoxIsSameBillingAddress.isChecked = isChecked


            if (isChecked) {
                payor?.let {
                    binding.editTextStreetAddress.setText(it.billAddress1)
                    binding.editTextApartment.setText(it.billAddress2)
                    binding.editTextCity.setText(it.billCity)
                    binding.editTextState.setText(it.stateName)
                    binding.editTextZip.setText(it.billZip)

                    saveOrderRq.shippingStateID = it.billStateID
                    saveOrderRq.shippingStateName = it.stateName

                    if (it.billStateID == 0 && stateList.isNotEmpty()) {

                        val selectedState = stateList.filter {
                            it.stateName.equals(it.stateName)
                        }

                        if (selectedState.isNotEmpty()) {
                            saveOrderRq.shippingStateID = selectedState[0].id
                        }


                    }
                }
            } else {
                binding.editTextStreetAddress.setText("")
                binding.editTextApartment.setText("")
                binding.editTextCity.setText("")
                binding.editTextState.setText("")
                binding.editTextZip.setText("")
                binding.editTextContactPerson.setText("")

                saveOrderRq.shippingStateID = 0
                saveOrderRq.shippingStateName = ""
            }

        }

        binding.imageViewHome.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

        presenter.getCustomerLocationList(saveOrderRq.deviceTypeID)
        Log.e("Nilam","call reservation details page---------")
        presenter.getSameDayReservation(saveOrderRq.deviceTypeID)
        presenter.getBlackoutDatesByLocationAndDevice(saveOrderRq.deviceTypeID)
        presenter.getLocationById()
        presenter.getCustomer()
        presenter.getAllState()

        if (saveOrderRq.devicePropertyIDs.contains("1")) {
            presenter.getDevicePropertyOptions(DeviceOptionID.JOYSTICK_POSITION)
        }
        if (saveOrderRq.devicePropertyIDs.contains("2")) {
            presenter.getDevicePropertyOptions(DeviceOptionID.PREFERRED_WHEEL_CHAIR_SIZE)
        }
        if (saveOrderRq.devicePropertyIDs.contains("3")) {
            presenter.getDevicePropertyOptions(DeviceOptionID.CHAIR_PAD_REQUIREMENT)
        }
        if (saveOrderRq.devicePropertyIDs.contains("4")) {
            presenter.getDevicePropertyOptions(DeviceOptionID.HAND_CONTROLLER)
        }


        binding.rootConstraintLayout.setOnTouchListener { p0, p1 ->

            if (p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.rootConstraintLayout)
            }
            false
        }

        binding.rootNestedScrollView.setOnTouchListener { p0, p1 ->

            if (p1.action == MotionEvent.ACTION_DOWN) {
                closeKeyBoard(binding.rootNestedScrollView)
            }
            false
        }

        showInfo()
        //presenter.getSameDayReservation(deviceId)
    }


    override fun showOperatorList(operatorList: List<User>) {

        //val list = arrayOf<String>()
        val list = arrayOfNulls<String>(operatorList.size)
        for (i in operatorList.indices) {
            // 5672 -> Can we add the (*) to the create order screen in MMS also?
            // And if not already to the Apps and Comp Portal. This should be a feature accross all platforms

//          list[i] =
//                operatorList[i].firstName + " " + operatorList[i].middleName + " " + operatorList[i].lastName
            list[i] = buildString {
                append(operatorList[i].firstName + " " + operatorList[i].middleName + " " + operatorList[i].lastName)

                if (operatorList[i].isDefault) {
                    append(" ")
                    append("*")
                }
            }
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
        builder.setTitle(R.string.select_operator)
            .setItems(list, DialogInterface.OnClickListener { dialog, which ->

                saveOrderRq.operatorID = operatorList[which].operatorID
                saveOrderRq.operatorName = list[which].toString()
                saveOrderRq.isDefault = operatorList[which].isDefault

                binding.editTextSelectOperatorOccupant.setText(list[which])
                binding.editTextSelectOccupant.setText(getString(R.string.select_occupant))

                checkButtonStatus()
            })
        builder.show()
    }

    override fun getOperatorListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showOccupantList(occupantList: List<User>) {

        if (occupantList.isNotEmpty()) {
            val list = arrayOfNulls<String>(occupantList.size)
            for (i in occupantList.indices) {
                list[i] =
                    occupantList[i].firstName + " " + occupantList[i].middleName + " " + occupantList[i].lastName
            }
            val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
            builder.setTitle(R.string.select_occupant)
                .setItems(list, DialogInterface.OnClickListener { dialog, which ->

                    saveOrderRq.occupantID = occupantList[which].id
                    saveOrderRq.occupantName = list[which].toString()
                    binding.editTextSelectOccupant.setText(list[which])

                    checkButtonStatus()
                })
            builder.show()
        }
    }

    override fun getOccupantListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showCustomerLocationList(customerLocationList: List<Location>) {
        customerLocationAdapter.submitList(customerLocationList)
    }

    override fun getCustomerLocationListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showAccessoryList(accessoryList: List<Accessory>) {
        val list = arrayOfNulls<String>(accessoryList.size)
        for (i in accessoryList.indices) {
            list[i] = accessoryList[i].accessoryTypeName
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(homeActivity)
        builder.setTitle(R.string.select_accessory_type)
            .setItems(list, DialogInterface.OnClickListener { dialog, which ->

                saveOrderRq.accessoryID = accessoryList[which].id
                saveOrderRq.accessoryTypeName = accessoryList[which].accessoryTypeName
                binding.editTextSelectAccessoryType.setText(list[which])
            })
        builder.show()
    }

    override fun getAccessoryListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }


    private fun getOrderBillingProfile() {

        val pickupDate = binding.textViewArrivalDate.text.toString()
        val returnDate = binding.textViewDepartureDate.text.toString()
        val pickupTime = binding.textViewArrivalTime.text.toString()
        val returnTime = binding.textViewDepartureTime.text.toString()

        presenter.getOrderBillingProfile(
            pickupDate,
            pickupTime,
            returnDate,
            returnTime,
            saveOrderRq.deviceTypeID,
            "",
            "",
            ""
        )
    }

    override fun showOrderBillingProfile(getOrderBillingProfileRs: GetOrderBillingProfileRs) {


        saveOrderRq.billingProfileID = getOrderBillingProfileRs.billingProfileID
        saveOrderRq.billingDescription = getOrderBillingProfileRs.description
        saveOrderRq.billingRegularPrice = getOrderBillingProfileRs.regularPrice.toFloat()
        saveOrderRq.billingRewardPoint = getOrderBillingProfileRs.rewardPoint
        //saveOrderRq.billingTotalPrice = getOrderBillingProfileRs.totalPrice.toFloat()


        binding.textViewRentalPeriod.text = saveOrderRq.billingDescription
        binding.textViewRiderRewardsPointsEarned.text = saveOrderRq.billingRewardPoint.toString()
        binding.textViewPrice.text =
            "$" + Utility.convertUpTo2Decimal((saveOrderRq.billingRegularPrice + saveOrderRq.chairPadPrice)) + " (excl. tax)"

        binding.textViewErrorOfBillingProfile.visibility = View.GONE


        checkButtonStatus()
    }


    override fun getOrderBillingProfileFail(errorMessage: String?) {


        saveOrderRq.billingProfileID = 0
        saveOrderRq.billingDescription = ""
        saveOrderRq.billingRegularPrice = 0.0F
        saveOrderRq.billingRewardPoint = 0.0F
        //saveOrderRq.billingTotalPrice = 0.0F
        checkButtonStatus()


        if (errorMessage.isNullOrEmpty()) {
            binding.textViewErrorOfBillingProfile.visibility = View.GONE
        } else {
            binding.textViewErrorOfBillingProfile.visibility = View.VISIBLE
            binding.textViewErrorOfBillingProfile.text = errorMessage
        }
    }

    override fun showLocation(location: Location) {

        Log.e("Nilam", (saveOrderRq.taxRate).toString())
        saveOrderRq.locationID = location.id
        saveOrderRq.locationName = location.locationName
        saveOrderRq.taxRate = location.taxRate
        saveOrderRq.deliveryFee = location.deliveryFee
        saveOrderRq.isGenerateAcceptBonusDay = location.isGenerateAcceptBonusDay
    }

    override fun getLocationFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showRewardPolicy(rewardPolicy: RewardPolicy) {
        homeActivity.showContentDialogFragment(rewardPolicy.templateContent)
    }

    override fun getRewardPolicyFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showEntity(entity: String) {
        homeActivity.showContentDialogFragment(entity)
    }

    override fun showCustomer(customer: User) {
        payor = customer
    }

    override fun getCustomerFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showDefaultOccupant(selectedOccupantID: Int, occupantList: List<User>) {

        if (occupantList.isNotEmpty()) {

            val defalutOccupantFilter = occupantList.filter { it.id == selectedOccupantID }

            if (defalutOccupantFilter.isNotEmpty()) {

                saveOrderRq.occupantID = defalutOccupantFilter[0].id
                saveOrderRq.occupantName = defalutOccupantFilter[0].fullName
                binding.editTextSelectOccupant.setText(defalutOccupantFilter[0].fullName)
            }
        }
    }

    override fun getDefaultOccupantFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showPickupLocationInstruction(pickupLocationInstruction: PickupLocationInstruction) {
        homeActivity.showContentDialogFragment(pickupLocationInstruction.pickupInstructionContent)
    }

    override fun getPickupLocationInstructionFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showDevicePropertyOptions(
        deviceOptionID: DeviceOptionID,
        devicePropertyList: List<DeviceProperty>,
    ) {


        when (deviceOptionID) {
            DeviceOptionID.JOYSTICK_POSITION -> {

                binding.textViewLblJoystickPosition.visibility = View.VISIBLE
                binding.recycleViewJoystickPosition.visibility = View.VISIBLE
                val devicePropertyAdapter = DevicePropertyAdapter(
                    saveOrderRq.joystickID,
                    deviceOptionID,
                    saveOrderRq.chairPadPrice
                )
                binding.recycleViewJoystickPosition.adapter = devicePropertyAdapter
                devicePropertyAdapter.submitList(devicePropertyList)
                devicePropertyAdapter.actionCallback = { id, currentNote ->
                    saveOrderRq.joystickID = id
                    saveOrderRq.joystickName = currentNote

                }
            }

            DeviceOptionID.PREFERRED_WHEEL_CHAIR_SIZE -> {

                binding.textViewLblPreferredWheelchairSize.visibility = View.VISIBLE
                binding.recycleViewPreferredWheelchairSize.visibility = View.VISIBLE
                val devicePropertyAdapter = DevicePropertyAdapter(
                    saveOrderRq.wheelchairSizeID,
                    deviceOptionID,
                    saveOrderRq.chairPadPrice
                )
                binding.recycleViewPreferredWheelchairSize.adapter = devicePropertyAdapter
                devicePropertyAdapter.submitList(devicePropertyList)
                devicePropertyAdapter.actionCallback = { id, currentNote ->
                    saveOrderRq.wheelchairSizeID = id
                    saveOrderRq.wheelchairSizeName = currentNote
                }

            }

            DeviceOptionID.CHAIR_PAD_REQUIREMENT -> {

                binding.textViewLblChairPadRequirement.visibility = View.VISIBLE
                binding.recycleViewChairPadRequirement.visibility = View.VISIBLE

                if (saveOrderRq.chairPadPrice > 0.0F && devicePropertyList.isNotEmpty()) {
                    saveOrderRq.chairpadID = devicePropertyList[0].devicePropertyOptionID
                    saveOrderRq.chairpadName = devicePropertyList[0].devicePropertyOption
                }

                val devicePropertyAdapter = DevicePropertyAdapter(
                    saveOrderRq.chairpadID,
                    deviceOptionID,
                    saveOrderRq.chairPadPrice
                )
                binding.recycleViewChairPadRequirement.adapter = devicePropertyAdapter
                devicePropertyAdapter.submitList(devicePropertyList)
                devicePropertyAdapter.actionCallback = { id, currentNote ->
                    saveOrderRq.chairpadID = id
                    saveOrderRq.chairpadName = currentNote
                }

            }

            DeviceOptionID.HAND_CONTROLLER -> {

                binding.textViewLblHandController.visibility = View.VISIBLE
                binding.recycleViewHandController.visibility = View.VISIBLE
                val devicePropertyAdapter = DevicePropertyAdapter(
                    saveOrderRq.handControllerID,
                    deviceOptionID,
                    saveOrderRq.chairPadPrice
                )
                binding.recycleViewHandController.adapter = devicePropertyAdapter
                devicePropertyAdapter.submitList(devicePropertyList)
                devicePropertyAdapter.actionCallback = { id, currentNote ->
                    saveOrderRq.handControllerID = id
                    saveOrderRq.handControllerName = currentNote
                }
            }

            else -> { // Note the block

            }
        }

    }

    override fun getDevicePropertyOptionsFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showStateList(stateList: List<State>) {
        this.stateList = stateList
    }
    override fun getStateListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showBlackDatesList(datesList: List<BlackoutDatesRs>) {
        this.blackoutDatesList = datesList
        val json = Gson().toJson(blackoutDatesList)
        Log.e("Nilam", json)


    }

    override fun getBlackDatesListFail(errorMessage: String?) {
        Log.e("Nilam --->getBlackDatesListFail method fail message", errorMessage.toString())

    }

    private fun showInfo() {


        binding.textViewName.text = saveOrderRq.deviceTypeName
        binding.textViewShortDescription.showHtmlText(saveOrderRq.deviceItemShortDescription)
        if (saveOrderRq.operatorOccupantSame) {
            binding.textViewLblOccupant.visibility = View.GONE
            binding.textInputLayoutSelectOccupant.visibility = View.GONE
            binding.textViewLblNewOccupant.visibility = View.GONE
            binding.textViewLblOperatorNote.visibility = View.GONE
            binding.checkBoxOperatorOccupantSame.visibility = View.GONE
        } else {
            binding.textViewLblOccupant.visibility = View.VISIBLE
            binding.textInputLayoutSelectOccupant.visibility = View.VISIBLE
            binding.textViewLblNewOccupant.visibility = View.VISIBLE
            binding.textViewLblOperatorNote.visibility = View.VISIBLE
            binding.checkBoxOperatorOccupantSame.visibility = View.VISIBLE
        }



        if (saveOrderRq.operatorName.isNotEmpty())
            binding.editTextSelectOperatorOccupant.setText(saveOrderRq.operatorName)

        if (saveOrderRq.occupantName.isNotEmpty())
            binding.editTextSelectOccupant.setText(saveOrderRq.occupantName)

        customerLocationAdapter.setPickupLocationID(saveOrderRq.pickUpLocationID)

        if (saveOrderRq.accessoryTypeName.isNotEmpty())
            binding.editTextSelectAccessoryType.setText(saveOrderRq.accessoryTypeName)

        if (saveOrderRq.arrivalDateAndTime != 0L) {
            val arrivalDateCal = Calendar.getInstance()
            arrivalDateCal.timeInMillis = saveOrderRq.arrivalDateAndTime

            binding.textViewArrivalDate.setText(
                DateFormatHelper.getStringFromCalendar(
                    DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                    arrivalDateCal
                )
            )

            binding.textViewArrivalTime.setText(
                DateFormatHelper.getStringFromCalendar(
                    DateFormatHelper.FORMAT_S_HH_MM_A,
                    arrivalDateCal
                ).toUpperCase()
            )
        }

        if (saveOrderRq.departureDateAndTime != 0L) {
            val departureDateCal = Calendar.getInstance()
            departureDateCal.timeInMillis = saveOrderRq.departureDateAndTime

            binding.textViewDepartureDate.setText(
                DateFormatHelper.getStringFromCalendar(
                    DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                    departureDateCal
                )
            )

            binding.textViewDepartureTime.setText(
                DateFormatHelper.getStringFromCalendar(
                    DateFormatHelper.FORMAT_S_HH_MM_A,
                    departureDateCal
                ).toUpperCase()
            )
        }


        if (saveOrderRq.billingProfileID != 0) {

            binding.textViewRentalPeriod.text = saveOrderRq.billingDescription
            binding.textViewRiderRewardsPointsEarned.text =
                saveOrderRq.billingRewardPoint.toInt().toString()
            binding.textViewPrice.text =
                "$" + Utility.convertUpTo2Decimal((saveOrderRq.billingRegularPrice + saveOrderRq.chairPadPrice)) + " (excl. tax)"
            binding.textViewErrorOfBillingProfile.visibility = View.GONE

        }

        if (saveOrderRq.localOrderId != 0) {
            binding.textViewSaveAndAddAdditional.visibility = View.GONE
            binding.textViewSaveAndContinue.text = getString(R.string.update_reservation)
        } else {
            binding.textViewSaveAndAddAdditional.visibility = View.VISIBLE
            binding.textViewSaveAndContinue.text = getString(R.string.save_this_order_continue)
        }

        if (saveOrderRq.isShippingAddress) {
            binding.linearLayoutOtherPickupLocation.visibility = View.VISIBLE
            binding.editTextStreetAddress.setText(saveOrderRq.shippingAddressLine1)
            binding.editTextApartment.setText(saveOrderRq.shippingAddressLine2)
            binding.editTextCity.setText(saveOrderRq.shippingCity)
            binding.editTextState.setText(saveOrderRq.shippingStateName)
            binding.editTextZip.setText(saveOrderRq.shippingZipcode)
            binding.editTextContactPerson.setText(saveOrderRq.shippingDeliveryNote)
        } else {
            binding.linearLayoutOtherPickupLocation.visibility = View.GONE
        }
        checkButtonStatus()
    }

    private fun checkButtonStatus() {

        if (saveOrderRq.operatorID != 0 && saveOrderRq.pickUpLocationID != 0 && saveOrderRq.billingProfileID != 0) {

            binding.textViewSaveAndAddAdditional.setBackgroundResource(R.drawable.bg_mow_rounded_blue_button)
            binding.textViewSaveAndContinue.setBackgroundResource(R.drawable.bg_mow_rounded_blue_button)

            if (!saveOrderRq.operatorOccupantSame && saveOrderRq.occupantID == 0) {

                binding.textViewSaveAndAddAdditional.setBackgroundResource(R.drawable.bg_mow_rounded_gray_button)
                binding.textViewSaveAndContinue.setBackgroundResource(R.drawable.bg_mow_rounded_gray_button)
            }

        } else {
            binding.textViewSaveAndAddAdditional.setBackgroundResource(R.drawable.bg_mow_rounded_gray_button)
            binding.textViewSaveAndContinue.setBackgroundResource(R.drawable.bg_mow_rounded_gray_button)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CODE_ADD_OCCUPANT) {

                data?.let { intent ->
                    val occupant: User = intent.getSerializableExtra("occupant") as User
                    saveOrderRq.occupantID = occupant.id
                    saveOrderRq.occupantName = occupant.firstName + " " + occupant.lastName
                    binding.editTextSelectOccupant.setText(
                        occupant.firstName + " " + occupant.lastName
                    )
                }
            } else if (requestCode == REQUEST_CODE_ADD_OPERATOR) {

                data?.let { intent ->
                    val operator: User = intent.getSerializableExtra("operator") as User

                    saveOrderRq.operatorID = operator.operatorID
                    saveOrderRq.operatorName = operator.firstName + " " + operator.lastName
                    saveOrderRq.isDefault = operator.isDefault
                    binding.editTextSelectOperatorOccupant.setText(
                        operator.firstName + " " + operator.lastName
                    )

                    checkButtonStatus()
                }
            }

        }

    }

    private fun checkValidationOfShippingAddress(): Boolean {

        if (saveOrderRq.isShippingAddress) {

            if (saveOrderRq.isShippingAddress) {

                if (binding.editTextStreetAddress.text.toString().trim().isNotEmpty()) {

                    if (binding.editTextCity.text.toString().trim().isNotEmpty()) {

                        if (saveOrderRq.shippingStateID != 0) {

                            if (binding.editTextZip.text.toString().trim().isNotEmpty()) {

                                saveOrderRq.shippingAddressLine1 =
                                    binding.editTextStreetAddress.text.toString().trim()
                                saveOrderRq.shippingAddressLine2 =
                                    binding.editTextApartment.text.toString().trim()
                                saveOrderRq.shippingZipcode =
                                    binding.editTextZip.text.toString().trim()
                                saveOrderRq.shippingCity =
                                    binding.editTextCity.text.toString().trim()
                                saveOrderRq.shippingDeliveryNote =
                                    binding.editTextContactPerson.text.toString().trim()
                                return true

                            } else {
                                showToastMessage(getString(R.string.validation_zip_code_is_required))
                            }

                        } else {
                            showToastMessage(getString(R.string.validation_please_select_state))
                        }

                    } else {
                        showToastMessage(getString(R.string.validation_city_is_required))
                    }

                } else {
                    showToastMessage(getString(R.string.validation_address_line1_is_required))
                }

            }
        } else {
            return true
        }

        return false
    }

    companion object {

        fun newInstance(saveOrderRq: SaveOrderRq): AddToReservationFragment {

            val bundle = Bundle()
            bundle.putSerializable("saveOrderRq", saveOrderRq)
            val addToReservationFragment = AddToReservationFragment()
            addToReservationFragment.arguments = bundle
            return addToReservationFragment
        }
    }
}


