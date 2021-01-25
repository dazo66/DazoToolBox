package com.dazo66.utils.app

import com.dazo66.DazoTools.Companion.TEXT_AREA_LOGGER
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
        private var logger: Logger = Logger.getLogger(TEXT_AREA_LOGGER)
        val appDiscover = AppDiscover()
        val instance = Singleton.getInstance()

        private fun info(msg: String, app : App) {
            logger.info("[${app.name}] $msg")
        }

        private fun error(msg: String, app : App) {
            logger.error("[${app.name}] $msg")
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
    private val timeLimiter = SimpleTimeLimiter()

    fun check(){
        apps = appDiscover.scanApp(apps as MutableList<App>)
        for (app in apps) {
            if (app.isAlive() && app.isAble.get()) {
                tryGetLine(app, app.process.scanner!!, true)
                tryGetLine(app, app.process.errorScanner!!, false)
            } else if (app.isAble.get() && app.policy.canRun()) {
                tryGetLine(app, app.process.scanner!!, true)
                tryGetLine(app, app.process.errorScanner!!, false)
                app.reStart()
            }
        }
    }

    fun tryGetLine(app: App, scanner: Scanner, isInfo: Boolean) {
        try {
            timeLimiter.callWithTimeout (
                    Callable {
                        try {
                            while (scanner.hasNextLine()) {
                                if (isInfo) {
                                    info(scanner.nextLine(), app)
                                } else {
                                    error(scanner.nextLine(), app)
                                }
                            }
                        } catch (e: Exception) {
                            //ignore
                        }
                        return@Callable ""
                    }
                    , 500L, TimeUnit.MILLISECONDS, false)
        } catch (e : Exception) {
        }
    }



}
