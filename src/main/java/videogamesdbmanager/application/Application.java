package videogamesdbmanager.application;

import videogamesdbmanager.components.frames.login.LoginFrame;
import javax.swing.*;

public class Application {

  public static final String ownerID = "inf141235";

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame loginFrame = new LoginFrame();
      loginFrame.setVisible(true);
    });
  }
}
