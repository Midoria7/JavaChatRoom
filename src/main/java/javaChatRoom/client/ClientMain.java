package javaChatRoom.client;

import javaChatRoom.client.ClientUI.LoginView;
import javaChatRoom.client.clientConfig.ClientConfig;
import javaChatRoom.client.clientConnection.ClientConnection;

import javax.swing.*;

public class ClientMain {

    void start(){

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new LoginView();
        });
    }

    public static void main(String[] args) {
        new ClientMain().start();
    }
}
