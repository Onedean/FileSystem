package com.cw.filesystem.viewer;

import com.cw.filesystem.model.Disk;
import com.cw.filesystem.model.FAT;
import com.cw.filesystem.model.File;
import com.cw.filesystem.model.Folder;
import com.cw.filesystem.servicw.FATService;
import com.cw.filesystem.util.FileSystemUtil;
import com.cw.filesystem.util.MessageUtil;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFrame extends Frame implements KeyListener, TreeSelectionListener {
    //创建面板，文本栏等
    private JPanel jp1,jp2,jp3,jp4,jp5;
    private JTextField jtf;
    private JTree tree;
    private JTextArea showText;
    private JTable jta1,jta2;
    private JScrollPane jsp1,jsp2;
    private JMenuBar jmb;
    private JMenu jm;
    private JMenuItem jmi;
    private JLabel jl1,jl2,jl3;
    private JButton jb;
    private TableModel tm;
    //存放树状展示的路径和结点
    private Map<String, DefaultMutableTreeNode> map;
    private FATService fatService;
    //需要用的从其他包中导入的类创建的对象
    private OpenFileTableModel oftm;
    private List<FAT> fatList;
    private boolean isFile;
    private int n;
    private int fatIndex = 0;

    //构造函数，实现窗口
    public MainFrame(){
        //容器组件初始化
        initComponent();
        //初始化后台数据
        initService();
        //初始化窗口
        initMainFrameUI();
        //添加介绍菜单
        addJieShao();
        //初始化树组件即列表索引
        initTreeList();
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
        jl1 = new JLabel("路径：");
        jl2 = new JLabel("磁盘分析");
        jl3 = new JLabel("已打开文件");
        jmb = new JMenuBar();
        jm = new JMenu("系统");
        jmi = new JMenuItem("介绍");
        jb = new JButton("帮助");
        tm = new com.cw.filesystem.viewer.TableModel();
        oftm = new OpenFileTableModel();
        jta1 = new JTable(tm);
        jta2 = new JTable(oftm);
        jsp1 = new JScrollPane(jta1);
        jsp2 = new JScrollPane(jta2);
        //创建存放路径和结点的树
        map = new HashMap<String,DefaultMutableTreeNode>();
        //创建存储文件分配表的动态数组
        fatList = new ArrayList<FAT>();
        validate();
    }
    //窗口的实现
    private void initMainFrameUI(){
        //创建Toolkit抽象类对象，方便后续调用默认工具包操作
        Toolkit t = Toolkit.getDefaultToolkit();
        //设置布局
        this.setLayout(new BorderLayout());
        //设置窗体位置屏幕正中间大小800*600
        this.setBounds(((int)t.getScreenSize().getWidth()-800)/2,((int)t.getScreenSize().getHeight()-600)/2,800,600);
        //设置标题
        this.setTitle("模拟磁盘文件系统");
        //设置窗体图标
        URL url=this.getClass().getClassLoader().getResource("images/myComputer.png");
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
    private void addJPanel(){
        //路径帮助面板设置
        jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp2.setPreferredSize(new Dimension(1000, 40));
        jtf.setPreferredSize(new Dimension(300,30));
        jtf.setText("C:");
        jp2.add(jl1);
        jp2.add(jtf);
        jp2.add(jb);
        jbAddListener();
        //分析面板设置
        //jp4.setPreferredSize(new Dimension(300, 500));
        jsp1.setPreferredSize(new Dimension(200,400));
        jsp2.setPreferredSize(new Dimension(200,400));
        jp4.add(jl2);
        jp4.add(jsp1);
        jp4.add(jl3);
        jp4.add(jsp2);
        validate();
        //总体面板设置
        jp1.setLayout(new BorderLayout());
        jp1.add(jp2, BorderLayout.NORTH);
        jp1.add(jp3, BorderLayout.WEST);
        jp1.add(jp4, BorderLayout.EAST);
        //设置整体下方布局
        this.add(jp1,BorderLayout.CENTER);
        validate();
    }
    //创建一个实现磁盘文件夹及文件树的内部类(每个内部类都能独立地继承一个(接口的)实现，所以无论外围类是否已经继承了某个(接口的)实现，对于内部类都没有影响)
    public class tree extends JPanel{
        //Java对象转换为字节序列即对象的序列化即对象存档
        private static final long serialVersionUID = 2352829445429133249L;
        private JTree tree;
        private JScrollPane jsp1,jsp2;
        private JSplitPane jsp;
        private JPanel jp1;
        private MyJLabel[] jLabel;
        //创建点击操作面板pm1和点击文件/文件夹pm2右键菜单
        private JPopupMenu pm1,pm2;
        //创建右键菜单项
        private JMenuItem jmi1,jmi2,jmi3,jmi4,jmi5,jmi6;
        private DefaultMutableTreeNode node1;
        //内部类树的构造方法??????????????????????????????????????????????
        public tree(){
             this.initMenuItem();
             this.initMenuItemByJLabel();
             this.menuItemAddListener();
             //初始化树
             this.initTree();
             //为树增减事件监听器
             this.treeAddListener();
             //为面板增加事件监听器
             this.jpanelAddListener();

             //设置操作面板属性
             jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
             jp1.setBackground(Color.white);
             jp1.add(pm1);

             //为操作面板设置水平滚动条总是隐藏和竖直滚动条总是出现
             jsp2 = new JScrollPane(jp1);
             jsp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
             jsp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            //为操作面板设置最佳大小,背景白色
             jsp2.setPreferredSize(new Dimension(482,515));
             jsp2.setBackground(Color.white);
             //为操作面板创建一个视口jp1（如果有必要）并设置其视图
             jsp2.setViewportView(jp1);

             //创建水平拆分窗格，分为一个存放树和一个操作面板的滚动窗格
             jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jsp1,jsp2);
             //设置分隔条的大小
             jsp.setDividerSize(0);
             //设置分隔条的位置，相对于左边的像素长度
             jsp.setDividerLocation(200);
             //禁止分隔条可以拖动
             jsp.setEnabled(false);
             this.add(jsp);
        }
        //初始化树
        private void initTree(){
            //创建树的根"C盘"
            node1 = new DefaultMutableTreeNode(new Disk("C"));
            map.put("C",node1);
            //创建操作面板组件
            jp1 = new JPanel();
            //创建用node1做根的树组件对象
            tree = new JTree(node1);
            //添加可滚动的树的窗格
            jsp1 = new JScrollPane(tree);
            jsp1.setPreferredSize(new Dimension(200,515));
        }

        //增加监听树的事件监听器
        private void treeAddListener(){
            tree.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //点击树的结点,则相应改变路径栏对应地址,及对应所属操作面板
                    TreePath path = tree.getSelectionPath();
                    if(path != null){
                        //更新路径栏
                        String pathStr = path.toString().replace("[","").replace("]","").replace(",","\\").replace(" ","").replace("C","C:");
                        jtf.setText(pathStr);
                        //更新操作面板
                        jp1.removeAll();
                        addJLabel(fatService.getFATs(pathStr),pathStr);
                        jp1.updateUI();
                    }
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
        //在面板中添加文件夹/文件
        private void addJLabel(List<FAT> fats,String path){
            fatList = fats;
            isFile = true;
            n = fats.size();
            jp1.setPreferredSize(new Dimension(482, FileSystemUtil.getHeight(n)));
            jLabel = new MyJLabel[n];
            for(int i=0;i<n;i++){
                if(fats.get(i).getIndex() == FileSystemUtil.END){
                    if(fats.get(i).getType() == FileSystemUtil.FOLDER){
                        isFile = false;
                        jLabel[i] = new MyJLabel(isFile,((Folder)fats.get(i).getObject()).getFolderName());
                    }else{
                        isFile = true;
                        jLabel[i] = new MyJLabel(isFile,((File)fats.get(i).getObject()).getFileName());
                    }
                    jp1.add(jLabel[i]);
                    jLabel[i].add(pm2);
                    jLabel[i].addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            //如果鼠标点击次数为2，意为双击打开文件夹/文件
                            if(e.getClickCount() == 2){
                                //判断双击的目标是文件还是文件夹
                                if(fatList.get(fatIndex).getType() == FileSystemUtil.FILE){
                                    //如果是文件，判断文件已打开数目是否超过五个
                                    if(fatService.getOpenFiles().getFiles().size()<FileSystemUtil.num){
                                        if(fatService.checkOpenFile(fatList.get(fatIndex))){
                                            //判断该文件是否已打开
                                            MessageUtil.showErrorMessages(jp1,"文件已打开");
                                            return;
                                        }
                                        //否则打开该文件，更新???????????????????????????
                                        fatService.addOpenFile(fatList.get(fatIndex),FileSystemUtil.flagWrite);
                                        oftm.initData();
                                        jta2.updateUI();
                                        new OpenFileJFrame(fatList.get(fatIndex),fatService,oftm,jta2,tm,jta1);
                                    }else{
                                        MessageUtil.showErrorMessages(jp1,"文件已打开数目超过5个");
                                    }
                                }else{

                                }
                            }
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
            }
        }
        //在面板中增加监听器
        private void jpanelAddListener(){
            jp1.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //TODO
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
        //初始化右键菜单
        public void initMenuItem(){

        }
        //初始化右键菜单项
        public void initMenuItemByJLabel(){

        }
        //点击右键选项增加监听器
        public void menuItemAddListener(){

        }
    }
    //树组件索引的实现
    private void initTreeList(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("C盘");
        DefaultMutableTreeNode nodeTV = new DefaultMutableTreeNode("文件夹类");
        DefaultMutableTreeNode nodePhone = new DefaultMutableTreeNode("可执行程序类");
        DefaultMutableTreeNode tv1 = new DefaultMutableTreeNode(new Goods("A文本",5699));
        DefaultMutableTreeNode tv2 = new DefaultMutableTreeNode(new Goods("B文本",7800));
        DefaultMutableTreeNode phone1 = new DefaultMutableTreeNode(new Goods("A可执行程序",3600));
        DefaultMutableTreeNode phone2 = new DefaultMutableTreeNode(new Goods("B可执行程序",2500));
        root.add(nodeTV);
        root.add(nodePhone);
        nodeTV.add(tv1);
        nodeTV.add(tv2);
        nodePhone.add(phone2);
        nodePhone.add(phone1);
        //创建用root做根的树组件对象
        tree = new JTree(root);
        //窗口监视树组件上的事件
        tree.addTreeSelectionListener(this);
        showText = new JTextArea();
        jp3.setLayout(new GridLayout(1,2));
        jp3.add(new JScrollPane(tree));
        jp3.add(new JScrollPane(showText));
        //使容器再次布置其子组件
        validate();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    //鼠标单击树上结点的监视器实现接口的方法
    public void valueChanged(TreeSelectionEvent e) {
        //获得鼠标单击的那个结点
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        //判断是不是叶子结点
        if(node.isLeaf()){
            //返回Object类型点击的结点转换为Goods类对象
            Goods s = (Goods)node.getUserObject();
            showText.append(s.name+","+s.price+"容量\n");
        }
    }
}