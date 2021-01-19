package videogamesdbmanager.components.frames.manager;

import videogamesdbmanager.controllers.ManagerController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
  private JTextField statusField;
  private JTextField typeField;
  private JTextField idField;
  private JButton resultButton;
  private JButton signUpButton;
  //private JTextField statusTextField;


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

    if (statusField.getText().equals("przed rozpoczęciem")){
      signUpButton.setEnabled(true);
    }
    else{
      signUpButton.setEnabled(false);
    }
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
    statusField.setText(championshipsParams[8]);
  }

  private void onSignUp() {
    if (typeField.getText().equals("Drużynowe")) {
      int input = JOptionPane.showConfirmDialog(null,
              "Czy na pewno chcesz zapisać drużynę na te mistrzostwa?",
              "Potwierdź decyzję", JOptionPane.YES_NO_OPTION);

      if (input == 0) {
        controller_.signUpTeam(idField.getText());
      }
    }
    else if (typeField.getText().equals("Indywidualne")) {
      SwingUtilities.invokeLater(() -> {
        SignUpParticipantFrame participantFrame = new SignUpParticipantFrame(controller_, idField.getText());
        participantFrame.setVisible(true);
      });
    }
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
