package com.cw.filesystem.model;

//文件分配表类
public class FAT {
    //磁盘块
    private int index;
    //该磁盘块中存放的是文件还是文件夹类型
    private int type;
    //文件还是文件夹
    private Object object;
    //重写构造方法，文件分配表分别存放磁盘块、文件或文件夹类型、文件或文件夹
    public FAT(int index,int type,Object object){
        //调用父类的构造函数
        super();
        this.index = index;
        this.type = type;
        this.object = object;
    }
    public int getIndex(){
        return index;
    }
    public void setIndex(int index){
        this.index = index;
    }
    public int getType(){
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Object getObject(){
        return object;
    }
    public void setObject(Object object){
        this.object = object;
    }
}
