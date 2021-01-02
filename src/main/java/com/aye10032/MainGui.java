package com.aye10032;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @program: DazoTool
 * @description: GUI界面入口
 * @author: Aye10032
 * @create: 2021-01-02 15:07
 **/
public class MainGui {

    public static void main(String[] args) {
        new MainGui();
    }

    public MainGui() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 6);
            UIManager.put("Component.arc", 6);
            UIManager.put("CheckBox.arc", 6);
            UIManager.put("ProgressBar.arc", 6);
            UIManager.put("CheckBox.icon.style", "filled");
            UIManager.put("Component.arrowType", "triangle");
            UIManager.put("Component.focusWidth", 2);
            UIManager.put("ScrollBar.showButtons", true);
            UIManager.put("ScrollBar.width", 16);
            UIManager.put("ScrollBar.thumbArc", 6);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int windowWidth = 600;
        int windowHeight = 500;

        MainWindow window = new MainWindow();
        window.setTitle("DazoToolBox");
        Image icon = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/res/gui/tray_logo.jpg").getFile());
        window.setIconImage(icon);
        window.setBounds((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2, windowWidth, windowHeight);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

}
