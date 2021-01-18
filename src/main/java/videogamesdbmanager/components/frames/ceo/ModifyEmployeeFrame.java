package videogamesdbmanager.components.frames.ceo;

import videogamesdbmanager.controllers.CeoController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ModifyEmployeeFrame extends JFrame {

  private JTextField peselTextField;
  private JTextField nameTextField;
  private JTextField surnameTextField;
  private JTextField salaryTextField;
  private JTextField empDateTextField;
  private JTextField departmentTextField;
  private JPanel mainPanel;
  private JButton deleteButton;
  private JButton modifyButton;
  private JButton closeButton;

  private final CeoController controller_;
  private final ManageEmployeesFrame parentFrame_;

  public ModifyEmployeeFrame(String[] employeeParams, CeoController controller, ManageEmployeesFrame parentFrame) {
    super("Zarządzaj pracownikiem");

    controller_ = controller;
    parentFrame_ = parentFrame;

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    modifyButton.addActionListener(e -> onModify());
    deleteButton.addActionListener(e -> onDelete());
    closeButton.addActionListener(e -> onClose());

    setTextFields(employeeParams);
  }

  private void setTextFields(String[] employeeParams) {
    peselTextField.setText(employeeParams[0]);
    nameTextField.setText(employeeParams[1]);
    surnameTextField.setText(employeeParams[2]);
    salaryTextField.setText(employeeParams[3]);
    empDateTextField.setText(employeeParams[4]);
    departmentTextField.setText(employeeParams[5]);
  }

  private void onModify() {
    if (controller_.modifyEmployee(peselTextField.getText(),
            salaryTextField.getText(),
            departmentTextField.getText())
    ) {
      JOptionPane.showMessageDialog(null, "Zmodyfikowano pracownika");
      parentFrame_.refreshEmployeesTable();
    }
  }

  private void onDelete() {
    String pesel = peselTextField.getText();
    if (controller_.deleteEmployee(pesel)) {
      JOptionPane.showMessageDialog(null, "Usunięto pracownika");
      onClose();
    }
  }

  private void onClose() {
    this.dispose();
    parentFrame_.refreshEmployeesTable();
  }
}
