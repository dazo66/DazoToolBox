package com.dazo66.gui

import com.dazo66.DazoTools.Companion.TEXT_AREA_LOGGER
import kotlinx.coroutines.delay
import org.apache.commons.lang3.concurrent.ConcurrentUtils
import java.io.IOException
import java.lang.StringBuilder
import java.util.*
import java.util.concurrent.CountDownLatch
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.text.Document
import kotlin.collections.ArrayList

/**
 * 重定向
 *
 * @author lyrics
 * @since 2020/07/01
 */
class TextAreaLogAppender @Throws(IOException::class)
constructor(
    private val config: GuiModel,
    private val textArea: JTextArea,
    private val scroll: JScrollPane
) : AbstractLogAppender(TEXT_AREA_LOGGER) {

    val logHistroy = LinkedList<String>()

    val checkLog: (String, String, String, String) -> Boolean =
        checkLog@{ date, logLevel, appName, msg ->
            if (logLevel.trim() == config.data.logLevel.trim() || config.data.logLevel.isBlank()) {
                if (appName.trim() == config.data.appName.trim() || config.data.appName.isBlank()) {
                    return@checkLog true
                }
            }
            return@checkLog false
        }

    fun run() {
        val scanner = Scanner(reader)
        val document = textArea.document
        while (tryHasNextLine(scanner)) {
            try {
                val line = scanner.nextLine()
                if (logHistroy.size > 1000) {
                    logHistroy.removeFirst()
                }
                if (line.contains(FLAG)) {
                    val list = filter(regex, logHistroy, checkLog)
                    val builder = StringBuilder()
                    list.forEach { builder.append(it).append("\n") }
                    textArea.text = builder.toString()
                } else {
                    logHistroy.add(line)
                    try {
                        if (document.length > 50000) {
                            val s = document.getText(0, document.length)
                            val l = s.split("\n")
                            var i = 0
                            for (s1 in l) {
                                i += s1.length + 1
                                if (i > 40000) {
                                    break
                                }
                            }
                            document.remove(0, i)
                        }
                        if (filter(regex, line, checkLog) != null) {
                            textArea.append(line)
                            textArea.append("\n")
                        }

                    } catch (ex: Exception) {
                    }
                }
                if (config.data.autoFlush) {
                    scroll.verticalScrollBar.value = Int.MAX_VALUE
                }
            } catch (e: Exception) {

            }
        }
    }

    companion object {
        val FLAG = "e5ce018048d76ed0df049c82752dc2ce31ac6630"
        val regex =
            """(\d{4}-\d{2}-\d{2} +\d{2}:\d{2}:\d{2}) +\[ (\S+) ] +(\[([\w|\W]+)])* +([\w|\W]*)"""
                .toRegex()

        fun tryHasNextLine(scanner: Scanner): Boolean {
            return try {
                scanner.hasNextLine()
            } catch (e: Exception) {
                false
            }
        }

        fun filter(
            regex: Regex,
            logs: List<String>,
            judge: (date: String, logLevel: String, appName: String, msg: String) -> Boolean
        )
                : List<String> {
            var ret = ArrayList<String>()
            logs.forEach {
                var filterResult = filter(regex, it, judge)
                if (filterResult != null) {
                    ret.add(filterResult)
                }
            }
            return ret
        }

        fun filter(
            regex: Regex,
            log: String,
            judge: (date: String, logLevel: String, appName: String, msg: String) -> Boolean
        )
                : String? {

            var matchResult = regex.matchEntire(log)
            if (matchResult == null) {
                return log
            } else {
                val groups = matchResult.groupValues
                if (judge(groups[1], groups[2], groups[4], groups[5])) {
                    return log
                }
            }
            return null
        }

    }
}