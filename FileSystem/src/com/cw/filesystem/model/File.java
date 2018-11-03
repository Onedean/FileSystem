package com.cw.filesystem.model;

import java.text.SimpleDateFormat;
import java.util.Date;
//文件类
public class File {
    //文件的目录项
    private String fileName;      //文件名，3个字节
    private String type;          //文件类型，2个字节
    private String property;      //文件属性，1个字节
    private int diskNum;          //文件起始盘符号，1个字节
    private int length;           //文件长度，1个字节
    private String content;       //文件内容

    private int numOfFAT;          //在文件分配表中位置
    private Folder parent;        //所属父类文件夹

    //文件查看的属性
    private String location;      //文件路径
    private String size;          //文件大小
    private String space;         //文件占用空间
    private Date createTime;      //文件创建时间

    private boolean isReadOnly;   //是否只可读
    private boolean isHide;       //是否隐藏

    public File(String fileName){
        super();
        this.fileName = fileName;
    }

    public File(String fileName,String location,int diskNum){
        super();
        this.fileName = fileName;
        this.location = location;
        this.diskNum = diskNum;
        this.length = 8;
        this.content = "";
        this.size = "100KB";
        this.space = "100KB";
        this.createTime = new Date();
        this.type = "File";
        this.isReadOnly = false;
        this.isHide = false;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getCreateTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        return format.format(createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
        return fileName;
    }
}
