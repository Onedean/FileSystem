package com.cw.filesystem.viewer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.cw.filesystem.model.FAT;
import com.cw.filesystem.model.File;
import com.cw.filesystem.model.Folder;
import com.cw.filesystem.util.FileSystemUtil;

public class ShowPropertyDialog extends JDialog {

    private JPanel jp1;
    private JLabel jl1, jl2, jl3, jl4, jl5, jl6;
    private FAT fat;
    private JButton jb1, jb2, jb3;
    private JRadioButton jrb1, jrb2;
    private boolean isFile = false;
    private JDialog jd1;

    public ShowPropertyDialog(Component c, FAT f){
        jd1 = this;
        this.fat = f;
        this.init();
        this.setTitle("属性");
        this.setSize(280, 330);
        this.setLayout(new FlowLayout());
        this.setModal(true);
        this.setLocationRelativeTo(c);
        this.setVisible(true);
    }

    private void init(){
        if (fat.getType()==FileSystemUtil.FOLDER){
            Folder folder =  (Folder)(fat.getObject());
            jl1 = new JLabel("名字 :                        " + folder.getFolderName());
            jl2 = new JLabel("类型 :                        " + folder.getType());
            jl3 = new JLabel("路径 :                        " + folder.getLocation());
            jl4 = new JLabel("占用空间大小:           " + folder.getSize());
            jl5 = new JLabel("创建日期: " + folder.getCreateTime());
            jl6 = new JLabel("属性");
            jrb1 = new JRadioButton("只读");
            jrb2 = new JRadioButton("隐藏");
            if (folder.isReadOnly()){
                jrb1.setSelected(true);
            }
            if (folder.isHide()){
                jrb2.setSelected(true);
            }
        } else if (fat.getType()==FileSystemUtil.FILE){
            isFile = true;
            File file =  (File)(fat.getObject());
            jl1 = new JLabel("名字 :                        " + file.getFileName());
            jl2 = new JLabel("类型 :                        " + file.getType());
            jl3 = new JLabel("路径 :                        " + file.getLocation() );
            jl4 = new JLabel("占用空间大小:            " + file.getSize());
            jl5 = new JLabel("创建日期：" + file.getCreateTime());
            jl6 = new JLabel("属性");
            jrb1 = new JRadioButton("只读");
            jrb2 = new JRadioButton("隐藏");
            if (file.isReadOnly()){
                jrb1.setSelected(true);
            }
            if (file.isHide()){
                jrb2.setSelected(true);
            }
        }


        jl1.setPreferredSize(new Dimension(230, 40));
        jl2.setPreferredSize(new Dimension(230, 40));
        jl3.setPreferredSize(new Dimension(230, 40));
        jl4.setPreferredSize(new Dimension(230, 40));
        jl5.setPreferredSize(new Dimension(230, 40));
        this.add(jl1);
        this.add(jl2);
        this.add(jl3);
        this.add(jl4);
        this.add(jl5);

        this.add(jl6);
        this.add(jrb1);
        this.add(jrb2);

        jp1 = new JPanel();
        jb1 = new JButton("确定");
        jb2 = new JButton("取消");
        jb3 = new JButton("应用");
        jp1.add(jb1);
        jp1.add(jb2);
        jp1.add(jb3);
        this.add(jp1);


        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jrb1.isSelected()){
                    if (isFile){
                        ((File)(fat.getObject())).setReadOnly(true);
                    } else {
                        ((Folder)(fat.getObject())).setReadOnly(true);
                    }
                } else {
                    if (isFile){
                        ((File)(fat.getObject())).setReadOnly(false);
                    } else {
                        ((Folder)(fat.getObject())).setReadOnly(false);
                    }
                }

                if (jrb2.isSelected()){
                    if (isFile){
                        ((File)(fat.getObject())).setHide(true);
                    } else {
                        ((Folder)(fat.getObject())).setHide(true);
                    }
                } else {
                    if (isFile){
                        ((File)(fat.getObject())).setHide(false);
                    } else {
                        ((Folder)(fat.getObject())).setHide(false);
                    }
                }

                jd1.setVisible(false);
            }
        });

        jb2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jd1.setVisible(false);
            }
        });

        jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jrb1.isSelected()){
                    if (isFile){
                        ((File)(fat.getObject())).setReadOnly(true);
                    } else {
                        ((Folder)(fat.getObject())).setReadOnly(true);
                    }
                } else {
                    if (isFile){
                        ((File)(fat.getObject())).setReadOnly(false);
                    } else {
                        ((Folder)(fat.getObject())).setReadOnly(false);
                    }
                }

                if (jrb2.isSelected()){
                    if (isFile){
                        ((File)(fat.getObject())).setHide(true);
                    } else {
                        ((Folder)(fat.getObject())).setHide(true);
                    }
                } else {
                    if (isFile){
                        ((File)(fat.getObject())).setHide(false);
                    } else {
                        ((Folder)(fat.getObject())).setHide(false);
                    }
                }

            }
        });
    }
}