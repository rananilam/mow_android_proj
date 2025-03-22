package com.ui.home.checkOut

import com.data.model.order.BonusDayBillingProfileInfo
import com.data.model.order.Card
import com.data.model.order.ECheckCard
import com.data.model.user.User
import com.data.remote.response.order.ApplyBonusRs
import com.data.remote.response.order.GetDeviceTrainingVideoAndDataRs
import com.data.remote.response.order.GetDisclaimerOfPaymentMethodRs
import com.data.remote.response.order.GetProcessingFeeRs
import com.data.remote.response.order.GetPromotionDetailByLocationIDNCodeRs
import com.data.remote.response.order.SaveOrderListRs
import com.data.remote.response.user.ValidateLicenseRs
import com.ui.core.IBasePresenter
import com.ui.core.IBaseView


interface CheckOutFragmentContract {

    interface View : IBaseView {
        fun showCardList(cardList: List<Card>)
        fun getCardListFail(errorMessage: String?)

        fun showECheckList(cardList: List<ECheckCard>)
        fun getECheckListFail(errorMessage: String?)


        fun showCustomer(customer: User)
        fun getCustomerFail(errorMessage: String?)

        fun saveOrderListSuccessfully(saveOrderListRs: SaveOrderListRs?)
        fun saveOrderListFail(errorMessage: String?)

        fun showDeviceTrainingVideoAndData(isViewDialog: Boolean, getDeviceTrainingVideoAndDataRs: GetDeviceTrainingVideoAndDataRs)
        fun getDeviceTrainingVideoAndDataFail(errorMessage: String?)

        fun applyPromoCodeSuccessfully(
            promoCode: String,
            getPromotionDetailByLocationIDNCodeRs: GetPromotionDetailByLocationIDNCodeRs
        )

        fun applyPromoCodeFail(errorMessage: String?)

        fun getBonusDayBillingProfileInfoFail(errorMessage: String?)

        fun showBonusDayBillingProfileInfoList(bonusDayBillingProfileInfoList: List<BonusDayBillingProfileInfo>)
        fun getBonusDayBillingProfileInfoListFail(errorMessage: String?)

        fun applyBonusSuccessfully(applyBonusRs: ApplyBonusRs)
        fun applyBonusFail(errorMessage: String?)

        fun deleteAuthorizedPaymentProfileSuccessfully(isEcheck:Boolean)
        fun deleteAuthorizedPaymentProfileFail(errorMessage: String?)

        fun validateOrderListTimeSuccessfully()
        fun validateOrderListTimeFail(errorMessage: String?)

        fun showValidateLicense(validateLicenseRs: ValidateLicenseRs)
        fun getValidateLicenseFail(errorMessage: String?)
        fun showDisclaimerOfPaymentMethodResponse(response: GetDisclaimerOfPaymentMethodRs)
        fun showDisclaimerOfPaymentMethodResponseError(errorMessage: String)
        fun setProcessFeeData(getProcessingFeeRs: GetProcessingFeeRs)


    }

    interface Presenter : IBasePresenter<View> {
        fun getCardList()
        fun getECheckList()
        fun getCustomer()
        fun saveOrderList(isAcceptTerms: Boolean, isCreditCardRadioButtonSelected: Boolean?)

        fun getDeviceTrainingVideoAndData(isViewDialog: Boolean)
        fun getDeviceTrainingVideoAndDataStr(): String?

        fun applyPromoCode(promoCode: String)

        fun getBonusDayBillingProfileInfo(
            deviceTypeID: Int,
            rentalHours: Int,
            locationID: Int
        )

        fun getBonusDayBillingProfileInfoList(
            deviceTypeID: Int,
            rentalHours: Int,
            locationID: Int
        )

        fun applyBonus(
            chairPadPrice: Float,
            locationID: Int,
            deviceTypeID: Int,
            pickUpDate: String,
            pickUpTime: String,
            returnDate: String,
            returnTime: String,
            rewardPoint: Int,
            deliveryFee: Float,
            pickupLocationTaxRate: Float,
            orderRegularPrice: Float,
            isBonusDayCheck:Boolean
        )

        fun deleteAuthorizedPaymentProfile(
            authorizedPaymentProfileID: Int,isEcheck:Boolean
        )



        fun validateOrderListTime()
        fun validateLicense()
        fun getDisclaimerOfPaymentMethod(id:String)
        fun getProcessingFee(IsCardChecked:Boolean,IsEcheckChecked: Boolean,
                             priceWithTaxTotal: String,)





    }
}