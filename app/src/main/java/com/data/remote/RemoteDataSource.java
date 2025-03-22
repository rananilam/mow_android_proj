package com.data.remote;


import android.annotation.SuppressLint;
import android.util.Log;

import com.data.CallbackSubscriber;
import com.data.DataSource;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import libraries.image.helper.models.MediaResult;
import libraries.retrofit.RestError;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RemoteDataSource extends DataSource {

    private static RemoteDataSource remoteDataSource;

    private IService iService;

    private RemoteDataSource(IService iService) {
        super();
        this.iService = iService;

    }

    public static synchronized RemoteDataSource getInstance(
            IService iService) {
        if (remoteDataSource == null) {
            remoteDataSource = new RemoteDataSource(iService);
        }
        return remoteDataSource;
    }

    @Override
    public void getDeviceListByLocationId(int locationId, CallbackSubscriber<GetDeviceListRs> callbackSubscriber) {


        Observable<GetDeviceListRs> getDeviceListRsCall = iService.getDeviceListByLocationId(getRequestBody(String.valueOf(locationId)));

        getDeviceListRsCall = (Observable<GetDeviceListRs>) ((Observable<?>) getDeviceListRsCall).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        getDeviceListRsCall.subscribe(new RestsubScriber<GetDeviceListRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetDeviceListRs getDeviceListRs) {
                callbackSubscriber.onSuccess(getDeviceListRs);
            }
        });


//        Observable<GetDeviceListRs> getDeviceListRsCall = iService.getDeviceListByLocationId(getRequestBody(String.valueOf(locationId)));
//
//        getDeviceListRsCall = ((Observable<?>) getDeviceListRsCall).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
//
//        getDeviceListRsCall.subscribe(new RestsubScriber<GetDeviceListRs>() {
//            @Override
//            public void onRestError(RestError restError) {
//                callbackSubscriber.onFailure(restError);
//            }
//
//            @Override
//            public void onResponse(GetDeviceListRs getDeviceListRs) {
//                callbackSubscriber.onSuccess(getDeviceListRs);
//            }
//        });


    }

    @Override
    public void getDestinationList(CallbackSubscriber<GetDestinationListRs> callbackSubscriber) {
        Observable<GetDestinationListRs> getDestinationListRsCall = iService.getDestinationList();
        getDestinationListRsCall = getDestinationListRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getDestinationListRsCall.subscribe(new RestsubScriber<GetDestinationListRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetDestinationListRs getDestinationListRs) {
                callbackSubscriber.onSuccess(getDestinationListRs);
            }
        });
    }


    @Override
    public void generateOTP(String phoneNumber, CallbackSubscriber<GenerateOTPRs> callbackSubscriber) {


        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, "\"" + phoneNumber + "\"");


        Observable<GenerateOTPRs> generateOTPRsCall = iService.generateOTP(body);

        generateOTPRsCall = generateOTPRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        generateOTPRsCall.subscribe(new RestsubScriber<GenerateOTPRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GenerateOTPRs generateOTPRs) {
                callbackSubscriber.onSuccess(generateOTPRs);
            }
        });
    }

    @Override
    public void retrieveCellPhoneNumber(String email, CallbackSubscriber<RetrieveCellPhoneNumberRs> callbackSubscriber) {


        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, "\"" + email + "\"");


        Observable<RetrieveCellPhoneNumberRs> retrieveCellPhoneNumberRsCall = iService.retrieveCellPhoneNumber(body);


        retrieveCellPhoneNumberRsCall = retrieveCellPhoneNumberRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        retrieveCellPhoneNumberRsCall.subscribe(new RestsubScriber<RetrieveCellPhoneNumberRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(RetrieveCellPhoneNumberRs retrieveCellPhoneNumberRs) {
                callbackSubscriber.onSuccess(retrieveCellPhoneNumberRs);
            }
        });


    }


    @Override
    public void verifyOTP(HashMap<String, Object> body, CallbackSubscriber<JsonObject> callbackSubscriber) {

        Observable<JsonObject> verifyOTPRsCall = iService.verifyOTP(body);

        verifyOTPRsCall = verifyOTPRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        verifyOTPRsCall.subscribe(new RestsubScriber<JsonObject>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(JsonObject verifyOTPRs) {
                callbackSubscriber.onSuccess(verifyOTPRs);
            }
        });
    }


    @Override
    public void getDeviceTypeByID(int id, CallbackSubscriber<GetDeviceInfoRs> callbackSubscriber) {

        Observable<GetDeviceInfoRs> getDeviceInfoRsCall = iService.getDeviceTypeByID(getRequestBody(String.valueOf(id)));

        getDeviceInfoRsCall = getDeviceInfoRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        getDeviceInfoRsCall.subscribe(new RestsubScriber<GetDeviceInfoRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetDeviceInfoRs getDeviceInfoRs) {
                callbackSubscriber.onSuccess(getDeviceInfoRs);
            }
        });
    }

    @Override
    public void getRentalRatesByDeviceID(HashMap<String, Object> body, CallbackSubscriber<RentalPriceListRs> callbackSubscriber) {

        Observable<RentalPriceListRs> rentalPriceListRsCall = iService.getRentalRatesByDeviceID(body);

        rentalPriceListRsCall = rentalPriceListRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        rentalPriceListRsCall.subscribe(new RestsubScriber<RentalPriceListRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(RentalPriceListRs rentalPriceListRs) {
                callbackSubscriber.onSuccess(rentalPriceListRs);
            }
        });
    }

    @Override
    public void getAllOperatorByCustomerID(int customerID, CallbackSubscriber<GetOperatorListRs> callbackSubscriber) {

        Observable<GetOperatorListRs> getOperatorListRsCall = iService.getAllOperatorByCustomerID(getRequestBody(String.valueOf(customerID)));

        getOperatorListRsCall = getOperatorListRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        getOperatorListRsCall.subscribe(new RestsubScriber<GetOperatorListRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetOperatorListRs getOperatorListRs) {
                callbackSubscriber.onSuccess(getOperatorListRs);
            }
        });
    }

    @Override
    public void getOccupantListByOperatorID(int operatorID, CallbackSubscriber<GetOccupantListRs> callbackSubscriber) {
        Observable<GetOccupantListRs> getOccupantListRsCall = iService.getOccupantListByOperatorID(getRequestBody(String.valueOf(operatorID)));

        getOccupantListRsCall = getOccupantListRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        getOccupantListRsCall.subscribe(new RestsubScriber<GetOccupantListRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetOccupantListRs getOccupantListRs) {
                callbackSubscriber.onSuccess(getOccupantListRs);
            }
        });
    }

    @Override
    public void getCustomerPickupLocationByDestination(HashMap<String, Object> body, CallbackSubscriber<GetCustomerLocationListRs> callbackSubscriber) {

        Observable<GetCustomerLocationListRs> getCustomerLocationListRsCall = iService.getCustomerPickupLocationByDestination(body);

        getCustomerLocationListRsCall = getCustomerLocationListRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        getCustomerLocationListRsCall.subscribe(new RestsubScriber<GetCustomerLocationListRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetCustomerLocationListRs getCustomerLocationListRs) {
                callbackSubscriber.onSuccess(getCustomerLocationListRs);
            }
        });
    }

    @Override
    public void getAccessoryByDeviceTypeIdList(HashMap<String, Object> body, CallbackSubscriber<GetAccessoryByDeviceTypeIdListRs> callbackSubscriber) {

        Observable<GetAccessoryByDeviceTypeIdListRs> getAccessoryByDeviceTypeIdListRsCall = iService.getAccessoryByDeviceTypeIdList(body);

        getAccessoryByDeviceTypeIdListRsCall = getAccessoryByDeviceTypeIdListRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        getAccessoryByDeviceTypeIdListRsCall.subscribe(new RestsubScriber<GetAccessoryByDeviceTypeIdListRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetAccessoryByDeviceTypeIdListRs getAccessoryByDeviceTypeIdListRs) {
                callbackSubscriber.onSuccess(getAccessoryByDeviceTypeIdListRs);
            }
        });
    }

    @Override
    public void getSameDayReservation(HashMap<String, Object> body, CallbackSubscriber<GetSameDayReservationRs> callbackSubscriber) {

        Observable<GetSameDayReservationRs> getSameDayReservationRsCall = iService.getSameDayReservation(body);

        getSameDayReservationRsCall = getSameDayReservationRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        getSameDayReservationRsCall.subscribe(new RestsubScriber<GetSameDayReservationRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetSameDayReservationRs getSameDayReservationRs) {
                callbackSubscriber.onSuccess(getSameDayReservationRs);
            }
        });

    }

    @Override
    public void getOrderBillingProfile(HashMap<String, Object> body, CallbackSubscriber<GetOrderBillingProfileRs> callbackSubscriber) {

        Observable<GetOrderBillingProfileRs> getOrderBillingProfileRsCall = iService.getOrderBillingProfile(body);

        getOrderBillingProfileRsCall = getOrderBillingProfileRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        getOrderBillingProfileRsCall.subscribe(new RestsubScriber<GetOrderBillingProfileRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetOrderBillingProfileRs getOrderBillingProfileRs) {
                callbackSubscriber.onSuccess(getOrderBillingProfileRs);
            }
        });


    }

    @Override
    public void getAllPaymentProfileByCustomerID(int id, CallbackSubscriber<GetAllPaymentProfileByCustomerIDRs> callbackSubscriber) {


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + id + "\"");


        Observable<GetAllPaymentProfileByCustomerIDRs> getAllPaymentProfileByCustomerIDRsCall = iService.getAllPaymentProfileByCustomerID(body);
        getAllPaymentProfileByCustomerIDRsCall = getAllPaymentProfileByCustomerIDRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getAllPaymentProfileByCustomerIDRsCall.subscribe(new RestsubScriber<GetAllPaymentProfileByCustomerIDRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetAllPaymentProfileByCustomerIDRs getAllPaymentProfileByCustomerIDRs) {
                callbackSubscriber.onSuccess(getAllPaymentProfileByCustomerIDRs);
            }
        });
    }


    @Override
    public void getDisclaimerOfPaymentMethod(String id, CallbackSubscriber<GetDisclaimerOfPaymentMethodRs> callbackSubscriber) {


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + id + "\"");


        Observable<GetDisclaimerOfPaymentMethodRs> getDisclaimerOfPaymentMethodCall = iService.getDisclaimerOfPaymentMethod(body);
        getDisclaimerOfPaymentMethodCall = getDisclaimerOfPaymentMethodCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getDisclaimerOfPaymentMethodCall.subscribe(new RestsubScriber<GetDisclaimerOfPaymentMethodRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetDisclaimerOfPaymentMethodRs getAllPaymentProfileByCustomerIDRs) {
                callbackSubscriber.onSuccess(getAllPaymentProfileByCustomerIDRs);

            }
        });
    }


    @Override
    public void getAllECheckPaymentProfileByCustomerID(int id, CallbackSubscriber<GetAllECheckPaymentProfileByCustomerIDRs> callbackSubscriber) {


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + id + "\"");


        Observable<GetAllECheckPaymentProfileByCustomerIDRs> getAllPaymentProfileByCustomerIDRsCall = iService.getAllECheckPaymentProfileByCustomerID(body);
        getAllPaymentProfileByCustomerIDRsCall = getAllPaymentProfileByCustomerIDRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getAllPaymentProfileByCustomerIDRsCall.subscribe(new RestsubScriber<GetAllECheckPaymentProfileByCustomerIDRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetAllECheckPaymentProfileByCustomerIDRs getAllPaymentProfileByCustomerIDRs) {
                callbackSubscriber.onSuccess(getAllPaymentProfileByCustomerIDRs);
            }
        });
    }

    @Override
    public void getCustomerByID(int id, CallbackSubscriber<GetCustomerByIDRs> callbackSubscriber) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + id + "\"");

        Observable<GetCustomerByIDRs> getCustomerByIDRsCall = iService.getCustomerByID(body);
        getCustomerByIDRsCall = getCustomerByIDRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getCustomerByIDRsCall.subscribe(new RestsubScriber<GetCustomerByIDRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetCustomerByIDRs getCustomerByIDRs) {
                callbackSubscriber.onSuccess(getCustomerByIDRs);
            }
        });

    }

    @Override
    public void getPromotionDetailByLocationIDNCode(String request, CallbackSubscriber<GetPromotionDetailByLocationIDNCodeRs> callbackSubscriber) {


        Observable<GetPromotionDetailByLocationIDNCodeRs> getPromotionDetailByLocationIDNCodeRsCall = iService.getPromotionDetailByLocationIDNCode(getRequestBody(request));
        getPromotionDetailByLocationIDNCodeRsCall = getPromotionDetailByLocationIDNCodeRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getPromotionDetailByLocationIDNCodeRsCall.subscribe(new RestsubScriber<GetPromotionDetailByLocationIDNCodeRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }


            @Override
            public void onResponse(GetPromotionDetailByLocationIDNCodeRs getPromotionDetailByLocationIDNCodeRs) {
                callbackSubscriber.onSuccess(getPromotionDetailByLocationIDNCodeRs);
            }
        });

    }

    @Override
    public void getCustomerOrderHistory(int id, CallbackSubscriber<GetCustomerOrderHistoryRs> callbackSubscriber) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + id + "\"");

        Observable<GetCustomerOrderHistoryRs> getCustomerOrderHistoryRsCall = iService.getCustomerOrderHistory(body);
        getCustomerOrderHistoryRsCall = getCustomerOrderHistoryRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getCustomerOrderHistoryRsCall.subscribe(new RestsubScriber<GetCustomerOrderHistoryRs>() {


            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetCustomerOrderHistoryRs getCustomerOrderHistoryRs) {
                String json = new Gson().toJson(getCustomerOrderHistoryRs);
                Log.e("Nilam Rana", json);
                callbackSubscriber.onSuccess(getCustomerOrderHistoryRs);
            }
        });

    }

    @Override
    public void authorizePaymentRequest(HashMap<String, Object> body, CallbackSubscriber<AuthorizePaymentRequestRs> callbackSubscriber) {

        Observable<AuthorizePaymentRequestRs> authorizePaymentRsCall = iService.authorizePaymentRequest(body);
        authorizePaymentRsCall = authorizePaymentRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        authorizePaymentRsCall.subscribe(new RestsubScriber<AuthorizePaymentRequestRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(AuthorizePaymentRequestRs jsonObject) {
                callbackSubscriber.onSuccess(jsonObject);
            }
        });
    }

    @Override
    public void authorizeECheckRequest(HashMap<String, Object> body, CallbackSubscriber<AuthorizeECheckRequestRs> callbackSubscriber) {

        Observable<AuthorizeECheckRequestRs> authorizePaymentRsCall = iService.authorizeECheckRequest(body);
        authorizePaymentRsCall = authorizePaymentRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        authorizePaymentRsCall.subscribe(new RestsubScriber<AuthorizeECheckRequestRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(AuthorizeECheckRequestRs jsonObject) {
                callbackSubscriber.onSuccess(jsonObject);
            }
        });
    }

    @Override
    public void getProcessingFee(HashMap<String, Object> body, CallbackSubscriber<GetProcessingFeeRs> callbackSubscriber) {

        Observable<GetProcessingFeeRs> authorizePaymentRsCall = iService.getProcessingFee(body);
        authorizePaymentRsCall = authorizePaymentRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        authorizePaymentRsCall.subscribe(new RestsubScriber<GetProcessingFeeRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetProcessingFeeRs jsonObject) {
                callbackSubscriber.onSuccess(jsonObject);
            }
        });
    }


    @Override
    public void getAllActiveOrderByCustomerID(int id, CallbackSubscriber<GetAllActiveOrderByCustomerIDRs> callbackSubscriber) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + id + "\"");

        Observable<GetAllActiveOrderByCustomerIDRs> getAllActiveOrderByCustomerIDRsCall = iService.getAllActiveOrderByCustomerID(body);
        getAllActiveOrderByCustomerIDRsCall = getAllActiveOrderByCustomerIDRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getAllActiveOrderByCustomerIDRsCall.subscribe(new RestsubScriber<GetAllActiveOrderByCustomerIDRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetAllActiveOrderByCustomerIDRs getAllActiveOrderByCustomerIDRs) {
                callbackSubscriber.onSuccess(getAllActiveOrderByCustomerIDRs);
            }
        });

    }

    @Override
    public void getLocationByID(int id, CallbackSubscriber<GetLocationByIdRs> callbackSubscriber) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + id + "\"");

        Observable<GetLocationByIdRs> getLocationByIdRsCall = iService.getLocationByID(body);
        getLocationByIdRsCall = getLocationByIdRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getLocationByIdRsCall.subscribe(new RestsubScriber<GetLocationByIdRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetLocationByIdRs getLocationByIdRs) {
                callbackSubscriber.onSuccess(getLocationByIdRs);
            }
        });

    }

    @Override
    public void saveOrderList(String orderList, CallbackSubscriber<SaveOrderListRs> callbackSubscriber) {


        Observable<SaveOrderListRs> saveOrderListRsCall = iService.saveOrderList(getRequestBody(orderList));
        saveOrderListRsCall = saveOrderListRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        saveOrderListRsCall.subscribe(new RestsubScriber<SaveOrderListRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(SaveOrderListRs saveOrderListRs) {
                callbackSubscriber.onSuccess(saveOrderListRs);
            }
        });
    }

    @Override
    public void getDeviceTrainingVideoAndData(String request, CallbackSubscriber<JsonArray> callbackSubscriber) {


        Observable<JsonArray> getDeviceTrainingVideoAndDataRsCall = iService.getDeviceTrainingVideoAndData(getRequestBody(request));
        getDeviceTrainingVideoAndDataRsCall = getDeviceTrainingVideoAndDataRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getDeviceTrainingVideoAndDataRsCall.subscribe(new RestsubScriber<JsonArray>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(JsonArray getDeviceTrainingVideoAndDataRs) {
                callbackSubscriber.onSuccess(getDeviceTrainingVideoAndDataRs);
            }
        });


    }

    @Override
    public void getOrderDetailsByOrderID(int id, CallbackSubscriber<GetOrderDetailRs> callbackSubscriber) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + id + "\"");

        Observable<GetOrderDetailRs> getOrderDetailRsCall = iService.getOrderDetailsByOrderID(body);
        getOrderDetailRsCall = getOrderDetailRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getOrderDetailRsCall.subscribe(new RestsubScriber<GetOrderDetailRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetOrderDetailRs getOrderDetailRs) {
                callbackSubscriber.onSuccess(getOrderDetailRs);
            }
        });

    }

    @Override
    public void getAllState(CallbackSubscriber<GetAllStateRs> callbackSubscriber) {

        Observable<GetAllStateRs> getAllStateRsCall = iService.getAllState();
        getAllStateRsCall = getAllStateRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getAllStateRsCall.subscribe(new RestsubScriber<GetAllStateRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetAllStateRs getAllStateRs) {
                callbackSubscriber.onSuccess(getAllStateRs);
            }
        });
    }

    @Override
    public void saveCustomer(String customerInfo, CallbackSubscriber<SaveCustomerRs> callbackSubscriber) {

        Observable<SaveCustomerRs> saveCustomerRsCall = iService.saveCustomer(getRequestBody(customerInfo));
        saveCustomerRsCall = saveCustomerRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        saveCustomerRsCall.subscribe(new RestsubScriber<SaveCustomerRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(SaveCustomerRs saveCustomerRs) {
                callbackSubscriber.onSuccess(saveCustomerRs);
            }
        });

    }

    @Override
    public void saveOccupant(String occupant, CallbackSubscriber<AddOccupantRs> callbackSubscriber) {

        Observable<AddOccupantRs> saveOccupantRsCall = iService.saveOccupant(getRequestBody(occupant));
        saveOccupantRsCall = saveOccupantRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        saveOccupantRsCall.subscribe(new RestsubScriber<AddOccupantRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(AddOccupantRs saveOccupantRs) {
                callbackSubscriber.onSuccess(saveOccupantRs);
            }
        });
    }

    @Override
    public void addEditNewOperator(String operator, CallbackSubscriber<AddAndEditOperatorRs> callbackSubscriber) {
        Observable<AddAndEditOperatorRs> addAndEditOperatorRsCall = iService.addEditNewOperator(getRequestBody(operator));
        addAndEditOperatorRsCall = addAndEditOperatorRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        addAndEditOperatorRsCall.subscribe(new RestsubScriber<AddAndEditOperatorRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(AddAndEditOperatorRs addAndEditOperatorRs) {
                callbackSubscriber.onSuccess(addAndEditOperatorRs);
            }
        });
    }

    @Override
    public void getDocument(String url, String apikey, Boolean authenticate, String type, Boolean vaultSave, MediaResult file, CallbackSubscriber<GetDocumentRs> callbackSubscriber) {

        MultipartBody.Part fileMultipart = getMultipartBodyPart(file.getPath(),
                file.getMimeType(),
                "file");

        Observable<GetDocumentRs> getDocumentRsCall = iService.getDocument(url,
                getRequestBody(apikey),
                getRequestBody(authenticate.toString()),
                getRequestBody(type),
                getRequestBody(vaultSave.toString()),
                fileMultipart);
        getDocumentRsCall = getDocumentRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getDocumentRsCall.subscribe(new RestsubScriber<GetDocumentRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetDocumentRs getDocumentRs) {
                callbackSubscriber.onSuccess(getDocumentRs);
            }
        });

    }


    @Override
    public void getRewardPolicy(CallbackSubscriber<GetRewardPolicyRs> callbackSubscriber) {

        Observable<GetRewardPolicyRs> getRewardPolicyRsCall = iService.getRewardPolicy();
        getRewardPolicyRsCall = getRewardPolicyRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getRewardPolicyRsCall.subscribe(new RestsubScriber<GetRewardPolicyRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetRewardPolicyRs getRewardPolicyRs) {
                callbackSubscriber.onSuccess(getRewardPolicyRs);
            }
        });

    }

    @Override
    public void checkForReturnDeviceImage(int orderId, CallbackSubscriber<String> callbackSubscriber) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + orderId + "\"");

        Observable<String> checkForReturnDeviceImageRsCall = iService.checkForReturnDeviceImage(body);
        checkForReturnDeviceImageRsCall = checkForReturnDeviceImageRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        checkForReturnDeviceImageRsCall.subscribe(new RestsubScriber<String>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(String checkForReturnDeviceImageRs) {
                callbackSubscriber.onSuccess(checkForReturnDeviceImageRs);
            }
        });
    }

    @Override
    public void imageUploadedForReturnDevice(String request, CallbackSubscriber<ImageUploadedForReturnDeviceRs> callbackSubscriber) {

        //   MediaType mediaType = MediaType.parse("application/json");
        //  RequestBody body = RequestBody.create(mediaType, "\""+orderId+"\"");

        Observable<ImageUploadedForReturnDeviceRs> imageUploadedForReturnDeviceRsCall = iService.imageUploadedForReturnDevice(getRequestBody(request));
        imageUploadedForReturnDeviceRsCall = imageUploadedForReturnDeviceRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        imageUploadedForReturnDeviceRsCall.subscribe(new RestsubScriber<ImageUploadedForReturnDeviceRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(ImageUploadedForReturnDeviceRs response) {
                callbackSubscriber.onSuccess(response);
            }
        });

    }

    @Override
    public void getEntityDefination(CallbackSubscriber<GetEntityDefinationRs> callbackSubscriber) {

        Observable<GetEntityDefinationRs> getEntityDefinationRsCall = iService.getEntityDefination();
        getEntityDefinationRsCall = getEntityDefinationRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getEntityDefinationRsCall.subscribe(new RestsubScriber<GetEntityDefinationRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetEntityDefinationRs getEntityDefinationRs) {
                callbackSubscriber.onSuccess(getEntityDefinationRs);
            }
        });

    }

    @Override
    public void addDefaultOccupant(int operatorID, CallbackSubscriber<AddDefaultOccupantRs> callbackSubscriber) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + operatorID + "\"");

        Observable<AddDefaultOccupantRs> addDefaultOccupantRsCall = iService.addDefaultOccupant(body);
        addDefaultOccupantRsCall = addDefaultOccupantRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        addDefaultOccupantRsCall.subscribe(new RestsubScriber<AddDefaultOccupantRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(AddDefaultOccupantRs addDefaultOccupantRs) {
                callbackSubscriber.onSuccess(addDefaultOccupantRs);
            }
        });
    }

    @Override
    public void getExistingOccupant(String request, CallbackSubscriber<SearchOccupantListRs> callbackSubscriber) {


        Observable<SearchOccupantListRs> searchOccupantListRsCall = iService.getExistingOccupant(getRequestBody(request));
        searchOccupantListRsCall = searchOccupantListRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        searchOccupantListRsCall.subscribe(new RestsubScriber<SearchOccupantListRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(SearchOccupantListRs searchOccupantListRs) {
                callbackSubscriber.onSuccess(searchOccupantListRs);
            }
        });
    }

    @Override
    public void getPickupLocationInstruction(String request, CallbackSubscriber<GetPickupLocationInstructionRs> callbackSubscriber) {

        Observable<GetPickupLocationInstructionRs> getPickupLocationInstructionRsCall = iService.getPickupLocationInstruction(getRequestBody(request));
        getPickupLocationInstructionRsCall = getPickupLocationInstructionRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getPickupLocationInstructionRsCall.subscribe(new RestsubScriber<GetPickupLocationInstructionRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetPickupLocationInstructionRs getPickupLocationInstructionRs) {
                callbackSubscriber.onSuccess(getPickupLocationInstructionRs);
            }
        });

    }

    @Override
    public void getDevicePropertyOptions(int optionId, CallbackSubscriber<GetDevicePropertyOptionRs> callbackSubscriber) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + optionId + "\"");

        Observable<GetDevicePropertyOptionRs> getDevicePropertyOptionRsCall = iService.getDevicePropertyOptions(body);
        getDevicePropertyOptionRsCall = getDevicePropertyOptionRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getDevicePropertyOptionRsCall.subscribe(new RestsubScriber<GetDevicePropertyOptionRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetDevicePropertyOptionRs getDevicePropertyOptionRs) {
                callbackSubscriber.onSuccess(getDevicePropertyOptionRs);
            }
        });

    }

    @Override
    public void getOperatorByID(int operatorID, CallbackSubscriber<GetOperatorByIDRs> callbackSubscriber) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + operatorID + "\"");

        Observable<GetOperatorByIDRs> getOperatorByIDRsRsCall = iService.getOperatorByID(body);
        getOperatorByIDRsRsCall = getOperatorByIDRsRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getOperatorByIDRsRsCall.subscribe(new RestsubScriber<GetOperatorByIDRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetOperatorByIDRs getOperatorByIDRs) {
                callbackSubscriber.onSuccess(getOperatorByIDRs);
            }
        });

    }

    @Override
    public void getOccupantByID(int occupantID, CallbackSubscriber<GetOccupantByIDRs> callbackSubscriber) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + occupantID + "\"");

        Observable<GetOccupantByIDRs> getOccupantByIDRsCall = iService.getOccupantByID(body);
        getOccupantByIDRsCall = getOccupantByIDRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getOccupantByIDRsCall.subscribe(new RestsubScriber<GetOccupantByIDRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetOccupantByIDRs getOccupantByIDRs) {
                callbackSubscriber.onSuccess(getOccupantByIDRs);
            }
        });

    }

    @Override
    public void removeOccupantById(int occupantID, CallbackSubscriber<String> callbackSubscriber) {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"" + occupantID + "\"");

        Observable<String> removeOccupantByIdRsCall = iService.removeOccupantById(body);
        removeOccupantByIdRsCall = removeOccupantByIdRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        removeOccupantByIdRsCall.subscribe(new RestsubScriber<String>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(String jsonObject) {
                callbackSubscriber.onSuccess(jsonObject);
            }
        });

    }

    @Override
    public void reSendAttestation(String request, CallbackSubscriber<String> callbackSubscriber) {


        Observable<String> reSendAttestationRsCall = iService.reSendAttestation(getRequestBody(request));
        reSendAttestationRsCall = reSendAttestationRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        reSendAttestationRsCall.subscribe(new RestsubScriber<String>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(String jsonObject) {
                callbackSubscriber.onSuccess(jsonObject);
            }
        });

    }

    @Override
    public void cancelOrderByOrderId(String request, CallbackSubscriber<String> callbackSubscriber) {

        Observable<String> cancelOrderByOrderIdRsCall = iService.cancelOrderByOrderId(getRequestBody(request));
        cancelOrderByOrderIdRsCall = cancelOrderByOrderIdRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        cancelOrderByOrderIdRsCall.subscribe(new RestsubScriber<String>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(String jsonObject) {
                callbackSubscriber.onSuccess(jsonObject);
            }
        });
    }

    @Override
    public void getBonusDayBillingProfileInfo(String request, CallbackSubscriber<GetBonusDayBillingProfileInfoRs> callbackSubscriber) {

        Observable<GetBonusDayBillingProfileInfoRs> getBonusDayBillingProfileInfoRsCall = iService.getBonusDayBillingProfileInfo(getRequestBody(request));
        getBonusDayBillingProfileInfoRsCall = getBonusDayBillingProfileInfoRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getBonusDayBillingProfileInfoRsCall.subscribe(new RestsubScriber<GetBonusDayBillingProfileInfoRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetBonusDayBillingProfileInfoRs getBonusDayBillingProfileInfoRs) {
                callbackSubscriber.onSuccess(getBonusDayBillingProfileInfoRs);
            }
        });
    }

    @Override
    public void getBonusDayBillingProfileInfoList(String request, CallbackSubscriber<GetBonusDayBillingProfileInfoListRs> callbackSubscriber) {

        Observable<GetBonusDayBillingProfileInfoListRs> getBonusDayBillingProfileInfoListRsCall = iService.getBonusDayBillingProfileInfoList(getRequestBody(request));
        getBonusDayBillingProfileInfoListRsCall = getBonusDayBillingProfileInfoListRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getBonusDayBillingProfileInfoListRsCall.subscribe(new RestsubScriber<GetBonusDayBillingProfileInfoListRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetBonusDayBillingProfileInfoListRs getBonusDayBillingProfileInfoListRs) {
                callbackSubscriber.onSuccess(getBonusDayBillingProfileInfoListRs);
            }
        });

    }

    @Override
    public void applyBonus(String request, CallbackSubscriber<ApplyBonusRs> callbackSubscriber) {

        Observable<ApplyBonusRs> applyBonusRsCall = iService.applyBonus(getRequestBody(request));
        applyBonusRsCall = applyBonusRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        applyBonusRsCall.subscribe(new RestsubScriber<ApplyBonusRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(ApplyBonusRs applyBonusRs) {
                callbackSubscriber.onSuccess(applyBonusRs);
            }
        });
    }

    @Override //GetDestinationListRs
    public void getAppConfiguration(CallbackSubscriber<JsonObject> callbackSubscriber) {

        Observable<JsonObject> getAppConfigurationRsCall = iService.getAppConfiguration();
        getAppConfigurationRsCall = getAppConfigurationRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getAppConfigurationRsCall.subscribe(new RestsubScriber<JsonObject>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(JsonObject jsonObject) {
                callbackSubscriber.onSuccess(jsonObject);
            }
        });

    }

    @Override
    public void deleteAuthorizedPaymentProfile(String request, CallbackSubscriber<DeleteAuthorizedPaymentProfileRs> callbackSubscriber) {

        Observable<DeleteAuthorizedPaymentProfileRs> deleteAuthorizedPaymentProfileRsCall = iService.deleteAuthorizedPaymentProfile(getRequestBody(request));
        deleteAuthorizedPaymentProfileRsCall = deleteAuthorizedPaymentProfileRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        deleteAuthorizedPaymentProfileRsCall.subscribe(new RestsubScriber<DeleteAuthorizedPaymentProfileRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(DeleteAuthorizedPaymentProfileRs deleteAuthorizedPaymentProfileRs) {
                callbackSubscriber.onSuccess(deleteAuthorizedPaymentProfileRs);
            }
        });
    }

    @Override
    public void updateProfile(String customerInfo, CallbackSubscriber<SaveCustomerRs> callbackSubscriber) {

        Observable<SaveCustomerRs> saveCustomerRsCall = iService.updateCustomer(getRequestBody(customerInfo));
        saveCustomerRsCall = saveCustomerRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        saveCustomerRsCall.subscribe(new RestsubScriber<SaveCustomerRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(SaveCustomerRs saveCustomerRs) {
                callbackSubscriber.onSuccess(saveCustomerRs);
            }
        });

    }

    @Override
    public void addDeviceInventoryID(String request, CallbackSubscriber<AddDeviceInventoryIDRs> callbackSubscriber) {

        Observable<AddDeviceInventoryIDRs> addDeviceInventoryIDRsCall = iService.addDeviceInventoryID(getRequestBody(request));
        addDeviceInventoryIDRsCall = addDeviceInventoryIDRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        addDeviceInventoryIDRsCall.subscribe(new RestsubScriber<AddDeviceInventoryIDRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(AddDeviceInventoryIDRs addDeviceInventoryIDRs) {
                callbackSubscriber.onSuccess(addDeviceInventoryIDRs);
            }
        });

    }

    @Override
    public void validateLicense(CallbackSubscriber<ValidateLicenseRs> callbackSubscriber) {


        Observable<ValidateLicenseRs> validateLicenseRsCall = iService.validateLicense();
        validateLicenseRsCall = validateLicenseRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        validateLicenseRsCall.subscribe(new RestsubScriber<ValidateLicenseRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(ValidateLicenseRs validateLicenseRs) {
                callbackSubscriber.onSuccess(validateLicenseRs);
            }
        });

    }

    @Override
    public void validateOrderListTime(String request, CallbackSubscriber<ValidateOrderListTimeRs> callbackSubscriber) {


        Observable<ValidateOrderListTimeRs> validateOrderListTimeRsCall = iService.validateOrderListTime(getRequestBody(request));
        validateOrderListTimeRsCall = validateOrderListTimeRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        validateOrderListTimeRsCall.subscribe(new RestsubScriber<ValidateOrderListTimeRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(ValidateOrderListTimeRs validateOrderListTimeRs) {
                callbackSubscriber.onSuccess(validateOrderListTimeRs);
            }
        });

    }

    @Override
    public void getNotificationList(CallbackSubscriber<NotificationListRs> callbackSubscriber) {

        Observable<NotificationListRs> notificationListRsCall = iService.getNotificationList();
        notificationListRsCall = notificationListRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        notificationListRsCall.subscribe(new RestsubScriber<NotificationListRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(NotificationListRs notificationListRs) {
                callbackSubscriber.onSuccess(notificationListRs);
            }
        });

    }

    @Override
    public void getBadgeCount(CallbackSubscriber<GetBadgeCountRs> callbackSubscriber) {

        Observable<GetBadgeCountRs> getBadgeCountRsCall = iService.getBadgeCount();
        getBadgeCountRsCall = getBadgeCountRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getBadgeCountRsCall.subscribe(new RestsubScriber<GetBadgeCountRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetBadgeCountRs getBadgeCountRs) {
                String json = new Gson().toJson(getBadgeCountRs);
                Log.e("Nilam Rana", json);
                callbackSubscriber.onSuccess(getBadgeCountRs);
            }
        });

    }

    @Override
    public void markNotificationAsRead(CallbackSubscriber<MarkNotificationAsReadRs> callbackSubscriber) {

        Observable<MarkNotificationAsReadRs> markNotificationAsReadRsCall = iService.markNotificationAsRead();
        markNotificationAsReadRsCall = markNotificationAsReadRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        markNotificationAsReadRsCall.subscribe(new RestsubScriber<MarkNotificationAsReadRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(MarkNotificationAsReadRs markNotificationAsReadRs) {
                callbackSubscriber.onSuccess(markNotificationAsReadRs);
            }
        });

    }

    @Override
    public void addUpdateFCMToken(String request, CallbackSubscriber<AddUpdateFCMTokenRs> callbackSubscriber) {

        Log.e("Nilam", "data is --- > " + request);

        Observable<AddUpdateFCMTokenRs> addUpdateFCMTokenRsCall = iService.addUpdateFCMToken(getRequestBody(request));
        addUpdateFCMTokenRsCall = addUpdateFCMTokenRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        addUpdateFCMTokenRsCall.subscribe(new RestsubScriber<AddUpdateFCMTokenRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(AddUpdateFCMTokenRs addUpdateFCMTokenRs) {
                callbackSubscriber.onSuccess(addUpdateFCMTokenRs);
            }
        });

    }


    //Nilam add api calling function for force update app
    @Override
    public void updateApp(String request, CallbackSubscriber<UpdateAppRs> callbackSubscriber) {

        Observable<UpdateAppRs> getBadgeCountRsCall = iService.updateApp();
        getBadgeCountRsCall = getBadgeCountRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getBadgeCountRsCall.subscribe(new RestsubScriber<UpdateAppRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(UpdateAppRs updateAppRs) {
                callbackSubscriber.onSuccess(updateAppRs);
            }
        });


    }

    private RequestBody getRequestBody(String value) {
        RequestBody description =
                RequestBody.create(
                        MultipartBody.FORM, value);
        return description;
    }

    private MultipartBody.Part getMultipartBodyPart(String filePath, String mimeType, String paramName) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(paramName, file.getName(), requestFile);
        return body;
    }

    private MultipartBody.Part getMultipartBodyPart(String paramName, String value) {
        return MultipartBody.Part.createFormData(paramName, value);
    }

    @Override
    public void getBlackoutDatesByLocationAndDevice(HashMap<String, Object> body, CallbackSubscriber<GetBlackoutDatesByLocationAndDeviceRs> callbackSubscriber) {

        Observable<GetBlackoutDatesByLocationAndDeviceRs> getBlackoutDatesByLocationAndDeviceRsCall = iService.getBlackoutDatesByLocationAndDevice(body);
        getBlackoutDatesByLocationAndDeviceRsCall = getBlackoutDatesByLocationAndDeviceRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        getBlackoutDatesByLocationAndDeviceRsCall.subscribe(new RestsubScriber<GetBlackoutDatesByLocationAndDeviceRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(GetBlackoutDatesByLocationAndDeviceRs jsonObject) {
                callbackSubscriber.onSuccess(jsonObject);
            }
        });
    }

    @Override
    public void sentBugReport(HashMap<String, Object> body, CallbackSubscriber<SentBugReportRs> callbackSubscriber) {

        Observable<SentBugReportRs> sentBugReportRsCall = iService.sentBugReport(body);
        sentBugReportRsCall = sentBugReportRsCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        sentBugReportRsCall.subscribe(new RestsubScriber<SentBugReportRs>() {
            @Override
            public void onRestError(RestError restError) {
                callbackSubscriber.onFailure(restError);
            }

            @Override
            public void onResponse(SentBugReportRs jsonObject) {
                callbackSubscriber.onSuccess(jsonObject);
            }
        });
    }


}
