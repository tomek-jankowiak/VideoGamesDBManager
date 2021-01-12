package videogamesdbmanager.components.frames.ceo;

import videogamesdbmanager.controllers.CeoController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ManageEmployeesFrame extends JFrame{
  private JPanel mainPanel;
  private JTable employeesTable;
  private JButton addEmployeeButton;
  private JButton refreshButton;
  private JButton closeButton;

  private final CeoController controller_;

  public ManageEmployeesFrame(CeoController controller) {
    super("Zarządzanie pracownikami");

    controller_ = controller;

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    refreshEmployeesTable();

    addEmployeeButton.addActionListener(e -> onAddEmployee());
    refreshButton.addActionListener(e -> refreshEmployeesTable());
    closeButton.addActionListener(e -> onClose());

    ListSelectionModel selectionModel = employeesTable.getSelectionModel();
    selectionModel.addListSelectionListener(e -> onRowSelection(e, selectionModel));
  }

  public void refreshEmployeesTable() {
    String[] columnNames = {"PESEL", "Imię", "Nazwisko", "Płaca", "Data zatrudnienia", "Dział"};
    DefaultTableModel model = (DefaultTableModel)employeesTable.getModel();
    model.setRowCount(0);
    model.setColumnIdentifiers(columnNames);

    controller_.setEmployeesTable(model);
    employeesTable.setModel(model);
  }

  private void onAddEmployee() {
    SwingUtilities.invokeLater(() -> {
      JFrame addEmployeeFrame = new AddEmployeeFrame(controller_, this);
      addEmployeeFrame.setVisible(true);
    });
  }

  private void onRowSelection(ListSelectionEvent e, ListSelectionModel selectionModel) {
    if (!e.getValueIsAdjusting() && !selectionModel.isSelectionEmpty()) {
      int selectedRowIndex = selectionModel.getMinSelectionIndex();
      String[] employeeParams = new String[6];
      for (int i = 0; i < 6; i++) {
        Object param = employeesTable.getValueAt(selectedRowIndex, i);
        if (param != null) {
          employeeParams[i] = param.toString();
        } else {
          employeeParams[i] = null;
        }
      }
      SwingUtilities.invokeLater(() -> {
        ModifyEmployeeFrame modifyFrame = new ModifyEmployeeFrame(employeeParams, controller_, this);
        modifyFrame.setVisible(true);
      });
    }
  }

  private void onClose() {
    this.dispose();
  }
}
