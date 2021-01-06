package videogamesdbmanager.components.frames.roles;

import javax.swing.*;

public class NewChampionshipSheet extends JFrame {
  private JButton registerCsButton;
  private JTextField dateField;
  private JTextField nameField;
  private JTextField localizationField;
  private JTextField rewardField;
  private JComboBox typeComboBox;
  private JPanel newChampionshipPanel;
  private String[] types = {"Mistrzostwa indywidualne", "Mistrzostwa drużynowe"};

  public NewChampionshipSheet() {
    super("Nowe mistrzostwa");

    this.typeComboBox = new JComboBox(types); // nie wiem czy to działa
    this.setContentPane(newChampionshipPanel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.pack();

    registerCsButton.addActionListener(e -> register());
  }

  private void register() {
    String name = nameField.getText();
    String date = dateField.getText();
    String localization = localizationField.getText();
    String reward;
    if (rewardField.getText().isEmpty())
      reward = null;
    else
      reward = localizationField.getText();
    String type = types[typeComboBox.getSelectedIndex()];
  }
}
