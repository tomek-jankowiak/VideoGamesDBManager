package videogamesdbmanager.components.frames.manager;

import videogamesdbmanager.controllers.ManagerController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ParticipantsListFrame extends JFrame {
  private JPanel mainPanel;
  private JTable participantsTable;
  //private JButton addParticipantButton;
  private JButton refreshButton;
  private JButton closeButton;

  private final ManagerController controller_;
  private final String championshipsId_;
  private final String type_;

  public ParticipantsListFrame(ManagerController controller, String championshipsId, String type) {
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
    //addParticipantButton.addActionListener(e -> onAddParticipant());
    closeButton.addActionListener(e -> onClose());

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

  //private void onAddParticipant() {
    //NOTTODO
  //}

  private void onClose() {
    this.dispose();
  }
}
