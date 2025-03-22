package libraries.retrofit.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.application.BaseApplication;

import libraries.logger.Logger;


public class NetworkUtil
{
    /**
     * Check whether the device is connected, and if so, whether the connection
     * is wifi or mobile (it could be something else).
     */
    public static boolean isInternetAvailable()
    {
        Logger.traceM("NetworkUtil::isInternetAvailable");
        Context context = BaseApplication.appContext;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected())
        {
            boolean wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            boolean mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if (wifiConnected || mobileConnected)
            {
                Logger.trace("Wifi Connected");
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            Logger.trace("Not Connected");
            return false;
        }
    }
}
