package libraries.retrofit.interceptors;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import java.io.IOException;
import java.nio.charset.Charset;

import libraries.logger.Logger;
import libraries.retrofit.exceptions.NetworkNotAvailableException;
import libraries.retrofit.utility.NetworkUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public abstract class RequestInterceptor implements Interceptor
{
    private Context context;
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public RequestInterceptor(Context context)
    {
        this.context = context;
    }

    public abstract void modifyRequest(Request.Builder requestBuilder);

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        if (!NetworkUtil.isInternetAvailable())
        {
            Logger.trace("⇢ Network Check Result : NETWORK NOT AVAILABLE");
            throw new NetworkNotAvailableException();
        }
        Logger.trace("⇢ Network Check Result : OK");

        /* Build Request with headers */
        Request.Builder requestBuilder = request.newBuilder();
        modifyRequest(requestBuilder);
        request = requestBuilder.build();

        Response response = chain.proceed(request);
        return response;
    }


}