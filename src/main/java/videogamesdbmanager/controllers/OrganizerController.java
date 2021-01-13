package videogamesdbmanager.controllers;

import videogamesdbmanager.application.Application;
import videogamesdbmanager.error.SqlExceptionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

  public boolean addChampionships(String name, String beginDate, String endDate, String localization,
                                  String type, String game, String prize) {
    try {
     PreparedStatement preparedStatement = connection_.prepareStatement(
       String.format(
               "BEGIN " +
                       "%s.Organizator.NoweMistrzostwa(" +
                          "?, TO_DATE(?, 'DD-MM-YYYY'), TO_DATE(?, 'DD-MM-YYYY'), ?, ?, ?, ?, ?);" +
               "END;",
               Application.ownerID
       )
     );
     preparedStatement.setString(1, name);
     preparedStatement.setString(2, beginDate);
     preparedStatement.setString(3, endDate);
     preparedStatement.setString(4, getOrganizerName());
     preparedStatement.setString(5, localization);
     preparedStatement.setString(6, type);
     preparedStatement.setString(7, game);
     if (!prize.isEmpty()) {
       preparedStatement.setDouble(8, Double.parseDouble(prize));
     } else {
       preparedStatement.setString(8, null);
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

  public void setChampionshipsTable(DefaultTableModel tableModel) {
    ResultSet championshipsSet = getChampionships();
    try {
      while (championshipsSet.next()) {
        Object[] objects = new Object[4];
        for (int i = 0; i < 4; i++) {
          objects[i] = championshipsSet.getObject(i + 1);
        }
        tableModel.addRow(objects);
      }
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
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

  public void setChampionshipsParams(String[] championshipsKey, String[] championshipsParams) {
    ResultSet details = getChampionshipsDetails(championshipsKey);
    try {
      details.next();
      for (int i = 0; i < 8; i++) {
        championshipsParams[i] = details.getString(i + 1);
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

  private ResultSet getGames() {
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

  private ResultSet getChampionships() {
    try {
      Statement statement = connection_.createStatement();
      /*return statement.executeQuery(
        String.format(
                "SELECT nazwa, TO_CHAR(data_begin, 'YYYY-MM-DD'), TO_CHAR(data_end, 'YYYY-MM-DD'), organizator, " +
                        "lokalizacja, nagroda, gra_tytul, status " +
                "FROM %s.mistrzostwa",
                Application.ownerID)
      );*/
      return statement.executeQuery(
              String.format("SELECT nazwa, gra_tytul, TO_CHAR(data_begin, 'YYYY-MM-DD'), status FROM %s.mistrzostwa",
                      Application.ownerID)
      );
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }

  private ResultSet getChampionshipsDetails(String[] championshipsKey) {
    try {
      Statement statement = connection_.createStatement();
      return statement.executeQuery(
        String.format(
                "SELECT nazwa, TO_CHAR(data_begin, 'YYYY-MM-DD'), TO_CHAR(data_end, 'YYYY-MM-DD'), organizator, " +
                        "lokalizacja, nagroda, gra_tytul, status " +
                "FROM %s.mistrzostwa " +
                "WHERE nazwa = '%s' AND data_begin = TO_DATE('%s', 'YYYY-MM-DD')",
                Application.ownerID, championshipsKey[0], championshipsKey[1]
        )
      );
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }
}
