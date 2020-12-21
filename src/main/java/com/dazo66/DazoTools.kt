package com.dazo66

import com.dazo66.Gui.GuiFrame
import com.dazo66.utils.InstanceControl
import com.dazo66.utils.app.LoggerProcess
import com.dazo66.utils.app.LoggerProcess.Companion.TEXT_AREA_LOGGER
import com.dazo66.utils.StdOutErrRedirect
import com.dazo66.utils.TimeUtil.TimeTaskPool
import com.dazo66.utils.app.AppsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.apache.log4j.Logger
import java.io.File
import java.io.IOException
import kotlin.system.exitProcess

class DazoTools {
    private val instanceControl: InstanceControl = InstanceControl(this, 27890)
    var frame: GuiFrame = GuiFrame(this)
    var pool: TimeTaskPool = TimeTaskPool()

    fun exit(i: Int) {
        process!!.exit()
        exitProcess(i)
    }

    init {
        instanceControl.start()
    }

    companion object {

        private var process: LoggerProcess? = null

        private val logger = Logger.getLogger(TEXT_AREA_LOGGER)

        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            StdOutErrRedirect.redirectSystemOutAndErrToLog()
            javax.swing.SwingUtilities.invokeLater { DazoTools() }
            val appsManager = AppsManager.instance
            runBlocking {
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
