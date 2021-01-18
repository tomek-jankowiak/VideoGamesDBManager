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

  public boolean addParticipantResult(String championshipsId, String type, DefaultTableModel tableModel, int row) {
    try {
      String[] params = new String[tableModel.getColumnCount()];
      for (int i = 0; i < tableModel.getColumnCount(); i++) {
        Object param = tableModel.getValueAt(row, i);
        if (param != null) {
          params[i] = param.toString();
        } else {
          params[i] = null;
        }
      }
      PreparedStatement preparedStatement;
      if (type.equals("Drużynowe")) {
        preparedStatement = connection_.prepareStatement(
                String.format(
                        "BEGIN " +
                                "%s.Organizator.DodajWynikDruzynowy(?, ?, ?, ?); " +
                                "END;",
                        Application.ownerID
                )
        );
        preparedStatement.setInt(1, Integer.parseInt(championshipsId));
        assert params[0] != null;
        preparedStatement.setInt(2, Integer.parseInt(params[0]));
        if (!params[2].isEmpty()) {
          preparedStatement.setInt(3, Integer.parseInt(params[2]));
        } else {
          preparedStatement.setString(3, null);
        }
        if (!params[4].isEmpty()) {
          preparedStatement.setDouble(4, Double.parseDouble(params[4]));
        } else {
          preparedStatement.setString(4, null);
        }
      } else {
        preparedStatement = connection_.prepareStatement(
                String.format(
                        "BEGIN " +
                                "%s.Organizator.DodajWynikIndywidualny(?, ?, ?, ?); " +
                                "END;",
                        Application.ownerID
                )
        );
        preparedStatement.setInt(1, Integer.parseInt(championshipsId));
        preparedStatement.setString(2, params[0]);
        if (!params[1].isEmpty()) {
          preparedStatement.setInt(3, Integer.parseInt(params[1]));
        } else {
          preparedStatement.setString(3, null);
        }
        if (!params[3].isEmpty()) {
          preparedStatement.setDouble(4, Double.parseDouble(params[3]));
        } else {
          preparedStatement.setString(4, null);
        }
      }
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    } catch (NullPointerException ex) {
      JOptionPane.showMessageDialog(null,
              "Procent puli nie może być pusty!", "Błąd", JOptionPane.ERROR_MESSAGE);
      return false;
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(null,
              "Niepoprawny format liczbowy!", "Błąd", JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  public void setChampionshipsTable(DefaultTableModel tableModel, boolean ownChampionships) {
    ResultSet championshipsSet = getChampionships(ownChampionships);
    if (championshipsSet != null) {
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
  }

  public void setParticipantsTable(DefaultTableModel tableModel, String championshipsId, String type) {
    ResultSet teamsSet = getParticipants(championshipsId, type);
    if (teamsSet != null) {
      try {
        while (teamsSet.next()) {
          Object[] objects = new Object[tableModel.getColumnCount()];
          for (int i = 0; i < tableModel.getColumnCount(); i++) {
            objects[i] = teamsSet.getObject(i + 1);
          }
          tableModel.addRow(objects);
        }
      } catch (SQLException ex) {
        SqlExceptionHandler.handle(ex);
      }
    }
  }

  public void setGamesComboBox(JComboBox<String> comboBox) {
    ResultSet games = getGames();
    if (games != null) {
      try {
        while (games.next()) {
          comboBox.addItem(games.getString(1));
        }
      } catch (SQLException ex) {
        SqlExceptionHandler.handle(ex);
      }
    }
  }

  public void setChampionshipsParams(String championshipsId, String[] championshipsParams) {
    ResultSet details = getChampionshipsDetails(championshipsId);
    if (details != null) {
      try {
        details.next();
        for (int i = 0; i < 9; i++) {
          championshipsParams[i] = details.getString(i + 1);
        }
      } catch (SQLException ex) {
        SqlExceptionHandler.handle(ex);
      }
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

  private ResultSet getChampionships(boolean ownChampionships) {
    String filter;
    if (ownChampionships) {
      filter = String.format("WHERE organizator = '%s'", getOrganizerName());
    } else {
      filter = "";
    }
    try {
      Statement statement = connection_.createStatement();
      return statement.executeQuery(
        String.format("SELECT id, nazwa, gra_tytul, typ, nagroda, status " +
                      "FROM %s.mistrzostwa %s",
                      Application.ownerID, filter
        )
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

  private ResultSet getParticipants(String championshipsId, String type) {
    try {
      Statement statement = connection_.createStatement();
      if (type.equals("Drużynowe")) {
        return statement.executeQuery(
          String.format(
                  "SELECT id, nazwa, wynik, nagroda, procent_puli " +
                  "FROM %s.druzyny d JOIN %s.udzialy_druzynowe ud ON d.id = ud.druzyna_id " +
                  "WHERE d.id IN (SELECT druzyna_id " +
                                  "FROM %s.udzialy_druzynowe " +
                                  "WHERE druzynowe_id = %s)",
                  Application.ownerID, Application.ownerID, Application.ownerID, championshipsId
          )
        );
      } else {
        return statement.executeQuery(
          String.format(
                  "SELECT pseudonim, wynik, nagroda, procent_puli " +
                  "FROM %s.zawodnicy z JOIN %s.udzialy_indywidualne ui ON z.pseudonim = ui.zawodnik_pseudonim " +
                  "WHERE z.pseudonim IN (SELECT zawodnik_pseudonim " +
                                        "FROM %s.udzialy_indywidualne " +
                                        "WHERE indywidualne_id = %s)",
                  Application.ownerID, Application.ownerID, Application.ownerID, championshipsId
          )
        );
      }
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }


}
