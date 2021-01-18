package videogamesdbmanager.components.frames.organizer;

import videogamesdbmanager.controllers.OrganizerController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BrowseChampionshipsFrame extends JFrame {
  private JPanel mainPanel;
  private JTable championshipsTable;
  private JButton closeButton;
  private JButton refreshButton;
  private JRadioButton ownRadioButton;
  private JRadioButton allRadioButton;

  private final OrganizerController controller_;

  public BrowseChampionshipsFrame(OrganizerController controller) {
    super("Przegląd mistrzostw");

    controller_ = controller;

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(allRadioButton);
    buttonGroup.add(ownRadioButton);

    refreshChampionshipsTable();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    allRadioButton.addActionListener(e -> refreshChampionshipsTable());
    ownRadioButton.addActionListener(e -> refreshChampionshipsTable());
    refreshButton.addActionListener(e -> refreshChampionshipsTable());
    closeButton.addActionListener(e -> onClose());

    ListSelectionModel selectionModel = championshipsTable.getSelectionModel();
    selectionModel.addListSelectionListener(e -> onRowSelection(e, selectionModel));
  }

  private void refreshChampionshipsTable() {
    String[] columnNames = {"ID", "Nazwa", "Gra", "Typ", "Pula nagród", "Status"};
    DefaultTableModel model = (DefaultTableModel)championshipsTable.getModel();
    model.setRowCount(0);
    model.setColumnIdentifiers(columnNames);

    controller_.setChampionshipsTable(model, ownRadioButton.isSelected());
    championshipsTable.setModel(model);
  }

  private void onRowSelection(ListSelectionEvent e, ListSelectionModel selectionModel) {
    if (!e.getValueIsAdjusting() && !selectionModel.isSelectionEmpty()) {
      int selectedRowIndex = selectionModel.getMinSelectionIndex();
      String championshipsId = championshipsTable.getValueAt(selectedRowIndex, 0).toString();
      SwingUtilities.invokeLater(() -> {
        ChampionshipDetailsFrame detailsFrame = new ChampionshipDetailsFrame(controller_, championshipsId);
        detailsFrame.setVisible(true);
      });
    }
  }

  private void onClose() {
    this.dispose();
  }
}
