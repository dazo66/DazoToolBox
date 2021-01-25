package com.dazo66.gui

import org.apache.log4j.Logger
import org.apache.log4j.WriterAppender

import java.io.IOException
import java.io.PipedReader
import java.io.PipedWriter

/**
 *
 * @author dazo66
 * @since 2020/07/01
 */
abstract class AbstractLogAppender @Throws(IOException::class)
constructor(appenderName: String) {

    protected var reader: PipedReader

    init {
        val root = Logger.getRootLogger()
        // 获取子记录器的输出源
        val appender = root.getAppender(appenderName)
        // 定义一个未连接的输入流管道
        reader = PipedReader()
        // 定义一个已连接的输出流管理，并连接到reader
        val writer = PipedWriter(reader)
        // 设置 appender 输出流
        (appender as WriterAppender).setWriter(writer)
    }
}