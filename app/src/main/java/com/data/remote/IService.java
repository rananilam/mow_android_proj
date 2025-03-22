package com.data.remote;


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

//import io.reactivex.Observable;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;


public interface IService {

    @Multipart
    @POST("login")
    @Headers("Accept:application/json")
    Observable<JsonObject> login(@Part("email") RequestBody emailNotification);

    @POST("GenerateOTP")
    @Headers("Content-Type:application/json")
    Observable<GenerateOTPRs> generateOTP(
            @Body RequestBody phoneNumber
    );

    @POST("VerifyOTP")
    @Headers("Content-Type:application/json")
    Observable<JsonObject> verifyOTP(
            @Body HashMap<String, Object> body
    );

    @POST("RetriveCellNumber")
    @Headers("Content-Type:application/json")
    Observable<RetrieveCellPhoneNumberRs> retrieveCellPhoneNumber(
            @Body RequestBody email
    );


    @POST("GetDeviceByLocationID")
    @Headers("Content-Type:application/json")
    Observable<GetDeviceListRs> getDeviceListByLocationId(
            @Body RequestBody locationId
    );

    @POST("GetAllDestiantion")
    @Headers("Content-Type:application/json")
    Observable<GetDestinationListRs> getDestinationList();

    @POST("GetDeviceTypeByID")
    @Headers("Content-Type:application/json")
    Observable<GetDeviceInfoRs> getDeviceTypeByID(
            @Body RequestBody id
    );

    @POST("GetRentalRatesByDeviceID")
    @Headers("Content-Type:application/json")
    Observable<RentalPriceListRs> getRentalRatesByDeviceID(
            @Body HashMap<String, Object> body
    );

    @POST("GetAllOperatorByCustomerID")
    @Headers("Content-Type:application/json")
    Observable<GetOperatorListRs> getAllOperatorByCustomerID(
            @Body RequestBody id
    );

    @POST("GetOccupantListByOperatorID")
    @Headers("Content-Type:application/json")
    Observable<GetOccupantListRs> getOccupantListByOperatorID(
            @Body RequestBody id
    );

    @POST("GetCustomerPickupLocationByDestination")
    @Headers("Content-Type:application/json")
    Observable<GetCustomerLocationListRs> getCustomerPickupLocationByDestination(
            @Body HashMap<String, Object> body
    );

    @POST("RestrictedAccessoryByDeviceTypeID")
    @Headers("Content-Type:application/json")
    Observable<GetAccessoryByDeviceTypeIdListRs> getAccessoryByDeviceTypeIdList(
            @Body HashMap<String, Object> body
    );

    @POST("GetSameDayReservation")
    @Headers("Content-Type:application/json")
    Observable<GetSameDayReservationRs> getSameDayReservation(
            @Body HashMap<String, Object> body
    );

    @POST("GetOrderBillingProfile")
    @Headers("Content-Type:application/json")
    Observable<GetOrderBillingProfileRs> getOrderBillingProfile(
            @Body HashMap<String, Object> body
    );

    @POST("GetAllPaymentProfileByCustomerID")
    @Headers("Content-Type:application/json")
    Observable<GetAllPaymentProfileByCustomerIDRs> getAllPaymentProfileByCustomerID(
            @Body RequestBody id
    );

    @POST("GetCustomerByID")
    @Headers("Content-Type:application/json")
    Observable<GetCustomerByIDRs> getCustomerByID(
            @Body RequestBody id
    );


    @POST("GetPromotionDetailByLocationIDNCode")
    @Headers("Content-Type:application/json")
    Observable<GetPromotionDetailByLocationIDNCodeRs> getPromotionDetailByLocationIDNCode(
            @Body RequestBody request
    );

    @POST("GetCustomerOrderHistory")
    @Headers("Content-Type:application/json")
    Observable<GetCustomerOrderHistoryRs> getCustomerOrderHistory(
            @Body RequestBody id
    );

    @POST("AuthorizePaymentRequest")
    @Headers("Content-Type:application/json")
    Observable<AuthorizePaymentRequestRs> authorizePaymentRequest(
            @Body HashMap<String, Object> body
    );

    @POST("GetAllActiveOrderByCustomerID")
    @Headers("Content-Type:application/json")
    Observable<GetAllActiveOrderByCustomerIDRs> getAllActiveOrderByCustomerID(
            @Body RequestBody id
    );

    @POST("GetLocationByID")
    @Headers("Content-Type:application/json")
    Observable<GetLocationByIdRs> getLocationByID(
            @Body RequestBody id
    );

    @POST("SaveOrderList")
    @Headers("Content-Type:application/json")
    Observable<SaveOrderListRs> saveOrderList(
            @Body RequestBody orderList
    );

    @POST("GetDeviceTrainingVideoAndData")
    @Headers("Content-Type:application/json")
    Observable<JsonArray> getDeviceTrainingVideoAndData(
            @Body RequestBody request
    );


    @POST("GetOrderDetailsByOrderID")
    @Headers("Content-Type:application/json")
    Observable<GetOrderDetailRs> getOrderDetailsByOrderID(
            @Body RequestBody id
    );

    @POST("GetAllState")
    @Headers("Content-Type:application/json")
    Observable<GetAllStateRs> getAllState();

    @POST("Registration")
    @Headers("Content-Type:application/json")
    Observable<SaveCustomerRs> saveCustomer(
            @Body RequestBody customerInfo
    );

    @POST("SaveCustomer")
    @Headers("Content-Type:application/json")
    Observable<SaveCustomerRs> updateCustomer(
            @Body RequestBody customerInfo
    );

    @Multipart
    @POST
    @Headers("Accept:application/json")
    Observable<GetDocumentRs> getDocument(@Url String url,
                                            @Part("apikey") RequestBody apikey,
                                            @Part("authenticate") RequestBody authenticate,
                                            @Part("type") RequestBody type,
                                            @Part("vault_save") RequestBody vaultSave,
                                            @Part MultipartBody.Part file
    );

    @POST("SaveOccupant")
    @Headers("Content-Type:application/json")
    Observable<AddOccupantRs> saveOccupant(
            @Body RequestBody occupant
    );

    @POST("AddEditNewOperator")
    @Headers("Content-Type:application/json")
    Observable<AddAndEditOperatorRs> addEditNewOperator(
            @Body RequestBody operator
    );

    @POST("GetRewardPolicy")
    @Headers("Content-Type:application/json")
    Observable<GetRewardPolicyRs> getRewardPolicy();

    @POST("CheckForReturnDeviceImage")
    @Headers("Content-Type:application/json")
    Observable<String> checkForReturnDeviceImage(@Body RequestBody orderId);

    @POST("ImageUploadedForReturnDevice")
    @Headers("Content-Type:application/json")
    Observable<ImageUploadedForReturnDeviceRs> imageUploadedForReturnDevice(@Body RequestBody request);

    @POST("GetEntityDefination")
    @Headers("Content-Type:application/json")
    Observable<GetEntityDefinationRs> getEntityDefination();

    @POST("AddDefaultOccupant")
    @Headers("Content-Type:application/json")
    Observable<AddDefaultOccupantRs> addDefaultOccupant(
            @Body RequestBody operatorID
    );


    @POST("GetExistingOccupant")
    @Headers("Content-Type:application/json")
    Observable<SearchOccupantListRs> getExistingOccupant(
            @Body RequestBody searchRq
    );

    @POST("GetPickupInstructionDetailByDeviceLocationAndPickupLocation")
    @Headers("Content-Type:application/json")
    Observable<GetPickupLocationInstructionRs> getPickupLocationInstruction(
            @Body RequestBody request
    );

    @POST("GetDevicePropertyOptionsBy")
    @Headers("Content-Type:application/json")
    Observable<GetDevicePropertyOptionRs> getDevicePropertyOptions(
            @Body RequestBody request
    );


    @POST("GetOperatorByID")
    @Headers("Content-Type:application/json")
    Observable<GetOperatorByIDRs> getOperatorByID(
            @Body RequestBody request
    );

    @POST("GetOccupantByID")
    @Headers("Content-Type:application/json")
    Observable<GetOccupantByIDRs> getOccupantByID(
            @Body RequestBody request
    );

    @POST("RemoveOccupantById")
    @Headers("Content-Type:application/json")
    Observable<String> removeOccupantById(
            @Body RequestBody request
    );


    @POST("ReSendAttestation")
    @Headers("Content-Type:application/json")
    Observable<String> reSendAttestation(
            @Body RequestBody request
    );

    @POST("CancelOrderByOrderId")
    @Headers("Content-Type:application/json")
    Observable<String> cancelOrderByOrderId(
            @Body RequestBody request
    );

    @POST("GetBonusDayBillingProfileInfoList")
    @Headers("Content-Type:application/json")
    Observable<GetBonusDayBillingProfileInfoRs> getBonusDayBillingProfileInfo(
            @Body RequestBody request
    );

    @POST("GetBonusDayBillingProfileInfoList")
    @Headers("Content-Type:application/json")
    Observable<GetBonusDayBillingProfileInfoListRs> getBonusDayBillingProfileInfoList(
            @Body RequestBody request
    );

    @POST("ApplyBonus")
    @Headers("Content-Type:application/json")
    Observable<ApplyBonusRs> applyBonus(
            @Body RequestBody request
    );

    @POST("GetAppConfiguration")
    @Headers("Content-Type:application/json")
    Observable<JsonObject> getAppConfiguration();

    @POST("DeleteAuthorizedPaymentProfile")
    @Headers("Content-Type:application/json")
    Observable<DeleteAuthorizedPaymentProfileRs> deleteAuthorizedPaymentProfile(
            @Body RequestBody request
    );


    @POST("AddDeviceInventoryID")
    @Headers("Content-Type:application/json")
    Observable<AddDeviceInventoryIDRs> addDeviceInventoryID(
            @Body RequestBody request
    );

    @POST("ValidateLicense")
    @Headers("Content-Type:application/json")
    Observable<ValidateLicenseRs> validateLicense();

    @POST("ValidateOrderListTime")
    @Headers("Content-Type:application/json")
    Observable<ValidateOrderListTimeRs> validateOrderListTime(
            @Body RequestBody request
    );

    @POST("AllPushNotificationList")
    @Headers("Content-Type:application/json")
    Observable<NotificationListRs> getNotificationList();

    @POST("GetBadgeCount")
    @Headers("Content-Type:application/json")
    Observable<GetBadgeCountRs> getBadgeCount(

    );

    @POST("MarkNotificationAsRead")
    @Headers("Content-Type:application/json")
    Observable<MarkNotificationAsReadRs> markNotificationAsRead();

    @POST("AddUpdateFCMToken")
    @Headers("Content-Type:application/json")
    Observable<AddUpdateFCMTokenRs> addUpdateFCMToken(
            @Body RequestBody request
    );

    @POST("AppVersionValidate")
    @Headers("Content-Type:application/json")
    Observable<UpdateAppRs> updateApp(
           //@Body RequestBody request
 //@Body RequestBody id
         // @Body RequestBody id
    );


    @POST("AuthorizeECheckPaymentRequest")
    @Headers("Content-Type:application/json")
    Observable<AuthorizeECheckRequestRs> authorizeECheckRequest(
            @Body HashMap<String, Object> body
    );

    @POST("GetAllECheckPaymentProfileByCustomerID")
    @Headers("Content-Type:application/json")
    Observable<GetAllECheckPaymentProfileByCustomerIDRs> getAllECheckPaymentProfileByCustomerID(
            @Body RequestBody id
    );

    @POST("GetDisclaimerOfPaymentMethod")
    @Headers("Content-Type:application/json")
    Observable<GetDisclaimerOfPaymentMethodRs> getDisclaimerOfPaymentMethod(
            @Body RequestBody id
    );


    @POST("GetProcessingFee")
    @Headers("Content-Type:application/json")
    Observable<GetProcessingFeeRs> getProcessingFee(
            @Body HashMap<String, Object> body
    );

    @POST("GetBlackoutDatesByLocationAndDevice")
    @Headers("Content-Type:application/json")
        Observable<GetBlackoutDatesByLocationAndDeviceRs> getBlackoutDatesByLocationAndDevice(
            @Body HashMap<String, Object> body
    );

    @POST("SentBugReport")
    @Headers("Content-Type:application/json")
    Observable<SentBugReportRs> sentBugReport(
            @Body HashMap<String, Object> body
    );


}
