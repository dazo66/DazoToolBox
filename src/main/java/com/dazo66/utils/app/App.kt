package com.dazo66.utils.app

import java.io.File

class App(val name: String,
          val path: String,
          val dataPath: String,
          val updateDate: Long,
          val policy: LaunchPolicy,
          val rowName: String) {

    init {
        val path = File(dataPath)
        if (!path.exists()) {
            path.mkdirs()
        }
    }

    var process: LoggerProcess = LoggerProcess(name, path, File(dataPath))

    fun reStart(){
        process.exit()
        process = LoggerProcess(name, path, File(dataPath))
    }

    fun stop() {
        process.exit()
    }

    fun isAlive():Boolean {
        return process.isAlive()
    }

}
