package com.cw.filesystem.viewer;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.image.BufferStrategy;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class JieShao extends JDialog {
    private JDialog jdJS;
    private JLabel jlJS;
    public JieShao(Component c){
        jdJS = this;
        this.setTitle("介绍");
        this.setSize(280,200);
        this.setLayout(new FlowLayout());
        this.setModal(true);
        this.setLocationRelativeTo(c);
        this.addJLabel();
        this.setVisible(true);
    }
    private void addJLabel(){
        jlJS = new JLabel();
        String text = "<html><br><br>设计产品：<br>模拟磁盘文件系统<br><br>设计人员：<br>陈伟、程俊涛、廖洪园纯、岑安会</htnl>";
        jlJS.setText(text);
        this.add(jlJS);
    }
}
