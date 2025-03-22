package iCode.utility;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.application.BaseApplication;
import com.mow.cash.android.R;

import java.util.HashSet;
import java.util.Set;


public class PrefHelper
{
    private static Context context;
    private static PrefHelper instance;

    public static PrefHelper getInstance()
    {
        if (instance == null)
        {
            instance = new PrefHelper();
        }
        return instance;
    }

    private PrefHelper()
    {
        context = BaseApplication.appContext.getApplicationContext();
    }

    public boolean getBoolean(String paramString, boolean paramBoolean)
    {
        return getSharedPreferences(context).getBoolean(paramString, paramBoolean);
    }

    public float getFloat(String paramString, float paramFloat)
    {
        return getSharedPreferences(context).getFloat(paramString, paramFloat);
    }

    public int getInt(String paramString, int paramInt)
    {
        return getSharedPreferences(context).getInt(paramString, paramInt);
    }

    public long getLong(String paramString, long paramLong)
    {
        return getSharedPreferences(context).getLong(paramString, paramLong);
    }

    public SharedPreferences getSharedPreferences(Context paramContext)
    {
        if (TextUtils.isEmpty(paramContext.getString(R.string.app_name)))
        {
        }
        SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
        localSharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return localSharedPreferences;
    }

    public String getString(String paramString1, String paramString2)
    {
        return getSharedPreferences(context).getString(paramString1, paramString2);
    }

    public void putBoolean(String paramString, boolean paramBoolean)
    {
        getSharedPreferences(context).edit().putBoolean(paramString, paramBoolean).apply();
    }

    public void putFloat(String paramString, float paramFloat)
    {
        getSharedPreferences(context).edit().putFloat(paramString, paramFloat).apply();
    }

    public void putInt(String paramString, int paramInt)
    {
        getSharedPreferences(context).edit().putInt(paramString, paramInt).apply();
    }

    public void putLong(String paramString, long paramLong)
    {
        getSharedPreferences(context).edit().putLong(paramString, paramLong).apply();
    }

    public void putString(String paramString1, String paramString2)
    {
        getSharedPreferences(context).edit().putString(paramString1, paramString2).apply();
    }

    public void putStringSet(String paramString1, HashSet<String> paramString2)
    {
        getSharedPreferences(context).edit().putStringSet(paramString1, paramString2).apply();
    }

    public Set<String> getStringSet(String paramString1, Set<String> paramString2)
    {
        return getSharedPreferences(context).getStringSet(paramString1, paramString2);
    }

    public void remove(String paramString)
    {
        getSharedPreferences(context).edit().remove(paramString).apply();
    }

    public void clearAll()
    {
        getSharedPreferences(context).edit().clear().apply();
    }
}