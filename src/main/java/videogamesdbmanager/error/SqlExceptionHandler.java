package videogamesdbmanager.error;

import javax.swing.*;
import java.sql.SQLException;

public class SqlExceptionHandler {
  public static void handle(SQLException ex) {
    String msg;
    switch (ex.getErrorCode()) {
      case 1005:
        msg = "Nie podano hasła!";
        break;
      case 1017:
        msg = "Podano błędny login lub hasło!";
        break;
      case 1400:
        msg = "Wymagane pole nie może być puste!";
        break;
      case 1438:
        msg = "Podana wartość jest za duża!";
        break;
      case 1839:
        msg = "Niepoprawna data!";
        break;
      case 1861:
        msg = "Niepoprawny format daty (powinien być \"DD-MM-YYYY\")!";
        break;
      case 17002:
        msg = "Błąd połączenia!";
        break;
      case 20001:
        msg = "Istnieje już użytkownik o takiej nazwie!";
        break;
      case 20301:
        msg = "Istnieje już studio o takiej nazwie!";
        break;
      case 20302:
        msg = "Istnieje już pracownik o takim numerze PESEL!";
        break;
      case 20303:
        msg = "Płaca musi być większa od 0!";
        break;
      case 20304:
        msg = "Istnieje już gra o takim tytule!";
        break;
      case 20305:
        msg = "Niepoprawna kategoria wiekowa!";
        break;
      case 20306:
        msg = "Box office i budżet (jeśli podane) muszą być większe od 0!";
        break;
      case 20307:
        msg = "Istnieje już taki gatunek!";
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
