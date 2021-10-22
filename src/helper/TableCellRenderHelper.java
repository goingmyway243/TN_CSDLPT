/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Nguyen Hai Dang
 */
public class TableCellRenderHelper extends JTextArea
        implements TableCellRenderer {
    
    public TableCellRenderHelper() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable jTable,
            Object obj, boolean isSelected, boolean hasFocus, int row,
            int column) {
        setText(obj == null ? "" : obj.toString());
        setSize(jTable.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
        if (isSelected) {
            setBackground(jTable.getSelectionBackground());
            setForeground(jTable.getSelectionForeground());
        }
        else {
            setBackground(jTable.getBackground());
            setForeground(jTable.getForeground());
        }
        
        if (jTable.getRowHeight(row) != getPreferredSize().height) {
            jTable.setRowHeight(row, getPreferredSize().height);
        }
        
        return this;
    }
}
