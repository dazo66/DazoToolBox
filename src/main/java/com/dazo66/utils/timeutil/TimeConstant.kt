package com.dazo66.utils.timeutil

import java.util.Calendar
import java.util.Date

/**
 * @author Dazo66
 */
object TimeConstant {

    var SEC = 1000
    var MIN = 60 * SEC
    var HOUR = 60 * MIN
    var DAY = 24 * HOUR
    var WEEK = 7 * DAY
    var YEAR = 365 * DAY

    var PER_YEAR: TaskCycle = PreYear()
    var PER_MONTH: TaskCycle = PreMonth()
    var PER_WEEK: TaskCycle = PreWeek()
    var PER_DAY: TaskCycle = PreDay()
    var PER_HOUR: TaskCycle = PreHour()
    var PER_MIN: TaskCycle = PreMin()

    class PreYear : TaskCycle {

        override fun getNextTime(date: Date): Date {
            val now = Date()
            return if (now.compareTo(date) > 0) {
                getNextTime(Date(date.time + YEAR))
            } else date
        }

    }

    class PreMonth : TaskCycle {

        override fun getNextTime(date: Date): Date {
            val now = Date()
            if (now.compareTo(date) >= 0) {
                val cal = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.MONTH, 1)
                return getNextTime(Date(cal.time.time))
            }
            return date
        }

    }

    class PreWeek : TaskCycle {

        override fun getNextTime(date: Date): Date {
            val now = Date()
            return if (now.compareTo(date) >= 0) {
                getNextTime(Date(date.time + DAY * 7))
            } else date
        }

    }

    class PreDay : TaskCycle {

        override fun getNextTime(date: Date): Date {
            val now = Date()
            return if (now.compareTo(date) >= 0) {
                getNextTime(Date(date.time + DAY))
            } else date
        }

    }

    class PreHour : TaskCycle {

        override fun getNextTime(date: Date): Date {
            val now = Date()
            return if (now.compareTo(date) >= 0) {
                getNextTime(Date(date.time + HOUR))
            } else date
        }

    }

    class PreMin : TaskCycle {

        override fun getNextTime(date: Date): Date {
            val now = Date()
            return if (now.compareTo(date) >= 0) {
                getNextTime(Date(date.time + MIN))
            } else date
        }

    }


}
