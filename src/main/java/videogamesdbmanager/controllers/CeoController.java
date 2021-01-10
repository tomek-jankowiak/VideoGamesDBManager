package videogamesdbmanager.controllers;


import videogamesdbmanager.application.Application;
import videogamesdbmanager.error.SqlExceptionHandler;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class CeoController {

  private final Connection connection_;

  public CeoController(Connection connection) {
    connection_ = connection;
  }

  public boolean addEmployee(String pesel, String name, String surname,
                             Double salary, String empDate, String department) {
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
    } catch (SQLException ex) {
      SqlExceptionHandler.handle(ex);
      return false;
    }

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
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
              String.format("SELECT %s.Prezes.PobierzNazweStudia FROM dual", Application.ownerID)
      );
      resultSet.next();
      String company = resultSet.getString(1);
      resultSet.close();

      return statement.executeQuery(
        String.format(
                "SELECT pesel, imie, nazwisko, placa, data_zatrudnienia, dzial " +
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

}
