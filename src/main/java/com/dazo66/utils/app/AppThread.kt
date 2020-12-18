package com.dazo66.utils.app

import com.dazo66.utils.LoggerProcess
import java.io.File

/**
 * @author Dazo66
 */
class AppThread(val app: App) : Thread() {

    val process: LoggerProcess = LoggerProcess(app.name, app.path, File(app.dataPath))

    override fun run() {
        while (app.policy.canRun()) {

        }
    }


}
