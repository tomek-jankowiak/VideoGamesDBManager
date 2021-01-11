package videogamesdbmanager.components.frames.ceo;

import videogamesdbmanager.controllers.CeoController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BrowseGamesFrame extends JFrame {
  private JPanel mainPanel;
  private JTable gamesTable;
  private JRadioButton allRadioButton;
  private JRadioButton ownRadioButton;
  private JButton closeButton;

  private final CeoController controller_;

  public BrowseGamesFrame(CeoController controller) {
    super("Przegląd gier");

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

    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(allRadioButton);
    buttonGroup.add(ownRadioButton);

    allRadioButton.addActionListener(e -> refreshGamesTable(true));
    ownRadioButton.addActionListener(e -> refreshGamesTable(false));
    closeButton.addActionListener(e -> onClose());

    refreshGamesTable(allRadioButton.isSelected());
  }

  public void refreshGamesTable(boolean allGames) {
    String[] columnNames = {"Tytuł", "Data wydania", "Kategoria wiekowa", "Gatunek", "Studio", "Budżet", "Box office"};
    DefaultTableModel model = (DefaultTableModel)gamesTable.getModel();
    model.setRowCount(0);
    model.setColumnIdentifiers(columnNames);

    controller_.setGamesTable(model, allGames);
    gamesTable.setModel(model);
  }

  private void onClose() {
    this.dispose();
  }
}
