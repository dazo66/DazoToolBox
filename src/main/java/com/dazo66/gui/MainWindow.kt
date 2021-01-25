package com.dazo66.gui

import com.aye10032.util.LayoutUtil
import com.dazo66.DazoTools
import com.dazo66.utils.app.AppDiscover
import com.dazo66.utils.app.AppsManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.log4j.Logger

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener


/**
 * @program: DazoTool
 * @description: 主页面
 * @author: Aye10032
 * @create: 2021-01-02 15:08
 */
open class MainWindow(
    val model: GuiModel
) : JFrame(), ActionListener {

    private val menuBar = JMenuBar()
    private val menu_file = JMenu("文件")
    private val item_exit = JMenuItem("退出")

    private val checkBox1 = JCheckBox("保持最新")
    private val checkBox2 = JCheckBox("文本2")
    private val comboBox1 = JComboBox<String>()
    private val comboBox2 = JComboBox<String>()
    private val textArea = JTextArea()
    private val scrollPane = JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED)
    private val logger = Logger.getLogger(DazoTools.TEXT_AREA_LOGGER)

    companion object {
        private const val INFO = "INFO"
        private const val ERROR = "ERROR"
    }

    init {
        val panel_top = JPanel()
        panel_top.border = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 5, 10),
                BorderFactory.createMatteBorder(0, 0, 0, 0, Color.GRAY))
        panel_top.layout = GridBagLayout()

        textArea.isEditable = false
        scrollPane.autoscrolls = true

        LayoutUtil.add(panel_top, GridBagConstraints.NONE, GridBagConstraints.WEST,
                2, 1, 0, 0, 2, 1, checkBox1, Insets(10, 10, 10, 0))
        LayoutUtil.add(panel_top, GridBagConstraints.NONE, GridBagConstraints.WEST,
                2, 1, 2, 0, 2, 1, checkBox2, Insets(10, 0, 10, 0))
        LayoutUtil.add(panel_top, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER,
                6, 1, 4, 0, 6, 1, comboBox1, Insets(10, 0, 10, 10))
        LayoutUtil.add(panel_top, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER,
                6, 1, 10, 0, 6, 1, comboBox2, Insets(10, 0, 10, 10))
        contentPane.add(panel_top, BorderLayout.NORTH)

        val panel_bottom = JPanel()
        panel_bottom.border = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY))
        panel_bottom.layout = GridBagLayout()

        LayoutUtil.add(panel_bottom, GridBagConstraints.BOTH, GridBagConstraints.CENTER,
                1, 6, 0, 1, 16, 1, scrollPane, Insets(20, 10, 10, 10))
        contentPane.add(panel_bottom, BorderLayout.CENTER)

        //        菜单栏

        menu_file.add(item_exit)
        menuBar.add(menu_file)

        jMenuBar = menuBar
        flushGui()
        initLog()
        logger.info(TextAreaLogAppender.FLAG)
        checkBox1.addActionListener(this)
        checkBox2.addActionListener(this)
        item_exit.addActionListener(this)
        item_exit.addActionListener(this)
        comboBox1.addActionListener(this)
        comboBox2.addActionListener(this)
        defaultCloseOperation = HIDE_ON_CLOSE
        checkBox2.isVisible = false

    }

    private fun flushGui() {

        checkBox1.isSelected = model.data.autoFlush
        checkBox2.isSelected = model.data.autoSum
        if (comboBox1.itemCount == 0) {
            comboBox1.addItem("")
            comboBox1.addItem(INFO)
            comboBox1.addItem(ERROR)
        }
        if (AppsManager.instance.apps.size != comboBox2.itemCount - 1) {
            comboBox2.removeAllItems()
            comboBox2.addItem("")
            AppsManager.instance.apps.forEach {
                comboBox2.addItem(it.name)
            }
        }
        if (model.data.logLevel == INFO) {
            comboBox1.selectedItem = INFO
        } else if (model.data.logLevel == ERROR) {
            comboBox1.selectedItem = ERROR
        }
        comboBox2.selectedItem = model.data.appName
    }

    private fun initLog() {
        try {
            GlobalScope.launch {
                TextAreaLogAppender(model, textArea, scrollPane).run()
            }
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, "重定向错误")
        }
    }

    override fun actionPerformed(e: ActionEvent) {
        val source = e.source

        if (source === item_exit) {
            model.exit()
        } else if (source === checkBox1) {
            model.setautoFlush(checkBox1.isSelected)
            if (checkBox1.isSelected) {
                scrollPane.verticalScrollBar.value = Int.MAX_VALUE
            }
        } else if (source === checkBox2) {
            model.setAutoSum(checkBox2.isSelected)
        } else if (source == comboBox1) {
            model.setLogLevel(comboBox1.selectedItem as String)
            logger.info(TextAreaLogAppender.FLAG)
        } else if (source == comboBox2) {
            model.setAppName(comboBox2.selectedItem as String)
            logger.info(TextAreaLogAppender.FLAG)
        }
    }

}
