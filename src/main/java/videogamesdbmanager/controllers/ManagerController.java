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
                      "SELECT pseudonim, imie, nazwisko, kraj, TO_CHAR(data_urodzenia, 'yyyy-MM-dd'), placa " +
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
                             String country, String birthDate, String salary) {
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
      preparedStatement.setString(7, salary);
      preparedStatement.setInt(6, team_id_);
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  public boolean modifyPlayer(String nick, String salary) {
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
      preparedStatement.setString(2, salary);
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

  private ResultSet getChampionships(int participation, String type) {

    type = type.toLowerCase();
    String filter = "";
    String possibleNOT = "";
    if (participation == -1) possibleNOT = "NOT";

    switch (type) {
      case "wszystkie":
        if (participation != 0) { //not all
          filter = String.format("where id " + possibleNOT + " in (select druzynowe_id from %s.udzialy_druzynowe where druzyna_id = %d) " +
                          "or " +
                          "id in (select indywidualne_id from %s.udzialy_indywidualne " +
                          "where zawodnik_pseudonim in (select pseudonim from %s.zawodnicy  " +
                          "where druzyna_id = %d))",
                  Application.ownerID, team_id_, Application.ownerID, Application.ownerID, team_id_);
        }
        break;

      case "drużynowe":
        filter = "where typ = \'Drużynowe\' ";
        if (participation != 0) { //not all
          filter += String.format("and id " + possibleNOT + " in (select druzynowe_id from %s.udzialy_druzynowe where druzyna_id = %d)",
                  Application.ownerID, team_id_);
        }
        break;

      case "indywidualne":
        filter = "where typ = \'Indywidualne\' ";
        if (participation != 0) { //not all
          filter += String.format(
                  "and id " + possibleNOT + " in (select indywidualne_id from %s.udzialy_indywidualne " +
                          "where zawodnik_pseudonim in (select pseudonim from %s.zawodnicy  " +
                          "where druzyna_id = %d))",
                  Application.ownerID, Application.ownerID, team_id_);
        }
        break;
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

  public void setChampionshipsTable(DefaultTableModel tableModel, int participation, String type) {

    ResultSet championshipsSet = getChampionships(participation, type);
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

  public void signUpTeam(String champ_id) {
    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
              String.format(
                      "BEGIN " +
                              "%s.Menadzer.ZarejestrujUdzialDruzynowy(?, ?);" +
                              "END;",
                      Application.ownerID
              )
      );
      preparedStatement.setInt(1, team_id_);
      preparedStatement.setString(2, champ_id);
      preparedStatement.execute();
      preparedStatement.close();

    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
    }
  }

  public void signUpPlayer(String pseudonym, String champ_id) {
    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
              String.format(
                      "BEGIN " +
                              "%s.Menadzer.ZarejestrujUdzialZawodnika(?, ?);" +
                              "END;",
                      Application.ownerID
              )
      );
      preparedStatement.setString(1, pseudonym);
      preparedStatement.setString(2, champ_id);
      preparedStatement.execute();
      preparedStatement.close();

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
