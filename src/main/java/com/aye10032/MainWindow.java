package com.aye10032;

import com.aye10032.util.LayoutUtil;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @program: DazoTool
 * @description: 主页面
 * @author: Aye10032
 * @create: 2021-01-02 15:08
 **/
public class MainWindow extends JFrame implements ActionListener {

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menu_file = new JMenu("文件");
    private JMenuItem item_exit = new JMenuItem("退出");

    private JCheckBox checkBox1 = new JCheckBox("text1");
    private JCheckBox checkBox2 = new JCheckBox("文本2");
    private JComboBox comboBox1 = new JComboBox();
    private JComboBox comboBox2 = new JComboBox();
    private JTextArea textArea = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    public MainWindow() {
        JPanel panel_top = new JPanel();
        panel_top.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 5, 10),
                BorderFactory.createMatteBorder(0, 0, 0, 0, Color.GRAY)));
        panel_top.setLayout(new GridBagLayout());

        textArea.setEditable(false);
        scrollPane.setAutoscrolls(true);

        LayoutUtil.add(panel_top, GridBagConstraints.NONE, GridBagConstraints.WEST,
                2, 1, 0, 0, 2, 1, checkBox1, new Insets(10, 10, 10, 0));
        LayoutUtil.add(panel_top, GridBagConstraints.NONE, GridBagConstraints.WEST,
                2, 1, 2, 0, 2, 1, checkBox2, new Insets(10, 0, 10, 0));
        LayoutUtil.add(panel_top, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER,
                6, 1, 4, 0, 6, 1, comboBox1, new Insets(10, 0, 10, 10));
        LayoutUtil.add(panel_top, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER,
                6, 1, 10, 0, 6, 1, comboBox2, new Insets(10, 0, 10, 10));
        getContentPane().add(panel_top, BorderLayout.NORTH);

        JPanel panel_bottom = new JPanel();
        panel_bottom.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY)));
        panel_bottom.setLayout(new GridBagLayout());

        LayoutUtil.add(panel_bottom, GridBagConstraints.BOTH, GridBagConstraints.CENTER,
                1, 6, 0, 1, 16, 1, scrollPane, new Insets(20, 10, 10, 10));
        getContentPane().add(panel_bottom, BorderLayout.CENTER);

//        菜单栏

        menu_file.add(item_exit);
        menuBar.add(menu_file);

        setJMenuBar(menuBar);

        checkBox1.addActionListener(this);
        checkBox2.addActionListener(this);
        item_exit.addActionListener(this);
        item_exit.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == item_exit) {
            System.exit(0);
        } else if (source == checkBox1) {
            textArea.append("复选框1当前值为" + checkBox1.isSelected() + "\n");
        } else if (source == checkBox2) {
            textArea.append("复选框2当前值为" + checkBox2.isSelected() + "\n");
        }
    }
}
