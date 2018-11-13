package com.cw.filesystem.viewer;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.cw.filesystem.model.OpenFiles;
import com.cw.filesystem.service.FATService;
import com.cw.filesystem.util.FileSystemUtil;

public class OpenFileTableModel extends AbstractTableModel {
    private Vector<String> OpenFileTableTitle;
    private Vector<Vector<String>> OpenFileTableData;
    private FATService fatService;
    public OpenFileTableModel(){
        fatService = new FATService();
        initData();
    }
    public void initData(){
        OpenFileTableTitle = new Vector<String>();
        OpenFileTableTitle.add("文件名称");
        OpenFileTableTitle.add("文件打开方式");
        OpenFileTableTitle.add("文件起始盘块号");
        OpenFileTableTitle.add("文件路径");

        Vector<String> vc = null;
        OpenFileTableData = new Vector<Vector<String>>();
        OpenFiles openFiles = fatService.getOpenFiles();

        for(int i = 0; i< FileSystemUtil.num; i++) {
            vc = new Vector<String>();
            if(i<openFiles.getFiles().size()){
                vc.add(openFiles.getFiles().get(i).getFile().getFileName());
                vc.add(openFiles.getFiles().get(i).getFlag()==FileSystemUtil.flagRead ? "只读" : "写读");
                vc.add(openFiles.getFiles().get(i).getFile().getDiskNum()+"");
                vc.add(openFiles.getFiles().get(i).getFile().getLocation());
            }else {
                vc.add("");
                vc.add("");
                vc.add("");
                vc.add("");
            }
            OpenFileTableData.add(vc);
        }
    }

    @Override
    public int getRowCount() {
        return OpenFileTableData.size();
    }

    @Override
    public int getColumnCount() {
        return OpenFileTableTitle.size();
    }

    @Override
    public String getColumnName(int column) {
        return OpenFileTableTitle.get(column);
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //根据坐标直接返回对应的获取数据
        return OpenFileTableData.get(rowIndex).get(columnIndex);
    }
}
