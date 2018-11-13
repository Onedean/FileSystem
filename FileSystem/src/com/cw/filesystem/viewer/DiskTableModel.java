package com.cw.filesystem.viewer;

import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

import com.cw.filesystem.model.FAT;
import com.cw.filesystem.service.FATService;

public class DiskTableModel extends AbstractTableModel {
    private Vector<String> TableTitle;
    private Vector<Vector<Integer>> TableData;
    private FATService fatService;
    private int index = 0;

    public DiskTableModel(){
        fatService = new FATService();
        initData();
    }


    public void initData(){
        //初始化列标题
        TableTitle = new Vector<String>();
        TableTitle.add("磁盘块");
        TableTitle.add("值");
        //初始化行数据
        TableData = new Vector<Vector<Integer>>();
        Vector<Integer> vs = null;
        FAT[] list = fatService.getMyFAT();
        for(int i=0;i<128;i++) {
            vs = new Vector<Integer>();
            if (list[i] != null) {
                vs.add(i);
                vs.add(list[i].getIndex());
            }else {
                vs.add(i);
                vs.add(0);
            }
            TableData.add(vs);
        }
    }

    @Override
    public int getRowCount() {
        return TableData.size();
    }

    @Override
    public int getColumnCount() {
        return TableTitle.size();
    }

    @Override
    public String getColumnName(int column) {
        return TableTitle.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //根据坐标直接返回对应的获取数据
        return TableData.get(rowIndex).get(columnIndex);
    }

}