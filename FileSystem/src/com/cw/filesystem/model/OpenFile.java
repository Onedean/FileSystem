package com.cw.filesystem.model;

import java.util.List;

//已打开文件类
public class OpenFile {
    //设置打开方式，0以读打开，1以写打开
    private int flag;
    private Pointer read;
    private Pointer write;
    private File file;
    public int getFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public Pointer getRead() {
        return read;
    }
    public void setRead(Pointer read) {
        this.read = read;
    }
    public Pointer getWrite() {
        return write;
    }
    public void setWrite(Pointer write) {
        this.write = write;
    }
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
}
