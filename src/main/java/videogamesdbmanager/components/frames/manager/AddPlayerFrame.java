package videogamesdbmanager.components.frames.manager;

import videogamesdbmanager.controllers.ManagerController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddPlayerFrame extends JFrame {
  private JButton addButton;
  private JButton closeButton;
  private JTextField nickTextField;
  private JTextField nameTextField;
  private JTextField surnameTextField;
  private JTextField birthDateTextField;
  private JTextField salaryTextField;
  private JTextField countryTextField;
  private JPanel mainPanel;

  private final ManagerController controller_;
  private final MembersFrame parentFrame_;

  public AddPlayerFrame(ManagerController controller, MembersFrame parentFrame) {
    super("Dodaj zawodnika");

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
    if (controller_.addPlayer(
            nickTextField.getText(),
            nameTextField.getText(),
            surnameTextField.getText(),
            countryTextField.getText(),
            birthDateTextField.getText(),
            salaryTextField.getText())
    ) {
      JOptionPane.showMessageDialog(null, "Dodano pracownika");
      parentFrame_.refreshPlayersTable();
    }
  }

  private void onClose() {
    this.dispose();
  }
}
