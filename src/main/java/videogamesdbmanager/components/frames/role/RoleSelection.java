package videogamesdbmanager.components.frames.role;

import javax.swing.*;

public class RoleSelection extends JFrame {
  private JButton organizatorMistrzostwButton;
  private JButton prezesStudiaButton;
  private JButton menadzerDruzynyButton;
  private JButton pracownikButton;
  private JPanel rolePanel;

  public RoleSelection() {
    super("Wybór roli");

    this.setContentPane(rolePanel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.pack();
  }
}
