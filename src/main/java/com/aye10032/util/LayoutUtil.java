package com.aye10032.util;

import java.awt.*;

/**
 * @program: DazoTool
 * @description: 布局工具类
 * @author: Aye10032
 * @create: 2021-01-02 17:03
 **/
public class LayoutUtil {

    /**
     * @param fill 网格的填充方式，有纵向和横向扩展
     * @param anchor 组件在网格中的填充方式，居中、纵向对齐等
     * @param weightx 与 weighty 一起，设定当前网格在本行（列）所占的权重
     * @param x 与 y 一起，代表当前控件位于第几行第几列
     * @param width 与 height 一起，表示当前控件占几格
     **/
    public static void add(Container container, int fill, int anchor, int weightx, int weighty, int x, int y, int width, int height, Component component) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = fill;//填充方式
        constraints.anchor = anchor;//组件比网格小时在网格中的填塞方式
        constraints.weightx = weightx;//与fill字段配合使用
        constraints.weighty = weighty;
        constraints.gridx = x;//起始的网格坐标
        constraints.gridy = y;
        constraints.gridwidth = width;//所占的格数
        constraints.gridheight = height;
        container.add(component, constraints);
    }

    public static void add(Container container, int fill, int anchor, int weightx, int weighty, int x, int y, int width, int height, Component component, Insets insets) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = insets;//外部边界
        constraints.fill = fill;
        constraints.anchor = anchor;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        container.add(component, constraints);
    }

}
