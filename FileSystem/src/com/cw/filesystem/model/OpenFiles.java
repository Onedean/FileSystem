package com.cw.filesystem.model;

import com.cw.filesystem.util.FileSystemUtil;

import java.util.ArrayList;
import java.util.List;

//已打开文件夹类
public class OpenFiles {
    //创建OpenFile类集合链表
    private List<OpenFile> files;
    private int length;

    public OpenFiles(){
        files = new ArrayList<OpenFile>(FileSystemUtil.num);
        length = 0;
    }
    public void addFile(OpenFile openFile){
        files.add(openFile);
    }
    public void removeFile(OpenFile openFile){
        files.remove(openFile);
    }
    public List<OpenFile> getFiles(){
        return files;
    }
    public void setFiles(List<OpenFile> files){
        this.files = files;
    }
    public int getLength(){
        return length;
    }
    public void setLength(int length){
        this.length = length;
    }
}
