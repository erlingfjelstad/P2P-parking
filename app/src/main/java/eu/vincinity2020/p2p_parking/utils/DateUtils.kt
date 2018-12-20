package eu.vincinity2020.p2p_parking.utils

import android.content.Context
import eu.vincinity2020.p2p_parking.R
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        fun isMoreThanOneDayAfter(calendar1: Calendar, calendar2: Calendar): Boolean {
            val year1 = calendar1.get(Calendar.YEAR)
            val month1 = calendar1.get(Calendar.MONTH)
            val day1 = calendar1.get(Calendar.DAY_OF_MONTH)

            val year2 = calendar2.get(Calendar.YEAR)
            val month2 = calendar2.get(Calendar.MONTH)
            val day2 = calendar2.get(Calendar.DAY_OF_MONTH)

            if (year1 <= year2 && month1 <= month2 && day1 < day2) {
                return true
            }


            return false
        }

        fun getReadableDate(calendar: Calendar, context: Context): String {
            val builder = StringBuilder()

            val day = calendar.get(Calendar.DAY_OF_MONTH)

            builder.append(day)

            val month = calendar.get(Calendar.MONTH)
            val readableMonth = getReadableMonth(month, context)

            return builder.toString()
        }

        private fun getReadableMonth(month: Int, context: Context): String {
            return when (month) {
                0 -> {
                    context.getString(R.string.january)
                }
                1 -> {
                    context.getString(R.string.february)
                }
                2 -> {
                    context.getString(R.string.march)
                }
                3 -> {
                    context.getString(R.string.april)
                }
                4 -> {
                    context.getString(R.string.may)
                }
                5 -> {
                    context.getString(R.string.june)
                }
                6 -> {
                    context.getString(R.string.july)
                }
                7 -> {
                    context.getString(R.string.august)
                }
                8 -> {
                    context.getString(R.string.september)
                }
                9 -> {
                    context.getString(R.string.oktober)
                }
                10 -> {
                    context.getString(R.string.november)
                }
                11 -> {
                    context.getString(R.string.december)
                }

                else -> ""
            }
        }

        fun getReadableDay(calendar: Calendar, context: Context): String {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

            val readableDayOfWeek = getReadableDayOfWeek(dayOfWeek, context)
            val readableAmOrPm = getReadableAmOrPm(hourOfDay, context)

            return String.format("%s %s", readableDayOfWeek, readableAmOrPm)
        }

        private fun getReadableAmOrPm(hourOfDay: Int, context: Context): String {
            return when (hourOfDay) {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 -> (context.getString(R.string.morning))
                else -> context.getString(R.string.after_noon)
            }
        }

        private fun getReadableDayOfWeek(dayOfWeek: Int, context: Context): Any {
            return when (dayOfWeek) {
                0 -> {
                    context.getString(R.string.monday)
                }
                1 -> {
                    context.getString(R.string.tuesday)
                }
                2 -> {
                    context.getString(R.string.wednesday)
                }
                3 -> {
                    context.getString(R.string.thursday)
                }
                4 -> {
                    context.getString(R.string.friday)
                }
                5 -> {
                    context.getString(R.string.saturday)
                }
                6 -> {
                    context.getString(R.string.sunday)
                }
                else -> {
                    ""
                }
            }
        }

        fun formatDateTime(calendar: Calendar, expectedFormat: String) : String
        {
            val toFormat = SimpleDateFormat(expectedFormat)
            return  toFormat.format(calendar.time)
        }
    }
}