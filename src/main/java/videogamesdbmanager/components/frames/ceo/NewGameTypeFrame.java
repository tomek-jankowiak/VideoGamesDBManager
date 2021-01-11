package videogamesdbmanager.components.frames.ceo;

import videogamesdbmanager.controllers.CeoController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewGameTypeFrame extends JFrame {
  private JPanel mainPanel;
  private JTextField typeNameTextField;
  private JButton addButton;

  private final CeoController controller_;
  private final ReleaseGameFrame parentFrame_;

  public NewGameTypeFrame (CeoController controller, ReleaseGameFrame parentFrame) {
    super("Nowy gatunek");

    this.setContentPane(mainPanel);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setResizable(false);
    this.pack();

    controller_ = controller;
    parentFrame_ = parentFrame;

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    addButton.addActionListener(e -> onAdd());
  }

  private void onAdd() {
    String typeName = typeNameTextField.getText();
    if (controller_.addGameType(typeName)) {
      parentFrame_.updateComboBox(typeName);
      onClose();
    }
  }

  private void onClose() {
    this.dispose();
    parentFrame_.setVisible(true);
  }
}
