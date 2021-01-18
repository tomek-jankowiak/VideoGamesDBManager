package videogamesdbmanager.components.frames.organizer;

import videogamesdbmanager.controllers.OrganizerController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ParticipantsListFrame extends JFrame {
  private JPanel mainPanel;
  private JButton closeButton;
  private JTable participantsTable;
  private JButton addResultButton;
  private JButton refreshButton;
  private JLabel annotationLabel;

  private final OrganizerController controller_;
  private final String championshipsId_;
  private final String type_;

  public ParticipantsListFrame(OrganizerController controller, String championshipsId, String type) {
    super("Spis uczestników");

    controller_ = controller;
    championshipsId_ = championshipsId;
    type_ = type;

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    refreshParticipantsTable();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    refreshButton.addActionListener(e -> refreshParticipantsTable());
    addResultButton.addActionListener(e -> onAddResult());
    closeButton.addActionListener(e -> onClose());

    if (controller.isOrganizersChampionship(championshipsId_)) {
      addResultButton.setEnabled(true);
      annotationLabel.setVisible(true);
    } else {
      addResultButton.setToolTipText("Nie jesteś organizatorem tych mistrzostw, nie możesz ustalić ich wyniku.");
    }
  }

  private void refreshParticipantsTable() {
    DefaultTableModel model = (DefaultTableModel) participantsTable.getModel();
    model.setRowCount(0);

    if (type_.equals("Drużynowe")) {
      String[] columnNames = {"ID", "Nazwa drużyny", "Miejsce", "Nagroda", "Procent puli"};
      model.setColumnIdentifiers(columnNames);
    } else if (type_.equals("Indywidualne")) {
      String[] columnNames = {"Pseudonim", "Miejsce", "Nagroda", "Procent puli"};
      model.setColumnIdentifiers(columnNames);
    }
    controller_.setParticipantsTable(model, championshipsId_, type_);
    participantsTable.setModel(model);
  }

  private void onAddResult() {
    boolean success = true;
    for (int i = 0; i < participantsTable.getRowCount(); i++) {
      if (!controller_.addParticipantResult(championshipsId_, type_, (DefaultTableModel) participantsTable.getModel(), i)) {
        success = false;
      }
    }
    if (success) {
      JOptionPane.showMessageDialog(null, "Dodano wyniki.");
    }
  }

  private void onClose() {
    this.dispose();
  }
}
