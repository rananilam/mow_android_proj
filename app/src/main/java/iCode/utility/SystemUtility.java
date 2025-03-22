package iCode.utility;

import android.os.Build;
import android.provider.Settings;

import com.application.BaseApplication;
import com.mow.cash.android.BuildConfig;

public class SystemUtility {

    private static SystemUtility systemUtility = null;

    public static SystemUtility getInstance()
    {
        if ( systemUtility == null)
            systemUtility = new SystemUtility();

        return systemUtility;
    }

    public String getDeviceId()
    {
        /*
        String info = "SERIAL: " + Build.SERIAL + "\n" +
                "MODEL: " + Build.MODEL + "\n" +
                "DEVICE: "+Build.DEVICE + "\n" +
                "ID: " + Build.ID + "\n" +
                "Manufacture: " + Build.MANUFACTURER + "\n" +
                "Brand: " + Build.BRAND + "\n" +
                "Type: " + Build.TYPE + "\n" +
                "User: " + Build.USER + "\n" +
                "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                "INCREMENTAL: " + Build.VERSION.INCREMENTAL + "\n" +
                "SDK:  " + Build.VERSION.SDK + "\n" +
                "BOARD: " + Build.BOARD + "\n" +
                "BRAND: " + Build.BRAND + "\n" +
                "HOST: " + Build.HOST + "\n" +
                "FINGERPRINT: "+Build.FINGERPRINT + "\n" +
                "Version Code: " + Build.VERSION.RELEASE;
         */

        String deviceId = null;
        if(Build.SERIAL !=null && !Build.SERIAL.isEmpty()  && Build.SERIAL.compareTo("unknown") == 1)
            deviceId =  Build.SERIAL;
        else
            deviceId = Settings.Secure.getString(BaseApplication.appContext.getContentResolver(), Settings.Secure.ANDROID_ID);


        if(deviceId!=null && !deviceId.isEmpty())
            return deviceId;
        else
        {
            deviceId = Build.SERIAL+" " + Build.MODEL + " " + Build.DEVICE + " " + Build.MANUFACTURER + " " + Build.BRAND + " " + Build.BOARD;
        }

        return deviceId;
    }
    public String getModel()
    {
        return Build.MODEL;
    }
    public String getBrand()
    {
        return Build.BRAND;
    }

    public String getPackageName()
    {
        return BuildConfig.APPLICATION_ID;
    }
}
