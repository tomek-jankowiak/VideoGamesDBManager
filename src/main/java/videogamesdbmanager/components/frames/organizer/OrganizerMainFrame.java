package videogamesdbmanager.components.frames.organizer;

import videogamesdbmanager.controllers.OrganizerController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

public class OrganizerMainFrame extends JFrame {
  private JPanel mainPanel;
  private JButton createChampionshipButton;
  private JButton logoutButton;
  private JButton browseChampionshipButton;
  private JButton summaryButton;
  private JButton deleteAccountButton;

  private final OrganizerController controller_;

  public OrganizerMainFrame(Connection connection) {
    super("Konto organizatora");

    controller_ = new OrganizerController(connection);

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    createChampionshipButton.addActionListener(e -> onAdd());
    browseChampionshipButton.addActionListener(e -> onBrowse());
    summaryButton.addActionListener(e -> onSummary());
    deleteAccountButton.addActionListener(e -> onDelete());
    logoutButton.addActionListener(e -> onClose());
  }

  private void onAdd() {
    SwingUtilities.invokeLater(() -> {
      NewChampionshipFrame newChampionship = new NewChampionshipFrame(controller_);
      newChampionship.setVisible(true);
    });
  }

  private void onBrowse() {
    SwingUtilities.invokeLater(() -> {
      BrowseChampionshipsFrame browseFrame = new BrowseChampionshipsFrame(controller_);
      browseFrame.setVisible(true);
    });
  }

  private void onSummary() {
    SwingUtilities.invokeLater(() -> {
      SummaryFrame summaryFrame = new SummaryFrame(controller_);
      summaryFrame.setVisible(true);
    });
  }

  private void onDelete() {
    if (controller_.deleteAccount()) {
      onClose();
    }
  }

  private void onClose() {
    if (controller_.closeConnection()) {
      this.dispose();
    }
  }
}
