package videogamesdbmanager.components.frames.manager;

import videogamesdbmanager.controllers.ManagerController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MembersFrame extends JFrame {
  private JPanel mainPanel;
  private JButton addPlayerButton;
  private JButton refreshButton;
  private JButton closeButton;
  private JTable playersTable;

  private final ManagerController controller_;

  public MembersFrame(ManagerController controller) {
    super("Zawodnicy");

    controller_ = controller;

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    addPlayerButton.addActionListener(e -> onAddPlayer());
    refreshButton.addActionListener(e -> refreshPlayersTable());
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

  private void onAddPlayer() {
    SwingUtilities.invokeLater(() -> {
      JFrame addPlayerFrame = new AddPlayerFrame(controller_, this);
      addPlayerFrame.setVisible(true);
    });
  }

  private void onRowSelection(ListSelectionEvent e, ListSelectionModel selectionModel) {
    if (!e.getValueIsAdjusting() && !selectionModel.isSelectionEmpty()) {
      int selectedRowIndex = selectionModel.getMinSelectionIndex();
      String[] playerParams = new String[6];
      for (int i = 0; i < 6; i++) {
        Object param = playersTable.getValueAt(selectedRowIndex, i);
        if (param != null) {
          playerParams[i] = param.toString();
        } else {
          playerParams[i] = null;
        }
      }
      SwingUtilities.invokeLater(() -> {
        ModifyPlayerFrame modifyFrame = new ModifyPlayerFrame(playerParams, controller_, this);
        modifyFrame.setVisible(true);
      });
    }
  }

  private void onClose() {
    this.dispose();
  }
}
