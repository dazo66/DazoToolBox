package com.dazo66.utils.app

import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Dazo66
 */

fun getPolicy(rawPolicy: String): LaunchPolicy {
    var rawPolicy = rawPolicy.trim()
    var args = getArgs(rawPolicy)
    if (rawPolicy.startsWith("LimitedTimesPolicy", true)) {
        if (!args.isEmpty()) {
            return LimitedTimesPolicy(args[0].toInt())
        } else {
            return LimitedTimesPolicy(1)
        }
    }

    if (rawPolicy.startsWith("UnlimitedTimesPolicy", true)) {
        return UnlimitedTimesPolicy()
    }

    return UnlimitedTimesPolicy()
}

fun getArgs(rawPolicy: String): List<String> {
    if (rawPolicy.isBlank()) {
        return Collections.emptyList()
    }
    var rawArgs =  rawPolicy.subSequence(
            rawPolicy.indexOf("[", 0, false) + 1,
            rawPolicy.indexOf("]", 0, false))
    rawArgs = rawArgs.replace(" ".toRegex(), "")
    if (rawArgs.isBlank()) {
        return emptyList()
    }
    return rawArgs.split(",")
}

open class LimitedTimesPolicy(times: Int) :LaunchPolicy {

    private val times: AtomicInteger = AtomicInteger(0)

    init {
        this.times.set(times)
    }

    override fun canRun(): Boolean {
        if (times.get() != -1) {
            if (times.get() > 0) {
                times.decrementAndGet()
            } else {
                return false
            }
        }
        return true
    }
}

class UnlimitedTimesPolicy : LimitedTimesPolicy(-1) {}


