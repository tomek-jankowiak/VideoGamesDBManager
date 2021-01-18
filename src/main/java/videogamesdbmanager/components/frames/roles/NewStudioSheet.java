package videogamesdbmanager.components.frames.roles;

import videogamesdbmanager.controllers.NewUserController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewStudioSheet extends JFrame {
  private JButton registerStudioButton;
  private JTextField studioNameField;
  private JPanel newStudioPanel;
  private JTextField ceoNameField;
  private JTextField creationDateField;

  private final JFrame parentFrame_;
  private final NewUserController controller_;

  public NewStudioSheet(JFrame parentFrame, NewUserController controller) {
    super("Nowe studio");

    parentFrame_ = parentFrame;
    controller_ = controller;

    this.setContentPane(newStudioPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.pack();

    registerStudioButton.addActionListener(e -> onRegister());

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });
  }

  private void onRegister() {
    String ceoName = ceoNameField.getText();
    String studioName = studioNameField.getText();
    String creationDate = creationDateField.getText();

    if(controller_.addCEO(ceoName, studioName, creationDate)) {
      parentFrame_.dispose();
      this.dispose();
    } else {
      controller_.deleteAccount();
    }
  }

  private void onClose() {
    dispose();
    parentFrame_.setVisible(true);
  }
}
