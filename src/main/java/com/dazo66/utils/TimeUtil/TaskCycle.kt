package com.dazo66.utils.TimeUtil

import java.util.Date

interface TaskCycle {

    fun getNextTime(date: Date): Date

}
