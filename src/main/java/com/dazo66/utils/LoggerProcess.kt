package com.dazo66.utils

import kotlinx.coroutines.*
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.log4j.Priority
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author Dazo66
 */
class LoggerProcess(var name: String, var command: String, var dir: File)  {

    private lateinit var scanner: Scanner
    private lateinit var errorScanner: Scanner
    private var isLoop = false
    private var process: Process? = null
    private var isLoopJob: Job? = null

    init {
        init(name, command, dir)
        if (isLoop) {

        }
    }

    private fun init(name: String, command: String, dir: File) {
        if (process != null) {
            process!!.destroy()
        }
        process = tryExec(command, dir)!!
        logger = Logger.getLogger(name)
        scanner = Scanner(process!!.inputStream, "utf-8")
        errorScanner = Scanner(process!!.errorStream, "utf-8")
        start()
    }

    private fun start() {
        GlobalScope.launch {
            try {
                while (true) {
                    if (process!!.isAlive) {
                        streamToLog(Level.INFO, logger, scanner)
                    } else {
                        logger.info("${logger.name} process end , exit code:${process!!.exitValue()}")
                        break
                    }
                }
            } catch (e: Exception) {
                logger.error(e)
            }
        }
        GlobalScope.launch {
            try {
                while (true) {
                    if (process!!.isAlive) {
                        streamToLog(Level.ERROR, logger, errorScanner)
                    } else {
                        logger.info("${logger.name} process end , exit code:${process!!.exitValue()}")
                        break
                    }
                }
            } catch (e: Exception) {
                logger.error(e)
            }
        }
    }

    fun exit() {
        process?.destroy()
    }

    suspend fun setLoop(b: Boolean) {
        if (b) {
            if (isLoopJob == null) {
                isLoopJob = GlobalScope.launch {
                    while (true) {
                        if (process != null && !process!!.isAlive) {
                            init(name, command, dir)
                        }
                        delay(1000)
                    }
                }
            }
        } else {
            if (isLoopJob != null) {
                isLoopJob!!.cancelAndJoin()
            }
        }
        isLoop = b
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

        private fun streamToLog(priority: Priority, logger: Logger, stream: Scanner) {
            if (stream.hasNextLine()) {
                logger.log(priority, stream.nextLine())
            }
        }
    }
}
