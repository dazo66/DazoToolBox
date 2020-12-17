package com.dazo66.utils

import com.dazo66.DazoTools

import java.net.ServerSocket
import java.net.Socket


class InstanceControl(private val core: DazoTools, private val port: Int) : Thread() {

    override fun run() {
        try {
            val sock = Socket("127.0.0.1", port)
            //创建socket，连接22222端口　　　　　　　　　　　　　　　　　　　
        } catch (e: Exception) {
        }

        try {
            //创建socket,在port端口监听
            val server = ServerSocket(port)
            //等待连接
            server.accept()
            //有连接到来，也就是说有新的实例
            server.close()
            //这个实例退出
            core.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}