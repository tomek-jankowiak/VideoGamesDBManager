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

  public int getChampionshipNumber() {
    try {
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
              String.format("SELECT %s.Organizator.PobierzLiczbeMistrzostw FROM dual", Application.ownerID)
      );
      resultSet.next();
      int count = resultSet.getInt(1);
      resultSet.close();
      statement.close();

      return count;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return 0;
    }
  }

  public double getPrizeSum() {
    try {
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
              String.format("SELECT %s.Organizator.PobierzSumeNagrod FROM dual", Application.ownerID)
      );
      resultSet.next();
      double count = resultSet.getDouble(1);
      resultSet.close();
      statement.close();

      return count;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return 0;
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
              "Niepoprawny format liczbowy!", "Błąd", JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  public boolean modifyChampionships(String championshipsId, String name, String beginDate, String endDate,
                                     String localization, String type, String prize, String status) {
    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
        String.format(
                "BEGIN " +
                "%s.Organizator.EdytujMistrzostwa(?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), " +
                        "?, ?, ?, ?);" +
                "END;",
                Application.ownerID
        )
      );
      preparedStatement.setString(1, championshipsId);
      preparedStatement.setString(2, name);
      preparedStatement.setString(3, beginDate);
      preparedStatement.setString(4, endDate);
      preparedStatement.setString(5, localization);
      preparedStatement.setString(6, type);
      if (!prize.isEmpty()) {
        preparedStatement.setDouble(7, Double.parseDouble(prize));
      } else {
        preparedStatement.setString(7, null);
      }
      preparedStatement.setString(8, status);
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(null,
              "Niepoprawny format liczbowy!", "Błąd", JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  public void setChampionshipsTable(DefaultTableModel tableModel) {
    ResultSet championshipsSet = getChampionships();
    try {
      while (championshipsSet.next()) {
        Object[] objects = new Object[6];
        for (int i = 0; i < 6; i++) {
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

  public void setChampionshipsParams(String championshipsId, String[] championshipsParams) {
    ResultSet details = getChampionshipsDetails(championshipsId);
    try {
      details.next();
      for (int i = 0; i < 9; i++) {
        championshipsParams[i] = details.getString(i + 1);
      }
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
    }
  }

  public boolean isOrganizersChampionship(String championshipsId) {
    try {
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
        String.format(
                "SELECT organizator " +
                "FROM %s.mistrzostwa " +
                "WHERE id = '%s'",
                Application.ownerID, championshipsId
        )
      );
      resultSet.next();
      String organizer = resultSet.getString(1);
      resultSet.close();
      statement.close();

      return organizer.equals(getOrganizerName());
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

  private ResultSet getGames() {
    try {
      Statement statement = connection_.createStatement();
      return statement.executeQuery(
              String.format("SELECT tytul FROM %s.gry ORDER BY 1", Application.ownerID)
      );
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }

  private ResultSet getChampionships() {
    try {
      Statement statement = connection_.createStatement();
      return statement.executeQuery(
        String.format("SELECT id, nazwa, gra_tytul, typ, nagroda, status " +
                      "FROM %s.mistrzostwa",
                      Application.ownerID)
      );
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }

  private ResultSet getChampionshipsDetails(String championshipsId) {
    try {
      Statement statement = connection_.createStatement();
      return statement.executeQuery(
        String.format(
                "SELECT nazwa, TO_CHAR(data_begin, 'YYYY-MM-DD'), TO_CHAR(data_end, 'YYYY-MM-DD'), organizator, " +
                        "lokalizacja, typ, nagroda, gra_tytul, status " +
                "FROM %s.mistrzostwa " +
                "WHERE id = '%s'",
                Application.ownerID, championshipsId
        )
      );
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }
}
