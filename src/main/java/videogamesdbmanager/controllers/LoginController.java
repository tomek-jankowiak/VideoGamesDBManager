package videogamesdbmanager.controllers;


import videogamesdbmanager.application.Application;
import videogamesdbmanager.components.frames.ceo.CeoMainFrame;
import videogamesdbmanager.components.frames.roles.RoleSelection;
import videogamesdbmanager.error.SqlExceptionHandler;

import javax.swing.*;
import java.sql.*;
import java.util.Properties;

public class LoginController {

  private Connection connection_;
  private String username_;

  public boolean login(String username, String password) {
    Properties connectionProps = new Properties();
    connectionProps.put("user", username);
    connectionProps.put("password", password);
    username_ = username;

    try {
      connection_ = DriverManager.getConnection(Application.connectionString,
              connectionProps);
      SwingUtilities.invokeLater(this::successfulLoginCallback);
      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  private void successfulLoginCallback() {
    try {
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
              String.format("SELECT %s.Wspolne.PobierzTypUzytkownika FROM dual", Application.ownerID)
      );
      resultSet.next();
      String userType = resultSet.getString(1);
      resultSet.close();
      statement.close();

      if(userType == null) {
        SwingUtilities.invokeLater(() -> {
          JFrame roleFrame = new RoleSelection(connection_, username_);
          roleFrame.setVisible(true);
        });
      } else {
        switch (userType) {
          case "PREZES":
            SwingUtilities.invokeLater(() -> {
              JFrame ceoMainFrame = new CeoMainFrame(connection_);
              ceoMainFrame.setVisible(true);
            });
        }
      }
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
    }
  }
}
