package com.cw.filesystem.service;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.cw.filesystem.model.Disk;
import com.cw.filesystem.model.FAT;
import com.cw.filesystem.model.File;
import com.cw.filesystem.model.Folder;
import com.cw.filesystem.model.OpenFile;
import com.cw.filesystem.model.OpenFiles;
import com.cw.filesystem.util.FileSystemUtil;
import com.cw.filesystem.util.MessageUtil;

public class FATService {
    //创建FAT文件分配表类对象数组
    private static FAT[] myFAT;
    //创建打开文件夹对象
    private static OpenFiles openFiles;

    //构造方法
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

    //将以读或写方式的打开文件添加到打开文件集合中
    public void addOpenFile(FAT fat,int flag){
        //创建打开文件类对象并将文件分配表中传来的文件设置打开方式传入
        OpenFile openFile = new OpenFile();
        openFile.setFile((File) fat.getObject());
        openFile.setFlag(flag);
        //已打开文件集合添加刚刚创建的打开的文件
        openFiles.addFile(openFile);
    }

    //移除打开文件集合中的文件分配表传入的该文件
    public void removeOpenFile(FAT fat){
        for(int i=0;i<openFiles.getFiles().size();i++){
            //通过（打开文件夹对象的实例变量集合获取打开文件）与（文件分配表中存储的文件）对比是否相同判断是否移除
            if(openFiles.getFiles().get(i).getFile() == (File) fat.getObject()){
                openFiles.getFiles().remove(i);
            }
        }
    }

    //判断传入的文件分配表中文件是否已经打开
    public boolean checkOpenFile(FAT fat){
        for(int i=0;i<openFiles.getFiles().size();i++){
            //通过（打开文件夹对象的实例变量集合获取打开文件）与（文件分配表中存储的文件）对比是否相同判断是否打开
            if(openFiles.getFiles().get(i).getFile() == (File) fat.getObject()){
                return true;
            }
        }
        return false;
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
                            if (fileName.equals(file.getFileName())) {
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
            myFAT[index2] = new FAT(FileSystemUtil.END,FileSystemUtil.FILE,file);
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


    //得到空的磁盘块（文件分配表）数量
    public int getSpaceOfFAT(){
        int n = 0;
        for(int i=2;i<myFAT.length;i++){
            if(myFAT[i] == null){
                n++;
            }
        }
        return n;
    }

    //打开文件关闭时保存数据重新分配磁盘
    public boolean saveToModifyFATS(Component parent, int num, FAT fat){
        ////获取文件分配表对象中该打开文件起始盘符号,从哪片磁盘开始
        int begin = ((File)fat.getObject()).getDiskNum();
        //声明文件分配表集合中该元素的磁盘块变量index
        int index = myFAT[begin].getIndex();
        int oldNum = 1;
        while (index != FileSystemUtil.END){
            oldNum ++;
            if (myFAT[index].getIndex() == FileSystemUtil.END){
                begin = index;
            }
            index = myFAT[index].getIndex();
        }

        //
        if (num > oldNum){
            //需要添加磁盘块
            //获取需要新保存文件所占用的磁盘块与之前所占用磁盘块差额
            int n = num - oldNum;
            //判断空余磁盘块数量（文件分配表）如果不够添加，则弹出提示错误窗口
            if (this.getSpaceOfFAT() < n){
                MessageUtil.showErrorMgs(parent, "保存的内容已经超过磁盘的容量");
                return false;
            }
            //得到myFAT中第一个为空的磁盘块索引
            int space = this.searchEmptyFromMyFAT();
            //设置该文件分配表集合中该文件所占空间
            myFAT[begin].setIndex(space);
            for (int i=1; i<=n; i++){
                space = this.searchEmptyFromMyFAT();
                if (i == n){
                    myFAT[space] = new FAT(255, FileSystemUtil.FILE, (File)fat.getObject());
                } else {
                    myFAT[space] = new FAT(20, FileSystemUtil.FILE, (File)fat.getObject());
                    int space2 = this.searchEmptyFromMyFAT();
                    myFAT[space].setIndex(space2);
                }
            }
            return true;
        } else {
            //不需要添加磁盘块
            return true;
        }
    }

    //获得与文件分配表中文件夹路径相等的文件夹集合
    public List<Folder> getFolders(String path){
        //创建文件夹集合的动态数组
        List<Folder> list1 = new ArrayList<Folder>();
        for (int i=0; i<myFAT.length; i++){
            if (myFAT[i] != null){
                //判断文件分配表中该位置是否是文件夹（实例是否属于该类）
                if (myFAT[i].getObject() instanceof Folder){
                    //判断文件分配表中该文件夹位置是否等于传入的路径
                    if (((Folder)(myFAT[i].getObject())).getLocation().equals(path)){
                        //往文件夹集合中添加文件分配表中该文件夹
                        list1.add((Folder)myFAT[i].getObject());
                    }
                }
            }
        }
        return list1;
    }

    //获得与文件分配表中文件路径相等的文件集合
    public List<File> getFiles(String path){
        //创建文件集合的动态数组
        List<File> list2 = new ArrayList<File>();
        for (int i=0; i<myFAT.length; i++){
            if (myFAT[i] != null){
                //判断文件分配表中该位置是否是文件（实例是否属于该类）
                if (myFAT[i].getObject() instanceof File){
                    //判断文件分配表中该文件位置是否等于传入的路径
                    if (((File)(myFAT[i].getObject())).getLocation().equals(path)){
                        //往文件集合中添加文件分配表中该文件
                        list2.add((File)myFAT[i].getObject());
                    }
                }
            }
        }
        return list2;
    }

    //返回文件分配表集合
    public List<FAT> getFATs(String path){
        //创建文件分配表集合动态数组
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

    //将文件分配表中旧的路径修改为新的路径
    public void modifyLocation(String oldPath, String newPath){
        for (int i=0; i<myFAT.length; i++){
            if (myFAT[i] != null){
                if (myFAT[i].getType()==FileSystemUtil.FILE){
                    //如果是文件分配表中的文件则判断文件配表集合下该文件的实际路径是否包含旧的路径，是的话用新的路径代替旧的路径
                    if (((File)myFAT[i].getObject()).getLocation().contains(oldPath)){
                        ((File)myFAT[i].getObject()).setLocation(((File)myFAT[i].getObject()).getLocation().replace(oldPath, newPath));
                    }
                } else if (myFAT[i].getType()==FileSystemUtil.FOLDER){
                    //如果是文件分配表中的文件夹则判断文件配表集合下该文件夹的实际路径是否包含旧的路径，是的话用新的路径代替旧的路径
                    if (((Folder)myFAT[i].getObject()).getLocation().contains(oldPath)){
                        ((Folder)myFAT[i].getObject()).setLocation(((Folder)myFAT[i].getObject()).getLocation().replace(oldPath, newPath));
                    }
                }
            }
        }
    }

    /*删除文件/文件夹
    param 面板,文件分配表,树
    */
    public void delete(JPanel jp1, FAT fat,	Map<String, DefaultMutableTreeNode> map) {
        if (fat.getType() == FileSystemUtil.FILE){
            //判断删除的是文件
            //其次判断当前文件是否正在打开，若打开则不能删除发出提示
            for (int i=0; i<openFiles.getFiles().size(); i++){
                if (openFiles.getFiles().get(i).getFile().equals(fat.getObject())){
                    MessageUtil.showErrorMgs(jp1, "文件正打开着，不能删除");
                    return ;
                }
            }
            //删除文件
            for (int i=0; i<myFAT.length; i++){
                if (myFAT[i] != null && myFAT[i].getType() == FileSystemUtil.FILE){
                    if (((File)myFAT[i].getObject()).equals((File)fat.getObject())){
                        myFAT[i] = null;
                    }
                }
            }

        } else {
            //否则删除的是文件夹
            //得到不包含文件夹名的绝对路径
            String path = ((Folder)fat.getObject()).getLocation();
            //得到包含文件夹名的绝对路径
            String folderPath = ((Folder)fat.getObject()).getLocation() + "\\" + ((Folder)fat.getObject()).getFolderName();
            int index = 0;
            for (int i=2; i<myFAT.length; i++){
                if (myFAT[i] != null){
                    Object  obj = myFAT[i].getObject();
                    if (myFAT[i].getType() == FileSystemUtil.FOLDER){
                        if (((Folder)obj).getLocation().equals(folderPath)){
                            MessageUtil.showErrorMgs(jp1, "文件夹不为空，不能删除");
                            return ;
                        }
                    } else {
                        if (((File)obj).getLocation().equals(folderPath)){
                            MessageUtil.showErrorMgs(jp1, "文件夹不为空，不能删除");
                            return ;
                        }
                    }
                    if (myFAT[i].getType() == FileSystemUtil.FOLDER){
                        if (((Folder)myFAT[i].getObject()).equals((Folder)fat.getObject())){
                            //如果都不满足上述条件将此时的文件分配表该文件夹对象删除
                            index = i;
                        }
                    }
                }
            }

            myFAT[index] = null;
            //移除树上的结点
            DefaultMutableTreeNode parentNode = map.get(path);
            parentNode.remove(map.get(folderPath));
            map.remove(folderPath);
        }
    }

    //获取当前磁盘块对应的文件分配表对象
    public static FAT getFAT(int index){
        return myFAT[index];
    }

    //对成员变量的赋值及得到值
    public static void setMyFAT(FAT[] myFAT){
        FATService.myFAT = myFAT;
    }

    public static FAT[] getMyFAT(){
        return myFAT;
    }

    public static void setOpenFiles(OpenFiles openFiles){
        FATService.openFiles = openFiles;
    }

    public static OpenFiles getOpenFiles(){
        return openFiles;
    }

}