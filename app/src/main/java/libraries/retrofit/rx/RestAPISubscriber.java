package libraries.retrofit.rx;


import java.io.IOException;
import java.net.SocketTimeoutException;

//import io.reactivex.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableObserver;
import libraries.logger.Logger;
import libraries.retrofit.RestError;
import libraries.retrofit.enums.RestErrorType;
import libraries.retrofit.exceptions.InternetNotAvailableException;
import libraries.retrofit.exceptions.NetworkNotAvailableException;
import retrofit2.HttpException;


/**
 * Extended Rx Subscriber will will acts as bridge between plain Rx Subscriber and Retrofit Calls
 *
 * @param <T> Subscriber Type
 */
public abstract class RestAPISubscriber<T> extends DisposableObserver<T>
{

    @Override
    public void onComplete() {
        Logger.trace("RESTTEST onComplete");
    }

    @Override
    public void onError(Throwable e)
    {
       // Logger.trace("RESTTEST onError: "+e.getLocalizedMessage());
        Logger.trace("RESTTEST onError: "+e.toString());
        e.printStackTrace();
        RestError restError = getRestErrorFromThrowable(e);
        onRestError(restError);
    }

    public abstract void onRestError(RestError restError);

    public abstract void onResponse(T response);

    public abstract void onRestComplete();

    public abstract void handleRestErrorResponse(String errorBody);

    @Override
    public void onNext(T t)
    {
        Logger.trace("RESTTEST onNext");
        onResponse(t);
    }

    public RestError getRestErrorFromThrowable(Throwable throwable)
    {
        RestError restError = new RestError();
        restError.setThrowable(throwable);
        restError.setMessage(throwable.getLocalizedMessage());
        if (throwable instanceof NetworkNotAvailableException)
        {
            restError.setRestErrorType(RestErrorType.NETWORK_NOT_AVAILABLE);
        }
        else if (throwable instanceof InternetNotAvailableException)
        {
            restError.setRestErrorType(RestErrorType.INTERNET_NOT_AVAILABLE);
        }
        else if (throwable instanceof java.net.ConnectException)
        {
            restError.setRestErrorType(RestErrorType.INTERNET_NOT_AVAILABLE);
        }
        else if (throwable instanceof SocketTimeoutException)
        {
            restError.setRestErrorType(RestErrorType.NETWORK_NOT_AVAILABLE);
        }
        else if (throwable instanceof IOException)
        {
            restError.setRestErrorType(RestErrorType.NETWORK_NOT_AVAILABLE);
        }
        else if (throwable instanceof HttpException)
        {


            HttpException httpException = (HttpException) throwable;

            if(httpException.code() == 422) {
                restError.setRestErrorType(RestErrorType.TOKEN_EXPIRED);
            } else {
                restError.setRestErrorType(RestErrorType.REST_API_ERROR);
            }
            try
            {

                String errorBody = httpException.response().errorBody().string();
                restError.setMessage(errorBody);
                handleRestErrorResponse(errorBody);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            Logger.trace("No IO exception handled");
        }
        else
        {
            restError.setRestErrorType(RestErrorType.UNKNOWN);
        }
        return restError;
    }
}
