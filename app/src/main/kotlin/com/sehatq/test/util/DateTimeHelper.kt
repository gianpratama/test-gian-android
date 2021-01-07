package com.sehatq.test.util

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import java.text.SimpleDateFormat
import java.util.*

/**
 * A helper class for date/time format.
 */
object DateTimeHelper {

    private const val formatDate = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val newFormatDate = "yyyy-MM-dd HH:mm:ss"

    /**
     * Obtain a formatted String from a data.
     * @param millis Time in millis
     * @param format The date/time format
     * @param locale Requested [Locale]
     * @return the formatted String
     */
    fun getStringFromMillis(millis: Long?, format: String, locale: Locale = Locale.US): String? {
        val date = getDateFromMillis(millis)
        return getStringFromDate(date, format, locale)
    }

    /**
     * Obtain a formatted String from a data.
     * @param calendar The [Calendar] object
     * @param format The date/time format
     * @param locale Requested [Locale]
     * @return the formatted String
     */
    fun getStringFromCalendar(calendar: Calendar?, format: String, locale: Locale = Locale.US): String? {
        val date = getDateFromCalendar(calendar)
        return getStringFromDate(date, format, locale)
    }

    /**
     * Obtain a formatted String from a data.
     * @param date The [Date] object
     * @param format The date/time format
     * @param locale Requested [Locale]
     * @return the formatted String
     */
    fun getStringFromDate(date: Date?, format: String, locale: Locale = Locale.US): String? {
        return try {
            getFormatter(format, locale).format(date!!)
        } catch (e: Exception) {
            Debugger.logException(e)
            null
        }
    }

    /**
     * Convert data to millis.
     * @param string String to parse
     * @param format The date/time format
     * @param locale Requested [Locale]
     * @return time in millis
     */
    fun getMillisFromString(string: String?, format: String, locale: Locale = Locale.US): Long {
        return try {
            getDateFromString(string, format, locale)!!.time
        } catch (e: Exception) {
            Debugger.logException(e)
            0L
        }
    }

    /**
     * Convert data to millis.
     * @param calendar The [Calendar] object
     * @return time in millis
     */
    fun getMillisFromCalendar(calendar: Calendar?): Long {
        return calendar?.timeInMillis ?: 0L
    }

    /**
     * Convert data to millis.
     * @param date The [Date] object
     * @return time in millis
     */
    fun getMillisFromDate(date: Date?): Long {
        return date?.time ?: 0L
    }

    /**
     * Convert data to [Date].
     * @param string String to parse
     * @param format The date/time format
     * @param locale Requested [Locale]
     * @return the [Date] object
     */
    fun getDateFromString(string: String?, format: String, locale: Locale = Locale.US): Date? {
        return try {
            return getFormatter(format, locale).parse(string!!)
        } catch (e: Exception) {
            Debugger.logException(e)
            null
        }
    }

    /**
     * Convert data to [Date].
     * @param millis Time in millis
     * @return the [Date] object
     */
    fun getDateFromMillis(millis: Long?): Date? {
        return if (millis != null) Date().apply {
            time = millis
        } else {
            null
        }
    }

    /**
     * Convert data to [Date].
     * @param calendar The [Calendar] object
     * @return the [Date] object
     */
    fun getDateFromCalendar(calendar: Calendar?): Date? {
        return calendar?.time
    }

    /**
     * Convert data to [Calendar].
     * @param string String to parse
     * @param format The date/time format
     * @param locale Requested [Locale]
     * @return the [Calendar] object
     */
    fun getCalendarFromString(string: String?, format: String, locale: Locale = Locale.US): Calendar? {
        return try {
            getCalendarFromDate(getDateFromString(string, format, locale))
        } catch (e: Exception) {
            Debugger.logException(e)
            null
        }
    }

    /**
     * Convert data to [Calendar].
     * @param millis Time in millis
     * @return the [Calendar] object
     */
    fun getCalendarFromMillis(millis: Long?): Calendar? {
        return if (millis != null) Calendar.getInstance().apply {
            timeInMillis = millis
        } else {
            null
        }
    }

    /**
     * Convert data to [Calendar].
     * @param date The [Date] object
     * @return the [Calendar] object
     */
    fun getCalendarFromDate(date: Date?): Calendar? {
        return if (date != null) Calendar.getInstance().apply {
            time = date
        } else {
            null
        }
    }

    /** Obtain a [SimpleDateFormat] for formatting */
    @Throws(NullPointerException::class, IllegalArgumentException::class)
    private fun getFormatter(format: String, locale: Locale): SimpleDateFormat {
        return SimpleDateFormat(format, locale)
    }

    /**
     * Convert time in millis to the specified date/time format.
     * @param dateTime The time
     * @param dateFormat The date/time format
     * @param newDateFormat The new date/time format
     * @return [String] for date/time
     */
    @SuppressLint("SimpleDateFormat")
    fun convertToDate(@NonNull dateTime: String?): String? {
        return try {
            val formatter = SimpleDateFormat(formatDate)
            val date = formatter.parse(dateTime!!)
            val appDateFormat = SimpleDateFormat(newFormatDate)
            appDateFormat.format(date!!)
        } catch (e: java.lang.Exception) {
            dateTime
        }
    }
}
