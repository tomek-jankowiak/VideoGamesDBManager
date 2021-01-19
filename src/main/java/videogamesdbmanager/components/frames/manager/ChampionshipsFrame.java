package videogamesdbmanager.components.frames.manager;

import videogamesdbmanager.controllers.ManagerController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChampionshipsFrame extends JFrame {
  private JTable championshipsTable;
  private JButton refreshButton;
  private JRadioButton allParticipationRadioButton;
  private JRadioButton takingPartRadioButton;
  private JRadioButton notTakingPartRadioButton;
  private JButton closeButton;
  private JPanel mainPanel;
  private JRadioButton allTypeRadioButton;
  private JRadioButton individualRadioButton;
  private JRadioButton teamRadioButton;

  private final ManagerController controller_;

  private ButtonGroup participationButtonGroup = new ButtonGroup();
  private ButtonGroup typeButtonGroup = new ButtonGroup();

  public ChampionshipsFrame(ManagerController controller) {
    super("Przegląd mistrzostw");

    controller_ = controller;

    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.pack();

    allParticipationRadioButton.setActionCommand("0");
    takingPartRadioButton.setActionCommand("1");
    notTakingPartRadioButton.setActionCommand("-1");
    participationButtonGroup.add(allParticipationRadioButton);
    participationButtonGroup.add(takingPartRadioButton);
    participationButtonGroup.add(notTakingPartRadioButton);
    allParticipationRadioButton.setSelected(true);

    allTypeRadioButton.setActionCommand("Wszystkie");
    individualRadioButton.setActionCommand("Indywidualne");
    teamRadioButton.setActionCommand("Drużynowe");
    typeButtonGroup.add(allTypeRadioButton);
    typeButtonGroup.add(individualRadioButton);
    typeButtonGroup.add(teamRadioButton);
    allTypeRadioButton.setSelected(true);

    refreshChampionshipsTable();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    allParticipationRadioButton.addActionListener(e -> refreshChampionshipsTable());
    takingPartRadioButton.addActionListener(e -> refreshChampionshipsTable());
    notTakingPartRadioButton.addActionListener(e -> refreshChampionshipsTable());
    allTypeRadioButton.addActionListener(e -> refreshChampionshipsTable());
    individualRadioButton.addActionListener(e -> refreshChampionshipsTable());
    teamRadioButton.addActionListener(e -> refreshChampionshipsTable());
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

    controller_.setChampionshipsTable(model,
            Integer.parseInt(participationButtonGroup.getSelection().getActionCommand()),
            typeButtonGroup.getSelection().getActionCommand());

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
