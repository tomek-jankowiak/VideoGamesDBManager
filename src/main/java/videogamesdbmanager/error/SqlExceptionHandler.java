package videogamesdbmanager.error;

import javax.swing.*;
import java.sql.SQLException;

public class SqlExceptionHandler {
  public static void handle(SQLException ex) {
    String msg;
    switch (ex.getErrorCode()) {
      case 1005:
        msg = "Nie podano hasła";
        break;
      case 1017:
        msg = "Podano błędny login lub hasło";
        break;
      case 1400:
        msg = "Wymagane pole nie może być puste";
        break;
      case 1861:
        msg = "Niepoprawny format daty (powinien być \"DD-MM-YYYY\")";
        break;
      case 17002:
        msg = "Błąd połączenia";
        break;
      default:
        msg = "Nieznany błąd";
        System.out.println(ex.toString());
        break;
    }
    JOptionPane.showMessageDialog(null,
            msg, "Błąd", JOptionPane.ERROR_MESSAGE);
  }
}
