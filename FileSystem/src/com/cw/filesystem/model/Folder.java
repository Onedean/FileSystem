package com.cw.filesystem.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;

//文件夹类
public class Folder {
    //文件夹的目录项
    private String folderName;  //文件夹名，3个字节
    private String type;        //文件夹类型，1个字节
    private String property;    //文件夹属性，1个字节
    private int diskNum;

    private boolean hasChild;
    private int numOfFAT;
    //查看的属性
    private String location;
    private String size;
    private String space;
    private Date createTime;

    private boolean isReadOnly;
    private boolean isHide;
    //构造方法一
    public Folder(String folderName){
        super();
        this.folderName = folderName;
    }
    //构造方法二，创建新建文件夹
    public Folder(String folderName,String location,int diskNum){
        super();
        this.folderName = folderName;
        this.location = location;
        this.diskNum = diskNum;
        this.type = "Folder";
        this.size = "100KB";
        this.space = "100KB";
        this.createTime = new Date();
        this.isReadOnly = false;
        this.isHide = false;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public int getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(int diskNum) {
        this.diskNum = diskNum;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public int getNumOfFAT() {
        return numOfFAT;
    }

    public void setNumOfFAT(int numOfFAT) {
        this.numOfFAT = numOfFAT;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getCreateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        return format.format(createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean isHide) {
        this.isHide = isHide;
    }

    @Override
    public String toString() {
        return folderName;
    }
}
