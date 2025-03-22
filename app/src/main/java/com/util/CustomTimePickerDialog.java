package com.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;


import com.mow.cash.android.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomTimePickerDialog extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 5;
    private TimePicker mTimePicker;
    private NumberPicker minutePicker;
    private final OnTimeSetListener mTimeSetListener;


    private int minHour = 0;
    private int minMinute = 0;

    private int maxHour = 23;
    private int maxMinute = 59;

    private ITimeBound iTimeBound = null;
    public interface ITimeBound {
        void onTimeRangeError(String message);
    }
    public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                  int hourOfDay, int minute, boolean is24HourView,
                                  ITimeBound iTimeBound
                                  ) {
        super(context, null, hourOfDay,
                minute / TIME_PICKER_INTERVAL, is24HourView);
        this.iTimeBound = iTimeBound;
        mTimeSetListener = listener;
    }

    public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                  int hourOfDay, int minute, boolean is24HourView,
                                  int minHour, int minMinute, int maxHour, int maxMinute,
                                  ITimeBound iTimeBound
                                  ) {
        super(context, null, hourOfDay,
                minute / TIME_PICKER_INTERVAL, is24HourView);
        this.minHour = minHour;
        this.minMinute = minMinute;
        this.maxHour = maxHour;
        this.maxMinute = maxMinute;
        this.iTimeBound = iTimeBound;
        Log.i("TIMETEST","minHour: "+minHour+", minMinute: "+minMinute+", maxHour: "+maxHour+", maxMinute: "+maxMinute);

        mTimeSetListener = listener;
    }

    @Override
    public void updateTime(int hourOfDay, int minuteOfHour) {
        mTimePicker.setHour(hourOfDay);
        mTimePicker.setMinute(minuteOfHour / TIME_PICKER_INTERVAL);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null) {

                    Calendar maxCal = Calendar.getInstance();
                    maxCal.set(Calendar.HOUR_OF_DAY, maxHour);
                    maxCal.set(Calendar.MINUTE, maxMinute);
                    maxCal.set(Calendar.SECOND,0);
                    maxCal.set(Calendar.MILLISECOND,0);

                    Calendar minCal = Calendar.getInstance();
                    minCal.set(Calendar.HOUR_OF_DAY, minHour);
                    minCal.set(Calendar.MINUTE, minMinute);
                    minCal.set(Calendar.SECOND,0);
                    minCal.set(Calendar.MILLISECOND,0);


                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(Calendar.HOUR_OF_DAY, mTimePicker.getHour());
                    selectedCal.set(Calendar.MINUTE, mTimePicker.getMinute() * TIME_PICKER_INTERVAL);
                    selectedCal.set(Calendar.SECOND,0);
                    selectedCal.set(Calendar.MILLISECOND,0);


                    if(selectedCal.getTimeInMillis() <= maxCal.getTimeInMillis() && selectedCal.getTimeInMillis() >= minCal.getTimeInMillis()) {
                        Log.i("TIMETEST","Time in range");

                        mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getHour(),
                                mTimePicker.getMinute() * TIME_PICKER_INTERVAL);

                    } else {
                        Log.i("TIMETEST","Time is not in range");
                        iTimeBound.onTimeRangeError(String.format(getContext().getString(R.string.validation_select_time_between),
                                String.format("%02d", minHour),
                                String.format("%02d", minMinute),
                                String.format("%02d", maxHour),
                                String.format("%02d", maxMinute)));
                        //Toast.makeText(getContext(),, Toast.LENGTH_LONG).show();
                    }

                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        super.onTimeChanged(view, hourOfDay, minute);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){

                mTimePicker = findViewById(Resources.getSystem().getIdentifier(
                        "timePicker",
                        "id",
                        "android"
                ));

                minutePicker = mTimePicker.findViewById(Resources.getSystem().getIdentifier(
                        "minute",
                        "id",
                        "android"
                ));

            } else{
                Class<?> classForid = Class.forName("com.android.internal.R$id");
                Field timePickerField = classForid.getField("timePicker");
                mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
                Field field = classForid.getField("minute");

                minutePicker = (NumberPicker) mTimePicker.findViewById(field.getInt(null));
            }





            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);

            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }

            minutePicker.setDisplayedValues(displayedValues.toArray(new String[displayedValues.size()]));



        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}