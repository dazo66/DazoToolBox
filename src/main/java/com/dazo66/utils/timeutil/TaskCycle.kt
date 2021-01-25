package com.dazo66.utils.timeutil

import java.util.Date

interface TaskCycle {

    fun getNextTime(date: Date): Date

}
