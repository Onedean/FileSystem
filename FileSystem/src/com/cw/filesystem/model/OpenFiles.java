package com.cw.filesystem.model;

import com.cw.filesystem.util.FileSystemUtil;

import java.util.ArrayList;
import java.util.List;

//已打开文件夹类
public class OpenFiles {
    //声明打开文件集合
    private List<OpenFile> files;
    private int length;

    public OpenFiles(){
        //创建打开文件集合对象（最大同时打开5个文件）
        files = new ArrayList<OpenFile>(FileSystemUtil.num);
        length = 0;
    }

    //往打开文件夹类集合中增加的新的打开文件对象
    public void addFile(OpenFile openFile){
        files.add(openFile);
    }

    //从打开文件夹类集合中移除已打开文件对象
    public void removeFile(OpenFile openFile){
        files.remove(openFile);
    }

    //获取打开文件夹类集合中存放的打开文件对象
    public List<OpenFile> getFiles() {
        return files;
    }

    public void setFiles(List<OpenFile> files) {
        this.files = files;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
