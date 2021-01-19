package videogamesdbmanager.components.frames.manager;


import videogamesdbmanager.controllers.ManagerController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

public class ManagerMainFrame extends JFrame {
  private JButton deleteAccountButton;
  private JButton manageMembersButton;
  private JButton applyToChampsButton;
  private JPanel mainPanel;
  private JButton logoutButton;
  private JButton champsButton;

  private final ManagerController controller_;

  public ManagerMainFrame(Connection connection) {
    super("Konto menadżera");

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    controller_ = new ManagerController(connection);

    manageMembersButton.addActionListener(e -> onManage());
    //applyToChampsButton.addActionListener(e -> onApply());
    deleteAccountButton.addActionListener(e -> onDelete());
    champsButton.addActionListener(e -> onChamps());
    logoutButton.addActionListener(e -> onClose());
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });
  }

  private void onManage() {
    SwingUtilities.invokeLater(() -> {
      JFrame manageFrame = new MembersFrame(controller_);
      manageFrame.setVisible(true);
    });
  }

  private void onChamps() {
    SwingUtilities.invokeLater(() -> {
      JFrame champsFrame = new ChampionshipsFrame(controller_);
      champsFrame.setVisible(true);
    });
  }

  private void onApply() {
    //NOTTODO
  }

  private void onDelete() {
    if (controller_.deleteAccount()) {
      this.dispose();
    }
  }

  private void onClose() {
    if (controller_.closeConnection()) {
      this.dispose();
    }
  }
}
