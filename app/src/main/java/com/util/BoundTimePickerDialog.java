package com.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TimePicker;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.mow.cash.android.R;

public class BoundTimePickerDialog extends TimePickerDialog {

    private int minHour = -1, minMinute = -1, maxHour = 100, maxMinute = 100;

    private int currentHour, currentMinute;
    private Snackbar snackbar;

    public BoundTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(context, callBack, hourOfDay, minute, is24HourView);
    }

    public void setMin(int hour, int minute) {
        minHour = hour;
        minMinute = minute;
    }

    public void setMax(int hour, int minute) {
        maxHour = hour;
        maxMinute = minute;
    }



    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

        boolean validTime = true;
        if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)){
            validTime = false;
            currentHour = minHour;
            currentMinute = minMinute;
        }

        if (hourOfDay  > maxHour || (hourOfDay == maxHour && minute > maxMinute)){
            validTime = false;
            currentHour = maxHour;
            currentMinute = maxMinute;
        }

        if (validTime) {
            currentHour = hourOfDay;
            currentMinute = minute;
        } else {



            if(snackbar == null)
                snackbar = Snackbar.make(view, String.format(getContext().getString(R.string.validation_select_time_between),
                        String.format("%02d", minHour),
                        String.format("%02d", minMinute),
                        String.format("%02d", maxHour),
                        String.format("%02d", maxMinute)
                ), Snackbar.LENGTH_LONG);

            if(!snackbar.isShown()) {
                View snackbarView = snackbar.getView();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
                params.gravity = Gravity.TOP;
                snackbarView.setLayoutParams(params);
                snackbar.setBackgroundTint(ContextCompat.getColor(getContext(), R.color.colorRed));
                snackbar.show();
            }

            //Toast.makeText(BaseApplication.appContext,"Select time between "+minHour+":"+minMinute+" - "+maxHour+":"+maxMinute,Toast.LENGTH_LONG).show();
        }

        updateTime(currentHour, currentMinute);
       // updateDialogTitle(view, currentHour, currentMinute);
    }




    /*@Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        super.onTimeChanged(view, hourOfDay, minute);

        boolean validTime;
        if(hourOfDay < minHour) {
            validTime = false;
        }
        else if(hourOfDay == minHour) {
            validTime = minute >= minMinute;
        }
        else if(hourOfDay == maxHour) {
            validTime = minute <= maxMinute;
        }
        else {
            validTime = true;
        }

        if(validTime) {
            currentHour = hourOfDay;
            currentMinute = minute;
        }
        else {
            updateTime(currentHour, currentMinute);
        }
    }*/
}