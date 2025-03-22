package com.data.remote;


//import libraries.retrofit.rx.RestAPISubscriber;
import libraries.retrofit.rx.RestAPISubscriber;

public abstract class RestsubScriber<T> extends RestAPISubscriber<T>
{
    @Override
    public void handleRestErrorResponse(String errorBody)
    {

    }
    @Override
    public void onRestComplete() {

    }
}
