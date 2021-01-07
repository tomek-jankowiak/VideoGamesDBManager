package videogamesdbmanager.application;

import videogamesdbmanager.components.frames.login.LoginFrame;
import javax.swing.*;

public class Application {

  public static final String ownerID = "inf141235";
  public static final String connectionString =
          "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/dblab02_students.cs.put.poznan.pl";

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame loginFrame = new LoginFrame();
      loginFrame.setVisible(true);
    });
  }
}
