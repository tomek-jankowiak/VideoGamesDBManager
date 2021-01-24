package videogamesdbmanager.components.frames.ceo;

import videogamesdbmanager.components.elements.FilterComboBox;
import videogamesdbmanager.controllers.CeoController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Objects;

public class ReleaseGameFrame extends JFrame {
  private JPanel mainPanel;
  private JTextField titleTextField;
  private JTextField releaseDateTextField;
  private FilterComboBox typeComboBox;
  private JTextField ageCatTextField;
  private JTextField budgetTextField;
  private JTextField boxOfficeTextField;
  private JButton releaseButton;
  private JPanel typesPanel;

  private final CeoController controller_;

  public ReleaseGameFrame(CeoController controller) {
    super("Wydaj grę");

    controller_ = controller;

    setupGameTypes();
    releaseDateTextField.setText(controller.getSysdate());
    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    releaseButton.addActionListener(e -> onRelease());
    typeComboBox.addActionListener(e -> onTypeChoose());
  }

  public void updateComboBox(String typeName) {
    typeComboBox.removeItem("<NOWY GATUNEK>");
    typeComboBox.addItem(typeName);
    typeComboBox.addItem("<NOWY GATUNEK>");
  }

  private void setupGameTypes() {
    List<String> gameTypes = controller_.getGameTypesList();
    gameTypes.add("<NOWY GATUNEK>");
    typeComboBox = new FilterComboBox(gameTypes);
    for (String gameType : gameTypes) {
      typeComboBox.addItem(gameType);
    }
    typeComboBox.setSelectedIndex(0);
    typesPanel.add(typeComboBox);
  }

  private void onTypeChoose() {
    if (Objects.equals(typeComboBox.getSelectedItem(), "<NOWY GATUNEK>")) {
      this.setVisible(false);
      NewGameTypeFrame newGameTypeFrame = new NewGameTypeFrame(controller_, this);
      newGameTypeFrame.setVisible(true);
    }
  }

  private void onRelease() {
    if (controller_.releaseGame(
            titleTextField.getText(),
            releaseDateTextField.getText(),
            Objects.requireNonNull(typeComboBox.getSelectedItem()).toString(),
            ageCatTextField.getText(),
            boxOfficeTextField.getText(),
            budgetTextField.getText()
    )) {
      JOptionPane.showMessageDialog(null, "Wydano grę.");
      this.dispose();
    }
  }

  private void onClose() {
    this.dispose();
  }

  private void createUIComponents() {
    typesPanel = new JPanel();
  }
}
