package videogamesdbmanager.components.frames.organizer;

import videogamesdbmanager.controllers.OrganizerController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SummaryFrame extends JFrame {
  private JPanel mainPanel;
  private JButton closeButton;
  private JLabel prizeSumLabel;
  private JLabel championshipNumberLabel;
  private JLabel organizerNameLabel;

  public SummaryFrame(OrganizerController controller) {
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

    organizerNameLabel.setText(controller.getOrganizerName());
    championshipNumberLabel.setText(String.valueOf(controller.getChampionshipNumber()));
    prizeSumLabel.setText(String.valueOf(controller.getPrizeSum()));
  }

  private void onClose() {
    this.dispose();
  }
}
