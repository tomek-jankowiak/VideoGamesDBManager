package videogamesdbmanager.components.frames.organizer;

import videogamesdbmanager.components.elements.FilterComboBox;
import videogamesdbmanager.controllers.OrganizerController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
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
  private FilterComboBox gamesComboBox;
  private JTextField endDateField;
  private JPanel gamesPanel;

  private final OrganizerController controller_;

  public NewChampionshipFrame(OrganizerController controller) {
    super("Nowe mistrzostwa");

    controller_ = controller;

    setupGames();

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

  private void setupGames() {
    List<String> games = controller_.getGamesList();
    gamesComboBox = new FilterComboBox(games);

    for (String game : games) {
      gamesComboBox.addItem(game);
    }
    gamesComboBox.setSelectedIndex(0);
    gamesPanel.add(gamesComboBox);
  }

  private void onRegister() {
    if (controller_.addChampionships(nameField.getText(),
            beginDateField.getText(),
            endDateField.getText(),
            localizationField.getText(),
            Objects.requireNonNull(typeComboBox.getSelectedItem()).toString(),
            Objects.requireNonNull(gamesComboBox.getSelectedItem()).toString(),
            prizeField.getText()
            )) {
      JOptionPane.showMessageDialog(null, "Zarejestrowano mistrzostwa");
    }
  }

  private void onClose() {
    this.dispose();
  }

  private void createUIComponents() {
    gamesPanel = new JPanel();
  }
}
