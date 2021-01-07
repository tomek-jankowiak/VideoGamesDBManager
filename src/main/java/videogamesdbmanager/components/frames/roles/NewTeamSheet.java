package videogamesdbmanager.components.frames.roles;

import videogamesdbmanager.application.Application;
import videogamesdbmanager.components.frames.manager.NewRegionFrame;
import videogamesdbmanager.controllers.NewUserController;
import videogamesdbmanager.error.SqlExceptionHandler;

import java.sql.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.DriverManager;
import java.util.Objects;
import java.util.Properties;

public class NewTeamSheet extends JFrame {
  private JTextField teamNameField;
  private JPanel newTeamPanel;
  private JButton registerTeamButton;
  private JTextField managerNameField;
  private JComboBox<String> regionComboBox;

  private final JFrame parentFrame_;
  private final NewUserController controller_;
  private boolean isNewRegion = false;

  public NewTeamSheet(JFrame parentFrame, NewUserController controller) {
    super("Nowa drużyna");

    parentFrame_ = parentFrame;
    controller_ = controller;

    fillComboBox();
    this.setContentPane(newTeamPanel);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.pack();

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    regionComboBox.addActionListener(e -> onRegionChoose());
    registerTeamButton.addActionListener(e -> onRegister());
  }

  private void fillComboBox() {
    Properties connectionProps = new Properties();
    connectionProps.put("user", "inf141235");
    connectionProps.put("password", "inf141235");

    try {
      Connection adminConnection = DriverManager.getConnection(Application.connectionString, connectionProps);
      Statement statement = adminConnection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT nazwa_regionu FROM regiony");
      while(resultSet.next()) {
        regionComboBox.addItem(resultSet.getString(1));
      }
      resultSet.close();
      statement.close();
      adminConnection.close();
    } catch(SQLException ex) {
      SqlExceptionHandler.handle(ex);
    }

    regionComboBox.addItem("<NOWY REGION>");
  }

  public void updateComboBox(String newRegionName) {
    regionComboBox.removeItem("<NOWY REGION>");
    regionComboBox.addItem(newRegionName);
    isNewRegion = true;
  }

  private void onRegionChoose() {
    if(Objects.equals(regionComboBox.getSelectedItem(), "<NOWY REGION>")) {
      this.setVisible(false);
      JFrame newRegionFrame = new NewRegionFrame(this);
      newRegionFrame.setVisible(true);
    }
  }

  private void onRegister() {
    String managerName = managerNameField.getText();
    String teamName = teamNameField.getText();
    String regionName = Objects.requireNonNull(regionComboBox.getSelectedItem()).toString();

    if(controller_.addManager(managerName, teamName, regionName, isNewRegion)) {
      parentFrame_.dispose();
      this.dispose();
    }
  }

  private void onClose() {
    dispose();
    parentFrame_.setVisible(true);
  }

}
