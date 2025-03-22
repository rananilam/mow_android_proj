package com.util

import android.os.Parcel
import androidx.fragment.app.FragmentManager
import com.data.remote.response.order.BlackoutDatesRs
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarViewOpen(
    private val fragmentManager: FragmentManager,
    private val todayDate: Long,
    private val arrivalDate: Calendar,
    private val  blackoutDatesList: List<BlackoutDatesRs>?,
    private val onDateSelected: (Long) -> Unit // Callback when date is selected){}){}){}
) {

    fun showDatePicker() {

        val todayRandomDate = Calendar.getInstance()
        todayRandomDate.timeInMillis = todayDate
        todayRandomDate.set(Calendar.HOUR_OF_DAY, 0)
        todayRandomDate.set(Calendar.MINUTE, 0)
        todayRandomDate.set(Calendar.SECOND, 0)
        todayRandomDate.set(Calendar.MILLISECOND, 0)


        val constraintsBuilder = CalendarConstraints.Builder()
//            .setStart(todayRandomDate.timeInMillis)
            .setStart(todayDate)
            .setValidator(object : CalendarConstraints.DateValidator {
                override fun isValid(date: Long): Boolean {

                    // Block past dates
                    if (date < todayRandomDate.timeInMillis) return false


                    blackoutDatesList?.let {
                        if(it.isNotEmpty()) {
                            val disabledDateRanges: List<Pair<Long, Long>> = it.map { blackout ->
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val startCalendar = Calendar.getInstance().apply {
                                    time = dateFormat.parse(blackout.startDate)!!
                                    set(Calendar.HOUR_OF_DAY, 0)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }
                                val startMillis = startCalendar.timeInMillis
                                val endCalendar = Calendar.getInstance().apply {
                                    time = dateFormat.parse(blackout.endDate)!!
                                    set(Calendar.HOUR_OF_DAY, 23)
                                    set(Calendar.MINUTE, 59)
                                    set(Calendar.SECOND, 59)
                                    set(Calendar.MILLISECOND, 999) // Include the entire end day
                                }
                                val endMillis = endCalendar.timeInMillis
                                startMillis to endMillis
                            }
                            val selectedDateCalendar = Calendar.getInstance().apply {
                                timeInMillis = date
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0) // Ensure the selected date is normalized
                            }
                            val normalizedDate = selectedDateCalendar.timeInMillis
                            return disabledDateRanges.none { (startDate, endDate) -> normalizedDate in startDate..endDate }
                        }
                    }


                    return true // Allow selection otherwise

                }

                override fun writeToParcel(dest: Parcel, flags: Int) {}
                override fun describeContents(): Int = 0
            }).build()



        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
//            .setTheme(R.style.ThemeMaterialCalendar) // ✅ Apply Theme
//        .setTheme(com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
            .setCalendarConstraints(constraintsBuilder)
            .setSelection(arrivalDate.timeInMillis)
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            onDateSelected(selection) // Return selected date
        }

        picker.show(fragmentManager,picker.toString())
    }
}