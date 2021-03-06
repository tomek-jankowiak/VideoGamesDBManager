package videogamesdbmanager.components.frames.roles;

import videogamesdbmanager.controllers.NewUserController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class RoleSelection extends JFrame {
  private JButton organizerButton;
  private JButton ceoButton;
  private JButton managerButton;
  private JPanel rolePanel;

  private final NewUserController controller_;

  public RoleSelection(Connection connection, String username) {
    super("Wybór roli");

    this.setContentPane(rolePanel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });
    organizerButton.addActionListener(e -> onOrganizer());
    ceoButton.addActionListener(e -> onCEO());
    managerButton.addActionListener(e -> onManager());

    controller_ = new NewUserController(connection, username);
  }

  private void onOrganizer() {
    this.setVisible(false);
    JFrame newOrganizerFrame = new NewOrganizerSheet(this, controller_);
    newOrganizerFrame.setVisible(true);
  }

  private void onCEO() {
    this.setVisible(false);
    JFrame newCeoFrame = new NewStudioSheet(this, controller_);
    newCeoFrame.setVisible(true);
  }

  private void onManager() {
    this.setVisible(false);
    JFrame newManagerFrame = new NewTeamSheet(this, controller_);
    newManagerFrame.setVisible(true);
  }

  private void onClose() {
    if (controller_.closeConnection()) {
      this.dispose();
    }
  }
}
