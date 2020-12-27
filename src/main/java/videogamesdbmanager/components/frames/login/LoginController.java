package videogamesdbmanager.components.frames.login;


import videogamesdbmanager.application.Application;
import videogamesdbmanager.error.SqlExceptionHandler;

import javax.swing.*;
import java.sql.*;
import java.util.Properties;

public class LoginController {

  private Connection connection_;

  private static final String connectionString_ =
          "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/dblab02_students.cs.put.poznan.pl";

  public boolean login(String username, String password) {
    Properties connectionProps = new Properties();
    connectionProps.put("user", username);
    connectionProps.put("password", password);

    try {
      connection_ = DriverManager.getConnection(connectionString_,
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
              String.format("SELECT typ FROM %s.UZYTKOWNICY WHERE id=USER", Application.ownerID)
      );
      resultSet.next();
      String userType = resultSet.getString(1);
      resultSet.close();
      statement.close();

      if(userType == null) {
        /*SwingUtilities.invokeLater(() -> {
          //TODO
        });*/
        System.out.println("chuuj");
      } else {
        //TODO
        System.out.println("Witaj chuju");
      }
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
    }
  }
}
