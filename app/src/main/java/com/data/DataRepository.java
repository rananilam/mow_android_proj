package com.data;

import static java.sql.DriverManager.println;

import android.util.Log;

import com.data.model.order.OrderData;
import com.data.model.user.Config;
import com.data.objectforupdate.UpdateAppRs;
import com.data.remote.GsonInterface;
import com.data.remote.request.destination.GetPickupLocationInstructionRq;
import com.data.remote.request.notification.AddUpdateFCMTokenRq;
import com.data.remote.request.occupant.SearchOccupantListRq;
import com.data.remote.request.order.AddDeviceInventoryIDRq;
import com.data.remote.request.order.ApplyBonusRq;
import com.data.remote.request.order.CancelOrderByOrderIdRq;
import com.data.remote.request.order.DeleteAuthorizedPaymentProfileRq;
import com.data.remote.request.order.DeviceTrainingVideoDataRq;
import com.data.remote.request.order.GetBonusDayBillingProfileInfoRq;
import com.data.remote.request.order.GetPromotionDetailByLocationIDNCodeRq;
import com.data.remote.request.order.ImageUploadedForReturnDeviceRq;
import com.data.remote.request.order.ReSendAttestationRq;
import com.data.remote.request.order.SaveOrderRq;
import com.data.remote.request.order.ValidateOrderTimeRq;
import com.data.remote.request.user.AddAndEditOperatorRq;
import com.data.remote.request.user.AddOccupantRq;
import com.data.remote.request.user.SaveCustomerRq;
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
import com.data.remote.response.user.GetAppConfigurationRs;
import com.data.remote.response.user.RetrieveCellPhoneNumberRs;
import com.data.remote.response.user.SaveCustomerRs;
import com.data.remote.response.user.ValidateLicenseRs;
import com.data.remote.response.user.VerifyOTPRs;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.util.FilterHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iCode.utility.PrefHelper;
import libraries.image.helper.models.MediaResult;
import libraries.retrofit.RestError;


public class DataRepository {

    private static final String KEY_FIREBASE_TOKEN = "KEY_FIREBASE_TOKEN";

    private static final String KEY_LOGIN_USER = "KEY_LOGIN_USER";
    private static final String KEY_APP_CONFIG = "KEY_APP_CONFIG";
    private static final String KEY_LOCATION_ID = "KEY_LOCATION_ID";
    private static final String KEY_LOCATION_NAME = "KEY_LOCATION_NAME";
    private static final String KEY_LOCATION_CATEGORY = "KEY_LOCATION_CATEGORY";

    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_LEASE_ID = "KEY_LEASE_ID";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_PROFILE_PIC = "KEY_PROFILE_PIC";
    private static final String KEY_FIRST_NAME = "KEY_FIRST_NAME";
    private static final String KEY_LAST_NAME = "KEY_LAST_NAME";

    private static final String KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER";
    private static final String KEY_EMAIL_ADDRESS = "KEY_EMAIL_ADDRESS";

    private static final String KEY_USER_TYPE = "KEY_USER_TYPE";
    private static final String KEY_ENTITY_ID = "KEY_ENTITY_ID";
    private static final String KEY_UNIT_NAME = "KEY_UNIT_NAME";

    private static final String KEY_USER_ID_ROLE_ID = "KEY_USER_ID_ROLE_ID";

    //  private static final String KEY_SWITCH_USER_LIST = "KEY_SWITCH_USER_LIST";

    // nilam add below url to save url for open webview


    private static DataRepository dataRepository;
    private DataSource remoteDataSource;

    public DataRepository(DataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    private com.data.model.user.User user = null;

    public static synchronized DataRepository getInstance(DataSource remoteDataSource) {
        if (dataRepository == null) {
            dataRepository = new DataRepository(remoteDataSource);
        }
        return dataRepository;
    }


    public void getDeviceListByLocationId(int locationId, CallbackSubscriber<GetDeviceListRs> callbackSubscriber) {

        remoteDataSource.getDeviceListByLocationId(locationId, new CallbackSubscriber<GetDeviceListRs>() {
            @Override
            public void onSuccess(GetDeviceListRs getDeviceListRs) {
                callbackSubscriber.onSuccess(getDeviceListRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getDestinationList(CallbackSubscriber<GetDestinationListRs> callbackSubscriber) {

        remoteDataSource.getDestinationList(new CallbackSubscriber<GetDestinationListRs>() {
            @Override
            public void onSuccess(GetDestinationListRs getDestinationListRs) {

                /*if (getLocationId() == 0) {
                    if (getDestinationListRs != null && getDestinationListRs.getResult() != null && getDestinationListRs.getResult().isStatus()) {
                        if (getDestinationListRs.getDestinationList() != null && !getDestinationListRs.getDestinationList().isEmpty()) {
                            setLocationId(getDestinationListRs.getDestinationList().get(0).getId());
                            setLocationName(getDestinationListRs.getDestinationList().get(0).getLocationName());
                        }
                    }
                }*/
                callbackSubscriber.onSuccess(getDestinationListRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void generateOTP(String phoneNumber, CallbackSubscriber<GenerateOTPRs> callbackSubscriber) {

        remoteDataSource.generateOTP(phoneNumber, new CallbackSubscriber<GenerateOTPRs>() {
            @Override
            public void onSuccess(GenerateOTPRs generateOTPRs) {
                callbackSubscriber.onSuccess(generateOTPRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void verifyOTP(String phoneNumber, String otp, CallbackSubscriber<VerifyOTPRs> callbackSubscriber) {

        HashMap<String, Object> body = new HashMap<>();
        body.put("CellNumber", phoneNumber);
        body.put("OTP", otp);


        remoteDataSource.verifyOTP(body, new CallbackSubscriber<JsonObject>() {
            @Override
            public void onSuccess(JsonObject response) {


                VerifyOTPRs verifyOTPRs = GsonInterface.getInstance().getGson().fromJson(response, VerifyOTPRs.class);
                if (verifyOTPRs != null && verifyOTPRs.getResult() != null && verifyOTPRs.getResult().isStatus()) {
                    if (verifyOTPRs.getUser() != null && !verifyOTPRs.getUser().getToken().trim().isEmpty()) {
                        PrefHelper.getInstance().putString(KEY_LOGIN_USER, response.toString());
                    }
                }
                callbackSubscriber.onSuccess(verifyOTPRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void getDeviceTypeByID(int id, CallbackSubscriber<GetDeviceInfoRs> callbackSubscriber) {

        remoteDataSource.getDeviceTypeByID(id, new CallbackSubscriber<GetDeviceInfoRs>() {
            @Override
            public void onSuccess(GetDeviceInfoRs getDeviceInfoRs) {
                callbackSubscriber.onSuccess(getDeviceInfoRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getRentalRatesByDeviceID(int deviceTypeID, int locationID, CallbackSubscriber<RentalPriceListRs> callbackSubscriber) {

        HashMap<String, Object> body = new HashMap<>();
        body.put("DeviceTypeID", deviceTypeID);
        body.put("LocationID", locationID);
        body.put("IsCompPriceRequested", false);

        remoteDataSource.getRentalRatesByDeviceID(body, new CallbackSubscriber<RentalPriceListRs>() {
            @Override
            public void onSuccess(RentalPriceListRs rentalPriceListRs) {
                callbackSubscriber.onSuccess(rentalPriceListRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getAllOperatorByCustomerID(int id, CallbackSubscriber<GetOperatorListRs> callbackSubscriber) {

        remoteDataSource.getAllOperatorByCustomerID(id, new CallbackSubscriber<GetOperatorListRs>() {
            @Override
            public void onSuccess(GetOperatorListRs getOperatorListRs) {
                callbackSubscriber.onSuccess(getOperatorListRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getOccupantListByOperatorID(int id, CallbackSubscriber<GetOccupantListRs> callbackSubscriber) {

        remoteDataSource.getOccupantListByOperatorID(id, new CallbackSubscriber<GetOccupantListRs>() {
            @Override
            public void onSuccess(GetOccupantListRs getOccupantListRs) {
                callbackSubscriber.onSuccess(getOccupantListRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void getCustomerPickupLocationByDestination(int destinationID, int deviceTypeID, CallbackSubscriber<GetCustomerLocationListRs> callbackSubscriber) {

        HashMap<String, Object> body = new HashMap<>();
        body.put("destinationID", destinationID);
        body.put("deviceTypeID", deviceTypeID);


        remoteDataSource.getCustomerPickupLocationByDestination(body, new CallbackSubscriber<GetCustomerLocationListRs>() {
            @Override
            public void onSuccess(GetCustomerLocationListRs response) {
                callbackSubscriber.onSuccess(response);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getAccessoryByDeviceTypeIdList(int deviceTypeId, int locationid, CallbackSubscriber<GetAccessoryByDeviceTypeIdListRs> callbackSubscriber) {

        HashMap<String, Object> body = new HashMap<>();
        body.put("DeviceTypeId", deviceTypeId);
        body.put("Locationid", locationid);


        remoteDataSource.getAccessoryByDeviceTypeIdList(body, new CallbackSubscriber<GetAccessoryByDeviceTypeIdListRs>() {
            @Override
            public void onSuccess(GetAccessoryByDeviceTypeIdListRs response) {
                callbackSubscriber.onSuccess(response);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getSameDayReservation(String day, int deviceTypeId, int locationId, CallbackSubscriber<GetSameDayReservationRs> callbackSubscriber) {

        HashMap<String, Object> body = new HashMap<>();
        body.put("Day", day);
        body.put("DeviceTypeId", deviceTypeId);
        body.put("LocationId", locationId);


        remoteDataSource.getSameDayReservation(body, new CallbackSubscriber<GetSameDayReservationRs>() {
            @Override
            public void onSuccess(GetSameDayReservationRs response) {
                callbackSubscriber.onSuccess(response);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getOrderBillingProfile(String pickupDate, String pickupTime, String returnDate, String returnTime, int devicetypid, int locationId, int customerID, String newReturnDate, String newReturnTime, String orderId, CallbackSubscriber<GetOrderBillingProfileRs> callbackSubscriber) {

        HashMap<String, Object> body = new HashMap<>();
        body.put("pickupDate", pickupDate);
        body.put("pickupTime", pickupTime);
        body.put("returnDate", returnDate);
        body.put("returnTime", returnTime);
        body.put("devicetypid", devicetypid);
        body.put("locationID", locationId);
        body.put("CustomerID", customerID);
        body.put("newReturnDate", newReturnDate);
        body.put("newReturnTime", newReturnTime);
        body.put("orderId", orderId);


        remoteDataSource.getOrderBillingProfile(body, new CallbackSubscriber<GetOrderBillingProfileRs>() {
            @Override
            public void onSuccess(GetOrderBillingProfileRs response) {
                callbackSubscriber.onSuccess(response);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getAllPaymentProfileByCustomerID(int id, CallbackSubscriber<GetAllPaymentProfileByCustomerIDRs> callbackSubscriber) {

        remoteDataSource.getAllPaymentProfileByCustomerID(id, new CallbackSubscriber<GetAllPaymentProfileByCustomerIDRs>() {
            @Override
            public void onSuccess(GetAllPaymentProfileByCustomerIDRs getAllPaymentProfileByCustomerIDRs) {
                callbackSubscriber.onSuccess(getAllPaymentProfileByCustomerIDRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getAllECheckPaymentProfileByCustomerID(int id, CallbackSubscriber<GetAllECheckPaymentProfileByCustomerIDRs> callbackSubscriber) {

        remoteDataSource.getAllECheckPaymentProfileByCustomerID(id, new CallbackSubscriber<GetAllECheckPaymentProfileByCustomerIDRs>() {
            @Override
            public void onSuccess(GetAllECheckPaymentProfileByCustomerIDRs getAllPaymentProfileByCustomerIDRs) {
                callbackSubscriber.onSuccess(getAllPaymentProfileByCustomerIDRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getCustomerByID(int id, CallbackSubscriber<GetCustomerByIDRs> callbackSubscriber) {

        remoteDataSource.getCustomerByID(id, new CallbackSubscriber<GetCustomerByIDRs>() {
            @Override
            public void onSuccess(GetCustomerByIDRs getCustomerByIDRs) {
                callbackSubscriber.onSuccess(getCustomerByIDRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getPromotionDetailByLocationIDNCode(GetPromotionDetailByLocationIDNCodeRq getPromotionDetailByLocationIDNCodeRq, CallbackSubscriber<GetPromotionDetailByLocationIDNCodeRs> callbackSubscriber) {

        JsonObject getPromotionDetailByLocationIDNCodeJsonObject = new Gson().toJsonTree(getPromotionDetailByLocationIDNCodeRq).getAsJsonObject();

        remoteDataSource.getPromotionDetailByLocationIDNCode(getPromotionDetailByLocationIDNCodeJsonObject.toString(), new CallbackSubscriber<GetPromotionDetailByLocationIDNCodeRs>() {
            @Override
            public void onSuccess(GetPromotionDetailByLocationIDNCodeRs getPromotionDetailByLocationIDNCodeRs) {
                callbackSubscriber.onSuccess(getPromotionDetailByLocationIDNCodeRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getCustomerOrderHistory(int id, CallbackSubscriber<GetCustomerOrderHistoryRs> callbackSubscriber) {

        remoteDataSource.getCustomerOrderHistory(id, new CallbackSubscriber<GetCustomerOrderHistoryRs>() {
            @Override
            public void onSuccess(GetCustomerOrderHistoryRs getCustomerOrderHistoryRs) {
                callbackSubscriber.onSuccess(getCustomerOrderHistoryRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void authorizePaymentRequest(int customerID, String firstName, String middleName, String lastName, String email, String phone, String city, String zip, String state, int stateID, String country, int cardTypeID, String creditCardNumber, String csv, String address1, String address2, int expYear, int expMonth, CallbackSubscriber<AuthorizePaymentRequestRs> callbackSubscriber) {

        HashMap<String, Object> body = new HashMap<>();
        body.put("CustomerID", customerID);
        body.put("FirstName", firstName);
        body.put("MiddleName", middleName);
        body.put("LastName", lastName);
        body.put("Email", email);
        body.put("Phone", phone);
        body.put("City", city);
        body.put("Zip", zip);
        body.put("State", state);
        body.put("StateID", stateID);
        body.put("Country", country);
        body.put("CardTypeID", cardTypeID);
        body.put("CreditCardNumber", creditCardNumber);
        body.put("CSV", csv);
        body.put("address1", address1);
        body.put("address2", address2);
        body.put("ExpYear", expYear);
        body.put("ExpMonth", expMonth);

        remoteDataSource.authorizePaymentRequest(body, new CallbackSubscriber<AuthorizePaymentRequestRs>() {
            @Override
            public void onSuccess(AuthorizePaymentRequestRs response) {
                callbackSubscriber.onSuccess(response);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void authorizeECheckRequest(int customerID, String firstName, String middleName, String lastName, String address1, String address2, String country, String state, int stateID, String city, String zip, String phone, String email, String bankName, String bankAccountType, String accountNumber, String routingNumber,


                                       CallbackSubscriber<AuthorizeECheckRequestRs> callbackSubscriber) {

        HashMap<String, Object> body = new HashMap<>();

       /* {
            "StateID": 27,
                "City": "NEW YORk",
                "MiddleName": "",
                "Country": "USA",
                "State": null,
                "FirstName": "SwarupTest",
                "CustomerID": 37614,
                "Email": "NMIST@GMAI.COM",
                "LastName": "Panda",
                "address1": "TEST ADDRESS1",
                "Zip": "74015",
                "Phone": "(609)602-1442",
                "address2": "",
                "BankName": "Test Bank",
                "AccountNumber": "1234567891",
                "RoutingNumber": "111000025",
                "AccountType": "Checking"
        }*/
        body.put("StateID", stateID);
        body.put("City", city);
        body.put("MiddleName", middleName);
        body.put("Country", country);
        body.put("State", state);
        body.put("FirstName", firstName);
        body.put("CustomerID", customerID);
        body.put("Email", email);
        body.put("LastName", lastName);
        body.put("address1", address1);
        body.put("Zip", zip);
        body.put("Phone", phone);
        body.put("address2", address2);
        body.put("BankName", bankName);
        body.put("AccountNumber", accountNumber);
        body.put("RoutingNumber", routingNumber);
        body.put("AccountType", bankAccountType);

        remoteDataSource.authorizeECheckRequest(body, new CallbackSubscriber<AuthorizeECheckRequestRs>() {
            @Override
            public void onSuccess(AuthorizeECheckRequestRs response) {
                callbackSubscriber.onSuccess(response);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void getAllActiveOrderByCustomerID(int id, CallbackSubscriber<GetAllActiveOrderByCustomerIDRs> callbackSubscriber) {

        Log.e("nilam", "id.tosti ${id}" + id);
        remoteDataSource.getAllActiveOrderByCustomerID(id, new CallbackSubscriber<GetAllActiveOrderByCustomerIDRs>() {
            @Override
            public void onSuccess(GetAllActiveOrderByCustomerIDRs getAllActiveOrderByCustomerIDRs) {
                callbackSubscriber.onSuccess(getAllActiveOrderByCustomerIDRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getLocationByID(int id, CallbackSubscriber<GetLocationByIdRs> callbackSubscriber) {

        remoteDataSource.getLocationByID(id, new CallbackSubscriber<GetLocationByIdRs>() {
            @Override
            public void onSuccess(GetLocationByIdRs getLocationByIdRs) {
                callbackSubscriber.onSuccess(getLocationByIdRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void saveOrderList(List<SaveOrderRq> orderList, Boolean isCreditCardRadioButtonSelected, CallbackSubscriber<SaveOrderListRs> callbackSubscriber) {


        JsonArray orderListJsonArray = new JsonArray();

        Map<Integer, Long> orderDateAndLocationMap = new HashMap();

        for (int index = 0; index < orderList.size(); index++) {
            orderListJsonArray.add(orderList.get(index).convertToJson(orderDateAndLocationMap, isCreditCardRadioButtonSelected));
            orderDateAndLocationMap.put(orderList.get(index).getLocationID(), orderList.get(index).getArrivalDateAndTime());
        }
        Log.e("Nilam---->", orderListJsonArray.toString());
        remoteDataSource.saveOrderList(orderListJsonArray.toString(), new CallbackSubscriber<SaveOrderListRs>() {
            @Override
            public void onSuccess(SaveOrderListRs saveOrderListRs) {
                callbackSubscriber.onSuccess(saveOrderListRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void getDeviceTrainingVideoAndData(List<DeviceTrainingVideoDataRq> deviceTrainingVideoDataRq, CallbackSubscriber<JsonArray> callbackSubscriber) {

        JsonArray deviceTrainingVideoDataRqArray = new Gson().toJsonTree(deviceTrainingVideoDataRq).getAsJsonArray();


        remoteDataSource.getDeviceTrainingVideoAndData(deviceTrainingVideoDataRqArray.toString(), new CallbackSubscriber<JsonArray>() {
            @Override
            public void onSuccess(JsonArray getDeviceTrainingVideoAndDataRs) {
                callbackSubscriber.onSuccess(getDeviceTrainingVideoAndDataRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getOrderDetailsByOrderID(int id, CallbackSubscriber<GetOrderDetailRs> callbackSubscriber) {

        remoteDataSource.getOrderDetailsByOrderID(id, new CallbackSubscriber<GetOrderDetailRs>() {
            @Override
            public void onSuccess(GetOrderDetailRs getOrderDetailRs) {
                callbackSubscriber.onSuccess(getOrderDetailRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getAllState(CallbackSubscriber<GetAllStateRs> callbackSubscriber) {

        remoteDataSource.getAllState(new CallbackSubscriber<GetAllStateRs>() {
            @Override
            public void onSuccess(GetAllStateRs getAllStateRs) {
                callbackSubscriber.onSuccess(getAllStateRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void saveCustomer(SaveCustomerRq saveCustomerRq, CallbackSubscriber<SaveCustomerRs> callbackSubscriber) {

        JsonObject saveCustomerRqJsonObject = saveCustomerRq.convertToJson();

        remoteDataSource.saveCustomer(saveCustomerRqJsonObject.toString(), new CallbackSubscriber<SaveCustomerRs>() {
            @Override
            public void onSuccess(SaveCustomerRs saveCustomerRs) {
                callbackSubscriber.onSuccess(saveCustomerRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void saveOccupant(AddOccupantRq addOccupantRq, CallbackSubscriber<AddOccupantRs> callbackSubscriber) {

        JsonObject saveOccupantRqJsonObject = addOccupantRq.convertToJson();

        remoteDataSource.saveOccupant(saveOccupantRqJsonObject.toString(), new CallbackSubscriber<AddOccupantRs>() {
            @Override
            public void onSuccess(AddOccupantRs saveOccupantRs) {
                callbackSubscriber.onSuccess(saveOccupantRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void addEditNewOperator(AddAndEditOperatorRq addAndEditOperatorRq, CallbackSubscriber<AddAndEditOperatorRs> callbackSubscriber) {

        JsonObject addAndEditOperatorRqJsonObject = addAndEditOperatorRq.convertToJson();

        remoteDataSource.addEditNewOperator(addAndEditOperatorRqJsonObject.toString(), new CallbackSubscriber<AddAndEditOperatorRs>() {
            @Override
            public void onSuccess(AddAndEditOperatorRs addAndEditOperatorRs) {
                callbackSubscriber.onSuccess(addAndEditOperatorRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getDocument(String url, String apikey, Boolean authenticate, String type, Boolean vaultSave, MediaResult file, CallbackSubscriber<GetDocumentRs> callbackSubscriber) {


        remoteDataSource.getDocument(url, apikey, authenticate, type, vaultSave, file, new CallbackSubscriber<GetDocumentRs>() {
            @Override
            public void onSuccess(GetDocumentRs getDocumentRs) {
                callbackSubscriber.onSuccess(getDocumentRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void getRewardPolicy(CallbackSubscriber<GetRewardPolicyRs> callbackSubscriber) {


        remoteDataSource.getRewardPolicy(new CallbackSubscriber<GetRewardPolicyRs>() {
            @Override
            public void onSuccess(GetRewardPolicyRs getRewardPolicyRs) {
                callbackSubscriber.onSuccess(getRewardPolicyRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void checkForReturnDeviceImage(int orderId, CallbackSubscriber<String> callbackSubscriber) {


        remoteDataSource.checkForReturnDeviceImage(orderId, new CallbackSubscriber<String>() {
            @Override
            public void onSuccess(String checkForReturnDeviceImageRs) {
                callbackSubscriber.onSuccess(checkForReturnDeviceImageRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void imageUploadedForReturnDevice(ImageUploadedForReturnDeviceRq imageUploadedForReturnDeviceRq, CallbackSubscriber<ImageUploadedForReturnDeviceRs> callbackSubscriber) {


        JsonObject requestObject = new Gson().toJsonTree(imageUploadedForReturnDeviceRq).getAsJsonObject();

        remoteDataSource.imageUploadedForReturnDevice(requestObject.toString(), new CallbackSubscriber<ImageUploadedForReturnDeviceRs>() {
            @Override
            public void onSuccess(ImageUploadedForReturnDeviceRs imageUploadedForReturnDeviceRs) {
                callbackSubscriber.onSuccess(imageUploadedForReturnDeviceRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void getEntityDefination(CallbackSubscriber<GetEntityDefinationRs> callbackSubscriber) {

        remoteDataSource.getEntityDefination(new CallbackSubscriber<GetEntityDefinationRs>() {
            @Override
            public void onSuccess(GetEntityDefinationRs getEntityDefinationRs) {
                callbackSubscriber.onSuccess(getEntityDefinationRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void addDefaultOccupant(int operatorID, CallbackSubscriber<AddDefaultOccupantRs> callbackSubscriber) {

        remoteDataSource.addDefaultOccupant(operatorID, new CallbackSubscriber<AddDefaultOccupantRs>() {
            @Override
            public void onSuccess(AddDefaultOccupantRs addDefaultOccupantRs) {
                callbackSubscriber.onSuccess(addDefaultOccupantRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getExistingOccupant(SearchOccupantListRq searchOccupantListRq, CallbackSubscriber<SearchOccupantListRs> callbackSubscriber) {


        JsonObject searchOccupantListRqObject = new Gson().toJsonTree(searchOccupantListRq).getAsJsonObject();

        remoteDataSource.getExistingOccupant(searchOccupantListRqObject.toString(), new CallbackSubscriber<SearchOccupantListRs>() {
            @Override
            public void onSuccess(SearchOccupantListRs searchOccupantListRs) {
                callbackSubscriber.onSuccess(searchOccupantListRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getPickupLocationInstruction(GetPickupLocationInstructionRq getPickupLocationInstructionRq, CallbackSubscriber<GetPickupLocationInstructionRs> callbackSubscriber) {


        JsonObject getPickupLocationInstructionRqObject = new Gson().toJsonTree(getPickupLocationInstructionRq).getAsJsonObject();

        remoteDataSource.getPickupLocationInstruction(getPickupLocationInstructionRqObject.toString(), new CallbackSubscriber<GetPickupLocationInstructionRs>() {
            @Override
            public void onSuccess(GetPickupLocationInstructionRs getPickupLocationInstructionRs) {
                callbackSubscriber.onSuccess(getPickupLocationInstructionRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getDevicePropertyOptions(int optionId, CallbackSubscriber<GetDevicePropertyOptionRs> callbackSubscriber) {

        remoteDataSource.getDevicePropertyOptions(optionId, new CallbackSubscriber<GetDevicePropertyOptionRs>() {
            @Override
            public void onSuccess(GetDevicePropertyOptionRs getDevicePropertyOptionRs) {
                callbackSubscriber.onSuccess(getDevicePropertyOptionRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getOperatorByID(int operatorID, CallbackSubscriber<GetOperatorByIDRs> callbackSubscriber) {

        remoteDataSource.getOperatorByID(operatorID, new CallbackSubscriber<GetOperatorByIDRs>() {
            @Override
            public void onSuccess(GetOperatorByIDRs getOperatorByIDRs) {
                callbackSubscriber.onSuccess(getOperatorByIDRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getOccupantByID(int occupantID, CallbackSubscriber<GetOccupantByIDRs> callbackSubscriber) {

        remoteDataSource.getOccupantByID(occupantID, new CallbackSubscriber<GetOccupantByIDRs>() {
            @Override
            public void onSuccess(GetOccupantByIDRs getOccupantByIDRs) {
                callbackSubscriber.onSuccess(getOccupantByIDRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void removeOccupantById(int occupantID, CallbackSubscriber<String> callbackSubscriber) {

        remoteDataSource.removeOccupantById(occupantID, new CallbackSubscriber<String>() {
            @Override
            public void onSuccess(String jsonObject) {
                callbackSubscriber.onSuccess(jsonObject);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void reSendAttestation(ReSendAttestationRq reSendAttestationRq, CallbackSubscriber<String> callbackSubscriber) {


        JsonObject reSendAttestationRqObject = new Gson().toJsonTree(reSendAttestationRq).getAsJsonObject();

        remoteDataSource.reSendAttestation(reSendAttestationRqObject.toString(), new CallbackSubscriber<String>() {
            @Override
            public void onSuccess(String response) {
                callbackSubscriber.onSuccess(response);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void cancelOrderByOrderId(CancelOrderByOrderIdRq cancelOrderByOrderIdRq, CallbackSubscriber<String> callbackSubscriber) {


        JsonObject cancelOrderByOrderIdObject = new Gson().toJsonTree(cancelOrderByOrderIdRq).getAsJsonObject();

        remoteDataSource.cancelOrderByOrderId(cancelOrderByOrderIdObject.toString(), new CallbackSubscriber<String>() {
            @Override
            public void onSuccess(String response) {
                callbackSubscriber.onSuccess(response);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void getBonusDayBillingProfileInfo(GetBonusDayBillingProfileInfoRq getBonusDayBillingProfileInfoRq, CallbackSubscriber<GetBonusDayBillingProfileInfoRs> callbackSubscriber) {

        JsonObject getBonusDayBillingProfileInfoRqJsonObject = new Gson().toJsonTree(getBonusDayBillingProfileInfoRq).getAsJsonObject();

        remoteDataSource.getBonusDayBillingProfileInfo(getBonusDayBillingProfileInfoRqJsonObject.toString(), new CallbackSubscriber<GetBonusDayBillingProfileInfoRs>() {
            @Override
            public void onSuccess(GetBonusDayBillingProfileInfoRs getBonusDayBillingProfileInfoRs) {
                callbackSubscriber.onSuccess(getBonusDayBillingProfileInfoRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getBonusDayBillingProfileInfoList(GetBonusDayBillingProfileInfoRq getBonusDayBillingProfileInfoRq, CallbackSubscriber<GetBonusDayBillingProfileInfoListRs> callbackSubscriber) {

        JsonObject getBonusDayBillingProfileInfoRqJsonObject = new Gson().toJsonTree(getBonusDayBillingProfileInfoRq).getAsJsonObject();

        remoteDataSource.getBonusDayBillingProfileInfoList(getBonusDayBillingProfileInfoRqJsonObject.toString(), new CallbackSubscriber<GetBonusDayBillingProfileInfoListRs>() {
            @Override
            public void onSuccess(GetBonusDayBillingProfileInfoListRs getBonusDayBillingProfileInfoListRs) {
                callbackSubscriber.onSuccess(getBonusDayBillingProfileInfoListRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void applyBonus(ApplyBonusRq applyBonusRq, CallbackSubscriber<ApplyBonusRs> callbackSubscriber) {

        JsonObject applyBonusRqJsonObject = new Gson().toJsonTree(applyBonusRq).getAsJsonObject();

        remoteDataSource.applyBonus(applyBonusRqJsonObject.toString(), new CallbackSubscriber<ApplyBonusRs>() {
            @Override
            public void onSuccess(ApplyBonusRs applyBonusRs) {
                callbackSubscriber.onSuccess(applyBonusRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getAppConfiguration(CallbackSubscriber<GetAppConfigurationRs> callbackSubscriber) {

        remoteDataSource.getAppConfiguration(new CallbackSubscriber<JsonObject>() {
            @Override
            public void onSuccess(JsonObject response) {

                GetAppConfigurationRs getAppConfigurationRs = GsonInterface.getInstance().getGson().fromJson(response, GetAppConfigurationRs.class);
                if (getAppConfigurationRs != null && getAppConfigurationRs.getResult() != null && getAppConfigurationRs.getResult().isStatus()) {
                    PrefHelper.getInstance().putString(KEY_APP_CONFIG, response.toString());
                }
                callbackSubscriber.onSuccess(getAppConfigurationRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void deleteAuthorizedPaymentProfile(DeleteAuthorizedPaymentProfileRq deleteAuthorizedPaymentProfileRq, CallbackSubscriber<DeleteAuthorizedPaymentProfileRs> callbackSubscriber) {


        JsonObject deleteAuthorizedPaymentProfileRqObject = new Gson().toJsonTree(deleteAuthorizedPaymentProfileRq).getAsJsonObject();

        remoteDataSource.deleteAuthorizedPaymentProfile(deleteAuthorizedPaymentProfileRqObject.toString(), new CallbackSubscriber<DeleteAuthorizedPaymentProfileRs>() {
            @Override
            public void onSuccess(DeleteAuthorizedPaymentProfileRs deleteAuthorizedPaymentProfileRs) {
                callbackSubscriber.onSuccess(deleteAuthorizedPaymentProfileRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void deleteAuthorizedPaymentECheckCardProfile(DeleteAuthorizedPaymentProfileRq deleteAuthorizedPaymentProfileRq, CallbackSubscriber<DeleteAuthorizedPaymentProfileRs> callbackSubscriber) {


        JsonObject deleteAuthorizedPaymentProfileRqObject = new Gson().toJsonTree(deleteAuthorizedPaymentProfileRq).getAsJsonObject();

        remoteDataSource.deleteAuthorizedPaymentProfile(deleteAuthorizedPaymentProfileRqObject.toString(), new CallbackSubscriber<DeleteAuthorizedPaymentProfileRs>() {
            @Override
            public void onSuccess(DeleteAuthorizedPaymentProfileRs deleteAuthorizedPaymentProfileRs) {
                callbackSubscriber.onSuccess(deleteAuthorizedPaymentProfileRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void updateProfile(SaveCustomerRq saveCustomerRq, CallbackSubscriber<SaveCustomerRs> callbackSubscriber) {

        JsonObject saveCustomerRqJsonObject = saveCustomerRq.convertToJsonForUpdateProfile();

        remoteDataSource.updateProfile(saveCustomerRqJsonObject.toString(), new CallbackSubscriber<SaveCustomerRs>() {
            @Override
            public void onSuccess(SaveCustomerRs saveCustomerRs) {
                callbackSubscriber.onSuccess(saveCustomerRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void retrieveCellPhoneNumber(String email, CallbackSubscriber<RetrieveCellPhoneNumberRs> callbackSubscriber) {

        remoteDataSource.retrieveCellPhoneNumber(email, new CallbackSubscriber<RetrieveCellPhoneNumberRs>() {
            @Override
            public void onSuccess(RetrieveCellPhoneNumberRs retrieveCellPhoneNumberRs) {
                callbackSubscriber.onSuccess(retrieveCellPhoneNumberRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void addDeviceInventoryID(AddDeviceInventoryIDRq addDeviceInventoryIDRq, CallbackSubscriber<AddDeviceInventoryIDRs> callbackSubscriber) {

        JsonObject addDeviceInventoryIDRqJsonObject = new Gson().toJsonTree(addDeviceInventoryIDRq).getAsJsonObject();

        remoteDataSource.addDeviceInventoryID(addDeviceInventoryIDRqJsonObject.toString(), new CallbackSubscriber<AddDeviceInventoryIDRs>() {
            @Override
            public void onSuccess(AddDeviceInventoryIDRs addDeviceInventoryIDRs) {
                callbackSubscriber.onSuccess(addDeviceInventoryIDRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void validateLicense(CallbackSubscriber<ValidateLicenseRs> callbackSubscriber) {

        remoteDataSource.validateLicense(new CallbackSubscriber<ValidateLicenseRs>() {
            @Override
            public void onSuccess(ValidateLicenseRs validateLicenseRs) {
                callbackSubscriber.onSuccess(validateLicenseRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void validateOrderListTime(List<ValidateOrderTimeRq> validateOrderTimeRqRq, CallbackSubscriber<ValidateOrderListTimeRs> callbackSubscriber) {

        JsonArray validateOrderTimeRqRqArray = new Gson().toJsonTree(validateOrderTimeRqRq).getAsJsonArray();

        remoteDataSource.validateOrderListTime(validateOrderTimeRqRqArray.toString(), new CallbackSubscriber<ValidateOrderListTimeRs>() {
            @Override
            public void onSuccess(ValidateOrderListTimeRs validateOrderListTimeRs) {
                callbackSubscriber.onSuccess(validateOrderListTimeRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getNotificationList(CallbackSubscriber<NotificationListRs> callbackSubscriber) {

        remoteDataSource.getNotificationList(new CallbackSubscriber<NotificationListRs>() {
            @Override
            public void onSuccess(NotificationListRs notificationListRs) {
                callbackSubscriber.onSuccess(notificationListRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getBadgeCount(CallbackSubscriber<GetBadgeCountRs> callbackSubscriber) {

        remoteDataSource.getBadgeCount(new CallbackSubscriber<GetBadgeCountRs>() {
            @Override
            public void onSuccess(GetBadgeCountRs getBadgeCountRs) {
                callbackSubscriber.onSuccess(getBadgeCountRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });

    }

    public void markNotificationAsRead(CallbackSubscriber<MarkNotificationAsReadRs> callbackSubscriber) {

        remoteDataSource.markNotificationAsRead(new CallbackSubscriber<MarkNotificationAsReadRs>() {
            @Override
            public void onSuccess(MarkNotificationAsReadRs markNotificationAsReadRs) {
                callbackSubscriber.onSuccess(markNotificationAsReadRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });

    }

    public void addUpdateFCMToken(AddUpdateFCMTokenRq addUpdateFCMTokenRq, CallbackSubscriber<AddUpdateFCMTokenRs> callbackSubscriber) throws JSONException {
       /* JsonObject jsonObj = new Gson().toJsonTree(addUpdateFCMTokenRq).getAsJsonObject();
        String outputJson= new Gson().toJson(addUpdateFCMTokenRq);
        Log.e("Nilam","json data -->555. "+outputJson.toString());
        Log.e("Nilam","json data -->1. "+jsonObj.toString());
         String jsonString = new Gson().toJson(addUpdateFCMTokenRq);
        // Convert JSON string to JSONObject
        JSONObject addUpdateFCMTokenRqJsonObject = new JSONObject(jsonString);
        Log.e("Nilam","json data -->2. "+addUpdateFCMTokenRqJsonObject.toString());*/
        JsonObject addUpdateFCMTokenRqJsonObject = new Gson().toJsonTree(addUpdateFCMTokenRq).getAsJsonObject();

        remoteDataSource.addUpdateFCMToken(addUpdateFCMTokenRqJsonObject.toString(), new CallbackSubscriber<AddUpdateFCMTokenRs>() {
            @Override
            public void onSuccess(AddUpdateFCMTokenRs addUpdateFCMTokenRs) {
                callbackSubscriber.onSuccess(addUpdateFCMTokenRs);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void getToken(CallbackSubscriber<String> callbackSubscriber) {

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {

            if (token != null) {

                callbackSubscriber.onSuccess(token);
            } else {
                Log.e("TOKENTEST", "Error --> Token not get: ");

            }
        });

    }

    public com.data.model.user.User getUser() {

        if (user != null) return user;

        String response = PrefHelper.getInstance().getString(KEY_LOGIN_USER, null);

        if (response != null && !response.isEmpty()) {
            VerifyOTPRs verifyOTPRs = GsonInterface.getInstance().getGson().fromJson(response, VerifyOTPRs.class);

            if (verifyOTPRs != null && verifyOTPRs.getResult() != null && verifyOTPRs.getResult().isStatus()) {

                if (verifyOTPRs.getUser() != null && !verifyOTPRs.getUser().getToken().trim().isEmpty()) {

                    user = verifyOTPRs.getUser();
                }
            }
        }
        return user;

    }

    public Config getAppConfig() {

        String response = PrefHelper.getInstance().getString(KEY_APP_CONFIG, null);

        if (response != null && !response.isEmpty()) {
            GetAppConfigurationRs getAppConfigurationRs = GsonInterface.getInstance().getGson().fromJson(response, GetAppConfigurationRs.class);

            if (getAppConfigurationRs != null && getAppConfigurationRs.getResult() != null && getAppConfigurationRs.getResult().isStatus()) {
                return getAppConfigurationRs.getConfig();
            }
        }
        return null;
    }

    public boolean isLogin() {


        if (getUser() != null && !getUser().getToken().isEmpty()) return true;
        else return false;
    }

    public void setLocationId(int locationId) {
        PrefHelper.getInstance().putInt(KEY_LOCATION_ID, locationId);
    }

    public int getLocationId() {
        return PrefHelper.getInstance().getInt(KEY_LOCATION_ID, 0);
    }

    public void setLocationName(String locationName) {
        PrefHelper.getInstance().putString(KEY_LOCATION_NAME, locationName);
    }

    public String getLocationName() {
        return PrefHelper.getInstance().getString(KEY_LOCATION_NAME, "");
    }

    public void setLocationCategory(String locationCategory) {
        PrefHelper.getInstance().putString(KEY_LOCATION_CATEGORY, locationCategory);
    }

    public String getLocationCategory() {
        return PrefHelper.getInstance().getString(KEY_LOCATION_CATEGORY, "");
    }

    public int getLoginUserId() {
        int userId = PrefHelper.getInstance().getInt(KEY_USER_ID, 0);
        return userId;
    }

    public int getLeaseId() {
        int leaseId = PrefHelper.getInstance().getInt(KEY_LEASE_ID, 0);
        return leaseId;
    }

    public int getUserIdRoleId() {
        int userId = PrefHelper.getInstance().getInt(KEY_USER_ID_ROLE_ID, 0);
        return userId;
    }

    public String getToken() {

        if (getUser() != null) return getUser().getToken();

        return null;
    }

    public String getFirstName() {
        String firstName = PrefHelper.getInstance().getString(KEY_FIRST_NAME, null);
        return firstName;
    }

    public String getLastName() {
        String lastName = PrefHelper.getInstance().getString(KEY_LAST_NAME, null);
        return lastName;
    }

    public String getProfilePic() {
        String profilePic = PrefHelper.getInstance().getString(KEY_PROFILE_PIC, null);
        return profilePic;
    }

    public String getPhoneNumber() {
        String phoneNumber = PrefHelper.getInstance().getString(KEY_PHONE_NUMBER, null);
        return phoneNumber;
    }

    public String getEmailAddress() {
        String emailAddress = PrefHelper.getInstance().getString(KEY_EMAIL_ADDRESS, null);
        return emailAddress;
    }


    public int getEntityId() {
        return PrefHelper.getInstance().getInt(KEY_ENTITY_ID, 0);
    }

    public String getUnitName() {
        return PrefHelper.getInstance().getString(KEY_UNIT_NAME, null);
    }

    public void setFirebaseToken(String firebaseToken) {
        PrefHelper.getInstance().putString(KEY_FIREBASE_TOKEN, firebaseToken);
    }

    public String getFirebaseToken() {
        return PrefHelper.getInstance().getString(KEY_FIREBASE_TOKEN, null);
    }


    public void expiredToken() {

        PrefHelper.getInstance().putString(KEY_LOGIN_USER, null);
        PrefHelper.getInstance().putString(KEY_TOKEN, null);
        PrefHelper.getInstance().putString(KEY_PROFILE_PIC, null);
        PrefHelper.getInstance().putString(KEY_FIRST_NAME, null);
        PrefHelper.getInstance().putString(KEY_LAST_NAME, null);
        PrefHelper.getInstance().putInt(KEY_USER_ID, 0);
        PrefHelper.getInstance().putInt(KEY_USER_ID_ROLE_ID, 0);

        PrefHelper.getInstance().putString(KEY_EMAIL_ADDRESS, null);
        PrefHelper.getInstance().putString(KEY_PHONE_NUMBER, null);

        PrefHelper.getInstance().putInt(KEY_USER_TYPE, 0);
        PrefHelper.getInstance().putInt(KEY_ENTITY_ID, 0);
        PrefHelper.getInstance().putString(KEY_UNIT_NAME, null);

        PrefHelper.getInstance().putInt(KEY_LEASE_ID, 0);

        PrefHelper.getInstance().putString(KEY_FIREBASE_TOKEN, null);

        OrderData.INSTANCE.clearData();

        FilterHelper.getInstance().clearData();

        user = null;
    }

    public void getDisclaimerOfPaymentMethod(String id, CallbackSubscriber<GetDisclaimerOfPaymentMethodRs> callbackSubscriber) {

        remoteDataSource.getDisclaimerOfPaymentMethod(id, new CallbackSubscriber<GetDisclaimerOfPaymentMethodRs>() {
            @Override
            public void onSuccess(GetDisclaimerOfPaymentMethodRs getAllPaymentProfileByCustomerIDRs) {
                callbackSubscriber.onSuccess(getAllPaymentProfileByCustomerIDRs);
            }


            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void getProcessingFee(Boolean IsCardChecked, Boolean IsEcheckChecked, String priceWithTaxTotal,


                                 CallbackSubscriber<GetProcessingFeeRs> callbackSubscriber) {

        HashMap<String, Object> body = new HashMap<>();
        float number = Float.parseFloat(priceWithTaxTotal);
        body.put("IsCardChecked", IsCardChecked);
        body.put("IsEcheckChecked", IsEcheckChecked);
        body.put("priceWithTaxTotal", number);


        remoteDataSource.getProcessingFee(body, new CallbackSubscriber<GetProcessingFeeRs>() {
            @Override
            public void onSuccess(GetProcessingFeeRs response) {
                callbackSubscriber.onSuccess(response);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void getBlackoutDatesByLocationAndDevice(int deviceTypeId, int locationId,
                                                    CallbackSubscriber<GetBlackoutDatesByLocationAndDeviceRs> callbackSubscriber) {

        HashMap<String, Object> body = new HashMap<>();
        body.put("DeviceTypeId", deviceTypeId);
        body.put("LocationId", locationId);


        remoteDataSource.getBlackoutDatesByLocationAndDevice(body, new CallbackSubscriber<GetBlackoutDatesByLocationAndDeviceRs>() {
            @Override
            public void onSuccess(GetBlackoutDatesByLocationAndDeviceRs response) {
                callbackSubscriber.onSuccess(response);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }

    public void sentBugReport(String Description, String DeviceModel, String Email,  String AppVersion, String[][] documentData,
                                                    CallbackSubscriber<SentBugReportRs> callbackSubscriber) {

        HashMap<String, Object> body = new HashMap<>();
        body.put("Description",Description);
        body.put("DeviceModel", DeviceModel);
        if(!(Email.isBlank() || Email.isEmpty())){
            body.put("Email", Email);
        }
        body.put("AppVersion",AppVersion);


        // Create document list
        List<Map<String, Object>> documents = new ArrayList<>();


        // Loop to add documents dynamically
        for (String[] doc : documentData) {
            documents.add(Map.of("MimeType", doc[0], "Content", doc[1]));
        }

        // Add documents to body
        body.put("Documents", documents);


        remoteDataSource.sentBugReport(body, new CallbackSubscriber<SentBugReportRs>() {
            @Override
            public void onSuccess(SentBugReportRs response) {
                callbackSubscriber.onSuccess(response);
            }

            @Override
            public void onFailure(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }
        });
    }


    public void destroyInstance() {
        dataRepository = null;
    }
}
