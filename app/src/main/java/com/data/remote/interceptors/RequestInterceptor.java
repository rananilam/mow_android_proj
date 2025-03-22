package com.data.remote.interceptors;

import android.content.Context;

import com.data.DataRepository;
import com.data.Injection;
import com.mow.cash.android.BuildConfig;
import iCode.utility.SystemUtility;

public class RequestInterceptor extends libraries.retrofit.interceptors.RequestInterceptor
{
    public RequestInterceptor(Context context)
    {
        super(context);
    }

    @Override
    public void modifyRequest(okhttp3.Request.Builder requestBuilder) {
        DataRepository dataRepository = Injection.provideDataRepository();

        String token = dataRepository.getToken();

        requestBuilder.addHeader("LoggedOn", "2");

        String deviceId = SystemUtility.getInstance().getDeviceId();
        //added on 20 march 2023 added by yagnesh
        requestBuilder.addHeader("AndroidAppVersion", BuildConfig.VERSION_NAME);
        requestBuilder.addHeader("DeviceId", deviceId);

        if(token!=null && !token.trim().isEmpty())
        {
            //@Headers({"Accept:application/json","Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xOTIuMTY4LjEuNzdcL3Blc3RyZXF1ZXN0XC9wdWJsaWNcL2FwaVwvdjFcL3VzZXJcL2xvZ2luIiwiaWF0IjoxNTg0NTM3Njg3LCJuYmYiOjE1ODQ1Mzc2ODcsImp0aSI6InNFREpscU9zenUxZEw2dnkiLCJzdWIiOjQsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.pTFGIzWgd1Dt0V6f5deyBiFC8QfOTZ2_o0GADJ-uheI"})
            requestBuilder.addHeader("token", token);
        }
    }

}
