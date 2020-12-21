package com.dazo66.utils.app

import org.apache.log4j.Logger
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author Dazo66
 */
class LoggerProcess(name: String, command: String, dir: File)  {

    val scanner: Scanner
    val errorScanner: Scanner
    private var process: Process? = null

    init {
        if (process != null) {
            process!!.destroy()
        }
        process = tryExec(command, dir)!!
        logger = Logger.getLogger(name)
        scanner = Scanner(process!!.inputStream, "GB18030")
        errorScanner = Scanner(process!!.errorStream, "GB18030")


    }

    fun isAlive(): Boolean {
        if (process == null) {
            return false
        }
        return process!!.isAlive
    }

    fun exit() {
        try {
            process?.destroy()
        } catch (e: Exception) {
        }
        try {
            org.apache.commons.io.IOUtils.closeQuietly(scanner)
        } catch (e: Exception) {
        }
        try {
            org.apache.commons.io.IOUtils.closeQuietly(scanner)
        } catch (e: Exception) {
        }
    }

    companion object {

        const val TEXT_AREA_LOGGER: String = "textArea"
        private var logger: Logger = Logger.getLogger(TEXT_AREA_LOGGER)

        fun tryExec(command: String, dir: File): Process? {
            try {
                return Runtime.getRuntime().exec(command, null, dir)
            } catch (e: IOException) {
                logger.error("子程序执行异常$e")
                return null
            }

        }
    }
}