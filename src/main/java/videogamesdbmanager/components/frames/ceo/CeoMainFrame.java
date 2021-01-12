package videogamesdbmanager.components.frames.ceo;

import videogamesdbmanager.controllers.CeoController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

public class CeoMainFrame extends JFrame {
  private JPanel mainPanel;
  private JButton manageEmployeesButton;
  private JButton releaseGameButton;
  private JButton deleteAccountButton;
  private JButton browseGamesButton;
  private JButton summaryButton;
  private JButton logoutButton;

  private final CeoController controller_;

  public CeoMainFrame(Connection connection) {
    super("Zarządzanie studiem");

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    controller_ = new CeoController(connection);

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    manageEmployeesButton.addActionListener(e -> onManage());
    releaseGameButton.addActionListener(e -> onRelease());
    browseGamesButton.addActionListener(e -> onBrowse());
    summaryButton.addActionListener(e -> onSummary());
    deleteAccountButton.addActionListener(e -> onDelete());
    logoutButton.addActionListener(e -> onClose());
  }

  private void onManage() {
    SwingUtilities.invokeLater(() -> {
      JFrame manageFrame = new ManageEmployeesFrame(controller_);
      manageFrame.setVisible(true);
    });
  }

  private void onRelease() {
    SwingUtilities.invokeLater(() -> {
      JFrame releaseGameFrame = new ReleaseGameFrame(controller_);
      releaseGameFrame.setVisible(true);
    });
  }

  private void onBrowse() {
    SwingUtilities.invokeLater(() -> {
      JFrame browseGamesFrame = new BrowseGamesFrame(controller_);
      browseGamesFrame.setVisible(true);
    });
  }

  private void onSummary() {
    SwingUtilities.invokeLater(() -> {
      JFrame summaryFrame = new SummaryFrame(controller_);
      summaryFrame.setVisible(true);
    });
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
