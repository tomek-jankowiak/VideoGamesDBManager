package videogamesdbmanager.components.frames.roles;

import javax.swing.*;

public class NewStudioSheet extends JFrame {
  private JButton registerStudioButton;
  private JTextField studioNameField;
  private JPanel newStudioPanel;

  public NewStudioSheet() {
    super("Nowa drużyna");

    this.setContentPane(newStudioPanel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.pack();

    registerStudioButton.addActionListener(e -> register());
  }

  private void register() {
    //TODO
  }
}
