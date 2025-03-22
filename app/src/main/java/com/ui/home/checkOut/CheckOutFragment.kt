package com.ui.home.checkOut

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.data.DataRepository
import com.data.Injection
import com.data.model.order.BonusDayBillingProfileInfo
import com.data.model.order.Card
import com.data.model.order.ECheckCard
import com.data.model.order.OrderData
import com.data.model.user.Config
import com.data.model.user.User
import com.data.objectforupdate.DialogUtils
import com.data.objectforupdate.UpdateAppContract
import com.data.objectforupdate.updateAppPresenter
import com.data.remote.response.order.ApplyBonusRs
import com.data.remote.response.order.GetDeviceTrainingVideoAndDataRs
import com.data.remote.response.order.GetDisclaimerOfPaymentMethodRs
import com.data.remote.response.order.GetProcessingFeeRs
import com.data.remote.response.order.GetPromotionDetailByLocationIDNCodeRs
import com.data.remote.response.order.SaveOrderListRs
import com.data.remote.response.user.ValidateLicenseRs
import com.google.gson.Gson
import com.mow.cash.android.BuildConfig
import com.mow.cash.android.R
import com.mow.cash.android.databinding.FragMowCheckoutBinding
import com.ui.home.HomeActivity
import com.ui.home.videoTrainingRentalAgreementTermsAndConditions.VideoRentalTermsAndConditionsDialogFragment
import com.util.Utility
import com.util.showHtmlText
import iCode.android.BaseFragment
import iCode.utility.DateFormatHelper
import iCode.utility.PrefHelper
import java.util.Calendar


class CheckOutFragment : BaseFragment(), CheckOutFragmentContract.View, UpdateAppContract.View {

    private val REQUEST_CODE = 111


    private val binding: FragMowCheckoutBinding by bindingLazy()
    override val layoutResId = R.layout.frag_mow_checkout

    private val creditCardListAdapter: CardListAdapter by lazy { CardListAdapter() }
    private val eCheckListAdapter: ECheckCardListAdapter by lazy { ECheckCardListAdapter() }
    private val orderDetailAdapter: OrderDetailAdapter by lazy { OrderDetailAdapter() }

    private lateinit var homeActivity: HomeActivity
    private lateinit var presenter: CheckOutFragmentContract.Presenter
    private lateinit var presenter_updateApp: UpdateAppContract.Presenter
    private lateinit var dataRepository: DataRepository
    private var isProcessOrderButtonClick: Boolean = false

    private var isFromBackCreditCard: Boolean = false
    private var isFromBackECheckCard: Boolean = false

    private var customer: User? = null
    private var card: Card? = null
    private var isCreditCardRadioButtonSelected: Boolean = false
    private var eCheckcard: ECheckCard? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity
        dataRepository = Injection.provideDataRepository()
        Log.e("Nilam", "onCreate run-----*********")


        presenter = CheckOutFragmentPresenter(this, dataRepository)
        presenter_updateApp = updateAppPresenter(this, dataRepository)
    }

    @SuppressLint("ClickableViewAccessibility", "SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFromBackCreditCard = false;
        isFromBackECheckCard = false;
        this@CheckOutFragment.setProgressBar(true)
        Log.e("Nilam", "onViewCreated run-----******** $isCreditCardRadioButtonSelected")
        parentFragmentManager.setFragmentResultListener(
            "requestKeyCreditCard",
            viewLifecycleOwner
        ) { requestKey, bundle ->
            // Handle the result here
            val valueCreditCard = bundle.getBoolean("CreditCard")
            isFromBackCreditCard = valueCreditCard;
            Log.d("Nilam", "Received value requestKeyCreditCard: $isFromBackCreditCard")
        }
        parentFragmentManager.setFragmentResultListener(
            "requestKeyEcheck",
            viewLifecycleOwner
        ) { requestKey, bundle ->
            // Handle the result here
            val valueEcheckCard = bundle.getBoolean("eCheckCard")
            isFromBackECheckCard = valueEcheckCard;
            Log.d("Nilam", "Received value requestKeyEcheck: $isFromBackCreditCard")
        }
        // Nilam add html logo in checkout page start
        binding.webViewLogo.setOnTouchListener { _, _ -> true }
        binding.webViewLogo.settings.javaScriptEnabled = true
        binding.webViewLogo.isVerticalScrollBarEnabled = false
        binding.webViewLogo.isHorizontalScrollBarEnabled = false
        binding.webViewLogo.getSettings().defaultZoom = WebSettings.ZoomDensity.FAR
        binding.webViewLogo.webViewClient = WebViewClient()
        val htmlTextLogo = homeActivity.getString(R.string.html_text_logo)
        binding.webViewLogo.loadDataWithBaseURL(null, htmlTextLogo, "text/html", "UTF-8", null)
        // Nilam add html logo in checkout page end

        binding.recycleViewCardList.adapter = creditCardListAdapter
        creditCardListAdapter.actionCallback = {
            card = it
            checkButtonStatus()
        }

        binding.recycleViewECheckList.adapter = eCheckListAdapter
        eCheckListAdapter.actionCallback = {
            eCheckcard = it
        }



        binding.creditCardRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.eCheckRadioButton.isChecked = false
                isCreditCardRadioButtonSelected = true
                Log.e(
                    "Nilam",
                    "checked Id ---->>> creditCardRadioButton ---> ${isCreditCardRadioButtonSelected}"
                )

                binding.spaceBetweenView.visibility = View.VISIBLE
//                binding.textViewLblCreditAndDebitCard.visibility = View.VISIBLE
                binding.cardBelowLine.visibility = View.VISIBLE
                binding.CreditCardBackground.visibility = View.VISIBLE
                binding.textViewLblPaySecurely.visibility = View.VISIBLE
                binding.recycleViewCardList.visibility = View.VISIBLE
                binding.imageViewAddNew.visibility = View.VISIBLE
                binding.textViewLblAddNewCreditDebitCard.visibility = View.VISIBLE
                binding.echeckBelowLine.visibility = View.GONE
                binding.eCheckBackground.visibility = View.GONE
//                binding.textViewLblECheck.visibility = View.GONE
                binding.textViewLblECheckSecurely.visibility = View.GONE
                binding.recycleViewECheckList.visibility = View.GONE
                binding.imageViewECheckAddNew.visibility = View.GONE
                binding.textViewLblAddCustomerECheck.visibility = View.GONE
                val totalAmount =
                    Utility.convertUpTo2Decimal((OrderData.getSubTotal(true) + OrderData.getDeliveryFee() + OrderData.getTax()));

                presenter.getProcessingFee(
                    isCreditCardRadioButtonSelected,
                    !isCreditCardRadioButtonSelected,
                    totalAmount
                )

            }
        }

        binding.eCheckRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.creditCardRadioButton.isChecked = false
                isCreditCardRadioButtonSelected = false
                Log.e(
                    "Nilam",
                    "checked Id ---->>> eCheckRadioButton $isCreditCardRadioButtonSelected"
                )

                binding.spaceBetweenView.visibility = View.GONE
//                binding.textViewLblCreditAndDebitCard.visibility = View.GONE
                binding.textViewLblPaySecurely.visibility = View.GONE
                binding.recycleViewCardList.visibility = View.GONE
                binding.imageViewAddNew.visibility = View.GONE
                binding.cardBelowLine.visibility = View.GONE
                binding.CreditCardBackground.visibility = View.GONE
                binding.textViewLblAddNewCreditDebitCard.visibility = View.GONE
//                binding.textViewLblECheck.visibility = View.VISIBLE
                binding.textViewLblECheckSecurely.visibility = View.VISIBLE
                binding.recycleViewECheckList.visibility = View.VISIBLE
                binding.echeckBelowLine.visibility = View.VISIBLE
                binding.eCheckBackground.visibility = View.VISIBLE

                binding.imageViewECheckAddNew.visibility = View.VISIBLE
                binding.textViewLblAddCustomerECheck.visibility = View.VISIBLE
                val totalAmount =
                    Utility.convertUpTo2Decimal((OrderData.getSubTotal(true) + OrderData.getDeliveryFee() + OrderData.getTax()));

                presenter.getProcessingFee(
                    isCreditCardRadioButtonSelected,
                    !isCreditCardRadioButtonSelected,
                    totalAmount
                )

            }
        }


        eCheckListAdapter.actionDeleteCallback = {

            val builder = AlertDialog.Builder(homeActivity)
            builder.setMessage(getString(R.string.alert_are_you_sure_you_want_to_delete_selected_e_check_card))
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    eCheckcard?.let { selectedCard ->
                        if (selectedCard.id == it.id) {
                            eCheckcard = null
                            checkButtonStatus()
                        }
                    }

                    presenter.deleteAuthorizedPaymentProfile(it.id, true)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
            builder.show()
        }

        creditCardListAdapter.actionDeleteCallback = {

            val builder = AlertDialog.Builder(homeActivity)
            builder.setMessage(getString(R.string.alert_are_you_sure_you_want_to_delete_selected_credit_debit_card))
                .setPositiveButton(getString(R.string.ok)) { _, _ ->

                    card?.let { selectedCard ->
                        if (selectedCard.id == it.id) {
                            card = null
                            checkButtonStatus()
                        }
                    }

                    presenter.deleteAuthorizedPaymentProfile(it.id, false)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
            builder.show()
        }


        binding.recycleViewOrderDetails.adapter = orderDetailAdapter
        orderDetailAdapter.submitList(OrderData.get())

        binding.textViewPlaceOrder.setOnClickListener {
            //homeActivity.showThankYouFragment()

            callPlaceOrderApi()
        }

        binding.textViewCheckboxText.setOnClickListener {
            if (binding.creditCardRadioButton.isChecked) {
                if (card != null) {
                    presenter.getDeviceTrainingVideoAndData(true)
                } else {
                    showToastMessage(getString(R.string.validation_please_select_your_payment_card))
                }
            } else {
                Log.e(
                    "Nilam",
                    "checked Id ---->>> eCheckRadioButton 2222  -->$isCreditCardRadioButtonSelected"
                )

                if (eCheckcard != null) {
                    presenter.getDeviceTrainingVideoAndData(true)
                } else {
                    showToastMessage(getString(R.string.validation_please_select_your_e_check_bank))
                }

            }
        }

        binding.textViewBack.setOnClickListener {
            homeActivity.onBackPressed()
        }

        binding.imageViewHome.setOnClickListener {
            homeActivity.supportFragmentManager.popBackStackImmediate(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

        binding.textViewReadCreditCard.setOnClickListener {
            homeActivity.showCreditCardIssuerAggreementDialogFragment()
        }

        binding.imageViewAddNew.setOnClickListener {

            homeActivity.showAddCreditDebitCardFragment()
        }
        // Nilam open Add Customer E-Check page
        binding.imageViewECheckAddNew.setOnClickListener {
            homeActivity.showAddCustomerECheckFragment()
        }

        binding.textViewLblAddNewCreditDebitCard.setOnClickListener {

            homeActivity.showAddCreditDebitCardFragment()
        }
        binding.textViewLblChangeAddress.setOnClickListener {
            homeActivity.showBillingAddressFragment()
        }

        binding.textTextlblPromoCode.setOnClickListener {
            if (binding.editTextTextPromoCode.text.toString().isNotEmpty()) {
                presenter.applyPromoCode(binding.editTextTextPromoCode.text.toString())
            }
        }

        binding.checkBoxRiderRewards.setOnClickListener {
            if (OrderData.isBonusBillingProfile()) {
                binding.checkBoxRiderRewards.isChecked = false;
            } else {
                binding.checkBoxRiderRewards.isChecked = true;
            }
            if (binding.checkBoxRiderRewards.isChecked) {
                //apply bonus

                Log.e("Nilam", "checkbox is checked");
                val builder = AlertDialog.Builder(homeActivity)
                builder.setMessage(getString(R.string.alert_are_you_sure_you_want_to_select_bonus_day_profile))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        val saveOrderRqList = OrderData.get()

                        if (saveOrderRqList.isNotEmpty()) {
                            val saveOrderRq = saveOrderRqList[0]
                            val arrivalDateCal = Calendar.getInstance()
                            val departureDateCal = Calendar.getInstance()

                            if (saveOrderRq.newDepartureDateAndTime == 0L) {
                                arrivalDateCal.timeInMillis = saveOrderRq.arrivalDateAndTime
                                departureDateCal.timeInMillis = saveOrderRq.departureDateAndTime
                            } else {
                                arrivalDateCal.timeInMillis = saveOrderRq.departureDateAndTime
                                departureDateCal.timeInMillis = saveOrderRq.newDepartureDateAndTime
                            }

                            presenter.applyBonus(
                                saveOrderRq.chairPadPrice,
                                saveOrderRq.locationID,
                                saveOrderRq.deviceTypeID,
                                DateFormatHelper.getStringFromCalendar(
                                    DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                                    arrivalDateCal
                                ),
                                DateFormatHelper.getStringFromCalendar(
                                    DateFormatHelper.FORMAT_HH_MM_A_,
                                    arrivalDateCal
                                ).toUpperCase(),
                                DateFormatHelper.getStringFromCalendar(
                                    DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                                    departureDateCal
                                ),
                                DateFormatHelper.getStringFromCalendar(
                                    DateFormatHelper.FORMAT_HH_MM_A_,
                                    departureDateCal
                                ).toUpperCase(),
                                customer?.rewardsPoint?.toInt() ?: 0,
                                saveOrderRq.deliveryFee,
                                saveOrderRq.taxRate,
                                saveOrderRq.billingRegularPrice,
                                true
                            )
                        }
                    }
                    .setNegativeButton(R.string.cancel) { _, _ ->

                        binding.checkBoxRiderRewards.isChecked = false
                    }
                builder.show()
            } else {
                //reverse apply bonus

                Log.e("Nilam", "checkbox is unchecked");

                val builder = AlertDialog.Builder(homeActivity)
                builder.setMessage(getString(R.string.alert_are_you_sure_you_want_to_unselect_bonus_day_profile))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        val saveOrderRqList = OrderData.get()

                        if (saveOrderRqList.isNotEmpty()) {
                            val saveOrderRq = saveOrderRqList[0]

                            val arrivalDateCal = Calendar.getInstance()
                            val departureDateCal = Calendar.getInstance()

                            if (saveOrderRq.newDepartureDateAndTime == 0L) {
                                arrivalDateCal.timeInMillis = saveOrderRq.arrivalDateAndTime
                                departureDateCal.timeInMillis = saveOrderRq.departureDateAndTime
                            } else {
                                arrivalDateCal.timeInMillis = saveOrderRq.departureDateAndTime
                                departureDateCal.timeInMillis = saveOrderRq.newDepartureDateAndTime
                            }

                            presenter.applyBonus(
                                saveOrderRq.chairPadPrice,
                                saveOrderRq.locationID,
                                saveOrderRq.deviceTypeID,
                                DateFormatHelper.getStringFromCalendar(
                                    DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                                    arrivalDateCal
                                ),
                                DateFormatHelper.getStringFromCalendar(
                                    DateFormatHelper.FORMAT_HH_MM_A_,
                                    arrivalDateCal
                                ).toUpperCase(),
                                DateFormatHelper.getStringFromCalendar(
                                    DateFormatHelper.FORMAT_MM_DD_YYYY_S,
                                    departureDateCal
                                ),
                                DateFormatHelper.getStringFromCalendar(
                                    DateFormatHelper.FORMAT_HH_MM_A_,
                                    departureDateCal
                                ).toUpperCase(),
                                customer?.rewardsPoint?.toInt() ?: 0,
                                saveOrderRq.deliveryFee,
                                saveOrderRq.taxRate,
                                saveOrderRq.billingRegularPrice,
                                false
                            )
                        }
                    }
                    .setNegativeButton(R.string.cancel) { _, _ ->

                        binding.checkBoxRiderRewards.isChecked = true
                    }
                builder.show()
            }


        }

        binding.iconMoweCheck.setOnClickListener {
            Log.e("Nilam", "iconMoweCheck ---->>> clickedddddddd")
            presenter.getDisclaimerOfPaymentMethod("8")
        }

        binding.iconMowCreditCard.setOnClickListener {
            Log.e("Nilam", "iconMowCreditCard ---->>> clickedddddddd")
            presenter.getDisclaimerOfPaymentMethod("1")

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

        binding.editTextNote.filters = arrayOf<InputFilter>(AllCaps())





        setTotalView()
        // nilam add below line for force update app
        presenter_updateApp.updateAppConfig()

        /*  presenter.getCardList()
          presenter.getECheckList()
          presenter.getCustomer()
          presenter.getDeviceTrainingVideoAndData(false)*/


    }

    override fun onResume() {
        super.onResume()
        // Check if returning to this fragment
        Log.d("FragmentState", "Returned to this fragment")
        // Perform actions if necessary, such as refreshing data
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showCardList(cardList: List<Card>) {

        creditCardListAdapter.submitList(cardList.reversed())
        cardList.reversed().let {
            if (it.isNotEmpty() && isFromBackCreditCard) {


                // If returning, select the last item
                val firstItem = it.first()
                card = firstItem
                Log.e("Nilam", "Returning: Selected last one --> ${firstItem.cardNumber}")
                creditCardListAdapter.selectedCard = card
                creditCardListAdapter.notifyDataSetChanged()


            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showECheckList(eCheckCardList: List<ECheckCard>) {
        eCheckListAdapter.submitList(eCheckCardList.reversed())
        eCheckCardList.reversed().let {
            if (it.isNotEmpty() && isFromBackECheckCard) {

                // If returning, select the last item
                val firstItem = it.first()
                eCheckcard = firstItem
                Log.e("Nilam", "Returning: Selected last one --> ${firstItem.bankAccountLast4}")
                eCheckListAdapter.selectedCard = firstItem
                eCheckListAdapter.notifyDataSetChanged() // Refresh selection

            }
        }

    }

    override fun getCardListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun getECheckListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showCustomer(customer: User) {

        this.customer = customer
        binding.textViewRiderRewards.text =
            String.format(
                getString(R.string.rider_rewards),
                Utility.convertUpTo2Decimal(customer.rewardsPoint),
                (customer.rewardsPoint / 14).toInt()
            )
        if (customer.rewardsPoint < 14) {
            binding.checkBoxRiderRewards.isClickable = false
            binding.checkBoxRiderRewards.isEnabled = false
        }



        binding.textViewBillingAddress.text = String.format(
            getString(R.string.billing_address), customer.billAddress1,
            customer.billAddress2,
            customer.billCity,
            customer.stateName,
            customer.billZip,
            customer.country
        )


        checkButtonStatus()
    }

    override fun getCustomerFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun saveOrderListSuccessfully(saveOrderListRs: SaveOrderListRs?) {

        OrderData.setOrderCompleted()
        saveOrderListRs?.let {
            if (it.result.isStatus) {

                it.total =
                    OrderData.getSubTotal(true) + OrderData.getDeliveryFee() + OrderData.getTax() + OrderData.getProcessingFee()
                it.date = DateFormatHelper.getStringFromCalendar(
                    DateFormatHelper.FORMAT_MMMM_DD_YYYY,
                    Calendar.getInstance()
                )
            }
            homeActivity.showOrderStatusFragment(saveOrderListRs)
        }

    }


    override fun saveOrderListFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showDeviceTrainingVideoAndData(
        isViewDialog: Boolean,
        getDeviceTrainingVideoAndDataRs: GetDeviceTrainingVideoAndDataRs,
    ) {

        if (isViewDialog) {

            PrefHelper.getInstance()
                .putString("termsAndConditionData", presenter.getDeviceTrainingVideoAndDataStr())
            val dialogFragment =
                VideoRentalTermsAndConditionsDialogFragment.newInstance()
            dialogFragment.setTargetFragment(this, REQUEST_CODE)

            homeActivity.supportFragmentManager.beginTransaction().add(
                dialogFragment,
                "VideoRentalTermsAndConditionsDialogFragment"
            )
                .commitAllowingStateLoss()
        } else {
            binding.textViewAgreement.showHtmlText(getDeviceTrainingVideoAndDataRs.deviceTrainingVideoDataList[0].checkOutText)
        }

    }

    override fun getDeviceTrainingVideoAndDataFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun applyPromoCodeSuccessfully(
        promoCode: String,
        getPromotionDetailByLocationIDNCodeRs: GetPromotionDetailByLocationIDNCodeRs,
    ) {

        OrderData.updatePromoCode(
            promoCode, getPromotionDetailByLocationIDNCodeRs.promotionFigure,
            getPromotionDetailByLocationIDNCodeRs.promotionID,
            getPromotionDetailByLocationIDNCodeRs.promotionType
        )
        setTotalView()
    }


    private fun setTotalView() {
        Log.e("Nilam", "checked Id ---->>> setTotalView called $isCreditCardRadioButtonSelected")

        var totalAmount =
            Utility.convertUpTo2Decimal((OrderData.getSubTotal(true) + OrderData.getDeliveryFee() + OrderData.getTax()));

        presenter.getProcessingFee(
            isCreditCardRadioButtonSelected,
            !isCreditCardRadioButtonSelected,
            totalAmount
        )

        binding.textViewSubTotal.text = String.format(
            getString(R.string.sub_total),
            Utility.convertUpTo2Decimal(OrderData.getSubTotal(true))
        )
        binding.textViewDeliveryFee.text =
            String.format(
                getString(R.string.sub_total),
                Utility.convertUpTo2Decimal(OrderData.getDeliveryFee())
            )
        /*binding.textViewTotal.text =
            String.format(
                getString(R.string.total),
                Utility.convertUpTo2Decimal((OrderData.getSubTotal(true) + OrderData.getDeliveryFee() + OrderData.getTax()))
            )

*/
        binding.textViewIncludesTax.text = String.format(
            getString(R.string.includes_tax),
            Utility.convertUpTo2Decimal(OrderData.getTax())
        )

        if (OrderData.isPromoCodeUsed()) {
            //binding.viewBackgroundPromoCode.visibility = View.GONE
            binding.editTextTextPromoCode.visibility = View.GONE
            binding.textTextlblPromoCode.visibility = View.GONE
            binding.textViewRiderRewards.visibility = View.GONE
            binding.checkBoxRiderRewards.visibility = View.GONE

            binding.textViewLblHavePromoCode.text = getString(R.string.promo_code_is_accepted)

            binding.textViewLblPromo.visibility = View.VISIBLE
            binding.textViewPromo.visibility = View.VISIBLE

            binding.textViewLblPromo.text = getString(R.string.promo)
            if (OrderData.getPromotionType()) {
                binding.textViewPromo.setText(
                    String.format(
                        getString(R.string.promo_value_off),
                        Utility.convertUpTo2Decimal(OrderData.getPromotionFigureValue())
                    )
                )
            } else {
                binding.textViewPromo.text =
                    "$${Utility.convertUpTo2Decimal(OrderData.getPromotionFigureValue())}% ${
                        getString(R.string.off)
                    }"
                //binding.textViewPromo.text = String.format(getString(R.string.promo_percentage_off), Utility.convertUpTo2Decimal(OrderData.getPromotionFigureValue()))
            }

        } else if (OrderData.isBonusBillingProfile()) {

            //binding.viewBackgroundPromoCode.visibility = View.GONE
            binding.editTextTextPromoCode.visibility = View.GONE
            binding.textTextlblPromoCode.visibility = View.GONE
            binding.textViewLblHavePromoCode.visibility = View.GONE

            binding.textViewRiderRewards.visibility = View.VISIBLE
            binding.checkBoxRiderRewards.visibility = View.VISIBLE

//            this.customer.let {
//                if (it!!.rewardsPoint < 14) {
//                    binding.checkBoxRiderRewards.isClickable = false
//                    binding.checkBoxRiderRewards.isEnabled = false
//                }
//
//            }

//            binding.checkBoxRiderRewards.isClickable = false
//            binding.checkBoxRiderRewards.isEnabled = false
            binding.checkBoxRiderRewards.isChecked = true

            binding.textViewLblPromo.visibility = View.VISIBLE
            binding.textViewPromo.visibility = View.VISIBLE

            binding.textViewLblPromo.text = getString(R.string.price_adjustment_bonus_days)

            binding.textViewPromo.setText(
                String.format(
                    getString(R.string.promo_inverse_value_off),
                    Utility.convertUpTo2Decimal(OrderData.getPriceAdjustment())
                )
            )


        } else {
            binding.checkBoxRiderRewards.isChecked = false

            binding.textViewLblPromo.visibility = View.GONE
            binding.textViewPromo.visibility = View.GONE
        }

        checkButtonStatus()
    }


    override fun applyPromoCodeFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun getBonusDayBillingProfileInfoFail(errorMessage: String?) {

    }

    override fun showBonusDayBillingProfileInfoList(bonusDayBillingProfileInfoList: List<BonusDayBillingProfileInfo>) {

    }

    override fun getBonusDayBillingProfileInfoListFail(errorMessage: String?) {

    }

    override fun applyBonusSuccessfully(applyBonusRs: ApplyBonusRs) {
        binding.checkBoxRiderRewards.isChecked = true

        OrderData.applyBonus(applyBonusRs)
        setTotalView()
    }

    override fun applyBonusFail(errorMessage: String?) {
        showToastMessage(errorMessage)
        binding.checkBoxRiderRewards.isChecked = false
    }

    override fun deleteAuthorizedPaymentProfileSuccessfully(isEcheck: Boolean) {
        if (isEcheck) {
            Toast.makeText(
                homeActivity,
                getString(R.string.eCheck_remove_successfully),
                Toast.LENGTH_LONG
            ).show()

        } else {
            Toast.makeText(
                homeActivity,
                getString(R.string.remove_card_successfully),
                Toast.LENGTH_LONG
            ).show()

        }
        presenter.getCardList()
        presenter.getECheckList()
    }

    override fun deleteAuthorizedPaymentProfileFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun validateOrderListTimeSuccessfully() {

        customer?.let {
            OrderData.updateRewardTurnOn(it.rewardTurn)
            if (binding.checkBoxRiderRewards.isChecked) {
                var strdemo: Float = 0F
                strdemo =
                    (OrderData.getSubTotal(true) + OrderData.getDeliveryFee() + OrderData.getTax())
                if (strdemo != 0F) {
                    // paymentProfileID != 0

                    if (isCreditCardRadioButtonSelected) {
                        OrderData.updatePaymentProfileId(card!!.id,false)
                        OrderData.updatePaymentMethodId(1)

                    } else {
                        OrderData.updatePaymentProfileId(eCheckcard!!.id,false)
                        OrderData.updatePaymentMethodId(8)
                    }

                } else {
                    // paymentProfileID = 0
                    if (isCreditCardRadioButtonSelected) {
                        OrderData.updatePaymentProfileId(card!!.id,true)
                        OrderData.updatePaymentMethodId(4)

                    } else {
                        OrderData.updatePaymentProfileId(eCheckcard!!.id,true)
                        OrderData.updatePaymentMethodId(4)
                    }
                }


            } else {
                // paymentProfileID = 0
                if (isCreditCardRadioButtonSelected) {
                    OrderData.updatePaymentProfileId(card!!.id,true)
                    OrderData.updatePaymentMethodId(1)
                } else {
                    OrderData.updatePaymentProfileId(eCheckcard!!.id,true)
                    OrderData.updatePaymentMethodId(8)
                }
            }

            OrderData.updateNote(binding.editTextNote.text.toString())
            isProcessOrderButtonClick = true
            presenter_updateApp.updateAppConfig()
//            presenter.saveOrderList(binding.appCompatCheckBox.isChecked,isCreditCardRadioButtonSelected)
        }


    }

    override fun validateOrderListTimeFail(errorMessage: String?) {

        val builder = AlertDialog.Builder(homeActivity)
        builder.setMessage(errorMessage)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->

            }
        builder.show()

    }

    override fun showValidateLicense(validateLicenseRs: ValidateLicenseRs) {

        with(validateLicenseRs) {
            if (!isValidLicenseForPayor || notValidLicenseForOperator.isNotEmpty()) {

                val builder = AlertDialog.Builder(homeActivity)
                builder.setMessage(getString(R.string.validation_please_complete_profile_information))
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->

                        homeActivity.supportFragmentManager.popBackStack(
                            null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        homeActivity.showMyProfileFragment()
                    }
                builder.show()


            } else {
                presenter.validateOrderListTime()
            }
        }

    }

    override fun getValidateLicenseFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }

    override fun showDisclaimerOfPaymentMethodResponse(htmlResponse: GetDisclaimerOfPaymentMethodRs) {
        Log.e("nilam", "html text is _----- >>> " + htmlResponse.DisclaimerMessage)
        homeActivity.showContentDialogFragment(htmlResponse.DisclaimerMessage)
    }

    override fun showDisclaimerOfPaymentMethodResponseError(errorMessage: String) {
        Log.e("nilam", "html text is _----- >>> " + errorMessage)
        showToastMessage(errorMessage)
    }

    @SuppressLint("SetTextI18n")
    override fun setProcessFeeData(getProcessingFeeRs: GetProcessingFeeRs) {
        Log.e("nilam", "message----- >>> " + getProcessingFeeRs.note)
        Log.e("nilam", "fee_figure----- >>> " + getProcessingFeeRs.fee_figure.toString())
        Log.e(
            "nilam",
            "processing_fee_amount----- >>> " + getProcessingFeeRs.processing_fee_amount.toString()
        )
        Log.e("nilam", "fee_type----- >>> " + getProcessingFeeRs.fee_type.toString())
        OrderData.updateProcesingFee(
            getProcessingFeeRs.fee_type,
            getProcessingFeeRs.fee_figure,
            getProcessingFeeRs.processing_fee_amount
        )

        if (getProcessingFeeRs.processing_fee_amount != 0F) {
//            binding.textViewProcessingFee.visibility = View.VISIBLE
//            binding.textViewLblProcessingFee.visibility = View.VISIBLE
//            binding.textViewProcessingFeeTax.visibility = View.VISIBLE
            binding.textViewTotal.text =
                String.format(
                    getString(R.string.total),
                    Utility.convertUpTo2Decimal(
                        (getProcessingFeeRs.processing_fee_amount + OrderData.getSubTotal(
                            true
                        ) + OrderData.getDeliveryFee() + OrderData.getTax())
                    )
                )

            binding.textViewProcessingFee.text =
                String.format(
                    getString(R.string.total),
                    Utility.convertUpTo2Decimal((getProcessingFeeRs.processing_fee_amount))
                )
            binding.textViewProcessingFeeTax.text = " (" + getProcessingFeeRs.note + ")"

        } else {

            binding.textViewTotal.text =
                String.format(
                    getString(R.string.total),
                    Utility.convertUpTo2Decimal((OrderData.getSubTotal(true) + OrderData.getDeliveryFee() + OrderData.getTax()))
                )
            binding.textViewProcessingFee.text =
                String.format(
                    getString(R.string.total),
                    Utility.convertUpTo2Decimal((getProcessingFeeRs.processing_fee_amount))
                )
            binding.textViewProcessingFeeTax.text = " (" + getProcessingFeeRs.note + ")"

        }
    }

    private fun checkButtonStatus() {

        /*if (customer != null && card != null) {
            binding.textViewPlaceOrder.setBackgroundResource(R.drawable.bg_mow_rounded_blue_button)
        } else {
            binding.textViewPlaceOrder.setBackgroundResource(R.drawable.bg_mow_rounded_gray_button)
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (!binding.appCompatCheckBox.isChecked)
                binding.appCompatCheckBox.isChecked = resultCode == Activity.RESULT_OK
            callPlaceOrderApi()

        } else {
            Log.e("Nilam", "onActivityResult else portion called REQUEST_CODE -- >")

        }
    }


    private fun callPlaceOrderApi() {
        if (binding.creditCardRadioButton.isChecked) {
            if (card != null) {
                customer?.let {
                    if (binding.appCompatCheckBox.isChecked) {
                        presenter.validateLicense()
                    } else {
                        presenter.getDeviceTrainingVideoAndData(true)
                    }
                }

            } else {
                showToastMessage(getString(R.string.validation_please_select_your_payment_card))
            }
        } else {
            Log.e(
                "Nilam",
                "checked Id ---->>> eCheckRadioButton 2222  -->$isCreditCardRadioButtonSelected"
            )

            if (eCheckcard != null) {

                val gson = Gson()
                Log.e("Nilam", gson.toJson(eCheckcard))
                customer?.let {
                    if (binding.appCompatCheckBox.isChecked) {
                        presenter.validateLicense()
                    } else {
                        presenter.getDeviceTrainingVideoAndData(true)
                    }
                }

            } else {
                showToastMessage(getString(R.string.validation_please_select_your_e_check_bank))
            }

        }
    }

    override fun getAppConfigSuccessfully(config: Config?) {
        config?.let {
            val packageManager = homeActivity.packageManager
            val packageInfo = packageManager.getPackageInfo(homeActivity.packageName, 0)
            val versionNameFirst = (packageInfo.versionName)?.toFloat()
            val versionName = BuildConfig.VERSION_NAME
            Log.d("AppVersion", "Version Name: CheckOutFragment $versionName")


            if (it.minimumAndroidVersion <= versionNameFirst!!) {
                if (isProcessOrderButtonClick) {
                    presenter.saveOrderList(
                        binding.appCompatCheckBox.isChecked,
                        isCreditCardRadioButtonSelected
                    )

                } else {
                    presenter.getCardList()
                    presenter.getECheckList()
                    presenter.getCustomer()
                    presenter.getDeviceTrainingVideoAndData(false)
                }

            } else {
                //    DialogUtils.showAlertDialog(context = requireActivity(),)
                DialogUtils.showAlertDialog(context = requireContext(), activity = homeActivity)

            }
        }
    }

    override fun getAppConfigFail(errorMessage: String?) {
        showToastMessage(errorMessage)
    }


    companion object {
        fun newInstance() = CheckOutFragment()
    }
}