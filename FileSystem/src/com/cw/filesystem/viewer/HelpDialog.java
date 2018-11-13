package com.cw.filesystem.viewer;

import java.awt.Component;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.cw.filesystem.model.FAT;

public class HelpDialog extends JDialog {
    private JDialog jdHD;
    private JLabel jlHD;
    public HelpDialog(Component c){
        jdHD = this;
        this.setTitle("帮助");
        this.setSize(280,330);
        this.setLayout(new FlowLayout());
        //将对话框设置为标准型（在没被关闭之前不可以进行其他操作）
        this.setModal(true);
        //设置窗口相对于指定组件的位置,如果组件当前未显示或者c为null,则此窗口将置于屏幕的中央
        this.setLocationRelativeTo(c);
        this.addJLabel();
        this.setVisible(true);
    }
    private void addJLabel(){
        jlHD = new JLabel();
        String text = "<html>帮助：<br><br>中间的面板点击右键：<br><1>、创建文件夹<br><2>、创建文件<br><br>" +
                "文件或文件夹点击右键：<br><1>、打开<br><2>、重命名<br><3>、删除<br><4>、属性<br><br>" +
                "双击文件或文件夹则是打开</html>";
        jlHD.setText(text);
        this.add(jlHD);
    }
}