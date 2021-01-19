package videogamesdbmanager.components.frames.manager;

import videogamesdbmanager.components.frames.manager.ParticipantsListFrame;
import videogamesdbmanager.controllers.ManagerController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class ChampionshipDetailsFrame extends JFrame {
  private JPanel mainPanel;
  private JButton closeButton;
  private JTextField nameField;
  private JTextField beginDateField;
  private JTextField endDateField;
  private JTextField organizerField;
  private JTextField localizationField;
  private JTextField prizeField;
  private JTextField gameField;
  private JComboBox statusComboBox;
  private JTextField typeField;
  private JTextField idField;
  private JButton resultButton;
  private JButton signUpButton;


  private final ManagerController controller_;

  public ChampionshipDetailsFrame(ManagerController controller, String championshipsId) {
    super("Szczegóły mistrzostw");

    controller_ = controller;

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    signUpButton.addActionListener(e -> onSignUp());
    resultButton.addActionListener(e -> onResult());
    closeButton.addActionListener(e -> onClose());

    setTextFields(championshipsId);
  }


  private void setTextFields(String championshipsId) {
    String[] championshipsParams = new String[9];
    controller_.setChampionshipsParams(championshipsId, championshipsParams);
    idField.setText(championshipsId);
    nameField.setText(championshipsParams[0]);
    beginDateField.setText(championshipsParams[1]);
    endDateField.setText(championshipsParams[2]);
    organizerField.setText(championshipsParams[3]);
    localizationField.setText(championshipsParams[4]);
    typeField.setText(championshipsParams[5]);
    prizeField.setText(championshipsParams[6]);
    gameField.setText(championshipsParams[7]);
    statusComboBox.setSelectedItem(championshipsParams[8]);
  }

  private void onSignUp() {
    //TODO
  }

  private void onResult() {
    SwingUtilities.invokeLater(() -> {
      ParticipantsListFrame teamsFrame = new ParticipantsListFrame(controller_, idField.getText(), typeField.getText());
      teamsFrame.setVisible(true);
    });
  }

  private void onClose() {
    this.dispose();
  }
}
