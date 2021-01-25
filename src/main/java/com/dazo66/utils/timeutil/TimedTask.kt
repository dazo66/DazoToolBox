package com.dazo66.utils.timeutil

import java.util.Date

/**
 * @author Dazo66
 */
class TimedTask : Runnable {

    internal var times = -1

    internal var tiggerTime = Date()

    internal var cycle = TimeConstant.PER_DAY

    internal var runnable: Runnable? = null

    private val nextTiggerTime: Date
        get() = cycle.getNextTime(tiggerTime)

    fun setCycle(cycle: TaskCycle): TimedTask {
        this.cycle = cycle
        return this
    }

    fun getTiggerTime(): Date {
        return tiggerTime
    }

    fun setTiggerTime(tiggerTime: Date): TimedTask {
        this.tiggerTime = tiggerTime
        return this
    }

    fun setTimes(times: Int): TimedTask {
        this.times = times
        return this
    }

    fun setRunnable(runnable: Runnable): TimedTask {
        this.runnable = runnable
        return this
    }

    override fun run() {
        if (runnable != null && (times > 0 || times == -1)) {
            if (times > 0) {
                times--
            }
            runnable!!.run()
            tiggerTime = nextTiggerTime
        }
    }
}
