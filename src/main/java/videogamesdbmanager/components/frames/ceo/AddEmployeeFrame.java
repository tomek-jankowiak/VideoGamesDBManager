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

  private CeoController controller_;

  public AddEmployeeFrame(CeoController controller) {
    super("Dodaj pracownika");

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

    addButton.addActionListener(e -> onAdd());
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
      this.dispose();
      JOptionPane.showMessageDialog(null, "Dodano pracownika");
    }

  }

  private void onClose() {
    this.dispose();
  }
}
