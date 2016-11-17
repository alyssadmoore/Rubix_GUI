import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RubixDataModel extends AbstractTableModel{

    private int rowCount = 0;
    private int colCount = 0;
    ResultSet resultSet;

    public RubixDataModel(ResultSet rs) {
        this.resultSet = rs;
        setup();
    }

    // setup gets number of rows and columns
    private void setup(){
        countRows();
        try{
            colCount = resultSet.getMetaData().getColumnCount();
        } catch (SQLException se) {
            System.out.println("Error counting columns" + se);
        }
    }

    public void updateResultSet(ResultSet newRS){
        resultSet = newRS;
        setup();
    }

    private void countRows() {
        rowCount = 0;
        try {
            // Start the resultSet before the first entry, count each next entry
            resultSet.beforeFirst();
            while (resultSet.next()) {
                rowCount++;
            }
            // At the end, go back to before the first entry
            resultSet.beforeFirst();
        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }
    }
    @Override
    public int getRowCount() {
        countRows();
        return rowCount;
    }

    @Override
    public int getColumnCount(){
        return colCount;
    }

    @Override
    public Object getValueAt(int row, int col){
        try{
            // Row and column numbers in resultSet start at 0, so we must add 1 to match table
            resultSet.absolute(row + 1);
            Object o = resultSet.getObject(col + 1);
            return o.toString();
        } catch (SQLException sqle) {
            System.out.println(sqle);
            sqle.printStackTrace();
            // Must have a return statement if try block fails
            return sqle.toString();
        }
    }

    @Override
    public void setValueAt(Object newValue, int row, int col) {
        double newTime = Integer.parseInt(newValue.toString());
        try {
            resultSet.absolute(row + 1);
            resultSet.updateDouble(RubixCube.SECONDS_COLUMN, newTime);
            resultSet.updateRow();
            fireTableDataChanged();
        } catch (SQLException e) {
            System.out.println("Error changing rating " + e);
        }
    }

    @Override
    public boolean isCellEditable(int row, int col){
        // Seconds is column 2
        if (col == 2) {
            return true;
        }
        return false;
    }

    public boolean deleteRow(int row){
        try {
            resultSet.absolute(row + 1);
            resultSet.deleteRow();
            fireTableDataChanged();
            return true;
        } catch (SQLException se) {
            System.out.println("Delete row error " + se);
            return false;
        }
    }

    public boolean insertRow(String solver, double seconds) {
        try {
            // Cursor is moved to insert row
            resultSet.moveToInsertRow();
            // Updates from null to a given value
            resultSet.updateString(RubixCube.SOLVER_COLUMN, solver);
            resultSet.updateDouble(RubixCube.SECONDS_COLUMN, seconds);
            resultSet.insertRow();
            // Cursor is moved back to where it was after inserting row
            resultSet.moveToCurrentRow();
            fireTableDataChanged();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding row");
            System.out.println(e);
            return false;
        }
    }

    public boolean updateRow(int row, double seconds) {
        try {
            resultSet.absolute(row + 1);
            resultSet.updateDouble(RubixCube.SECONDS_COLUMN, seconds);
            resultSet.updateRow();
            fireTableDataChanged();
            return true;
        // There is possibility of NullPointerException as well as SQLException, so use one Exception to catch both
        } catch (Exception e) {
            System.out.println("Error updating row");
            System.out.println(e);
            return false;
        }
    }

    @Override
    public String getColumnName(int col){
        try {
            return resultSet.getMetaData().getColumnName(col + 1);
        } catch (SQLException se) {
            System.out.println("Error fetching column names" + se);
            // If there is an error, all column names are reset to a question mark
            return "?";
        }
    }
}