package com.cw.filesystem.viewer;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.cw.filesystem.model.Disk;
import com.cw.filesystem.model.FAT;
import com.cw.filesystem.model.File;
import com.cw.filesystem.model.Folder;
import com.cw.filesystem.service.FATService;
import com.cw.filesystem.util.FileSystemUtil;
import com.cw.filesystem.util.MessageUtil;
public class MainFrame extends Frame{
    //创建面板，文本栏等
    private JPanel jp1,jp2,jp3,jp4,jp5;
    private JTextField jtf;
    private Tree jtr;
    private JTable jta1,jta2;
    private JScrollPane jsp1,jsp2;
    private JMenuBar jmb;
    private JMenu jm;
    private JMenuItem jmi;
    private JLabel jl1,jl2,jl3;
    private JButton jb;
    private DiskTableModel tm;
    private OpenFileTableModel oftm;
    //存放树状展示的路径和结点
    private Map<String, DefaultMutableTreeNode> map;
    private List<FAT> fatList;
    private FATService fatService;
    private boolean isFile;
    private int n;
    private int fatIndex = 0;

    //构造函数，实现窗口
    public MainFrame(){
        //创建存放路径和结点的树
        map = new HashMap<String, DefaultMutableTreeNode>();
        //创建存储文件分配表的动态数组
        fatList = new ArrayList<FAT>();
        //初始化后台数据
        initService();
        //初始化窗口
        initMainFrameUI();
        //容器组件初始化
        initComponent();
        //添加介绍菜单
        addJieShao();
        //布局各个面板组件
        addJPanel();
    }
    //初始化后台数据
    private void initService(){
        fatService = new FATService();
        fatService.initFAT();
    }
    //组件初始化
    private void initComponent(){
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();
        jp5 = new JPanel();
        jtf = new JTextField();
        jtr = new Tree();
        jl1 = new JLabel("路径：");
        jl2 = new JLabel("磁盘分析");
        jl3 = new JLabel("已打开文件");
        jmb = new JMenuBar();
        jm = new JMenu("系统");
        jmi = new JMenuItem("介绍");
        jb = new JButton("帮助");
        tm = new DiskTableModel();
        oftm = new OpenFileTableModel();
        jta1 = new JTable(tm);
        jta2 = new JTable(oftm);
        jsp1 = new JScrollPane(jta1);
        jsp2 = new JScrollPane(jta2);
        validate();
    }
    //窗口的实现
    private void initMainFrameUI(){
        //创建Toolkit抽象类对象，方便后续调用默认工具包操作
        Toolkit t = Toolkit.getDefaultToolkit();
        //设置布局
        this.setLayout(new BorderLayout());
        //设置窗体位置屏幕正中间大小1000*600
        this.setBounds(((int)t.getScreenSize().getWidth()-1000)/2,((int)t.getScreenSize().getHeight()-600)/2,1000,600);
        //设置标题
        this.setTitle("模拟磁盘文件系统");
        //设置窗体图标
        URL url=this.getClass().getClassLoader().getResource("images/img1.jpg");
        Image image=t.getImage(url);
        this.setIconImage(image);
        //设置窗口关闭
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //设置窗口大小不可变
        this.setResizable(false);
        //设置窗口可见
        this.setVisible(true);
    }
    private void addJPanel(){
        //路径帮助面板设置
        jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp2.setPreferredSize(new Dimension(1000, 30));
        jtf.setPreferredSize(new Dimension(450,20));
        jtf.setText("C:");
        jp2.add(jl1);
        jp2.add(jtf);
        jp2.add(jb);
        jbAddListener();
        //分析面板设置
        //jp4.setPreferredSize(new Dimension(300, 500));
        jsp1.setPreferredSize(new Dimension(310,355));
        jsp2.setPreferredSize(new Dimension(310,100));
        jp4.add(jl2);
        jp4.add(jsp1);
        jp4.add(jl3,BorderLayout.CENTER);
        jp4.add(jsp2);
        jp4.setPreferredSize(new Dimension(360,500));
        //validate();
        //总体面板设置
        jp1.setLayout(new BorderLayout());
        jp1.add(jp2, BorderLayout.NORTH);
        jp1.add(jtr, BorderLayout.WEST);
        jp1.add(jp4, BorderLayout.EAST);
        //设置整体下方布局
        this.add(jp1,BorderLayout.CENTER);
        validate();
    }
    //添加介绍菜单
    private void addJieShao(){
        //设置介绍菜单
        jmiAddlistener();
        jm.add(jmi);
        jmb.add(jm);
        this.add(jmb,BorderLayout.NORTH);
    }
    //添加介绍菜单监听器帮助打开介绍面板
    private void jmiAddlistener(){
        jmi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JieShao(jp1);
            }
        });

    }
    //添加帮助按钮监听器打开帮助面板
    private void jbAddListener(){
        jb.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new HelpDialog(jp1);
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /*
     * 树
     */
    public class Tree extends JPanel {

        private static final long serialVersionUID = 2352829445429133249L;
        private JTree tree;
        private JScrollPane jsp1, jsp2;
        private JSplitPane jsp;
        private JPanel jp1;
        private MyJLabel[] jLabel;
        private JPopupMenu pm, pm2;
        private JMenuItem mi1, mi2, mi3, mi4, mi5, mi6;
        private DefaultMutableTreeNode node1;

        public Tree() {

            this.initMenuItem();
            this.initMenuItenByJLabel();
            this.menuItemAddListener();

            this.initTree();
            this.treeAddListener();
            this.jPanelAddListener();

            jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp1.setBackground(Color.white);
            jp1.add(pm);

            jsp2 = new JScrollPane(jp1);
            jsp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jsp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jsp2.setPreferredSize(new Dimension(442, 515));
            jsp2.setBackground(Color.white);
            jsp2.setViewportView(jp1);

            jsp1.setPreferredSize(new Dimension(200, 515));

            jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jsp1,jsp2);

            jsp.setDividerSize(0);
            jsp.setDividerLocation(200);
            jsp.setEnabled(false);

            this.add(jsp);
        }

        /**
         * 初始化树
         */
        private void initTree() {
            node1 = new DefaultMutableTreeNode(new Disk("C"));
            map.put("C:", node1);
            jp1 = new JPanel();
            tree = new JTree(node1);
            jsp1 = new JScrollPane(tree);
        }

        private void treeAddListener(){
            tree.addMouseListener(new MouseListener() {

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    //改变地址栏路径
                    TreePath path = tree.getSelectionPath();
                    if (path != null){
                        String pathStr = path.toString().replace("[", "").replace("]", "").replace(",", "\\").replace(" ", "").replaceFirst("C", "C:");
                        jtf.setText(pathStr);

                        //更新jp1
                        jp1.removeAll();
                        addJLabel(fatService.getFATs(pathStr), pathStr);
                        jp1.updateUI();
                    }
                }
            });
        }

        /**
         * 在面板中添加JLabel
         */
        private void addJLabel(List<FAT> fats, String path){
            fatList = fats;
            isFile = true;
            n = fats.size();
            jp1.setPreferredSize(new Dimension(482, FileSystemUtil.getHeight(n)));
            jLabel = new MyJLabel[n];
            for (int i=0; i<n; i++){
                if (fats.get(i).getIndex() == FileSystemUtil.END){
                    if (fats.get(i).getType() == FileSystemUtil.FOLDER){
                        isFile = false;
                        jLabel[i] = new MyJLabel(isFile, ((Folder)fats.get(i).getObject()).getFolderName());
                    } else {
                        isFile = true;
                        jLabel[i] = new MyJLabel(isFile, ((File)fats.get(i).getObject()).getFileName());
                    }
                    jp1.add(jLabel[i]);
                    jLabel[i].add(pm2);
                    jLabel[i].addMouseListener(new MouseListener() {

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            for (int j=0; j<n; j++){
                                if (e.getSource() == jLabel[j] && ((e.getModifiers() & InputEvent.BUTTON3_MASK)!=0)){
                                    pm2.show(jLabel[j], e.getX(), e.getY());
                                }
                            }
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            for (int j=0; j<n; j++){
                                if (e.getSource() == jLabel[j]){
                                    fatIndex = j;
                                    if (jLabel[j].type){
                                        jLabel[j].setIcon(new ImageIcon(getClass().getResource(FileSystemUtil.filePath)));
                                    } else {
                                        jLabel[j].setIcon(new ImageIcon(getClass().getResource(FileSystemUtil.folderPath)));
                                    }
                                }
                            }
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            for (int j=0; j<n; j++){
                                if (e.getSource() == jLabel[j]){
                                    fatIndex = j;
                                    if (jLabel[j].type){
                                        jLabel[j].setIcon(new ImageIcon(getClass().getResource(FileSystemUtil.file1Path)));
                                    } else {
                                        jLabel[j].setIcon(new ImageIcon(getClass().getResource(FileSystemUtil.folder1Path)));
                                    }
                                }
                            }
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getClickCount() == 2){
                                if (fatList.get(fatIndex).getType() == FileSystemUtil.FILE){
                                    //文件
                                    if (fatService.getOpenFiles().getFiles().size() < FileSystemUtil.num){
                                        if (fatService.checkOpenFile(fatList.get(fatIndex))){
                                            MessageUtil.showErrorMgs(jp1, "文件已打开");
                                            return;
                                        }
                                        fatService.addOpenFile(fatList.get(fatIndex), FileSystemUtil.flagWrite);
                                        oftm.initData();
                                        jta2.updateUI();
                                        new OpenFileJFrame(fatList.get(fatIndex), fatService, oftm, jta2, tm, jta1);
                                    } else {
                                        MessageUtil.showErrorMgs(jp1, "已经打开5个文件了，达到上限");
                                    }

                                } else {
                                    //文件夹
                                    Folder folder = (Folder)fatList.get(fatIndex).getObject();
                                    String path = folder.getLocation() + "\\" + folder.getFolderName();

                                    jp1.removeAll();
                                    addJLabel(fatService.getFATs(path), path);
                                    jp1.updateUI();
                                    jtf.setText(path);
                                }
                            }
                        }
                    });
                }
            }
        }

        /**
         * 面板中添加监听器
         */
        private void jPanelAddListener() {
            //点击右键时的事件
            jp1.addMouseListener(new MouseListener() {

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    int mods = e.getModifiers();
                    if ((mods&InputEvent.BUTTON3_MASK) != 0){
                        pm.show(jp1, e.getX(), e.getY());
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseClicked(MouseEvent e) {

                }
            });
        }

        //初始化右键菜单
        public void initMenuItem(){
            pm = new JPopupMenu();
            mi1 = new JMenuItem("新建文件");
            mi2 = new JMenuItem("新建文件夹");
            pm.add(mi1);
            pm.add(mi2);
        }

        public void initMenuItenByJLabel(){
            pm2 = new JPopupMenu();
            mi3 = new JMenuItem("打开");
            mi4 = new JMenuItem("重命名");
            mi5 = new JMenuItem("删除");
            mi6 = new JMenuItem("属性");
            pm2.add(mi3);
            pm2.add(mi4);
            pm2.add(mi5);
            pm2.add(mi6);
        }

        /**
         * 点击右键选项添加监听器
         */
        public void menuItemAddListener(){
            mi1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = fatService.createFile(jtf.getText());
                    if (index == FileSystemUtil.ERROR){
                        MessageUtil.showErrorMgs(jp1, "磁盘已满，无法创建文件");
                    } else {
                        //树的目录展示只需要文件夹
//						FAT fat = fatService.getFAT(index);
//						DefaultMutableTreeNode node = new DefaultMutableTreeNode((File)(fat.getObject()));
//						map.put(jtf1.getText() + "\\" + ((File)(fat.getObject())).getFileName(), node);
//						DefaultMutableTreeNode nodeParent = map.get(jtf1.getText());
//						nodeParent.add(node);
                        tree.updateUI();
                        ((DiskTableModel)tm).initData();
                        jta1.updateUI();

                        jp1.removeAll();
                        addJLabel(fatService.getFATs(jtf.getText()), jtf.getText());
                        jp1.updateUI();
                    }
                }
            });
            mi2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = fatService.createFolder(jtf.getText());
                    if (index == FileSystemUtil.ERROR){
                        MessageUtil.showErrorMgs(jp1, "磁盘已满，无法创建文件夹");
                    } else {
                        FAT fat = fatService.getFAT(index);
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode((Folder)(fat.getObject()));
                        map.put(jtf.getText() + "\\" + ((Folder)(fat.getObject())).getFolderName(), node);
                        DefaultMutableTreeNode nodeParent = map.get(jtf.getText());
                        nodeParent.add(node);
                        tree.updateUI();
                        ((DiskTableModel)tm).initData();
                        jta1.updateUI();

                        jp1.removeAll();
                        addJLabel(fatService.getFATs(jtf.getText()), jtf.getText());
                        jp1.updateUI();
                    }
                }

            });
            mi3.addActionListener(new ActionListener() {
                //打开
                @Override
                public void actionPerformed(ActionEvent e){
                    if (fatList.get(fatIndex).getType() == FileSystemUtil.FILE){
                        //文件
                        if (fatService.getOpenFiles().getFiles().size() < FileSystemUtil.num){
                            if (fatService.checkOpenFile(fatList.get(fatIndex))){
                                MessageUtil.showErrorMgs(jp1, "文件已打开");
                                return;
                            }
                            fatService.addOpenFile(fatList.get(fatIndex), FileSystemUtil.flagWrite);
                            oftm.initData();
                            jta2.updateUI();
                            new OpenFileJFrame(fatList.get(fatIndex), fatService, oftm, jta2, tm, jta1);
                        } else {
                            MessageUtil.showErrorMgs(jp1, "已经打开5个文件了，达到上限");
                        }

                    } else {
                        //文件夹
                        Folder folder = (Folder)fatList.get(fatIndex).getObject();
                        String path = folder.getLocation() + "\\" + folder.getFolderName();

                        jp1.removeAll();
                        addJLabel(fatService.getFATs(path), path);
                        jp1.updateUI();
                        jtf.setText(path);
                    }
                }
            });
            mi4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ShowRenameDialog(jp1, fatList.get(fatIndex), map, fatService);
                    tree.updateUI();
                    tm.initData();
                    jta1.updateUI();

                    jp1.removeAll();
                    addJLabel(fatService.getFATs(jtf.getText()), jtf.getText());
                    jp1.updateUI();
                }
            });
            mi5.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int i = MessageUtil.showConfirmMgs(jp1, "是否确定要删除该文件？");
                    if (i==0){
                        fatService.delete(jp1, fatList.get(fatIndex), map);

                        tree.updateUI();
                        ((DiskTableModel)tm).initData();
                        jta1.updateUI();

                        jp1.removeAll();
                        addJLabel(fatService.getFATs(jtf.getText()), jtf.getText());
                        jp1.updateUI();
                    }

                }
            });
            mi6.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ShowPropertyDialog(jp1, fatList.get(fatIndex));
                }
            });
        }


    }
}