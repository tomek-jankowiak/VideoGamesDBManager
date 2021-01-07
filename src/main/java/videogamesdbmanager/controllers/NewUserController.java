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

    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
        String.format(
                "BEGIN " +
                        "%s.Wspolne.DodajPrezesa(?);" +
                "END;",
                Application.ownerID
        )
      );
      preparedStatement.setString(1, ceoName);
      preparedStatement.execute();
      preparedStatement.close();

      Statement statement = connection_.createStatement();
      statement.execute("SET ROLE Prezes_rola");
      statement.close();

      preparedStatement = connection_.prepareStatement(
        String.format(
                "BEGIN " +
                        "%s.Prezes.NoweStudio(?, TO_DATE(?, 'DD-MM-YYYY'), ?);" +
                "END;",
                Application.ownerID
        )
      );
      preparedStatement.setString(1, studioName);
      preparedStatement.setString(2, creationDate);
      preparedStatement.setString(3, ceoName);
      preparedStatement.execute();
      preparedStatement.close();

      return true;

    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  public boolean addManager(String managerName, String teamName, String region, boolean isNewRegion) {
    if (!grantRole("Menadzer_rola")) {
      return false;
    }

    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
        String.format(
                "BEGIN " +
                        "%s.Wspolne.DodajMenadzera(?);" +
                        "END;",
                Application.ownerID
        )
      );
      preparedStatement.setString(1, managerName);
      preparedStatement.execute();
      preparedStatement.close();
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }

    if (isNewRegion) {
      if (!addRegion(region)) {
        return false;
      }
    }

    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
        String.format(
                "BEGIN " +
                        "%s.Menadzer.NowaDruzyna(?, ?, ?);" +
                        "END;",
                Application.ownerID
        )
      );
      preparedStatement.setString(1, teamName);
      preparedStatement.setString(2, managerName);
      preparedStatement.setString(3, region);
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  private boolean addRegion(String regionName) {
    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
        String.format(
                "BEGIN " +
                        "%s.Menadzer.NowyRegion(?);" +
                        "END;",
                Application.ownerID
        )
      );
      preparedStatement.setString(1, regionName);
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
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
