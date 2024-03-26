package com.sto_opka91.memorystick.repository

import androidx.fragment.app.FragmentManager
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TimePickerHelper @Inject constructor(private val calendar: Calendar) {

    private lateinit var fragmentManager: FragmentManager

    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    fun showDateTimePicker(onDateTimeSelected: (Long) -> Unit) {
        val dpd = DatePickerDialog.newInstance(
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showTimePicker(onDateTimeSelected)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show(fragmentManager, "DatePickerDialog")
    }

    private fun showTimePicker(onDateTimeSelected: (Long) -> Unit) {
        val tpd = TimePickerDialog.newInstance(
            { _, hourOfDay, minute, second ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val selectedDateTimeInMillis = calendar.timeInMillis
                onDateTimeSelected.invoke(selectedDateTimeInMillis)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        tpd.show(fragmentManager, "TimePickerDialog")
    }
}