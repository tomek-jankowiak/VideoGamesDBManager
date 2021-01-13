package videogamesdbmanager.components.frames.organizer;

import videogamesdbmanager.controllers.OrganizerController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChampionshipDetailsFrame extends JFrame {
  private JPanel mainPanel;
  private JButton closeButton;
  private JTextField nameField;
  private JTextField beginDateField;
  private JTextField endDateField;
  private JTextField organizerField;
  private JTextField localizationField;
  private JTextField prizeField;
  private JTextField gameField;
  private JTextField statusField;

  private final OrganizerController controller_;

  public ChampionshipDetailsFrame(OrganizerController controller, String[] championshipsKey) {
    super("Szcegóły mistrzostw");

    controller_ = controller;

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

    setTextFields(championshipsKey);
  }

  private void setTextFields(String[] championshipsKey) {
    String[] championshipsParams = new String[8];
    controller_.setChampionshipsParams(championshipsKey, championshipsParams);
    nameField.setText(championshipsParams[0]);
    beginDateField.setText(championshipsParams[1]);
    endDateField.setText(championshipsParams[2]);
    organizerField.setText(championshipsParams[3]);
    localizationField.setText(championshipsParams[4]);
    prizeField.setText(championshipsParams[5]);
    gameField.setText(championshipsParams[6]);
    statusField.setText(championshipsParams[7]);
  }

  private void onClose() {
    this.dispose();
  }
}
