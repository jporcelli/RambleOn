/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package accountdata;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author James C. Porcelli, SBU ID 108900819
 */
public class AbstractActiveAccountsTableModel extends AbstractTableModel implements Serializable{
        
    private final String[] columnNames = {"USER NAME"};
    private ArrayList<String> userNames;

    /**
     *
     * @param userNames
     */
    public AbstractActiveAccountsTableModel(ArrayList<String> userNames) {
        this.userNames = userNames;
    }

    /**
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return userNames.size();
    }

    /**
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     *
     * @param col
     * @return
     */
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     *
     * @param col
     * @return
     */
    @Override
    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return userNames.get(rowIndex);
    }
}
