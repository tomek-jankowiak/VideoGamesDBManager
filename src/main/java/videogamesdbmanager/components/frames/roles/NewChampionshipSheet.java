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

  public NewChampionshipSheet() {
    super("Nowe mistrzostwa");

    this.setContentPane(newChampionshipPanel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.pack();

    registerCsButton.addActionListener(e -> register());
  }

  private void register() {
    //TODO
  }
}
