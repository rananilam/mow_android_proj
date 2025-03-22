package com.ui.home.extendOrder

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import androidx.fragment.app.FragmentManager
import com.data.DataRepository
import com.data.Injection
import com.data.model.device.DeviceInfo
import com.data.model.device.DeviceOptionID
import com.data.model.device.DeviceProperty
import com.data.model.order.Order
import com.data.model.order.OrderData
import com.data.model.order.RewardPolicy
import com.data.model.user.User
import com.data.remote.request.order.SaveOrderRq
import com.data.remote.response.order.BlackoutDatesRs
import com.data.remote.response.order.GetOrderBillingProfileRs
import com.google.gson.Gson
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowExtendOrderBinding
import com.ui.home.HomeActivity
import com.ui.home.destination.DestinationAdapter
import com.util.CalendarViewOpen
import com.util.CustomTimePickerDialog
import com.util.Utility
import iCode.android.BaseFragment
import iCode.utility.DateFormatHelper
import java.util.Calendar


class ExtendOrderFragment : BaseFragment(), ExtendOrderFragmentContract.View {

    private val binding: FragMowExtendOrderBinding by bindingLazy()
    private val destinationAdapter: DestinationAdapter by lazy { DestinationAdapter(1) }
    override val layoutResId = R.layout.frag_mow_extend_order

    private lateinit var homeActivity: HomeActivity
    private lateinit var dataRepository: DataRepository
    private lateinit var presenter: ExtendOrderFragmentContract.Presenter
    private var blackoutDatesList = listOf<BlackoutDatesRs>()


    private val saveOrderRq = SaveOrderRq()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeActivity = activity as HomeActivity
        dataRepository = Injection.provideDataRepository()
        presenter = ExtendOrderFragmentPresenter(this, dataRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.getSerializable("order")?.let {
            if(saveOrderRq.localOrderId == 0)
                convertOrderToSaveOrderRq(it as Order)
        }

        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }

        binding.imageViewHome.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }



        binding.textViewNewDepartureDate.setOnClickListener {


            val newDepartureDate = Calendar.getInstance()
            newDepartureDate.timeInMillis = System.currentTimeMillis() - 1000
            if(saveOrderRq.newDepartureDateAndTime != 0L)
                newDepartureDate.timeInMillis = saveOrderRq.newDepartureDateAndTime - 1000


            val todayDate =  if (saveOrderRq.departureDateAndTime != 0L) saveOrderRq.departureDateAndTime - 1000 else System.currentTimeMillis() - 1000
//           calendar.timeInMillis

            CalendarViewOpen(parentFragmentManager,todayDate,newDepartureDate, blackoutDatesList
            ) { selection ->
                val selectedDate = Calendar.getInstance().apply { timeInMillis = selection }
                newDepartureDate.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR))
                newDepartureDate.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH))
                newDepartureDate.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH))

                saveOrderRq.newDepartureDateAndTime = newDepartureDate.timeInMillis
                binding.textViewNewDepartureDate.setText(
                    DateFormatHelper.getStringFromCalendar(
                        DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                        newDepartureDate
                    )
                )
                /*binding.textViewNewDepartureDate.setText(
                    (monthOfYear + 1).toString() + "/" + dayOfMonth + "/" + year
                )*/
                getOrderBillingProfile()
            }.showDatePicker()

          /*  val datePickerDialog = DatePickerDialog(
                homeActivity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    newDepartureDate.set(Calendar.YEAR, year)
                    newDepartureDate.set(Calendar.MONTH, monthOfYear)
                    newDepartureDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    saveOrderRq.newDepartureDateAndTime = newDepartureDate.timeInMillis
                    binding.textViewNewDepartureDate.setText(
                        DateFormatHelper.getStringFromCalendar(
                            DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                            newDepartureDate
                        )
                    )
                    *//*binding.textViewNewDepartureDate.setText(
                        (monthOfYear + 1).toString() + "/" + dayOfMonth + "/" + year
                    )*//*
                    getOrderBillingProfile()
                }, newDepartureDate.get(Calendar.YEAR),
                newDepartureDate.get(Calendar.MONTH),
                newDepartureDate.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate =
                if (saveOrderRq.departureDateAndTime != 0L) saveOrderRq.departureDateAndTime else System.currentTimeMillis() - 1000
            datePickerDialog.show()*/

        }

        binding.textViewNewDepartureTime.setOnClickListener {

            val newDepartureDate = Calendar.getInstance()
            if(saveOrderRq.newDepartureDateAndTime != 0L)
                newDepartureDate.timeInMillis = saveOrderRq.newDepartureDateAndTime

            newDepartureDate.set(Calendar.SECOND, 0)
            newDepartureDate.set(Calendar.MILLISECOND, 0)


            val mTimePicker = CustomTimePickerDialog(
                homeActivity, object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        newDepartureDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        newDepartureDate.set(Calendar.MINUTE, minute)

                        Log.i("TIMETEST","isTrue: "+(saveOrderRq.departureDateAndTime<newDepartureDate.timeInMillis))

                        if(saveOrderRq.departureDateAndTime<newDepartureDate.timeInMillis) {
                            saveOrderRq.newDepartureDateAndTime = newDepartureDate.timeInMillis
                            binding.textViewNewDepartureTime.setText(
                                DateFormatHelper.getStringFromCalendar(
                                    DateFormatHelper.FORMAT_S_HH_MM_A,
                                    newDepartureDate
                                ).toUpperCase()
                            )


                            getOrderBillingProfile()
                        } else {
                            showToastMessage(getString(R.string.validation_return_time_must_be_greater_than_pick_up_time))
                        }


                    }
                }, newDepartureDate.get(Calendar.HOUR_OF_DAY),
                newDepartureDate.get(Calendar.MINUTE), false,
                CustomTimePickerDialog.ITimeBound {
                    showToastMessage(it)
                }
            )

            mTimePicker.show()


            /*val tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                { view, hourOfDay, minute, _ ->

                    newDepartureDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    newDepartureDate.set(Calendar.MINUTE, minute)

                    saveOrderRq.newDepartureDateAndTime = newDepartureDate.timeInMillis
                    binding.textViewNewDepartureTime.setText(
                        DateFormatHelper.getStringFromCalendar(
                            DateFormatHelper.FORMAT_S_HH_MM_A,
                            newDepartureDate
                        ).toUpperCase()
                    )


                    getOrderBillingProfile()

                },
                newDepartureDate.get(Calendar.HOUR_OF_DAY),
                newDepartureDate.get(Calendar.MINUTE),
                false
            )
            tpd.version = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_1
            tpd.setTimeInterval(1, 5)
            tpd.show(requireFragmentManager(), "Timepickerdialog")*/

        }


        binding.textViewUpdateReservation.setOnClickListener {



            if(saveOrderRq.locationID != 0 && saveOrderRq.billingProfileID != 0 && saveOrderRq.deviceTypeID != 0) {

                if(saveOrderRq.localOrderId == 0) {

                    OrderData.clearData()
                    saveOrderRq.localOrderId = kotlin.random.Random.nextInt()
                    saveOrderRq.deliveryFee = 0.0F
                    saveOrderRq.isExtendOrder = true
                    OrderData.add(saveOrderRq)
                    OrderData.removeBonusAndPromo()
                } else {
                    saveOrderRq.deliveryFee = 0.0F
                    saveOrderRq.isExtendOrder = true
                    OrderData.update(saveOrderRq)
                    OrderData.removeBonusAndPromo()
                }
                homeActivity.showReservedItemFragment(true)
            }


        }


        binding.textViewLblRiderRewardsPolicy.setOnClickListener {
            presenter.getRewardPolicy()
        }

        binding.textViewLblOperator.setOnClickListener {
            presenter.getEntityDefination(true)
        }

        binding.textViewLblOccupant.setOnClickListener {
            presenter.getEntityDefination(false)
        }


        presenter.getLocationById(saveOrderRq.locationID)
        presenter.getDeviceTypeByID(saveOrderRq.deviceTypeID)
        presenter.getOperatorByID(saveOrderRq.operatorID)
        presenter.getBlackoutDatesByLocationAndDevice(saveOrderRq.deviceTypeID)

        if (saveOrderRq.occupantID != 0)
            presenter.getOccupantByID(saveOrderRq.occupantID)

        showOrderInfo()
    }


    private fun getOrderBillingProfile() {

        val pickupDate = binding.textViewArrivalDate.text.toString()
        val returnDate = binding.textViewDepartureDate.text.toString()
        val pickupTime = binding.textViewArrivalTime.text.toString()
        val returnTime = binding.textViewDepartureTime.text.toString()

        val newReturnDate = binding.textViewNewDepartureDate.text.toString()
        val newReturnTime = binding.textViewNewDepartureTime.text.toString()


        presenter.getOrderBillingProfile(
            pickupDate, pickupTime, returnDate, returnTime, saveOrderRq.deviceTypeID, saveOrderRq.locationID, newReturnDate,
            newReturnTime, saveOrderRq.primaryOrderId
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
        Log.e("Nilam----->>>>", "-->${saveOrderRq.billingRegularPrice}")
        Log.e("Nilam----->>>>", "-->${saveOrderRq.chairPadPrice}")

        binding.textViewPrice.text = String.format(getString(R.string.sub_total), Utility.convertUpTo2Decimal((saveOrderRq.billingRegularPrice + saveOrderRq.chairPadPrice)))
        binding.textViewErrorOfBillingProfile.visibility = View.GONE

        checkButtonStatus()

    }

    override fun getOrderBillingProfileFail(errorMessage: String?) {
        if (errorMessage.isNullOrEmpty()) {
            binding.textViewErrorOfBillingProfile.visibility = View.GONE
        } else {

            binding.textViewErrorOfBillingProfile.visibility = View.VISIBLE
            binding.textViewErrorOfBillingProfile.text = errorMessage
            //nilam set update reservation button desilect
            saveOrderRq.billingProfileID = 0
            checkButtonStatus()

        }
    }

    override fun showLocation(location: com.data.model.destination.Location) {

        saveOrderRq.locationID = location.id
        saveOrderRq.locationName = location.locationName
        saveOrderRq.taxRate = location.taxRate
        saveOrderRq.deliveryFee = location.deliveryFee
        saveOrderRq.isGenerateAcceptBonusDay = location.isGenerateAcceptBonusDay

    }

    override fun getLocationFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showDeviceInfo(deviceInfo: DeviceInfo) {

        saveOrderRq.chairPadPrice = deviceInfo.chairPadPrice
        saveOrderRq.description = deviceInfo.description
        saveOrderRq.devicePropertyIDs = deviceInfo.devicePropertyIDs
        saveOrderRq.deviceTypeName = deviceInfo.deviceTypeName
        saveOrderRq.deviceTypeShortName = deviceInfo.deviceTypeShortName
        saveOrderRq.operatorOccupantSame = deviceInfo.operatorOccupantSame
        saveOrderRq.itemName = deviceInfo.deviceTypeName


        if(saveOrderRq.itemImagePath.isEmpty())
            saveOrderRq.itemImagePath = deviceInfo.itemImagePath

        checkButtonStatus()

        if(saveOrderRq.devicePropertyIDs.contains("1")) {
            presenter.getDevicePropertyOptions(DeviceOptionID.JOYSTICK_POSITION)
        }
        if(saveOrderRq.devicePropertyIDs.contains("2")) {
            presenter.getDevicePropertyOptions(DeviceOptionID.PREFERRED_WHEEL_CHAIR_SIZE)
        }
        if(saveOrderRq.devicePropertyIDs.contains("3")) {
            presenter.getDevicePropertyOptions(DeviceOptionID.CHAIR_PAD_REQUIREMENT)
        }
        if(saveOrderRq.devicePropertyIDs.contains("4")) {
            presenter.getDevicePropertyOptions(DeviceOptionID.HAND_CONTROLLER)
        }
    }

    override fun getDeviceInfoFail(errorMessage: String?) {
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

    override fun showOperator(operator: User) {

        saveOrderRq.operatorName = operator.firstName+" "+operator.lastName
        saveOrderRq.isDefault = operator.isDefault

        binding.textViewSelectOperatorOccupant.text = String.format(getString(R.string.full_name), operator.firstName, operator.lastName)
        if (operator.occupantID == 0) {
            binding.textViewLblOccupant.visibility = View.GONE
            binding.textViewSelectOccupant.visibility = View.GONE
            binding.textViewLblNewOccupant.visibility = View.GONE
        }
    }

    override fun getOperatorFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showOccupant(occupant: User) {

        saveOrderRq.occupantName = occupant.fullName
        binding.textViewSelectOccupant.text = occupant.fullName
    }

    override fun getOccupantFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showDevicePropertyOptions(
        deviceOptionID: DeviceOptionID,
        devicePropertyList: List<DeviceProperty>
    ) {



        when (deviceOptionID) {
            DeviceOptionID.JOYSTICK_POSITION -> {

                binding.textViewLblJoystickPosition.visibility = View.VISIBLE
                binding.checkBoxJoystickPosition.visibility = View.VISIBLE

                val filterItemList = devicePropertyList.filter { it.devicePropertyOptionID == saveOrderRq.joystickID }
                if(filterItemList.isNotEmpty()) {
                    binding.checkBoxJoystickPosition.text = filterItemList[0].devicePropertyOption
                    saveOrderRq.joystickName = filterItemList[0].devicePropertyOption
                }

            }
            DeviceOptionID.PREFERRED_WHEEL_CHAIR_SIZE -> {

                binding.textViewLblPreferredWheelchairSize.visibility = View.VISIBLE
                binding.checkBoxPreferredWheelchairSize.visibility = View.VISIBLE


                val filterItemList = devicePropertyList.filter { it.devicePropertyOptionID == saveOrderRq.wheelchairSizeID }
                if(filterItemList.isNotEmpty()) {
                    binding.checkBoxPreferredWheelchairSize.text = filterItemList[0].devicePropertyOption
                    saveOrderRq.wheelchairSizeName = filterItemList[0].devicePropertyOption
                }

            }

            DeviceOptionID.CHAIR_PAD_REQUIREMENT -> {

                binding.textViewLblChairPadRequirement.visibility = View.VISIBLE
                binding.checkBoxChairPadRequirement.visibility = View.VISIBLE

                val filterItemList = devicePropertyList.filter { it.devicePropertyOptionID == saveOrderRq.chairpadID }
                if(filterItemList.isNotEmpty()) {

                    binding.checkBoxChairPadRequirement.text = String.format(getString(R.string.chair_pad_requirement), filterItemList[0].devicePropertyOption, Utility.convertUpTo2Decimal(saveOrderRq.chairPadPrice))
                    saveOrderRq.chairpadName = filterItemList[0].devicePropertyOption
                }

            }
            DeviceOptionID.HAND_CONTROLLER -> {

                binding.textViewLblHandController.visibility = View.VISIBLE
                binding.checkBoxHandController.visibility = View.VISIBLE


                val filterItemList = devicePropertyList.filter { it.devicePropertyOptionID == saveOrderRq.handControllerID }
                if(filterItemList.isNotEmpty()) {
                    binding.checkBoxHandController.text = filterItemList[0].devicePropertyOption
                    saveOrderRq.handControllerName = filterItemList[0].devicePropertyOption
                }
            }
            else -> { // Note the block

            }
        }

    }

    override fun getDevicePropertyOptionsFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    private fun showOrderInfo() {

        if (saveOrderRq.occupantName.isNotEmpty())
            binding.textViewSelectOccupant.text = saveOrderRq.occupantName

        if (saveOrderRq.operatorName.isNotEmpty())
            binding.textViewSelectOperatorOccupant.text = saveOrderRq.operatorName


        if(saveOrderRq.newDepartureDateAndTime != 0L) {

            val newDepartureDate = Calendar.getInstance()
            newDepartureDate.timeInMillis = saveOrderRq.newDepartureDateAndTime

            binding.textViewNewDepartureDate.setText(
                DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_MM_DD_YYYY_S, newDepartureDate)
            )

            binding.textViewNewDepartureTime.setText(
                DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_S_HH_MM_A, newDepartureDate)
                    .toUpperCase()
            )
        }

        binding.textViewRentalPeriod.text = saveOrderRq.billingDescription
        //12. Order Extend - last use reward point appear
        //Nilam hide below line for hiding reward point
      //  binding.textViewRiderRewardsPointsEarned.text = saveOrderRq.billingRewardPoint.toString()

        Log.e("Nilam----->>>>", "${saveOrderRq.billingRegularPrice}")
        Log.e("Nilam----->>>>", "${saveOrderRq.chairPadPrice}")

        // Nilam change price
      //  binding.textViewPrice.text = String.format(getString(R.string.sub_total), "0.00")
        if (saveOrderRq.billingRegularPrice != 0F )
            binding.textViewPrice.text = String.format(getString(R.string.sub_total), Utility.convertUpTo2Decimal((saveOrderRq.billingRegularPrice + saveOrderRq.chairPadPrice)))
        else
            binding.textViewPrice.text = String.format(getString(R.string.sub_total), "0.00")


        binding.textViewErrorOfBillingProfile.visibility = View.GONE

        binding.checkBoxCustomerPickLocation.text = saveOrderRq.pickUpLocationName

        if(saveOrderRq.isShippingAddress) {
            binding.textViewOtherPickupLocation.visibility = View.VISIBLE
            binding.textViewOtherPickupLocation.text = saveOrderRq.shippingAddressLine1+"\n"+saveOrderRq.shippingAddressLine2+", "+saveOrderRq.shippingCity+"\n"+saveOrderRq.shippingStateName+","+saveOrderRq.shippingZipcode+"\nNote: "+saveOrderRq.shippingDeliveryNote
        }

        binding.textViewSelectAccessoryType.text = saveOrderRq.accessoryTypeName

        val arrivalDateCal = Calendar.getInstance()
        arrivalDateCal.timeInMillis = saveOrderRq.arrivalDateAndTime

        binding.textViewArrivalDate.text = DateFormatHelper.getStringFromCalendar(
            DateFormatHelper.FORMAT_MM_DD_YYYY_S,
            arrivalDateCal
        )
        binding.textViewArrivalTime.text = DateFormatHelper.getStringFromCalendar(
            DateFormatHelper.FORMAT_S_HH_MM_A,
            arrivalDateCal
        ).toUpperCase()

        val departureDateCal = Calendar.getInstance()
        departureDateCal.timeInMillis = saveOrderRq.departureDateAndTime

        binding.textViewDepartureDate.text = DateFormatHelper.getStringFromCalendar(
            DateFormatHelper.FORMAT_MM_DD_YYYY_S,
            departureDateCal
        )
        binding.textViewDepartureTime.text = DateFormatHelper.getStringFromCalendar(
            DateFormatHelper.FORMAT_S_HH_MM_A,
            departureDateCal
        ).toUpperCase()

        checkButtonStatus()
    }

    private fun convertOrderToSaveOrderRq(order: Order) {


        //DEVICE INFORMATION
        saveOrderRq.deviceTypeID = order.deviceTypeID
        saveOrderRq.inventoryID = order.inventoryID
        saveOrderRq.itemImagePath = order.itemImagePath
        saveOrderRq.itemName = order.itemName

        //DEVICE_INFO DETAIL
        saveOrderRq.chairPadPrice = order.chairPadPrice
        saveOrderRq.devicePropertyIDs = order.devicePropertyIDs
        saveOrderRq.deviceTypeName = order.deviceTypeName

        //OPERATOR INFO
        saveOrderRq.operatorID = order.operatorID

        //OCCUPANT INFO
        saveOrderRq.occupantID = order.occupantID

        //MAIN LOCATION INFO
        saveOrderRq.locationID = order.locationID


        //PICKUP LOCATION INFO
        saveOrderRq.pickUpLocationID = order.customerPickupLocationID.toInt()
        saveOrderRq.pickUpLocationName = order.customerPickupLocation


        //ACCESSORY INFO
        saveOrderRq.accessoryID = order.accessoryTypeID
        saveOrderRq.accessoryTypeName = order.accessoryType


        //JOYSTICK INFO
        if(order.joyStickPosition.isNotEmpty())
            saveOrderRq.joystickID = order.joyStickPosition.toInt()

        //WHEELCHAIR INFO
        if(order.preferredWheelchairSize.isNotEmpty())
            saveOrderRq.wheelchairSizeID = order.preferredWheelchairSize.toInt()

        //CHAIRPAD INFO
        if(order.chairPad.isNotEmpty())
            saveOrderRq.chairpadID = order.chairPad.toInt()

        //HANDCONTROLLER INFO
        if(order.handController.isNotEmpty())
            saveOrderRq.handControllerID = order.handController.toInt()

        //ARRIVAL AND DEPARTURE DATE&TIME
        val arrivalDateCal = DateFormatHelper.getCalendarFromStringDate(
            DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_A,
            (order.pickUpDate+" "+order.pickUpTime).toLowerCase()
        )
        /*val arrivalTimeCal = DateFormatHelper.getCalendarFromStringDate(
            DateFormatHelper.FORMAT_HH_MM_A,
            order.pickUpTime
        )

        val arrivalCal = Calendar.getInstance()
        arrivalCal.timeInMillis = arrivalDateCal.timeInMillis

        arrivalCal.set(Calendar.HOUR_OF_DAY, arrivalTimeCal.get(Calendar.HOUR_OF_DAY))
        arrivalCal.set(Calendar.MINUTE, arrivalTimeCal.get(Calendar.MINUTE))
        arrivalCal.set(Calendar.SECOND, 0)
        arrivalCal.set(Calendar.MILLISECOND, 0)*/

        saveOrderRq.arrivalDateAndTime = arrivalDateCal.timeInMillis


        val departureDateCal = DateFormatHelper.getCalendarFromStringDate(
            DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_A,
            (order.returnDate+" "+order.returnTime).toLowerCase()
        )
        /*val departureTimeCal = DateFormatHelper.getCalendarFromStringDate(
            DateFormatHelper.FORMAT_HH_MM_A,
            order.returnTime
        )*/

        /*val departureCal = Calendar.getInstance()
        departureCal.timeInMillis = departureDateCal.timeInMillis

        departureCal.set(Calendar.HOUR_OF_DAY, departureTimeCal.get(Calendar.HOUR_OF_DAY))
        departureCal.set(Calendar.MINUTE, departureTimeCal.get(Calendar.MINUTE))
        departureCal.set(Calendar.SECOND, 0)
        departureCal.set(Calendar.MILLISECOND, 0)*/

        saveOrderRq.departureDateAndTime = departureDateCal.timeInMillis

        //ADDRESS RELATED INFO
        saveOrderRq.isShippingAddress = order.isShippingAddress
        saveOrderRq.shippingAddressLine1 = order.shippingAddressLine1
        saveOrderRq.shippingAddressLine2 = order.shippingAddressLine2
        saveOrderRq.shippingZipcode = order.shippingZipcode
        saveOrderRq.shippingCity = order.shippingCity
        saveOrderRq.shippingDeliveryNote = order.shippingDeliveryNote
        saveOrderRq.shippingStateID = order.shippingStateID
        saveOrderRq.shippingStateName = order.shippingStateName
        Log.e("Nilam","price take value here ${order?.price}")

        //BILLING PROFILE TEMP
        saveOrderRq.billingDescription = order.rentalPeriod
        saveOrderRq.billingRegularPrice = order.price

        saveOrderRq.billingRewardPoint = order.rewardPoint
       // saveOrderRq.billingTotalPrice = order.price

        //2. ORDER INFO


        saveOrderRq.orderId = order.primaryOrderID
        saveOrderRq.isPrimaryOrder = false
        saveOrderRq.primaryOrderId = order.primaryOrderID
        saveOrderRq.customerID = dataRepository.user.id


    }

    private fun checkButtonStatus() {

        if(saveOrderRq.billingProfileID != 0) {
            binding.textViewUpdateReservation.setBackgroundResource(R.drawable.bg_mow_rounded_blue_button)
        } else {
            binding.textViewUpdateReservation.setBackgroundResource(R.drawable.bg_mow_rounded_gray_button)
        }
    }

    override fun showBlackDatesList(datesList: List<BlackoutDatesRs>) {
        this.blackoutDatesList = datesList
        val json = Gson().toJson(blackoutDatesList)
        Log.e("Nilam", json)


    }
    override fun getBlackDatesListFail(errorMessage: String?) {
        Log.e("Nilam --->getBlackDatesListFail method fail message", errorMessage.toString())

    }

    companion object {

        fun newInstance(order: Order): ExtendOrderFragment {

            val bundle = Bundle()
            bundle.putSerializable("order", order)
            val extendOrderFragment = ExtendOrderFragment()
            extendOrderFragment.arguments = bundle
            return extendOrderFragment
        }
    }
}