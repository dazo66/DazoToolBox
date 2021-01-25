package com.dazo66.utils.app

import com.dazo66.DazoTools.Companion.TEXT_AREA_LOGGER
import com.dazo66.utils.ProcessUtils
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger
import sun.java2d.loops.ProcessPath
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*

/**
 * @author Dazo66
 */
class LoggerProcess(name: String, command: String, dir: File)  {

    var scanner: Scanner? = null
    var errorScanner: Scanner? = null
    private var process: Process? = null

    init {
        if (process != null) {
            process!!.destroy()
        }
        process = tryExec(command, dir)
        logger = Logger.getLogger(name)
        if (process != null) {
            scanner = Scanner(process!!.inputStream, "GB18030")
            errorScanner = Scanner(process!!.errorStream, "GB18030")
        }


    }

    fun isAlive(): Boolean {
        if (process == null) {
            return false
        }
        return process!!.isAlive
    }

    fun exit() {
        if(isAlive()) {
            try {
                ProcessUtils.killProcess(process!!)
            } catch (e: Exception) {
            } finally {
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
    }

    companion object {

        private var logger: Logger = Logger.getLogger(TEXT_AREA_LOGGER)

        fun tryExec(command: String, dir: File): Process? {
            try {
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                return Runtime.getRuntime().exec("cmd.exe /c \"$command\"", null, dir)
            } catch (e: IOException) {
                logger.error("子程序执行异常$e")
                return null
            }
        }
        fun buildBat(command: String): String {
            var file = File("temp/${command.hashCode()}.bat")
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            file.createNewFile()
            var writer = FileWriter(file)
            IOUtils.write(command, writer)
            writer.close()
            return file.absolutePath
        }
    }
}
