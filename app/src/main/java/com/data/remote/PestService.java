package com.data.remote;


import static java.sql.DriverManager.println;

import com.application.BaseApplication;
import com.data.remote.interceptors.RequestInterceptor;

import java.util.ArrayList;
import java.util.List;

import libraries.retrofit.BaseWebService;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;


public class PestService extends BaseWebService
{
    private Retrofit retrofit;
    private IService iService;
    private static PestService instance;

    public static PestService getInstance()
    {
        if (instance == null)
        {
            instance = new PestService();
        }
        return instance;
    }

    private PestService()
    {
        GsonInterface gsonInterface = GsonInterface.getInstance();
        List<Interceptor> interceptorList = new ArrayList<>();
        interceptorList.add(new RequestInterceptor(BaseApplication.appContext));

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        interceptorList.add(logging);

        retrofit = createRetrofit(BaseApplication.appContext,getBaseUrl(),
                gsonInterface.getGson(), interceptorList);

        iService = retrofit.create(IService.class);

    }

    public static String getBaseUrl()
    {

        //String finalLiveURL ="http://14.99.98.241:8080/MOW/";
        String devtest ="http://devapi.mobilityonwheelsonline.net/MOW/";
       // String finalLiveURL = "http://api.mobilityonwheelsonline.net/mow/";
        //return finalLiveURL;
         return devtest;
    }

    public static String getBaseURLForImage()
    {
        //return "http://14.99.98.241:8080/";
        //18602662666
        //Wbsite link: https://devcash.mobilityonwheels.com/
        // client URL
        //return "http://mobilityonwheelsonline.net/";
        //DevTest URL
        return "http://dev.mobilityonwheelsonline.net/";
        //Final URL
        //  return "http://mobilityonwheelsonline.net/";
    }

    public IService getIService()
    {
        return iService;
    }
}
