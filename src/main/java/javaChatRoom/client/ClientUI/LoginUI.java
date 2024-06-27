package javaChatRoom.client.ClientUI;

import javaChatRoom.client.clientConfig.ClientConfig;
import javaChatRoom.client.clientConnection.ClientConnection;
import javaChatRoom.client.loginHandler.LoginHandler;
import javaChatRoom.client.messageHandler.MessageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginUI extends JFrame {
    private JTextField serverAddressField;
    private JTextField serverPortField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginUI() {
        super("Chat Room Login");
        createUI();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(400, 300);
    }

    private void createUI() {
        JPanel gridPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 调整边框以适应更大的字体

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font textFont = new Font("SansSerif", Font.PLAIN, 16);

        serverAddressField = new JTextField("localhost");
        serverPortField = new JTextField("12345");
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        serverAddressField.setFont(textFont);
        serverPortField.setFont(textFont);
        usernameField.setFont(textFont);
        passwordField.setFont(textFont);

        JLabel serverIpLabel = new JLabel("Server IP:");
        JLabel serverPortLabel = new JLabel("Server Port:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        serverIpLabel.setFont(labelFont);
        serverPortLabel.setFont(labelFont);
        usernameLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);

        gridPanel.add(serverIpLabel);
        gridPanel.add(serverAddressField);
        gridPanel.add(serverPortLabel);
        gridPanel.add(serverPortField);
        gridPanel.add(usernameLabel);
        gridPanel.add(usernameField);
        gridPanel.add(passwordLabel);
        gridPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.addActionListener(this::performLogin);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);

        setLayout(new BorderLayout(5, 10));
        add(gridPanel, BorderLayout.CENTER);
        gridPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void performLogin(ActionEvent e) {
        String serverAddress = serverAddressField.getText();
        int serverPort = Integer.parseInt(serverPortField.getText());
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Connect and perform login
        ClientConnection connection = new ClientConnection(serverAddress, serverPort);
        ClientConfig config = new ClientConfig();
        try {
            connection.connect();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LoginHandler loginHandler = new LoginHandler(connection, config, username, password);

        if (loginHandler.performLogin()) {
            dispose(); // Close the login window
            MessageHandler messageHandler = new MessageHandler(connection, config);
            messageHandler.startHandling(); // Start handling messages
        } else {
            JOptionPane.showMessageDialog(this, "Login failed, please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }
}
