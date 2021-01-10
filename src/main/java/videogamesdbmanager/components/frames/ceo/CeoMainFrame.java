package videogamesdbmanager.components.frames.ceo;

import videogamesdbmanager.controllers.CeoController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

public class CeoMainFrame extends JFrame{
  private JPanel mainPanel;
  private JButton manageEmployeesButton;
  private JButton releaseGameButton;
  private JButton deleteAccountButton;

  private CeoController controller_;
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
    deleteAccountButton.addActionListener(e -> onDelete());
  }

  private void onManage() {
    SwingUtilities.invokeLater(() -> {
      JFrame manageFrame = new ManageEmployeesFrame(controller_);
      manageFrame.setVisible(true);
    });
  }

  private void onRelease() {

  }

  private void onDelete() {
    if(controller_.deleteAccount()) {
      this.dispose();
    }
  }

  private void onClose() {
    this.dispose();
  }
}
