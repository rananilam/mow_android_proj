package com.data;

import libraries.retrofit.RestError;


/**
 * Extended Rx Subscriber will will acts as bridge between plain Rx Subscriber and Retrofit Calls
 *
 * @param <T> Subscriber Type
 */
public abstract class CallbackSubscriber<T>
{
    public abstract void onSuccess(T response);

    public abstract void onFailure(RestError restError);

}
