package com.cw.filesystem.util;

public class FileSystemUtil {

    //设置文件、文件夹图片地址,最大同时打开文件数
    public static int num = 5;
    public static String folderPath = "/images/folder.jpg";
    public static String folder1Path = "/images/folder1.jpg";
    public static String filePath = "/images/file.jpg";
    public static String file1Path = "/images/file1.jpg";
    public static String diskPath = "/images/disk.jpg";
    public static String imgPath = "/images/img1.jpg";

    //设置该文件夹或文件占用磁盘块结束标记
    public static int END = 255;
    //磁盘块标记为0
    public static int DISK = 0;
    //文件夹标记为1
    public static int FOLDER = 1;
    //文件标记为2
    public static int FILE = 2;
    //设置错误标记为-1
    public static int ERROR = -1;
    //设置文件可读和可写静态变量
    public static int flagRead = 0;
    public static int flagWrite = 1;

    //动态地根据JLabel来设置JPanel的height
    public static int getHeight(int n){
        int a = n / 4;
        if(n%4 > 0){
            a++;
        }
        return a*120;
    }
    //每一次保存时都算出文件分配表的长度
    public static int getNumOfFAT(int length){
        //如果文件中内容长度小于等于64位按1KB算
        if(length<=64){
            return 1;
        }else{
            //否则长度按每满64位1KB，多出的多加1KB长度算
            int n=0;
            if(length%64 == 0){
                n = length / 64;
            }else{
                n = length / 64;
                n++;
            }
            return n;
        }
    }
}
