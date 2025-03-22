package com.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import iCode.utility.DateFormatHelper
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Calendar


class Utility {
    companion object {


        fun getRemainingTime(arrivalDate: String, departureDate: String): String {

            var remainingTime = "00:00:00:00"

            if(departureDate.isNotEmpty() && arrivalDate.isNotEmpty()) {
                val currentDateCal = Calendar.getInstance()
                val arrivalDateCal = DateFormatHelper.getCalendarFromStringDate(DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A_, arrivalDate)
                val departureDateCal = DateFormatHelper.getCalendarFromStringDate(DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A_, departureDate)

                if(arrivalDateCal.timeInMillis > currentDateCal.timeInMillis) {
                    remainingTime = getTimeDiffReadableFormat(arrivalDateCal.timeInMillis - currentDateCal.timeInMillis)
                }
                else if(departureDateCal.timeInMillis > currentDateCal.timeInMillis) {
                    remainingTime = getTimeDiffReadableFormat(departureDateCal.timeInMillis - currentDateCal.timeInMillis)
                } else if(currentDateCal.timeInMillis > departureDateCal.timeInMillis) {
                    remainingTime = getTimeDiffReadableFormat(currentDateCal.timeInMillis - departureDateCal.timeInMillis)
                }
            }

            return remainingTime

        }

        private fun getTimeDiffReadableFormat(diff: Long): String {


            val diffSeconds = (diff / 1000 % 60).toInt()
            val diffMinutes = (diff / (60 * 1000) % 60).toInt()
            val diffHours = (diff / (60 * 60 * 1000) % 24).toInt()
            val diffDays = (diff / (24 * 60 * 60 * 1000)).toInt()

            return "${String.format("%02d", diffDays)}:${String.format("%02d", diffHours)}:${String.format("%02d", diffMinutes)}:${String.format("%02d", diffSeconds)}"
        }

        fun convertUpTo2Decimal(value: Float): String {

            return String.format("%.2f", value.roundUp())
            //val df2 = DecimalFormat("#.##")
            //return df2.format(value)
        }

        fun Float.roundUp(): Float {
            return Math.round(this * 100.00F) / 100.00F
        }

        fun Calendar.convertServerTime(serverTimeOffset: Long): Calendar {


            val offset = timeZone.getOffset(timeInMillis)

            Log.i("TimeTest","Local Offset: "+offset)
            Log.i("TimeTest","Local Time: "+timeInMillis)
            timeInMillis -= offset
            Log.i("TimeTest","Local - Time: "+timeInMillis)
            timeInMillis += serverTimeOffset
            Log.i("TimeTest","Server Time: "+timeInMillis+","+DateFormatHelper.getStringFromCalendar(DateFormatHelper.FORMAT_MM_DD_YYYY_HH_MM_SS_A,this))
            return this
        }
        fun convertImageIntoBase64(imagePath: String): String {
            val bm = BitmapFactory.decodeFile(imagePath)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        }
        fun encodeFileToBase64(file: File): String {
            val inputStream = file.inputStream()
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024) // Process in 1KB chunks

            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            inputStream.close()
            return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
        }


    }


}
