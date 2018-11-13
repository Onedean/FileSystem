package com.cw.filesystem.viewer;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.cw.filesystem.model.FAT;
import com.cw.filesystem.model.File;
import com.cw.filesystem.model.Folder;
import com.cw.filesystem.service.FATService;
import com.cw.filesystem.util.FileSystemUtil;
import com.cw.filesystem.util.MessageUtil;

public class ShowRenameDialog {

    private FAT fat;
    private boolean isFile = false;
    private Component com;
    String oldName = "";
    String rename = "";
    String oldPath = "";
    String path = "";
    Map<String, DefaultMutableTreeNode> map;
    private FATService fatService;

    public ShowRenameDialog(Component c, FAT f, Map<String, DefaultMutableTreeNode> m, FATService fatService){
        this.fatService = fatService;
        this.map = m;
        this.fat = f;
        this.com = c;
        this.init();
    }

    private void init(){
        if (fat.getType()==FileSystemUtil.FILE){
            isFile = true;
            File file = (File)fat.getObject();
            oldName = file.getFileName();
            oldPath = file.getLocation() + "\\" + oldName;
            path = file.getLocation();
            rename = JOptionPane.showInputDialog(com, "请输入名称", oldName);
            if (rename != null && rename != "" && !rename.equals(oldName)){
                String path1 = ((File)fat.getObject()).getLocation() + "\\" + rename;
                if (this.checkHasName(path1, isFile)){
                    MessageUtil.showErrorMgs(com, "已有该名字的文件了");
                    return ;
                }
                if (rename.contains("$") || rename.contains(".") || rename.contains("/")){
                    MessageUtil.showErrorMgs(com, "文件名包含\"$\",\".\",\"/\" 字符");
                    return ;
                }
                ((File)fat.getObject()).setFileName(rename);
            }
        } else {
            isFile = false;
            Folder folder = (Folder)fat.getObject();
            oldName = folder.getFolderName();
            oldPath = folder.getLocation() + "\\" + oldName;
            path = folder.getLocation();
            rename = JOptionPane.showInputDialog(com, "请输入名称", oldName);
            if (rename != null &&( rename != "" && !rename.equals(oldName))){
                String path1 = ((Folder)fat.getObject()).getLocation() + "\\" + rename;
                if (this.checkHasName(path1, isFile)){
                    MessageUtil.showErrorMgs(com, "已有该名字的文件夹了");
                    return ;
                }

                if (rename.contains("$") || rename.contains(".") || rename.contains("/")){
                    MessageUtil.showErrorMgs(com, "文件名包含\"$\",\".\",\"/\" 字符");
                    return ;
                }

                ((Folder)fat.getObject()).setFolderName(rename);
            }
        }

        //更改map中的路径
//		DefaultMutableTreeNode node = map.get(oldPath);
//		map.remove(oldPath);
        String newPath = path + "\\" + rename;
        fatService.modifyLocation(oldPath, newPath);
//		map.put(newPath, node);
        Set<String> set = map.keySet();
        List<String> setStr = new ArrayList<String>();
        setStr.addAll(set);
        for (String s : setStr){
            if (s.contains(oldPath)){
                DefaultMutableTreeNode n = map.get(s);
                String newPaths = s.replace(oldPath, newPath);
                map.remove(s);
                map.put(newPaths, n);
            }
        }
    }

    //解决重命名时的冲突情况
    public boolean checkHasName(String path1, boolean isFile1){
        for (int i=2; i<fatService.getMyFAT().length; i++){
            if (fatService.getMyFAT()[i] != null){
                if (isFile1){
                    //文件
                    if (fatService.getMyFAT()[i].getType() == FileSystemUtil.FILE){
                        String path2 = ((File)(fatService.getMyFAT()[i].getObject())).getLocation() + "\\" + ((File)(fatService.getMyFAT()[i].getObject())).getFileName();
                        if (path2.equals(path1)){
                            return true;
                        }
                    }
                } else {
                    //文件夹
                    if (fatService.getMyFAT()[i].getType() == FileSystemUtil.FOLDER){
                        String path2 = ((Folder)(fatService.getMyFAT()[i].getObject())).getLocation() + "\\" + ((Folder)(fatService.getMyFAT()[i].getObject())).getFolderName();
                        if (path2.equals(path1)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
