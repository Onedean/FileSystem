package com.cw.filesystem.util;

public class FileSystemUtil {
    //设置文件、文件夹图片地址
    public static String filePath = "images/file.png";
    public static String folderPath = "images/folder.png";
    //设置最大同时打开文件数
    public static int num = 5;
    //设置静态变量
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

}
