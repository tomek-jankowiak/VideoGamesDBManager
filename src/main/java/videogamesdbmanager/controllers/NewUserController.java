package videogamesdbmanager.controllers;

import videogamesdbmanager.application.Application;
import videogamesdbmanager.error.SqlExceptionHandler;

import java.sql.*;
import java.util.Properties;

public class NewUserController {

  private final Connection connection_;
  private final String username_;

  private static final String adminUsername_ = "inf141235";
  private static final String adminPassword_ = "inf141235";

  public NewUserController(Connection connection, String username) {
    connection_ = connection;
    username_ = username;
  }

  public boolean addOrganizer() {
    if(!grantRole("Organizator_rola")) {
      return false;
    }
    //TODO
    return true;
  }

  public boolean addCEO(String ceoName, String studioName, String creationDate) {
    if(!grantRole("Prezes_rola")) {
      return false;
    }
    //TODO
    return true;
  }

  public boolean addManager() {
    if(!grantRole("Menadzer_rola")) {
      return false;
    }
    //TODO
    return true;
  }

  private boolean grantRole(String role) {
    Properties connectionProps = new Properties();
    connectionProps.put("user", adminUsername_);
    connectionProps.put("password", adminPassword_);

    try {
      Connection adminConnection = DriverManager.getConnection(Application.connectionString, connectionProps);

      Statement statement = adminConnection.createStatement();
      statement.execute(
        String.format(
                "GRANT %s TO %s",
                role, username_
        )
      );
      statement.close();
      adminConnection.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }
}
