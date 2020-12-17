package com.dazo66.Gui

import com.dazo66.utils.LoggerProcess
import kotlinx.coroutines.delay
import java.io.IOException
import java.util.*
import javax.swing.JScrollPane
import javax.swing.JTextArea

/**
 * 重定向
 *
 * @author lyrics
 * @since 2020/07/01
 */
class TextAreaLogAppender @Throws(IOException::class)
constructor(private val textArea: JTextArea, private val scroll: JScrollPane) : AbstractLogAppender(LoggerProcess.TEXT_AREA_LOGGER) {

    suspend fun run() {
        val scanner = Scanner(reader)
        val document = textArea.document
        while (scanner.hasNextLine()) {
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
                val line = scanner.nextLine()
                textArea.append(line)
                textArea.append("\n")
                delay(50)
                scroll.verticalScrollBar.value = Int.MAX_VALUE
            } catch (ex: Exception) {
            }

        }
    }
}