package com.dazo66.Gui

import com.dazo66.DazoTools
import com.dazo66.utils.Shortcut
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.log4j.Logger
import java.awt.*
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.JTextArea

class GuiFrame(private val core: DazoTools) : JFrame("DazoTools") {
    var tray: SystemTray? = null
        private set
    private val scrollPane = JScrollPane()
    private val textArea = JTextArea()


    private val popmenu: PopupMenu = PopupMenu().also {
        val font = Font("Default", Font.PLAIN, 14)
        val itmExit = MenuItem("退出")
        itmExit.font = font
        val itmOpenConfig = MenuItem("打开控制台")
        itmOpenConfig.font = font
        val itmAutoStart = MenuItem("\u5f00\u673a\u81ea\u542f  " + if (Shortcut.isAutoStart) "\u2713" else "\u2717")
        itmAutoStart.font = font
        itmExit.addActionListener { this.exit() }
        itmOpenConfig.addActionListener { this.isVisible = !this.isVisible }
        itmAutoStart.addActionListener { Shortcut.run(this) }
        it.add(itmOpenConfig)
        it.add(itmAutoStart)
        it.add(itmExit)
    }

    init {
        this.init()
    }

    private fun init() {
        initLog()
        this.setUI()
        this.setTray()
    }

    private fun setUI() {
        title = "Dazo66 Tools"
        this.defaultCloseOperation = HIDE_ON_CLOSE
        defaultCloseOperation = HIDE_ON_CLOSE
        this.setSize(470, 577)
        setBounds(0, 0, 470, 577)
        textArea.isEditable = false
        setLocationRelativeTo(null)
        contentPane.layout = null
        iconImage = ICON
        scrollPane.setBounds(0, 0, 454, 530)
        contentPane.add(scrollPane)
        scrollPane.setViewportView(textArea)
        this.addComponentListener(object : ComponentListener {
            override fun componentMoved(e: ComponentEvent?) {
            }

            override fun componentResized(e: ComponentEvent?) {
                if (e?.component == this@GuiFrame) {
                    scrollPane.setBounds(
                            0, 0,
                            e.component.size.width - 16, e.component.size.height - 48)
                }
            }

            override fun componentHidden(e: ComponentEvent?) {
            }

            override fun componentShown(e: ComponentEvent?) {
            }
        })
    }

    private fun setTray() {
        if (SystemTray.isSupported()) {
            this.tray = SystemTray.getSystemTray()
            val text = "DazoTools"
            val trayIcon = TrayIcon(ICON, text, this.popmenu)
            trayIcon.addMouseListener(object :MouseListener {
                override fun mouseExited(e: MouseEvent?) {
                }

                override fun mouseReleased(e: MouseEvent?) {
                }

                override fun mouseEntered(e: MouseEvent?) {

                }

                override fun mouseClicked(e: MouseEvent?) {
                    when (e?.button) {
                        1 -> this@GuiFrame.isVisible = true
                    }
                }
                override fun mousePressed(e: MouseEvent?) {
                    when (e?.button) {
                        3 -> this@GuiFrame.popmenu.getItem(1).label = getShortcutLabel()
                    }
                }

            })
            trayIcon.isImageAutoSize = true
            try {
                this.tray!!.add(trayIcon)
            } catch (e1: AWTException) {
                e1.printStackTrace()
            }

        }
    }

    private fun initLog() {
        try {
            GlobalScope.launch {
                TextAreaLogAppender(textArea, scrollPane).run()
            }
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, "重定向错误")
        }

    }

    private fun exit() {
        core.exit(0)
    }

    fun messageBox(message: Any, title: String, messageType: Int) {
        JOptionPane.showMessageDialog(this, message, title, messageType)
    }

    companion object {

        private val log = Logger.getLogger(GuiFrame::class.java)
        private val ICON = Toolkit.getDefaultToolkit().getImage(GuiFrame::class.java.getResource("/res/gui/tray_logo.jpg"))
        fun getShortcutLabel():String =
                "\u5f00\u673a\u81ea\u542f  " +
                        if (Shortcut.isAutoStart) "\u2713" else "\u2717"

    }


}