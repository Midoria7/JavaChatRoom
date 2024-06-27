package javaChatRoom.client;

import javaChatRoom.client.ClientUI.LoginUI;
import javaChatRoom.client.ClientUI.LoginView;

import javax.swing.*;

public class ClientMain {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new LoginView();
        });
    }
}
