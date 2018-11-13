package com.cw.filesystem.viewer;

import java.awt.Color;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.cw.filesystem.util.FileSystemUtil;

//新建文件夹还是文件类
public class MyJLabel extends JLabel {
    public boolean type = false;
    public MyJLabel(boolean isFile,String content){
        this.setVerticalTextPosition(JLabel.BOTTOM);
        this.setHorizontalTextPosition(JLabel.CENTER);
        this.setText(content);
        if(isFile){
            this.setIcon(new ImageIcon(getClass().getResource(FileSystemUtil.filePath)));
            this.type = true;
        }else{
            this.setIcon(new ImageIcon(getClass().getResource(FileSystemUtil.folderPath)));
            this.type = false;
        }
    }
    public boolean isType(){
        return type;
    }
    public void setType(boolean type){
        this.type = type;
    }
}