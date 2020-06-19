package com.stocksexchange.core.formatters

import android.content.Context
import com.stocksexchange.core.utils.extensions.capitalize
import com.stocksexchange.core.utils.extensions.stripPeriodCharacter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimeFormatter(locale: Locale) {


    private var mLocale: Locale = Locale.getDefault()

    private val mNewsBlogPostPublicationDateInputFormatter: SimpleDateFormat = SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss zzz",
        Locale.ENGLISH
    )
    private val mNewsItemPublicationDateOutputFormatter: SimpleDateFormat = SimpleDateFormat(
        "d MMM",
        Locale.ENGLISH
    )

    private lateinit var mDateFormatter: SimpleDateFormat
    private lateinit var mTradeHistoryTimeFormatter: SimpleDateFormat

    private lateinit var mCalendar: Calendar




    init {
        setLocale(locale)
    }


    fun setLocale(locale: Locale) {
        mDateFormatter = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            locale
        )
        mTradeHistoryTimeFormatter = SimpleDateFormat(
            "MMM d, HH:mm",
            locale
        )

        mCalendar = Calendar.getInstance(locale)
        mLocale = locale
    }


    /**
     * A generic method for formatting time in the following format: MM:SS AM/PM (in countries
     * that support AM/PM) or MM:SS (in countries that do support 24 hour format).
     *
     * @param context The context
     * @param hours The hours to format
     * @param minutes The minutes to format
     * @param appendDayPeriod Whether or not to append day period (AM / PM). Only applicable
     * to countries that support such format.
     *
     * @return The formatted time
     */
    fun formatTime(context: Context, hours: Int, minutes: Int, appendDayPeriod: Boolean): String {
        return if(isTwelveHourTimeZone(context)) {
            val adjustedHours: Int = if(hours >= 12) {
                (hours - 12)
            } else {
                hours
            }

            var result = when {
                (adjustedHours == 0) -> "12"
                else -> adjustedHours.toString()
            } + ":" + when {
                (minutes < 10) -> "0$minutes"
                else -> minutes.toString()
            }

            if(appendDayPeriod) {
                result += (if (isAm(context, hours)) " AM" else " PM")
            }

            return result
        } else {
            when {
                (hours < 10) -> "0$hours"
                else -> hours.toString()
            } + ":" + when {
                (minutes < 10) -> "0$minutes"
                else -> minutes.toString()
            }
        }
    }


    /**
     * Formats the passed in timestamp in the following format: 2018-04-19 14:06:32.
     * If the device's time zone is in 12 hour time zone, then the time will be
     * 2018-04-19 02:06:32 PM.
     *
     * @param timestamp The unix time to format
     *
     * @return The timestamp formatted in the aforementioned way
     */
    fun formatDate(timestamp: Long): String {
        return mDateFormatter.format(Date(timestamp))
    }


    /**
     * Formats the tweet's publication time.
     *
     * @param timestamp The timestamp of the tweet creation
     *
     * @return The timestamp formatted in the specified format (example: 11 Jan)
     */
    fun formatNewsTweetPublicationTime(timestamp: Long): String {
        return formatNewsItemPublicationDate(Date(timestamp))
    }


    /**
     * Formats the post's publication time of the STEX blog.
     *
     * @param publicationDate The exact date the post was published
     *
     * @return The time formatted in the specified format (example: 11 Jan)
     */
    fun formatNewsBlogPostPublicationTime(publicationDate: String): String {
        return try {
            mNewsBlogPostPublicationDateInputFormatter.parse(publicationDate)
                ?.let { formatNewsItemPublicationDate(it) }
                ?: ""
        } catch(exception: ParseException) {
            ""
        }
    }


    private fun formatNewsItemPublicationDate(date: Date): String {
        return mNewsItemPublicationDateOutputFormatter.format(date)
    }


    /**
     * Formats the passed in timestamp in the following format: Jan 5, 14:06.
     *
     * @param timestamp The unix time to format
     *
     * @return The timestamp formatted in the aforementioned way
     */
    fun formatTradeHistoryTime(timestamp: Long): String {
        return mTradeHistoryTimeFormatter.format(Date(timestamp))
            .stripPeriodCharacter()
            .capitalize(mLocale)
    }


    fun getYear(timeInMillis: Long): Int {
        mCalendar.timeInMillis = timeInMillis

        return mCalendar.get(Calendar.YEAR)
    }


    fun getMonth(timeInMillis: Long): Int {
        mCalendar.timeInMillis = timeInMillis

        return mCalendar.get(Calendar.MONTH)
    }


    fun getDayOfMonth(timeInMillis: Long): Int {
        mCalendar.timeInMillis = timeInMillis

        return mCalendar.get(Calendar.DAY_OF_MONTH)
    }


    fun getDayOfWeek(timeInMillis: Long): Int {
        mCalendar.timeInMillis = timeInMillis

        return mCalendar.get(Calendar.DAY_OF_WEEK)
    }


    fun getHour(context: Context, timeInMillis: Long): Int {
        mCalendar.timeInMillis = timeInMillis

        return mCalendar.get(if(isTwelveHourTimeZone(context)) {
            Calendar.HOUR
        } else {
            Calendar.HOUR_OF_DAY
        })
    }


    fun getHourOfDay(timeInMillis: Long): Int {
        mCalendar.timeInMillis = timeInMillis

        return mCalendar.get(Calendar.HOUR_OF_DAY)
    }


    fun getMinute(timeInMillis: Long): Int {
        mCalendar.timeInMillis = timeInMillis

        return mCalendar.get(Calendar.MINUTE)
    }


    fun getSecond(timeInMillis: Long): Int {
        mCalendar.timeInMillis = timeInMillis

        return mCalendar.get(Calendar.SECOND)
    }


    fun getMillisecond(timeInMillis: Long): Int {
        mCalendar.timeInMillis = timeInMillis

        return mCalendar.get(Calendar.MILLISECOND)
    }


    /**
     * Determines whether the device supports 12 hour time zone or not.
     *
     * @param context The context
     *
     * @return true if supports; false otherwise
     */
    fun isTwelveHourTimeZone(context: Context): Boolean {
        return !android.text.format.DateFormat.is24HourFormat(context)
    }


    /**
     * Determines whether the hour of day is in the AM range according
     * to the 12 hour time zone.
     *
     * @param context The context
     * @param hourOfDay The hour of day
     *
     * @return true if the hour of day resides in the AM range; false otherwise
     */
    fun isAm(context: Context, hourOfDay: Int): Boolean {
        return if(isTwelveHourTimeZone(context)) {
            (hourOfDay < 12)
        } else {
            false
        }
    }


    /**
     * Determines whether the hour of day is in the PM range according
     * to the 12 hour time zone.
     *
     * @param context The context
     * @param hourOfDay The hour of day
     *
     * @return true if the hour of day resides in the PM range; false otherwise
     */
    fun isPm(context: Context, hourOfDay: Int): Boolean {
        return if(isTwelveHourTimeZone(context)) {
            (hourOfDay >= 12)
        } else {
            false
        }
    }


}