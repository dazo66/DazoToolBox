package com.dazo66.gui

import com.dazo66.utils.Shortcut
import com.dazo66.utils.app.AppsManager
import com.formdev.flatlaf.FlatLightLaf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.apache.log4j.Logger
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.UIManager

/**
 * @program: DazoTool
 * @description: GUI界面入口
 * @author: Aye10032
 * @create: 2021-01-02 15:07
 */
class MainGui(guiModel: GuiModel) {

    var tray: SystemTray? = null
        private set
    val window = MainWindow(guiModel)

    private val popmenu: PopupMenu = PopupMenu().also {
        val font = Font("Default", Font.PLAIN, 14)
        val itmExit = MenuItem("退出")
        itmExit.font = font
        val itmOpenConfig = MenuItem("打开控制台")
        itmOpenConfig.font = font
        val itmAutoStart = MenuItem("\u5f00\u673a\u81ea\u542f  " + if (Shortcut.isAutoStart) "\u2713" else "\u2717")
        val appsMenu1 = getAppsMenu()
        itmAutoStart.font = font
        itmExit.addActionListener { guiModel.exit() }
        itmOpenConfig.addActionListener { window.isVisible = !window.isVisible }
        itmAutoStart.addActionListener { Shortcut.run(this) }
        it.add(itmOpenConfig)
        it.add(itmAutoStart)
        it.add(appsMenu1)
        it.add(itmExit)
    }

    private fun setTray() {
        if (SystemTray.isSupported()) {
            this.tray = SystemTray.getSystemTray()
            val text = "DazoTools"
            val trayIcon = TrayIcon(ICON, text, this.popmenu)
            trayIcon.addMouseListener(object : MouseListener {
                override fun mouseExited(e: MouseEvent?) {
                }

                override fun mouseReleased(e: MouseEvent?) {
                }

                override fun mouseEntered(e: MouseEvent?) {

                }

                override fun mouseClicked(e: MouseEvent?) {
                    when (e?.button) {
                        1 -> window.isVisible = true
                    }
                }

                override fun mousePressed(e: MouseEvent?) {
                    when (e?.button) {
                        3 -> {
                            this@MainGui.popmenu.getItem(1).label = getShortcutLabel()
                            this@MainGui.popmenu.remove(this@MainGui.popmenu.getItem(2))
                            this@MainGui.popmenu.insert(getAppsMenu(), 2)
                        }
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

    private fun getAppsMenu(): PopupMenu {
        return PopupMenu().also {
            it.label = "应用列表"
            it.name = "应用列表"
            val font = Font("Default", Font.PLAIN, 14)
            it.font = font
            for (app in AppsManager.instance.apps) {
                it.add(PopupMenu(app.name).also {
                    it.font = font
                    it.add(MenuItem("重启").also {
                        it.font = font
                        it.addActionListener {
                            GlobalScope.async {
                                app.reStart()
                            }
                        }
                    })
                    if (app.isAble.get()) {
                        it.add(MenuItem("禁用").also {
                            it.font = font
                            it.addActionListener { app.disable() }
                        })
                    } else {
                        it.add(MenuItem("启用").also {
                            it.font = font
                            it.addActionListener { app.enable() }
                        })
                    }
                })
            }
        }
    }

    init {
        this.setTray()
        try {
            UIManager.setLookAndFeel(FlatLightLaf())
            UIManager.put("Button.arc", 6)
            UIManager.put("Component.arc", 6)
            UIManager.put("CheckBox.arc", 6)
            UIManager.put("ProgressBar.arc", 6)
            UIManager.put("CheckBox.icon.style", "filled")
            UIManager.put("Component.arrowType", "triangle")
            UIManager.put("Component.focusWidth", 2)
            UIManager.put("ScrollBar.showButtons", true)
            UIManager.put("ScrollBar.width", 16)
            UIManager.put("ScrollBar.thumbArc", 6)
            UIManager.put("ScrollBar.thumbInsets", Insets(2, 2, 2, 2))
        } catch (ex: Exception) {
            System.err.println("Failed to initialize LaF")
        }

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val screenWidth = screenSize.width
        val screenHeight = screenSize.height
        val windowWidth = 600
        val windowHeight = 500

        window.title = "DazoToolBox"
        window.iconImage = ICON
        window.setBounds((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2, windowWidth, windowHeight)
        window.isVisible = true

    }

    private fun flushGui() {

    }

    companion object {

        private val log = Logger.getLogger(MainGui::class.java)
        private val ICON = Toolkit.getDefaultToolkit().getImage(MainGui::class.java.getResource("/res/gui/tray_logo.jpg"))
        fun getShortcutLabel(): String =
                "\u5f00\u673a\u81ea\u542f  " +
                        if (Shortcut.isAutoStart) "\u2713" else "\u2717"

    }

}
