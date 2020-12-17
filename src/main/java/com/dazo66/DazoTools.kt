package com.dazo66

import com.dazo66.Gui.GuiFrame
import com.dazo66.utils.InstanceControl
import com.dazo66.utils.LoggerProcess
import com.dazo66.utils.LoggerProcess.Companion.TEXT_AREA_LOGGER
import com.dazo66.utils.StdOutErrRedirect
import com.dazo66.utils.TimeUtil.TimeTaskPool
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
            process = LoggerProcess("huanfengLiveRecord", "java -Dfile" +
                    ".encoding=UTF_8 -jar BiliLiveRecorder.jar  " + "\"options=config" +
                    ".json&liver=huya&&id=131394&qn=-1\"", File("data/BilibiliLiveRecord" + ".v2.12.0/"))
            runBlocking { process!!.setLoop(true) }


        }
    }

}
