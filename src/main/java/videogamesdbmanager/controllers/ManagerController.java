package videogamesdbmanager.controllers;

import videogamesdbmanager.application.Application;
import videogamesdbmanager.error.SqlExceptionHandler;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ManagerController {

  private final Connection connection_;
  private final int team_id_;

  public ManagerController(Connection connection) {
    connection_ = connection;
    team_id_ = getTeamId();
  }

  private int getTeamId(){
    try {
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
              String.format("SELECT %s.Menadzer.PobierzIdDruzyny FROM dual", Application.ownerID)
      );
      resultSet.next();
      return resultSet.getInt(1);
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return -1;
    }
  }
  public ResultSet getPlayers() {
    try {
      Statement statement = connection_.createStatement();
      return statement.executeQuery(
              String.format(
                      "SELECT * " +
                              "FROM %s.zawodnicy " +
                              "WHERE druzyna_id = '%d'",
                      Application.ownerID, team_id_
              )
      );
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }

  public void setPlayersTable(DefaultTableModel tableModel) {
    ResultSet playersSet = getPlayers();
    try {
      while (playersSet.next()) {
        Object[] objects = new Object[6];
        for (int i = 0; i < 6; i++) {
          objects[i] = playersSet.getObject(i + 1);
        }
        tableModel.addRow(objects);
      }
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
    }
  }
  public boolean addPlayer(String nick, String name, String surname,
                             String country, String birthDate, Double salary) {
    System.out.println(team_id_);
    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
              String.format(
                      "BEGIN " +
                              "%s.Menadzer.DodajZawodnika(?, ?, ?, ?, TO_DATE(?, 'DD-MM-YYYY'), ?, ?);" +
                              "END;",
                      Application.ownerID
              )
      );
      preparedStatement.setString(1, nick);
      preparedStatement.setString(2, name);
      preparedStatement.setString(3, surname);
      preparedStatement.setString(4, country);
      preparedStatement.setString(5, birthDate);
      preparedStatement.setDouble(7, salary);
      preparedStatement.setInt(6, team_id_);
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  public boolean modifyPlayer(String nick, Double salary) {
    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
              String.format(
                      "BEGIN " +
                              "%s.Menadzer.ModyfikujZawodnika(?, ?);" +
                              "END;",
                      Application.ownerID
              )
      );
      preparedStatement.setString(1, nick);
      preparedStatement.setDouble(2, salary);
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }
  public boolean deletePlayer(String nick) {
    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
              String.format(
                      "BEGIN " +
                              "%s.Menadzer.UsunZawodnika(?);" +
                              "END;",
                      Application.ownerID
              )
      );
      preparedStatement.setString(1, nick);
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  public boolean deleteAccount() {
    try {
      Statement statement = connection_.createStatement();
      statement.execute(
              String.format(
                      "BEGIN " +
                              "%s.Wspolne.UsunKonto();" +
                              "END;",
                      Application.ownerID
              )
      );
      statement.close();
      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  public boolean closeConnection() {
    try {
      connection_.close();
      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }
}
