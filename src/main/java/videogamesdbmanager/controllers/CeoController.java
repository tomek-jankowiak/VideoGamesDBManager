package videogamesdbmanager.controllers;


import videogamesdbmanager.application.Application;
import videogamesdbmanager.error.SqlExceptionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.desktop.AppForegroundListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class CeoController {

  private final Connection connection_;

  public CeoController(Connection connection) {
    connection_ = connection;
  }

  public String getSysdate() {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date date = new Date(System.currentTimeMillis());
    return formatter.format((date));
  }

  public String getCompanyName() {
    String company;

    try {
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
              String.format("SELECT %s.Prezes.PobierzNazweStudia FROM dual", Application.ownerID)
      );
      resultSet.next();
      company = resultSet.getString(1);
      resultSet.close();
      statement.close();
      return company;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }

  public int getCompanyEmployeesNumber() {
    try {
      int number;

      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
        String.format("SELECT COUNT(*) " +
                      "FROM %s.pracownicy_studia " +
                      "WHERE studio_nazwa = '%s'",
                      Application.ownerID, getCompanyName())
      );
      resultSet.next();
      number = resultSet.getInt(1);
      resultSet.close();

      return number;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return 0;
    }
  }

  public int getCompanyGamesNumber() {
    try {
      int number;

      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
        String.format("SELECT COUNT(*) " +
                      "FROM %s.gry " +
                      "WHERE studio_nazwa = '%s'",
                      Application.ownerID, getCompanyName())
      );
      resultSet.next();
      number = resultSet.getInt(1);
      resultSet.close();

      return number;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return 0;
    }
  }

  public double getCompanyIncome() {
    try {
      double income;
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
        String.format("SELECT SUM(nvl(box_office, 0)) " +
                      "FROM %s.gry " +
                      "WHERE studio_nazwa = '%s'",
                      Application.ownerID, getCompanyName())
      );
      resultSet.next();
      income = resultSet.getDouble(1);
      resultSet.close();

      return income;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return 0;
    }
  }
  public boolean addEmployee(String pesel, String name, String surname,
                             Double salary, String empDate, String department) {
    String company = getCompanyName();

    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
        String.format(
                "BEGIN " +
                        "%s.Prezes.DodajPracownika(?, ?, ?, ?, ?, TO_DATE(?, 'DD-MM-YYYY'), ?);" +
                "END;",
                Application.ownerID
        )
      );
      preparedStatement.setString(1, pesel);
      preparedStatement.setString(2, name);
      preparedStatement.setString(3, surname);
      preparedStatement.setDouble(4, salary);
      preparedStatement.setString(5, company);
      preparedStatement.setString(6, empDate);
      preparedStatement.setString(7, department);
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  public boolean modifyEmployee(String pesel, Double salary, String department) {
    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
        String.format(
                "BEGIN " +
                        "%s.Prezes.EdytujPracownika(?, ?, ?);" +
                "END;",
                Application.ownerID
        )
      );
      preparedStatement.setString(1, pesel);
      preparedStatement.setDouble(2, salary);
      preparedStatement.setString(3, department);
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  public boolean deleteEmployee(String pesel) {
    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
        String.format(
                "BEGIN " +
                        "%s.Prezes.UsunPracownika(?);" +
                "END;",
                Application.ownerID
        )
      );
      preparedStatement.setString(1, pesel);
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  public ResultSet getEmployees() {
    try {
      String company = getCompanyName();
      Statement statement = connection_.createStatement();
      return statement.executeQuery(
        String.format(
                "SELECT pesel, imie, nazwisko, placa, TO_CHAR(data_zatrudnienia, 'YYYY-MM-DD'), dzial " +
                "FROM %s.pracownicy_studia " +
                "WHERE studio_nazwa = '%s'",
                Application.ownerID, company
        )
      );
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }

  public void setEmployeesTable(DefaultTableModel tableModel) {
    ResultSet employeesSet = getEmployees();
    try {
      while (employeesSet.next()) {
        Object[] objects = new Object[6];
        for (int i = 0; i < 6; i++) {
          objects[i] = employeesSet.getObject(i + 1);
        }
        tableModel.addRow(objects);
      }
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
    }
  }

  public ResultSet getGameTypes() {
    try {
      Statement statement = connection_.createStatement();
      return statement.executeQuery(
        String.format("SELECT nazwa_gatunku FROM %s.gatunki ORDER BY 1", Application.ownerID)
      );
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }

  public void setGameTypesComboBox(JComboBox<String> comboBox) {
    try {
      ResultSet types = getGameTypes();

      while(types.next()) {
        comboBox.addItem(types.getString(1));
      }
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
    }
  }

  public boolean addGameType(String typeName, String superType) {
    try {
      PreparedStatement preparedStatement = connection_.prepareStatement(
        String.format(
                "BEGIN " +
                        "%s.Prezes.NowyGatunek(?, ?);" +
                "END;",
                Application.ownerID
        )
      );
      preparedStatement.setString(1, typeName);
      if (superType.equals("<BRAK NADGATUNKU>")) {
        preparedStatement.setString(2, null);
      } else {
        preparedStatement.setString(2, superType);
      }
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  public boolean releaseGame(String title, String releaseDate, String gameType,
                             String ageCat, String boxOffice, String budget) {
    try {
      String company = getCompanyName();
      PreparedStatement preparedStatement = connection_.prepareStatement(
        String.format(
                "BEGIN " +
                        "%s.Prezes.WydajGre(?, TO_DATE(?, 'DD-MM-YYYY'), ?, ?, ?, ?, ?);" +
                "END;",
                Application.ownerID
        )
      );
      preparedStatement.setString(1, title);
      preparedStatement.setString(2, releaseDate);
      preparedStatement.setString(3, gameType);
      preparedStatement.setString(4, company);
      if (!ageCat.isEmpty()) {
        preparedStatement.setInt(5, Integer.parseInt(ageCat));
      } else {
        preparedStatement.setString(5, null);
      }
      if (!boxOffice.isEmpty()) {
        preparedStatement.setDouble(6, Double.parseDouble(boxOffice));
      } else {
        preparedStatement.setString(6, null);
      }
      if (!budget.isEmpty()) {
        preparedStatement.setDouble(7, Double.parseDouble(budget));
      } else {
        preparedStatement.setString(7, null);
      }
      preparedStatement.execute();
      preparedStatement.close();

      return true;
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }
  }

  public ResultSet getGames(boolean allGames) {
    String filter;
    if (!allGames) {
      filter = String.format("WHERE studio_nazwa = '%s'", getCompanyName());
    } else {
      filter = "";
    }

    try {
      Statement statement = connection_.createStatement();
      return statement.executeQuery(
        String.format("SELECT tytul, TO_CHAR(data_wydania, 'YYYY-MM-DD'), kategoria_wiekowa, nazwa_gatunku, " +
                      "studio_nazwa, budzet, box_office " +
                      "FROM %s.gry %s", Application.ownerID, filter)
      );
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return null;
    }
  }

  public void setGamesTable(DefaultTableModel tableModel, boolean allGames) {
    ResultSet gamesSet = getGames(allGames);
    try {
      while (gamesSet.next()) {
        Object[] objects = new Object[7];
        for (int i = 0; i < 7; i++) {
          objects[i] = gamesSet.getObject(i + 1);
        }
        tableModel.addRow(objects);
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
