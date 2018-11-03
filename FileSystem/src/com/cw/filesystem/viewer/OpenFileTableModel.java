package com.cw.filesystem.viewer;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class OpenFileTableModel extends AbstractTableModel {
    private Vector<String> OpenFileTableTitle;
    private Vector OpenFileTableData;
    public OpenFileTableModel(){
        OpenFileTableTitle = new Vector<String>();
        OpenFileTableData = new Vector();
        OpenFileTableTitle.add("文件名称");
        OpenFileTableTitle.add("文件打开位置");
        OpenFileTableTitle.add("文件起始盘");
        OpenFileTableTitle.add("文件路径");
        String b[][] = new String[50][4];
        for(int i=0;i<50;i++) {
            for (int j = 0; j < 4; j++) {
                b[i][j] = "";
            }
            OpenFileTableData.add(b[i]);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //单据格数据发生改变的时候调用该函数重设单元格的数据
        ((String[])this.OpenFileTableData.get(rowIndex))[columnIndex]=(String)aValue;
        super.setValueAt(aValue, rowIndex, columnIndex);
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        String[] LineTemp2 = (String[])this.OpenFileTableData.get(rowIndex);
        return LineTemp2[columnIndex];
    }

}
