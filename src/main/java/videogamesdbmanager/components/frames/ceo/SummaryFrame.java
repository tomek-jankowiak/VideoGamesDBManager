package videogamesdbmanager.components.frames.ceo;

import videogamesdbmanager.controllers.CeoController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SummaryFrame extends JFrame {
  private JPanel mainPanel;
  private JButton closeButton;
  private JLabel companyNameLabel;
  private JLabel employeesLabel;
  private JLabel gamesLabel;
  private JLabel incomeLabel;

  public SummaryFrame(CeoController controller) {
    super("Podsumowanie");

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    closeButton.addActionListener(e -> onClose());

    companyNameLabel.setText(controller.getCompanyName());
    employeesLabel.setText(String.valueOf(controller.getCompanyEmployeesNumber()));
    gamesLabel.setText(String.valueOf(controller.getCompanyGamesNumber()));
    incomeLabel.setText(String.valueOf(controller.getCompanyIncome()));
  }

  private void onClose() {
    this.dispose();
  }
}
