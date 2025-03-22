package iCode.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DimenRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.application.BaseApplication;


import java.lang.reflect.Field;


public class ToolbarUtils
{
    public static TextView getActionBarTextView(Toolbar toolbar)
    {
        TextView titleTextView = null;
        try
        {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
        }
        catch (Exception e)
        {
        }
        return titleTextView;
    }

    public static void setTitleFont(Context context, Toolbar toolbar, @DimenRes int fontSize)
    {
        TextView title = getActionBarTextView(toolbar);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(fontSize));
    }

    public static void setTitleFont(Context context, Toolbar toolbar, Typeface typeface, @DimenRes int fontSize)
    {
        TextView title = getActionBarTextView(toolbar);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(fontSize));
        title.setTypeface(typeface);
    }

    public static void reset(Toolbar toolbar, Context context)
    {
        toolbar.setTitle("");
        setTitle(toolbar,"");
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(null);
        toolbar.setLogo(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));

        /*ImageView ivToolbarLogo = toolbar.findViewById(R.id.ivToolbarLogo);
        ivToolbarLogo.setVisibility(View.GONE);

        TextView tvTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvTitle.setVisibility(View.GONE);

        EditText etSearch = toolbar.findViewById(R.id.etToolbarSearch);
        etSearch.setText("");
        etSearch.setVisibility(View.GONE);*/
    }

    public static void setTitle(Toolbar toolbar, String title)
    {
       /* TextView tvTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);*/
    }
    public static void showToolbarLogo(Toolbar toolbar)
    {
        /*ImageView ivToolbarLogo = toolbar.findViewById(R.id.ivToolbarLogo);
        ivToolbarLogo.setVisibility(View.VISIBLE);*/
    }
    public static void showSearchView(Toolbar toolbar)
    {
        /*EditText etSearch = toolbar.findViewById(R.id.etToolbarSearch);
        etSearch.setVisibility(View.VISIBLE);*/
    }


    public static int getActionBarHeight()
    {
        TypedValue tv = new TypedValue();
        if (BaseApplication.appContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            return TypedValue.complexToDimensionPixelSize(tv.data,BaseApplication.appContext.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
