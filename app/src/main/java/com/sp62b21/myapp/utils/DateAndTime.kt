package com.sp62b21.myapp.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId


object DateAndTime {
//
    fun getTime(time: Long): String {
        lateinit var timeFormat: SimpleDateFormat
        timeFormat = SimpleDateFormat("HH:mm")
        return timeFormat.format(time)
    }

    fun getDate(date: Long): String {
        lateinit var dateFormat: SimpleDateFormat
        dateFormat = SimpleDateFormat("dd.MM.yy")
        return dateFormat.format(date)
    }

    fun getFullDate(date: Long): String {
        lateinit var fullDateFormat: SimpleDateFormat
        fullDateFormat = SimpleDateFormat("dd MMM yyyy")
        return fullDateFormat.format(date)
    }

    fun getDateDif(fromDate: LocalDate, toDate: LocalDate): String {
        var diff: Period = Period.between(fromDate,toDate)
        lateinit var formattedDiff: String
        if(diff.years < 1){
            if (diff.months < 1) {
                if(diff.days > 1){
                    formattedDiff = String.format("%d \nDAYS", diff.days)
                } else if (diff.days == 1){
                    formattedDiff = String.format("%d \nDAY", diff.days)
                } else formattedDiff = "Expired"
            } else if(diff.months == 1)formattedDiff = String.format("%d \nMONTH", diff.months)
            else formattedDiff = String.format("%d \nMonths", diff.months)
        } else if(diff.years == 1)formattedDiff = String.format("%d \nYEAR", diff.years)
        else formattedDiff = String.format("%d \nYEARS", diff.years)
        return formattedDiff
    }

    fun getLocalDateFromLong(date: Long): LocalDate {
        val date: LocalDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
        return date
    }
}
