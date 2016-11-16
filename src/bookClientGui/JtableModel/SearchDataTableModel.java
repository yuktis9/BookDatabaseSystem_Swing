/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookClientGui.JtableModel;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dheeraj
 */
public class SearchDataTableModel extends AbstractTableModel {
    
    private static final long serialVersionUID = 1L;
    private String[] columnNames;
    private Object[][] data;

    public SearchDataTableModel(String[] columnNames, Object[][] data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class; 
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
         return false;
    }
    
    @Override
    public int getRowCount() {
       return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
    
    @Override
    public void setValueAt(Object value, int row, int column) {

        data[row][column] = value;
        fireTableCellUpdated(row, column);
     }   
}

