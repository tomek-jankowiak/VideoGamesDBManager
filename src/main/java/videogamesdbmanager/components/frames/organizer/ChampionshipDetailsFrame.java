package videogamesdbmanager.components.frames.organizer;

import videogamesdbmanager.controllers.OrganizerController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class ChampionshipDetailsFrame extends JFrame {
  private JPanel mainPanel;
  private JButton closeButton;
  private JTextField idField;
  private JTextField nameField;
  private JTextField beginDateField;
  private JTextField endDateField;
  private JTextField organizerField;
  private JTextField localizationField;
  private JTextField typeField;
  private JTextField prizeField;
  private JTextField gameField;
  private JButton confirmButton;
  private JButton editButton;
  private JComboBox<String> statusComboBox;

  private final OrganizerController controller_;

  public ChampionshipDetailsFrame(OrganizerController controller, String championshipsId) {
    super("Szcegóły mistrzostw");

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

    editButton.addActionListener(e -> onEdit());
    confirmButton.addActionListener(e -> onConfirm());
    closeButton.addActionListener(e -> onClose());

    setTextFields(championshipsId);
    if (controller_.isOrganizersChampionship(championshipsId)) {
      editButton.setEnabled(true);
      confirmButton.setVisible(true);
    } else {
      editButton.setToolTipText("Nie jesteś organizatorem tych mistrzostw, nie możesz ich edytować");
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
    statusComboBox.setSelectedItem(championshipsParams[8]);
  }

  private void onEdit() {
    nameField.setEditable(true);
    beginDateField.setEditable(true);
    endDateField.setEditable(true);
    localizationField.setEditable(true);
    prizeField.setEditable(true);
    statusComboBox.setEnabled(true);
    confirmButton.setEnabled(true);
    editButton.setEnabled(false);
  }

  private void onConfirm() {
    if (controller_.modifyChampionships(idField.getText(),
            nameField.getText(),
            beginDateField.getText(),
            endDateField.getText(),
            localizationField.getText(),
            typeField.getText(),
            prizeField.getText(),
            Objects.requireNonNull(statusComboBox.getSelectedItem()).toString())) {
      JOptionPane.showMessageDialog(null, "Zmodyfikowano mistrzostwa.");
    }
  }

  private void onClose() {
    this.dispose();
  }
}
