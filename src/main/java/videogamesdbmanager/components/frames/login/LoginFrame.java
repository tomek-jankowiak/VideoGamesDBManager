package videogamesdbmanager.components.frames.login;

import videogamesdbmanager.controllers.LoginController;

import javax.swing.*;

public class LoginFrame extends JFrame {

  private JTextField loginField;
  private JPanel loginPanel;
  private JPasswordField passwordField;
  private JButton logInButton;

  private final LoginController controller_;

  public LoginFrame() {
    super("Logowanie");

    this.setContentPane(loginPanel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.pack();

    controller_ = new LoginController();

    logInButton.addActionListener(e -> onLogin());
  }

  private void onLogin() {
    String username = loginField.getText();
    String password = new String(passwordField.getPassword());
    if (controller_.login(username, password)) {
      dispose();
    }
  }
}
