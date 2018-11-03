package com.cw.filesystem.servicw;

import com.cw.filesystem.model.*;
import com.cw.filesystem.util.FileSystemUtil;
import com.cw.filesystem.util.MessageUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//FAT文件分配表服务类
public class FATService {
    //创建FAT文件分配表对象数组
    private FAT[] myFAT;
    //创建打开文件夹对象
    private OpenFiles openFiles;
    //构造方法????????????????????????????????????????
    public FATService(){
        openFiles = new OpenFiles();
    }
    //初始化文件分配表
    public void initFAT(){
        //磁盘块有128块,所以设置文件分配表有128项
        myFAT = new FAT[128];
        //文件分配表第0,1项分别存储null对象和系统C盘
        myFAT[0] = new FAT(FileSystemUtil.END,FileSystemUtil.DISK,null);
        myFAT[1] = new FAT(FileSystemUtil.END,FileSystemUtil.DISK,new Disk("C"));
    }
    //创建新文件夹，并返回所在磁盘块号索引
    public int createFolder(String path){
        String folderName = null;
        boolean canName = true;
        int index1 = 1;
        //添加不重复的新建文件夹名
        do{
            folderName = "新建文件夹";
            canName = true;
            folderName += index1;
            for(int i=0;i<myFAT.length;i++){
                if(myFAT[i]!=null){
                    if(myFAT[i].getType() == FileSystemUtil.FOLDER){
                        Folder folder = (Folder)myFAT[i].getObject();
                        //判断当前路径下是否出现文件夹重名现象来决定新建文件夹名
                        if(path.equals(folder.getLocation())){
                            if(folderName.equals(folder.getFolderName())){
                                canName = false;
                            }
                        }
                    }
                }
            }
            index1++;
        }while(!canName);
        //在myFAT中添加文件夹
        //顺序查找第一个为空的磁盘块号安置新建文件夹
        int index2 = searchEmptyFromMyFAT();
        if (index2 == FileSystemUtil.ERROR)
            return FileSystemUtil.ERROR;
        else{
            Folder folder = new Folder(folderName,path,index2);
            myFAT[index2] = new FAT(FileSystemUtil.END,FileSystemUtil.FOLDER,folder);
        }
        return index2;
    }
    //创建新文件，并返回所在磁盘块索引号
    public int createFile(String path){
        String fileName = null;
        boolean canName = true;
        int index1 = 1;
        do{
            fileName = "新建文件";
            canName = true;
            fileName += index1;
            for(int i=0;i<myFAT.length;i++){
                if(myFAT[i]!=null){
                    if(myFAT[i].getType() == FileSystemUtil.FILE){
                        File file = (File)(myFAT[i].getObject());
                        if(path.equals(file.getLocation())){
                            if(fileName == file.getFileName()){
                                canName = false;
                            }
                        }
                    }
                }
            }
            index1++;
        }while(!canName);
        //在myFAT表中添加文件
        int index2 = searchEmptyFromMyFAT();
        if(index2 == FileSystemUtil.ERROR)
            return FileSystemUtil.ERROR;
        else{
            File file = new File(fileName,path,index2);
            myFAT[index2] = new FAT(index2,FileSystemUtil.FILE,file);
        }
        return index2;
    }
    //顺序查找myFAT中第一个为空的磁盘块索引
    public int searchEmptyFromMyFAT(){
        for(int i = 2;i<myFAT.length;i++){
            if(myFAT[i] == null)
                return i;
        }
        return FileSystemUtil.ERROR;
    }
    /*删除文件/文件夹
     param 面板,文件分配表,树?????????????????????????????????
     */
    public void deletFiles(JPanel jp1, FAT fat, Map<String, DefaultMutableTreeNode> map){
        if(fat.getType() == FileSystemUtil.FILE){
            //判断删除的是文件
            //其次判断当前文件是否正在打开，若打开则不能删除发出提示
            for(int i=0;i<openFiles.getFiles().size();i++){
                if(openFiles.getFiles().get(i).getFile().equals(fat.getObject())){
                    MessageUtil.showErrorMessages(jp1,"文件正在打开着，不能删除");
                    return;
                }
            }
            //删除文件
            for(int i=0;i<myFAT.length;i++){
                if(myFAT[i] != null && myFAT[i].getType() == FileSystemUtil.FILE){
                    if(((File)myFAT[i].getObject()).equals((File)fat.getObject())){
                        myFAT[i]=null;
                        System.out.println("----------------------->删除");
                    }
                }
            }
        }else{
            //否则删除的是文件夹
            String path = ((Folder)fat.getObject()).getLocation();  //得到不包含文件夹名的绝对路径
            String folderPath = ((Folder)fat.getObject()).getCreatTime()+"\\"+((Folder)fat.getObject()).getFolderName();  //得到包含文件夹名的绝对路径
            int index = 0;
            for(int i=2;i<myFAT.length;i++){
                if(myFAT[i]!=null){
                    Object obj = myFAT[i].getObject();
                    if(myFAT[i].getType() == FileSystemUtil.FOLDER) {
                        if (((Folder) obj).getLocation().equals(folderPath)) {
                            MessageUtil.showErrorMessages(jp1, "文件夹不为空，不能删除");
                            return;
                        }
                    }else {
                        if(((File)obj).getLocation().equals(folderPath)){
                            MessageUtil.showErrorMessages(jp1,"文件夹不为空，不能删除");
                            return;
                        }
                    }
                    if(myFAT[i].getType() == FileSystemUtil.FOLDER){
                        if(((Folder)myFAT[i].getObject()).equals((Folder)fat.getObject())){
                            index = i;
                        }
                    }
                }
            }
            myFAT[index] = null;
            //移除树上的结点?????????????????????????????????????????
            DefaultMutableTreeNode parentNode = map.get(path);
            parentNode.remove(map.get(folderPath));
            map.remove(folderPath);
        }
    }
    //返回文件分配表集合
    public List<FAT> getFATs(String path){
        //创建文件分配表的动态数组
        List<FAT> fats = new ArrayList<FAT>();
        for(int i=0;i<myFAT.length;i++){
            if(myFAT[i] != null){
                if(myFAT[i].getObject() instanceof Folder){
                    if((((Folder) myFAT[i].getObject()).getLocation()).equals(path) &&  myFAT[i].getIndex() == FileSystemUtil.END){
                        fats.add(myFAT[i]);
                    }
                }
                if(myFAT[i].getObject() instanceof File){
                    if((((File)myFAT[i].getObject()).getLocation()).equals(path) && myFAT[i].getIndex() == FileSystemUtil.END){
                        fats.add(myFAT[i]);
                    }
                }
            }
        }
        return fats;
    }

}