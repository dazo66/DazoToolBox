package com.dazo66.utils

import com.dazo66.DazoTools.Companion.TEXT_AREA_LOGGER
import org.apache.commons.logging.LogFactory
import java.io.PrintStream


object StdOutErrRedirect {
    private val logger = LogFactory.getLog(TEXT_AREA_LOGGER)

    fun redirectSystemOutAndErrToLog() {
        val printStreamForOut = createLoggingWrapper(System.out, false)
        val printStreamForErr = createLoggingWrapper(System.out, true)
        System.setOut(printStreamForOut)
        System.setErr(printStreamForErr)
    }

    fun createLoggingWrapper(printStream: PrintStream, isErr: Boolean): PrintStream {
        return object : PrintStream(printStream) {
            override fun print(string: String?) {
                if (!isErr) {
                    logger.info(string)
                } else {
                    logger.error(string)
                }
            }

            override fun print(b: Boolean) {
                if (!isErr) {
                    logger.info(b)
                } else {
                    logger.error(b)
                }
            }

            override fun print(c: Char) {
                if (!isErr) {
                    logger.info(c)
                } else {
                    logger.error(c)
                }
            }

            override fun print(i: Int) {
                if (!isErr) {
                    logger.info(i.toString())
                } else {
                    logger.error(i.toString())
                }
            }

            override fun print(l: Long) {
                if (!isErr) {
                    logger.info(l.toString())
                } else {
                    logger.error(l.toString())
                }
            }

            override fun print(f: Float) {
                if (!isErr) {
                    logger.info(f.toString())
                } else {
                    logger.error(f.toString())
                }
            }

            override fun print(d: Double) {
                if (!isErr) {
                    logger.info(d.toString())
                } else {
                    logger.error(d.toString())
                }
            }

            override fun print(x: CharArray) {
                if (!isErr) {
                    logger.info(String(x))
                } else {
                    logger.error(String(x))
                }
            }

            override fun print(obj: Any?) {
                if (!isErr) {
                    logger.info(obj)
                } else {
                    logger.error(obj)
                }
            }
        }
    }
}