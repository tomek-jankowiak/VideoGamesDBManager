package videogamesdbmanager.components.frames.roles;

import javax.swing.*;

public class NewTeamSheet extends JFrame {
  private JTextField teamNameField;
  private JTextField teamRegionField;
  private JPanel newTeamPanel;
  private JButton registerTeamButton;
  private JLabel teamNameLabel;

  public NewTeamSheet() {
    super("Nowa drużyna");

    this.setContentPane(newTeamPanel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.pack();

    registerTeamButton.addActionListener(e -> register());
  }

  private void register() {
    String name = teamNameField.getText();
    String region = teamRegionField.getText();
  }
}
