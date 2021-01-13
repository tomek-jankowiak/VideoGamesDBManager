package videogamesdbmanager.controllers;

import videogamesdbmanager.application.Application;
import videogamesdbmanager.error.SqlExceptionHandler;

import javax.swing.*;
import java.sql.*;

public class OrganizerController {
  private final Connection connection_;

  public OrganizerController(Connection connection) {
    connection_ = connection;
  }

  public String getOrganizerName() {
    try {
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
              String.format("SELECT %s.Organizator.PobierzNazweOrganizatora FROM dual", Application.ownerID)
      );
      resultSet.next();
      String organizer = resultSet.getString(1);
      resultSet.close();
      statement.close();

      return organizer;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }

  public boolean addChampionships(String name, String date, String localization,
                                  String type, String game, String prize) {
    try {
     PreparedStatement preparedStatement = connection_.prepareStatement(
       String.format(
               "BEGIN " +
                       "%s.Organizator.NoweMistrzostwa(?, TO_DATE(?, 'DD-MM-YYYY'), ?, ?, ?, ?, ?);" +
               "END;",
               Application.ownerID
       )
     );
     preparedStatement.setString(1, name);
     preparedStatement.setString(2, date);
     preparedStatement.setString(3, getOrganizerName());
     preparedStatement.setString(4, localization);
     preparedStatement.setString(5, type);
     preparedStatement.setString(6, game);
     if (!prize.isEmpty()) {
       preparedStatement.setDouble(7, Double.parseDouble(prize));
     } else {
       preparedStatement.setString(7, null);
     }
     preparedStatement.execute();
     preparedStatement.close();

     return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(null,
              "Niepoprawny format liczbowy", "Błąd", JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  public ResultSet getGames() {
    try {
      Statement statement = connection_.createStatement();
      return statement.executeQuery(
              String.format("SELECT tytul FROM %s.gry", Application.ownerID)
      );
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }

  public void setGamesComboBox(JComboBox<String> comboBox) {
    try {
      ResultSet games = getGames();
      while(games.next()) {
        comboBox.addItem(games.getString(1));
      }
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
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
