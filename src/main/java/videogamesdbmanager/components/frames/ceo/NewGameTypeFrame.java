package videogamesdbmanager.components.frames.ceo;

import videogamesdbmanager.controllers.CeoController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class NewGameTypeFrame extends JFrame {
  private JPanel mainPanel;
  private JTextField typeNameTextField;
  private JButton addButton;
  private JComboBox<String> supertypeComboBox;

  private final CeoController controller_;
  private final ReleaseGameFrame parentFrame_;

  public NewGameTypeFrame (CeoController controller, ReleaseGameFrame parentFrame) {
    super("Nowy gatunek");

    controller_ = controller;
    parentFrame_ = parentFrame;

    fillComboBox();
    supertypeComboBox.setSelectedItem("<BRAK NADGATUNKU>");
    this.setContentPane(mainPanel);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setResizable(false);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    addButton.addActionListener(e -> onAdd());
  }

  private void fillComboBox() {
    controller_.setGameTypesComboBox(supertypeComboBox);
    supertypeComboBox.addItem("<BRAK NADGATUNKU>");
  }

  private void onAdd() {
    String typeName = typeNameTextField.getText();
    String supertypeName = Objects.requireNonNull(supertypeComboBox.getSelectedItem()).toString();
    if (controller_.addGameType(typeName, supertypeName)) {
      parentFrame_.updateComboBox(typeName);
      onClose();
    }
  }

  private void onClose() {
    this.dispose();
    parentFrame_.setVisible(true);
  }
}
