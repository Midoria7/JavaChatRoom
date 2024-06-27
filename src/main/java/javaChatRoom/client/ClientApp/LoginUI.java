package javaChatRoom.client.ClientApp;

import javaChatRoom.client.connectionManager.ConnectionManager;
import javaChatRoom.server.serverLogger.ServerLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginUI() throws IOException {
        super("Login and Register");
        createUI();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null); // Center the window
    }

    private void createUI() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
//        registerButton = new JButton("Register");
        loginButton.addActionListener(e -> attemptLogin());
        add(loginButton);
        pack();
        setVisible(true);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                // Call your registration method here
                System.out.println("Registration requested for " + username);
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        try {
            Manager.sendLoginRequest(username, password);
            Command response = networkManager.receiveLoginResponse();
            if (response.getCommandType() == Command.CommandType.LOGINRESPONSE && "Success".equals(response.getArgs()[0])) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                // Transition to the next screen or state
            } else {
                JOptionPane.showMessageDialog(this, "Login Failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Network error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
//            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ServerLogger.writeError("Error setting look and feel: " + e.getMessage());
        }
        EventQueue.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}
