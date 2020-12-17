package com.dazo66.utils

import com.dazo66.Gui.GuiFrame
import org.apache.log4j.Logger
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream
import java.net.URLDecoder


object Shortcut {
    private val logger = Logger.getLogger(Shortcut::class.java)
    private var startupPath: String =
            "C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\StartUp\\"
    private var shortcutName: String = "DazoTools-Shortcut"

    val isAutoStart: Boolean
        get() = File("$startupPath$shortcutName.lnk").exists()

    fun run(frame: GuiFrame) {
        if (isAutoStart) {
            File("$startupPath$shortcutName.lnk").delete()
            val itmAutoStart = frame.tray!!.trayIcons[0].popupMenu.getItem(1)
            itmAutoStart.label = "\u5f00\u673a\u81ea\u542f    " + if (isAutoStart) "\u2713" else "\u2717"
        } else {
            createWindowsShortcut(URLDecoder.decode(Shortcut::class.java.protectionDomain.codeSource.location
                    .path, "UTF-8"), shortcutName)
            val itmAutoStart = frame.tray!!.trayIcons[0].popupMenu.getItem(1)
            itmAutoStart.label = "\u5f00\u673a\u81ea\u542f    " + if (isAutoStart) "\u2713" else "\u2717"
        }
    }

    private fun createWindowsShortcut(runnable: String, name: String) {
        val osName = System.getProperty("os.name")
        val absName = File(runnable).absolutePath
        val installDir = File(runnable).parentFile
        val runnableName = File(runnable).name
        val command: String
        command = if (osName.contains("9")) {
            "command.com   /c   cscript.exe   /nologo   "
        } else {
            "cmd.exe   /c   cscript.exe   /nologo   "
        }
        try {
            val shortcutMaker = File(installDir.absolutePath + "\\makeshortcut.js")
            val out = PrintStream(FileOutputStream(shortcutMaker))
            out.println("WScript.Echo(\"Creating   shortcuts...\");")
            out.println("Shell   =   new   ActiveXObject(\"WScript.Shell\");")
            out.println("link   =   Shell.CreateShortcut" +
                    "(\"${startupPath.replace("\\", "\\\\")}$name.lnk\");")
            out.println("link.Arguments   =   \"\";")
            out.println("link.Description   =   \"$name\";")
            out.println("link.HotKey   =   \"\";")
            out.println("link.IconLocation   =   \"" + escaped(installDir.absolutePath) + "\\\\winamp.ico,0\";")
            out.println("link.TargetPath   =   \"" + escaped(installDir.absolutePath) + "\\\\" + runnableName + "\";")
            out.println("link.WindowStyle   =   1;")
            out.println("link.WorkingDirectory   =   \"" + escaped(installDir.absolutePath) + "\";")
            out.println("link.Save();")
            out.println("WScript.Echo(\"Shortcuts   created.\");")
            out.close()
            val p = Runtime.getRuntime().exec("$command   makeshortcut.js", null, installDir)
            p.waitFor()
            val rv = p.exitValue()
            shortcutMaker.delete()
            if (rv == 0) {
                logger.info("注册开机自启成功")
            }
        } catch (e: Exception) {
            logger.warn("注册开机自启出错$e")
        }

    }

    private fun escaped(s: String): String {
        val r = StringBuilder()
        for (i in s.indices) {
            if (s[i] == '\\') {
                r.append('\\')
            }
            r.append(s[i])
        }
        return r.toString()
    }
}
