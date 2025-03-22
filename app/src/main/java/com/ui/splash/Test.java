package com.ui.splash;

import java.util.Calendar;

public class Test {
    public static void main(String[] args) {


        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c2.set(Calendar.DATE, c2.get(Calendar.DATE)+2);
        c2.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY)+2);
        c2.set(Calendar.MINUTE, c2.get(Calendar.MINUTE)+20);
        c2.set(Calendar.SECOND, c2.get(Calendar.SECOND)+77);

        long diff = c2.getTimeInMillis() - c1.getTimeInMillis();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);

        System.out.println(diffHours + ":"+diffMinutes+":"+diffSeconds);
    }
}