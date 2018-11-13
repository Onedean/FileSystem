package com.cw.filesystem.viewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

import com.cw.filesystem.model.Disk;
import com.cw.filesystem.model.FAT;
import com.cw.filesystem.model.File;
import com.cw.filesystem.model.Folder;
import com.cw.filesystem.service.FATService;
import com.cw.filesystem.util.FileSystemUtil;
import com.cw.filesystem.util.MessageUtil;


public class OpenFileJFrame extends JFrame{
    private JTextArea jta1;
    private JMenuBar jmb;
    private JMenu jm;
    private JMenuItem jmi1,jmi2;
    private FAT fat;
    private File file;
    private String oldContent;
    private int length;
    private FATService fatService;
    private OpenFileTableModel oftm;
    private JTable jt;
    private JFrame jf;
    private DiskTableModel tm;
    private JTable jta;
    private boolean canClose = true;
    public OpenFileJFrame(FAT fat, FATService fatService, OpenFileTableModel oftm, JTable jt, DiskTableModel tm, JTable jta){
        this.jf = this;
        this.fat = fat;
        this.fatService = fatService;
        this.oftm = oftm;
        this.jt = jt;
        this.tm = tm;
        this.jta = jta;
        this.file = (File) fat.getObject();
        jta1 = new JTextArea();
        jmb = new JMenuBar();
        jm = new JMenu("操作");
        jmi1 = new JMenuItem("保存");
        jmi2 = new JMenuItem("退出");
        jmb.add(jm);
        jm.add(jmi1);
        jm.add(jmi2);
        oldContent = file.getContent();
        jta1 = new JTextArea(oldContent);
        init();
        menuItemAddListener();
    }
    private void init(){
        this.setResizable(false);
        this.setBounds(200,150,600,500);
        this.setTitle("打开");
        this.add(jmb, BorderLayout.NORTH);
        this.add(jta1);
        this.addWindowListener(new WindowClosingListener());
        this.setVisible(true);
    }
    private void menuItemAddListener(){
        //保存菜单事件监听
        jmi1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        //退出事件菜单监听
        jmi2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.setVisible(false);
                //将打开文件项中该文件移除
                fatService.removeOpenFile(fat);
                //对打开文件信息表格更新
                oftm.initData();
                jt.updateUI();
            }
        });
    }
    //保存事件方法
    private void save(){
        length = jta1.getText().length();
        if(length>((File)(fat.getObject())).getLength()-8){
            //获取添加内容的在文件分配表中磁盘块数量
            int num = FileSystemUtil.getNumOfFAT(length);
            if(num>1){
                boolean boo = fatService.saveToModifyFATS(this,num,fat);
                if(boo){
                    file.setLength(length);
                    file.setContent(jta1.getText());
                }
            }else{
                file.setLength(length);
                file.setContent(jta1.getText());
            }
            ((DiskTableModel)tm).initData();
            jta.updateUI();
        }
    }
    //关闭窗口提示保存内部类
    class WindowClosingListener extends WindowAdapter{

        @Override
        public void windowClosing(WindowEvent e) {
            //如果文本内容修改发出提示
            if(!jta1.getText().equals(file.getContent())){
                int ret = MessageUtil.showConfirmMgs(jf, "还没有保存,是否保存");
                if(ret == 0){
                    save();
                }
                fatService.removeOpenFile(fat);
                oftm.initData();
                jt.updateUI();
            }
            fatService.removeOpenFile(fat);
            oftm.initData();
            jt.updateUI();
        }
    }
}

