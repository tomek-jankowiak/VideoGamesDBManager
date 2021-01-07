package videogamesdbmanager.components.frames.manager;

import videogamesdbmanager.components.frames.roles.NewTeamSheet;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewRegionFrame extends JFrame{
  private JTextField regionNameTextField;
  private JButton addRegionButton;
  private JPanel newRegionPanel;

  private final NewTeamSheet parentFrame_;

  public NewRegionFrame(NewTeamSheet parentFrame) {
    super("Nowy region");

    parentFrame_ = parentFrame;

    this.setContentPane(newRegionPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    addRegionButton.addActionListener(e -> onAdd());
  }

  private void onAdd() {
    parentFrame_.updateComboBox(regionNameTextField.getText());
    dispose();
    parentFrame_.setVisible(true);
  }

  private void onClose() {
    this.dispose();
    parentFrame_.setVisible(true);
  }
}
