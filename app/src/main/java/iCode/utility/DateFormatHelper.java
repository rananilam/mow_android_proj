package iCode.utility;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DateFormatHelper {


    //09/16/2021 12:00:00 pm 2022-03-09T12:57:04.177

    //3\/17\/2022 1:00:00 AM
    public static final String FORMAT_MM_DD_YYYY_HH_MM_SS_A="MM/dd/yyyy HH:mm:ss a";
    //12/11/2021 03:55 pm
    public static final String FORMAT_MM_DD_YYYY_HH_MM_A="MM/dd/yyyy hh:mm a";

    public static final String FORMAT_MM_DD_YYYY_HH_MM_SS_A_="MM/dd/yyyy hh:mm:ss a";


    public static final String FORMAT_MM_DD_YYYY_HH_MM_SS="MM/dd/yyyy HH:mm:ss";


    public static final String FORMAT_MMM_DD_YYYY ="MMM dd, yyyy";

    public static final String FORMAT_MMM_DD_YYYY_HH_A ="MMM dd, yyyy @ HH a";

    public static final String FORMAT_MMM_YYYY="MMM yyyy";


    public static final String FORMAT_HH_MM_SS_A="hh:mm:ss a";
    public static final String FORMAT_HH_MM_A_="hh:mm a";
    public static final String FORMAT_DD_MMM_YYYY ="dd MMM yyyy";


    public static final String FORMAT_MMMM_DD_YYYY ="MMMM dd, yyyy";
    public static final String FORMAT_YYYY_DD_MM ="yyyy/dd/MM";



    public static final String FORMAT_YYYY_DD_MM_HH_MM_SS="yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_MMMM_DD_YYYY_HH_MM_SS_A ="MMMM dd, yyyy HH:mm:ss a";
    public static final String FORMAT_MMMM_DD_YYYY_HH_MM_SS ="MMMM dd, yyyy HH:mm:ss";
    public static final String FORMAT_MMM_DD_YYYY_HH_MM ="MMM dd, yyyy HH:mm";



    public static final String FORMAT_MM_YYYY ="MM/yyyy";



    public static final String FORMAT_MMMM ="MMMM";


    public static final String FORMAT_DD_MM_YYYY="dd-MM-yyyy";
    public static final String FORMAT_DD_MM_YYYY_S="dd/MM/yyyy";

    //2020-04-07 08:31:30




    public static final String FORMAT_YYYY_MM_DD="yyyy/MM/dd";
    public static final String FORMAT_MM_DD_YYY_S ="mm/dd/yyy";
    public static final String FORMAT_MM_DD_YYYY_S ="MM/dd/yyyy";
    //2020-03-26 13:59:22
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS ="YYYY-MM-DD HH:mm:ss";
   //Tuesday, Apr 7 2020
    public static final String FORMAT_EEEE_MMM_D_YYYY ="EEEE, MMM d yyyy";
    public static final String FORMAT_MMM_D ="MMM d";

    //Feb 5,2020 5:45 PM
    public static final String FORMAT_EEE_MMM_D_YYYY_HH_MM_SS_A ="EEE, MMM d yyyy HH:mm:ss a";
    public static final String FORMAT_MMM_D_YYYY_HH_MM_A ="MMM d,yyyy HH:mm a";

    public static final String FORMAT_HH_MM_A ="HH:mm a";
    public static final String FORMAT_HH_MM ="HH:mm";

    public static final String FORMAT_S_HH_MM_A ="hh:mm a";



    public static Calendar convertGMTToLocalFromStr(String dateFormat,String date)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            cal.setTime(sdf.parse(date));
            cal.set(Calendar.SECOND,cal.get(Calendar.SECOND)+(getTimeZoneOffsetInSeconds()/1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  cal;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDate(String date, String inputFormat, String outputFormat) {

        if(!date.isEmpty()) {

            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(Objects.requireNonNull(new SimpleDateFormat(inputFormat).parse(date)));
                return new SimpleDateFormat(outputFormat).format(calendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return "";


    }
    public static Calendar getCalendarFromStringDate(String dateFormat,String date)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  cal;
    }

    public static String getStringFromCalendar(String dateFormat,Calendar calendar)
    {
        if(calendar!=null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.format(calendar.getTime());
        }
        return "";
    }


    public static String getFormateDateAndTime(String inputDateFormate, String outputDateFormate, String serverDate) {
        Calendar calendarCreatedDate = getCalendarFromStringDate(inputDateFormate,serverDate);
        return getStringFromCalendar(outputDateFormate,calendarCreatedDate);
    }
    public static String changeFormate(String strDate,String oldFormate,String newFormate){

        DateFormat originalFormat = new SimpleDateFormat(oldFormate, Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat(newFormate);
        Date date = null;
        try {
            date = originalFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return targetFormat.format(Objects.requireNonNull(date));

    }


    public static CharSequence getAgoTime(Calendar createdDate){

        long time = createdDate.getTimeInMillis();
        long now = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);


    }

    /*public static CharSequence getAgoTime(String inputPattern, String serverDate){

        SimpleDateFormat sdf = new SimpleDateFormat(inputPattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        CharSequence ago="";
        try {
            long time = sdf.parse(serverDate).getTime();
            long now = System.currentTimeMillis();
             ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ago;

    }*/


    public static int getTimeZoneOffsetInSeconds()
    {
        Calendar c = Calendar.getInstance();
        return (c.getTimeZone().getOffset(Calendar.getInstance().getTimeInMillis())/1000);
    }








}
