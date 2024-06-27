package javaChatRoom.client;

import javaChatRoom.client.clientConnection.ClientConnection;
import javaChatRoom.client.clientConfig.ClientConfig;
import javaChatRoom.client.messageHandler.MessageHandler;
import javaChatRoom.client.loginHandler.LoginHandler;
import java.util.Scanner;

public class ClientMain {
    private static ClientConnection connection;
    private static ClientConfig config = new ClientConfig();
    private static MessageHandler messageHandler;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter server IP:");
        String serverAddress = scanner.nextLine();
        System.out.println("Enter server port:");
        int serverPort = Integer.parseInt(scanner.nextLine());

        connection = new ClientConnection(serverAddress, serverPort);

        try {
            connection.connect();
            System.out.println("Connected to server at " + serverAddress + ":" + serverPort);

            LoginHandler loginHandler = new LoginHandler(connection, config, scanner);
            if (loginHandler.performLogin()) {
                messageHandler = new MessageHandler(connection, config);
                messageHandler.startHandling();
            } else {
                System.out.println("Login failed or cancelled by user.");
            }
        } catch (Exception e) {
            System.out.println("Cannot connect to server: " + e.getMessage());
        } finally {
            connection.close();
            scanner.close();
        }
    }
}
