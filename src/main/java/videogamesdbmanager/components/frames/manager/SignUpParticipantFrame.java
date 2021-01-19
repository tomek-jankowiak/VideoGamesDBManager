package videogamesdbmanager.components.frames.manager;

import videogamesdbmanager.controllers.ManagerController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SignUpParticipantFrame extends JFrame {
  private JTable playersTable;
  private JButton closeButton;
  private JPanel mainPanel;

  private final ManagerController controller_;
  private final String champ_id_;

  public SignUpParticipantFrame(ManagerController controller, String champ_id) {
    super("Rejestracja zawodnika");

    controller_ = controller;
    champ_id_ = champ_id;

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    closeButton.addActionListener(e -> onClose());
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    ListSelectionModel selectionModel = playersTable.getSelectionModel();
    selectionModel.addListSelectionListener(e -> onRowSelection(e, selectionModel));

    refreshPlayersTable();
  }
  public void refreshPlayersTable() {
    String[] columnNames = {"Pseudonim", "Imię", "Nazwisko", "Kraj", "Data urodzenia", "Płaca"};
    DefaultTableModel model = (DefaultTableModel)playersTable.getModel();
    model.setRowCount(0);
    model.setColumnIdentifiers(columnNames);

    controller_.setPlayersTable(model);
    playersTable.setModel(model);
  }

  private void onRowSelection(ListSelectionEvent e, ListSelectionModel selectionModel) {
    if (!e.getValueIsAdjusting() && !selectionModel.isSelectionEmpty()) {
      int selectedRowIndex = selectionModel.getMinSelectionIndex();

      String[] playerParams = new String[3];
      for (int i = 0; i < 3; i++) {
        Object param = playersTable.getValueAt(selectedRowIndex, i);
        if (param != null) {
          playerParams[i] = param.toString();
        } else {
          playerParams[i] = null;
        }
      }

      int input = JOptionPane.showConfirmDialog(null,
              String.format("Czy na pewno chcesz zapisać zawodnika * %s %s \"%s\" * na te mistrzostwa?",
                              playerParams[1], playerParams[2], playerParams[0]),
              "Potwierdź decyzję", JOptionPane.YES_NO_OPTION);

      if (input == 0) {
        controller_.signUpPlayer(playerParams[2], champ_id_);
      }
    }
  }

  private void onClose() {
    this.dispose();
  }
}
