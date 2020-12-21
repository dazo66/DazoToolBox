package com.dazo66.utils.app

import com.google.common.util.concurrent.SimpleTimeLimiter
import org.apache.log4j.Logger
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * @author Dazo66
 */
class AppsManager private constructor() {

    companion object {
        const val TEXT_AREA_LOGGER: String = "textArea"
        private var logger: Logger = Logger.getLogger(TEXT_AREA_LOGGER)
        val appDiscover = AppDiscover()
        val instance = Singleton.getInstance()

        private fun info(msg: String, app : App) {
            logger.info("[$app.name] $msg")
        }

        class Singleton private constructor() {
            companion object {
                private val appsManager = AppsManager()
                fun getInstance(): AppsManager {
                    return appsManager
                }
            }
        }
    }
    var apps: List<App> = appDiscover.scanApp(ArrayList())
    val timeLimiter = SimpleTimeLimiter()

    fun check(){
        apps = appDiscover.scanApp(apps as MutableList<App>)
        for (app in apps) {
            var info : String
            if (app.isAlive() && app.isAble.get()) {
                info = tryGetLine(app.process.scanner!!)
                if (!info.isBlank()) {
                    info(info, app)
                }
                info = tryGetLine(app.process.errorScanner!!)
                if (!info.isBlank()) {
                    info(info, app)
                }
            } else if (!tryGetLine(app.process.scanner!!).also { info = it }.isBlank()) {
                info(info, app)
                while (!tryGetLine(app.process.scanner!!).also { info = it }.isBlank()) {
                    info(info, app)
                }
            }else if (!tryGetLine(app.process.errorScanner!!).also { info = it }.isBlank()) {
                info(info, app)
                while (!tryGetLine(app.process.errorScanner!!).also { info = it }.isBlank()) {
                    info(info, app)
                }
            } else if (app.isAble.get() && app.policy.canRun()) {
                app.reStart()
            }
        }
    }

    fun tryGetLine(scanner: Scanner): String {
        var line : String
        try {
            line = timeLimiter.callWithTimeout(
                    Callable {
                        while (scanner.hasNextLine()) {
                            return@Callable scanner.nextLine()
                        }
                        return@Callable ""
                    }
                    , 10L, TimeUnit.MILLISECONDS, false)
        } catch (e : Exception) {
            line = ""
        }
        return line
    }



}
