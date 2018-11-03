package com.cw.filesystem.viewer;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class TableModel extends AbstractTableModel {
    private Vector TableData;
    private Vector<String> TableTitle;
    public TableModel(){
        TableData = new Vector();
        TableTitle = new Vector<String>();
        //初始化列标题
        TableTitle.add("磁盘块");
        TableTitle.add("值");
        String a[][] = new String[128][2];
        //初始化行数据
        for(int i=0;i<128;i++) {
            for (int j = 0; j < 2; j++) {
                if (j == 0)
                    a[i][j] = String.valueOf(i);
                else
                    a[i][j] = "0";
            }
            TableData.add(a[i]);
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        //根据坐标直接返回对应的获取数据
        String[] LineTemp1 = (String[])this.TableData.get(rowIndex);
        return LineTemp1[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //单据格数据发生改变的时候调用该函数重设单元格的数据
        ((String[])this.TableData.get(rowIndex))[columnIndex]=(String)aValue;
        super.setValueAt(aValue, rowIndex, columnIndex);
    }
}
