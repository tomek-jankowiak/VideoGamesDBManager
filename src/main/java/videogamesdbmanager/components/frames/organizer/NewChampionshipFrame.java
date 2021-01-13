package videogamesdbmanager.components.frames.organizer;

import videogamesdbmanager.controllers.OrganizerController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class NewChampionshipFrame extends JFrame {
  private JButton registerButton;
  private JTextField beginDateField;
  private JTextField nameField;
  private JTextField localizationField;
  private JTextField prizeField;
  private JComboBox<String> typeComboBox;
  private JPanel mainPanel;
  private JButton closeButton;
  private JComboBox<String> gameComboBox;
  private JTextField endDateField;

  private final OrganizerController controller_;

  public NewChampionshipFrame(OrganizerController controller) {
    super("Nowe mistrzostwa");

    controller_ = controller;

    fillGameComboBox();
    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    registerButton.addActionListener(e -> onRegister());
    closeButton.addActionListener(e -> onClose());
  }

  private void fillGameComboBox() {
    controller_.setGamesComboBox(gameComboBox);
  }

  private void onRegister() {
    String type;
    if (Objects.equals(typeComboBox.getSelectedItem(), "Indywidualne")) {
      type = "i";
    } else {
      type = "d";
    }

    if (controller_.addChampionships(nameField.getText(),
            beginDateField.getText(),
            endDateField.getText(),
            localizationField.getText(),
            type,
            Objects.requireNonNull(gameComboBox.getSelectedItem()).toString(),
            prizeField.getText()
            )) {
      JOptionPane.showMessageDialog(null, "Zarejestrowano mistrzostwa");
    }
  }

  private void onClose() {
    this.dispose();
  }
}
