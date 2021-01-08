package videogamesdbmanager.components.frames.roles;

import videogamesdbmanager.controllers.NewUserController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewOrganizerSheet extends JFrame{
  private JPanel newOrganizerPanel;
  private JButton registerOrganizerButton;
  private JTextField organizerNameTextField;

  private JFrame parentFrame_;
  private NewUserController controller_;

  public NewOrganizerSheet(JFrame parentFrame, NewUserController controller) {
    super("Nowy organizator");

    parentFrame_ = parentFrame;
    controller_ = controller;

    this.setContentPane(newOrganizerPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    registerOrganizerButton.addActionListener(e -> onRegister());
  }

  private void onRegister() {
    String organizerName = organizerNameTextField.getText();
    if (controller_.addOrganizer(organizerName)) {
      this.dispose();
      parentFrame_.dispose();
    }
  }

  private void onClose() {
    this.dispose();
    parentFrame_.setVisible(true);
  }
}
