package videogamesdbmanager.components.frames.manager;

import videogamesdbmanager.controllers.ManagerController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ModifyPlayerFrame extends JFrame {
  private JButton closeButton;
  private JButton deleteButton;
  private JButton modifyButton;
  private JTextField nickTextField;
  private JTextField nameTextField;
  private JTextField surnameTextField;
  private JTextField salaryTextField;
  private JTextField birthDateTextField;
  private JTextField countryTextField;
  private JPanel mainPanel;

  private final ManagerController controller_;
  private final MembersFrame parentFrame_;

  ModifyPlayerFrame(String[] playerParams, ManagerController controller, MembersFrame parentFrame){
    super("Zarządzaj członkiem");

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

    setTextFields(playerParams);
  }

  private void setTextFields(String[] playerParams) {
    nickTextField.setText(playerParams[0]);
    nameTextField.setText(playerParams[1]);
    surnameTextField.setText(playerParams[2]);
    countryTextField.setText(playerParams[3]);
    birthDateTextField.setText(playerParams[4]);
    salaryTextField.setText(playerParams[5]);
  }

  private void onModify() {
    String nick = nickTextField.getText();
    String salary = salaryTextField.getText();
    if (controller_.modifyPlayer(nick, salary)) {
      JOptionPane.showMessageDialog(null, "Zmodyfikowano zawodnika");
      parentFrame_.refreshPlayersTable();
    }
  }

  private void onDelete() {
    String nick = nickTextField.getText();
    if (controller_.deletePlayer(nick)) {
      JOptionPane.showMessageDialog(null, "Usunięto zawodnika");
      onClose();
    }
  }

  private void onClose() {
    this.dispose();
    parentFrame_.refreshPlayersTable();
  }
}
