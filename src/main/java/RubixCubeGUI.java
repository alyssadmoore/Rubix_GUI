import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class RubixCubeGUI extends JFrame implements WindowListener{

    private JPanel rootPanel;
    private JTable allSolversTable;
    private JButton deleteASolverButton;
    private JTextField newSolverTextField;
    private JSpinner newSecondsSpinner;
    private JSpinner newMillisecondsSpinner;
    private JButton addANewSolverButton;
    private JSpinner updateSecondsSpinner;
    private JSpinner updateMillisecondsSpinner;
    private JButton updateSolverTimeButton;

    public RubixCubeGUI(final RubixDataModel rubixDataModel) {
        setContentPane(rootPanel);
        pack();
        setTitle("Rubix Cube Fastest Solvers Database Application");
        addWindowListener(this);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Setting up JTable
        allSolversTable.setGridColor(Color.BLACK);
        allSolversTable.setModel(rubixDataModel);

        //Setting up spinners
        newSecondsSpinner.setModel(new SpinnerNumberModel(0, RubixCube.MIN_TIME, RubixCube.MAX_TIME, 1));
        newMillisecondsSpinner.setModel(new SpinnerNumberModel(0, RubixCube.MIN_TIME, RubixCube.MAX_TIME, 1));
        updateSecondsSpinner.setModel(new SpinnerNumberModel(0, RubixCube.MIN_TIME, RubixCube.MAX_TIME, 1));
        updateMillisecondsSpinner.setModel(new SpinnerNumberModel(0, RubixCube.MIN_TIME, RubixCube.MAX_TIME, 1));

        // Add solver button
        addANewSolverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Getting new solver name
                String newSolverName = newSolverTextField.getText();
                // If new solver name is empty, open a message dialog
                if (newSolverName == null || newSolverName.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a name for the new solver.");
                    return;
                }
                // If new solver name is not empty, get the values in appropriate spinners to calculate new time
                double totalSeconds = ((Integer)newSecondsSpinner.getValue() + ((Integer)(newMillisecondsSpinner.getValue()))/100.0);
                // If the insertRow command works, insertedRow is returned true
                boolean insertedRow = rubixDataModel.insertRow(newSolverName, totalSeconds);

                // If insertedRow is false that means the command did not work, inform the user
                if (!insertedRow) {
                    JOptionPane.showMessageDialog(rootPane, "Error adding new solver");
                }
            }
        });

        // Update time button
        updateSolverTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // First get the selected row number
                int currentRow = allSolversTable.getSelectedRow();
                // If the current row number is -1 that means no row is selected, open a message dialog
                if (currentRow == -1) {
                    JOptionPane.showMessageDialog(rootPane, "Please choose a solver to update");
                }
                // Get the values in appropriate spinners to calculate new time
                double totalTime = ((Integer)updateSecondsSpinner.getValue() + ((Integer)updateMillisecondsSpinner.getValue()/100.0));
                // If the updateRow command works, updated is returned true
                boolean updated = rubixDataModel.updateRow(currentRow, totalTime);
                // If the updateRow command works, reload the table
                if (updated) {
                    RubixCube.loadAllTimes();
                // If the updateRow command does not work, inform the user
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error updating solver");
                }
            }
        });

        // Delete solver button
        deleteASolverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // First get the selected row
                int currentRow = allSolversTable.getSelectedRow();
                // If the selected row is -1 that means no row is selected, open a message dialog
                if (currentRow == -1) {
                    JOptionPane.showMessageDialog(rootPane, "Please choose a solver to delete");
                }
                // If the deleteRow command works deleted is returned true
                boolean deleted = rubixDataModel.deleteRow(currentRow);
                // If the deleteRow command works, reload the table
                if (deleted) {
                    RubixCube.loadAllTimes();
                // If the deleteRow command does not work, inform the user
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error deleting solver");
                }
            }
        });
    }

    @Override
    public void windowClosing(WindowEvent e) {
        RubixCube.shutdown();
    }

    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}