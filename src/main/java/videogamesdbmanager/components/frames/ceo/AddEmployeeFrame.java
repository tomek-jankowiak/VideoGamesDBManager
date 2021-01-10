package videogamesdbmanager.components.frames.ceo;

import videogamesdbmanager.controllers.CeoController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddEmployeeFrame extends JFrame{
  private JTextField peselTextField;
  private JTextField nameTextField;
  private JTextField surnameTextField;
  private JTextField salaryTextField;
  private JTextField empDateTextField;
  private JTextField departmentTextField;
  private JButton addButton;
  private JPanel mainPanel;
  private JButton closeButton;

  private final CeoController controller_;
  private final ManageEmployeesFrame parentFrame_;

  public AddEmployeeFrame(CeoController controller, ManageEmployeesFrame parentFrame) {
    super("Dodaj pracownika");

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

    addButton.addActionListener(e -> onAdd());
    closeButton.addActionListener(e -> onClose());
  }

  private void onAdd() {
    if (controller_.addEmployee(
            peselTextField.getText(),
            nameTextField.getText(),
            surnameTextField.getText(),
            Double.parseDouble(salaryTextField.getText()),
            empDateTextField.getText(),
            departmentTextField.getText())
    ) {
      JOptionPane.showMessageDialog(null, "Dodano pracownika");
      parentFrame_.refreshEmployeesTable();
    }

  }

  private void onClose() {
    this.dispose();
  }
}
