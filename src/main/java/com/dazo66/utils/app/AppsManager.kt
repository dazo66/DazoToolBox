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
            logger.info("[${app.name}] $msg")
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
            if (app.isAlive() && app.isAble.get()) {
                tryGetLine(app, app.process.scanner!!)
                tryGetLine(app, app.process.errorScanner!!)
            } else if (app.isAble.get() && app.policy.canRun()) {
                app.reStart()
            }
        }
    }

    fun tryGetLine(app: App, scanner: Scanner): String {
        var line : String
        try {
            line = timeLimiter.callWithTimeout(
                    Callable {
                        while (scanner.hasNextLine()) {
                            info(scanner.nextLine(), app)
                        }
                        return@Callable ""
                    }
                    , 500L, TimeUnit.MILLISECONDS, false)
        } catch (e : Exception) {
            line = ""
        }
        return line
    }



}
