package com.data;

import com.data.objectforupdate.UpdateAppRs;
import com.data.remote.response.accessory.GetAccessoryByDeviceTypeIdListRs;
import com.data.remote.response.destination.GetCustomerLocationListRs;
import com.data.remote.response.destination.GetDestinationListRs;
import com.data.remote.response.destination.GetLocationByIdRs;
import com.data.remote.response.destination.GetPickupLocationInstructionRs;
import com.data.remote.response.device.GetDeviceInfoRs;
import com.data.remote.response.device.GetDeviceListRs;
import com.data.remote.response.device.GetDevicePropertyOptionRs;
import com.data.remote.response.device.RentalPriceListRs;
import com.data.remote.response.entityDefination.GetEntityDefinationRs;
import com.data.remote.response.idanalyzer.GetDocumentRs;
import com.data.remote.response.notification.AddUpdateFCMTokenRs;
import com.data.remote.response.notification.GetBadgeCountRs;
import com.data.remote.response.notification.MarkNotificationAsReadRs;
import com.data.remote.response.notification.NotificationListRs;
import com.data.remote.response.occupant.AddDefaultOccupantRs;
import com.data.remote.response.occupant.GetOccupantByIDRs;
import com.data.remote.response.occupant.GetOccupantListRs;
import com.data.remote.response.occupant.SearchOccupantListRs;
import com.data.remote.response.operator.GetOperatorByIDRs;
import com.data.remote.response.operator.GetOperatorListRs;
import com.data.remote.response.order.ApplyBonusRs;
import com.data.remote.response.order.AuthorizeECheckRequestRs;
import com.data.remote.response.order.AuthorizePaymentRequestRs;
import com.data.remote.response.order.CreditCardRs;
import com.data.remote.response.order.DeleteAuthorizedPaymentProfileRs;
import com.data.remote.response.order.GetAllActiveOrderByCustomerIDRs;
import com.data.remote.response.order.GetAllECheckPaymentProfileByCustomerIDRs;
import com.data.remote.response.order.GetAllPaymentProfileByCustomerIDRs;
import com.data.remote.response.order.GetAllStateRs;
import com.data.remote.response.order.GetBlackoutDatesByLocationAndDeviceRs;
import com.data.remote.response.order.GetBonusDayBillingProfileInfoListRs;
import com.data.remote.response.order.GetBonusDayBillingProfileInfoRs;
import com.data.remote.response.order.GetCustomerByIDRs;
import com.data.remote.response.order.GetCustomerOrderHistoryRs;
import com.data.remote.response.order.GetDisclaimerOfPaymentMethodRs;
import com.data.remote.response.order.GetOrderBillingProfileRs;
import com.data.remote.response.order.GetOrderDetailRs;
import com.data.remote.response.order.GetProcessingFeeRs;
import com.data.remote.response.order.GetPromotionDetailByLocationIDNCodeRs;
import com.data.remote.response.order.GetRewardPolicyRs;
import com.data.remote.response.order.ImageUploadedForReturnDeviceRs;
import com.data.remote.response.order.SaveOrderListRs;
import com.data.remote.response.order.SentBugReportRs;
import com.data.remote.response.order.ValidateOrderListTimeRs;
import com.data.remote.response.reservation.GetSameDayReservationRs;
import com.data.remote.response.user.AddAndEditOperatorRs;
import com.data.remote.response.user.AddDeviceInventoryIDRs;
import com.data.remote.response.user.AddOccupantRs;
import com.data.remote.response.user.GenerateOTPRs;
import com.data.remote.response.user.RetrieveCellPhoneNumberRs;
import com.data.remote.response.user.SaveCustomerRs;
import com.data.remote.response.user.ValidateLicenseRs;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

import libraries.image.helper.models.MediaResult;

/**
 * The interface that exposes fetching and storing data through helper methods. This is to be
 * implemented by all data sources such as
 */
public abstract class DataSource {

    public DataSource() {
    }

    // Using this function, generate OTP
    public abstract void generateOTP(String phoneNumber, CallbackSubscriber<GenerateOTPRs> callbackSubscriber);

    //Using this function verify OTP
    public abstract void verifyOTP(HashMap<String, Object> body, CallbackSubscriber<JsonObject> callbackSubscriber);


    // Using this function, get mobile numbe on email
    public abstract void retrieveCellPhoneNumber(String email, CallbackSubscriber<RetrieveCellPhoneNumberRs> callbackSubscriber);

    //This function return list of destination
    public abstract void getDestinationList(CallbackSubscriber<GetDestinationListRs> callbackSubscriber);

    //This function return list of devices
    public abstract void getDeviceListByLocationId(int locationId, CallbackSubscriber<GetDeviceListRs> callbackSubscriber);

    //This function return basic information of selected device
    public abstract void getDeviceTypeByID(int id, CallbackSubscriber<GetDeviceInfoRs> callbackSubscriber);

    //Get Rental Rates By Device ID
    public abstract void getRentalRatesByDeviceID(HashMap<String, Object> body, CallbackSubscriber<RentalPriceListRs> callbackSubscriber);

    //This function return list of operator
    public abstract void getAllOperatorByCustomerID(int customerID, CallbackSubscriber<GetOperatorListRs> callbackSubscriber);

    //This function return list of occupant
    public abstract void getOccupantListByOperatorID(int customerID, CallbackSubscriber<GetOccupantListRs> callbackSubscriber);

    //Get Rental Rates By Device ID
    public abstract void getCustomerPickupLocationByDestination(HashMap<String, Object> body, CallbackSubscriber<GetCustomerLocationListRs> callbackSubscriber);

    //Get Accessory list
    public abstract void getAccessoryByDeviceTypeIdList(HashMap<String, Object> body, CallbackSubscriber<GetAccessoryByDeviceTypeIdListRs> callbackSubscriber);

    //Get Same Day Reservation
    public abstract void getSameDayReservation(HashMap<String, Object> body, CallbackSubscriber<GetSameDayReservationRs> callbackSubscriber);

    //Get Order Billing Profile
    public abstract void getOrderBillingProfile(HashMap<String, Object> body, CallbackSubscriber<GetOrderBillingProfileRs> callbackSubscriber);

    // Using this function, get list of debit and credit card
    public abstract void getAllPaymentProfileByCustomerID(int id, CallbackSubscriber<GetAllPaymentProfileByCustomerIDRs> callbackSubscriber);
    public abstract void getDisclaimerOfPaymentMethod(String id, CallbackSubscriber<GetDisclaimerOfPaymentMethodRs> callbackSubscriber);
    public abstract void getAllECheckPaymentProfileByCustomerID(int id, CallbackSubscriber<GetAllECheckPaymentProfileByCustomerIDRs> callbackSubscriber);

    // Using this function, get login customer information include address...... etc
    public abstract void getCustomerByID(int id, CallbackSubscriber<GetCustomerByIDRs> callbackSubscriber);

    // Using this function, get promotion detail by location id and code for promo code
    public abstract void getPromotionDetailByLocationIDNCode(String request, CallbackSubscriber<GetPromotionDetailByLocationIDNCodeRs> callbackSubscriber);

    // Using this function, get list of order history
    public abstract void getCustomerOrderHistory(int id, CallbackSubscriber<GetCustomerOrderHistoryRs> callbackSubscriber);

    // Using this function, add credit or debit card
    public abstract void authorizePaymentRequest(HashMap<String, Object> body, CallbackSubscriber<AuthorizePaymentRequestRs> callbackSubscriber);
    public abstract void authorizeECheckRequest(HashMap<String, Object> body, CallbackSubscriber<AuthorizeECheckRequestRs> callbackSubscriber);

    public abstract void getProcessingFee(HashMap<String, Object> body, CallbackSubscriber<GetProcessingFeeRs> callbackSubscriber);
    public abstract void getBlackoutDatesByLocationAndDevice(HashMap<String, Object> body, CallbackSubscriber<GetBlackoutDatesByLocationAndDeviceRs> callbackSubscriber);

    public abstract void sentBugReport(HashMap<String, Object> body, CallbackSubscriber<SentBugReportRs> callbackSubscriber);

    // Using this function, get active order list
    public abstract void getAllActiveOrderByCustomerID(int id, CallbackSubscriber<GetAllActiveOrderByCustomerIDRs> callbackSubscriber);

    // Using this function, get more detail about location
    public abstract void getLocationByID(int id, CallbackSubscriber<GetLocationByIdRs> callbackSubscriber);

    // Using this function, save order list
    public abstract void saveOrderList(String orderList, CallbackSubscriber<SaveOrderListRs> callbackSubscriber);

    // Using this function, get device training video and data
    public abstract void getDeviceTrainingVideoAndData(String request, CallbackSubscriber<JsonArray> callbackSubscriber);

    // Using this function, get detail of order
    public abstract void getOrderDetailsByOrderID(int id, CallbackSubscriber<GetOrderDetailRs> callbackSubscriber);

    // Using this function, get all state list of US
    public abstract void getAllState(CallbackSubscriber<GetAllStateRs> callbackSubscriber);

    // This function is use for save register information
    public abstract void saveCustomer(String customerInfo, CallbackSubscriber<SaveCustomerRs> callbackSubscriber);

    // Using this function, scan document and get information from document
    public abstract void getDocument(String url, String apikey, Boolean authenticate,
                                     String type, Boolean vaultSave, MediaResult file,
                                     CallbackSubscriber<GetDocumentRs> callbackSubscriber);


    // Using this function, save occupant
    public abstract void saveOccupant(String occupant, CallbackSubscriber<AddOccupantRs> callbackSubscriber);

    // This function is use for add and edit operator
    public abstract void addEditNewOperator(String operator, CallbackSubscriber<AddAndEditOperatorRs> callbackSubscriber);

    // Using this function, get rider reward policy content
    public abstract void getRewardPolicy(CallbackSubscriber<GetRewardPolicyRs> callbackSubscriber);

    // Using this function, to check rental device would be return or not
    public abstract void checkForReturnDeviceImage(int orderId, CallbackSubscriber<String> callbackSubscriber);

    // Using this function, to check rental device would be return or not
    public abstract void imageUploadedForReturnDevice(String request, CallbackSubscriber<ImageUploadedForReturnDeviceRs> callbackSubscriber);

    // This function is use for get operator and occupant information
    public abstract void getEntityDefination(CallbackSubscriber<GetEntityDefinationRs> callbackSubscriber);

    // Using this function, check and if not, than add new occupant
    public abstract void addDefaultOccupant(int operatorID, CallbackSubscriber<AddDefaultOccupantRs> callbackSubscriber);

    // Using this function, check and if not, than add new occupant
    public abstract void getExistingOccupant(String request, CallbackSubscriber<SearchOccupantListRs> callbackSubscriber);

    // Using this function, get instruction of destination location
    public abstract void getPickupLocationInstruction(String request, CallbackSubscriber<GetPickupLocationInstructionRs> callbackSubscriber);

    // Using this function, get device property
    public abstract void getDevicePropertyOptions(int optionId, CallbackSubscriber<GetDevicePropertyOptionRs> callbackSubscriber);

    // Using this function, get operator detail by id
    public abstract void getOperatorByID(int operatorID, CallbackSubscriber<GetOperatorByIDRs> callbackSubscriber);

    // Using this function, get occupant detail by id
    public abstract void getOccupantByID(int occupantID, CallbackSubscriber<GetOccupantByIDRs> callbackSubscriber);

    // Using this function, remove occupant id
    public abstract void removeOccupantById(int occupantID, CallbackSubscriber<String> callbackSubscriber);

    // Using this function, resend attestation from order history
    public abstract void reSendAttestation(String request, CallbackSubscriber<String> callbackSubscriber);

    // Using this function, cancel order
    public abstract void cancelOrderByOrderId(String request, CallbackSubscriber<String> callbackSubscriber);

    // Using this function, get bonus day billing profile info for rider points
    public abstract void getBonusDayBillingProfileInfo(String request, CallbackSubscriber<GetBonusDayBillingProfileInfoRs> callbackSubscriber);

    // Using this function, get bonus day billing profile info listfor rider points
    public abstract void getBonusDayBillingProfileInfoList(String request, CallbackSubscriber<GetBonusDayBillingProfileInfoListRs> callbackSubscriber);

    // Using this function, apply bonus (reward point)
    public abstract void applyBonus(String request, CallbackSubscriber<ApplyBonusRs> callbackSubscriber);

    //Using this function, get configure detail
    public abstract void getAppConfiguration(CallbackSubscriber<JsonObject> callbackSubscriber);

    // Using this function, delete card
    public abstract void deleteAuthorizedPaymentProfile(String request, CallbackSubscriber<DeleteAuthorizedPaymentProfileRs> callbackSubscriber);

    // Update profile
    public abstract void updateProfile(String customerInfo, CallbackSubscriber<SaveCustomerRs> callbackSubscriber);

    // Using this function, add device inventory ID
    public abstract void addDeviceInventoryID(String request, CallbackSubscriber<AddDeviceInventoryIDRs> callbackSubscriber);

    // Using this function, validate licence of payor and operator
    public abstract void validateLicense(CallbackSubscriber<ValidateLicenseRs> callbackSubscriber);

    // Using this function, validate order time
    public abstract void validateOrderListTime(String request, CallbackSubscriber<ValidateOrderListTimeRs> callbackSubscriber);

    // Using this function, get notification list
    public abstract void getNotificationList(CallbackSubscriber<NotificationListRs> callbackSubscriber);

    // Using this function, get badge count of notification
    public abstract void getBadgeCount(CallbackSubscriber<GetBadgeCountRs> callbackSubscriber);

    // Using this function, read all notification
    public abstract void markNotificationAsRead(CallbackSubscriber<MarkNotificationAsReadRs> callbackSubscriber);

    // Using this function, store firebase token to server
    public abstract void addUpdateFCMToken(String request, CallbackSubscriber<AddUpdateFCMTokenRs> callbackSubscriber);
    public abstract void updateApp(String request, CallbackSubscriber<UpdateAppRs> callbackSubscriber);

}
