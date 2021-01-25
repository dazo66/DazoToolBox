package com.dazo66

import com.dazo66.gui.GuiModel
import com.dazo66.gui.MainGui
import com.dazo66.gui.TextAreaLogAppender
import com.dazo66.utils.InstanceControl
import com.dazo66.utils.StdOutErrRedirect
import com.dazo66.utils.timeutil.TimeTaskPool
import com.dazo66.utils.app.AppsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.apache.log4j.Logger
import kotlin.system.exitProcess

class DazoTools {

    private val instanceControl: InstanceControl = InstanceControl(this, 27890)

    var pool = TimeTaskPool()

    fun exit(i: Int) {
        exitProcess(i)
    }

    init {
        instanceControl.start()
    }



    companion object {
        const val TEXT_AREA_LOGGER: String = "textArea"
        private val logger = Logger.getLogger(TEXT_AREA_LOGGER)
        fun exit() {
            System.exit(0)
        }
        @JvmStatic
        fun main(args: Array<String>) {
            StdOutErrRedirect.redirectSystemOutAndErrToLog()
            MainGui(GuiModel())
            val appsManager = AppsManager.instance
            runBlocking {
                logger.info(TextAreaLogAppender.FLAG)
                while (true) {
                    try {
                        appsManager.check()
                    } catch (e : Exception) {}
                    delay(1000)
                }
            }
        }
    }

}
