package com.cw.filesystem.model;

//磁盘类
public class Disk {
    private String diskName;
    public Disk(String diskName){
        super();
        this.diskName = diskName;
    }
    public String getDiskName(){
        return diskName;
    }
    public void setDiskName(String diskName){
        this.diskName = diskName;
    }
    //重写构造函数
    @Override
    public String toString() {
        return diskName;
    }
}
