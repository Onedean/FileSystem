package com.cw.filesystem.viewer;

public class Goods {
    String name;
    double price;
    //构造方法
    Goods(String n,double p){
        name = n;
        price =p;
    }

    @Override
    //重写返回字符串类型
    public String toString() {
        return name;
    }
}
