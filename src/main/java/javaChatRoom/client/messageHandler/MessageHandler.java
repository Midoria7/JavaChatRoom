package javaChatRoom.client.messageHandler;

import javaChatRoom.client.clientConfig.ClientConfig;
import javaChatRoom.client.clientConnection.ClientConnection;
import javaChatRoom.common.command.Command;
import javaChatRoom.common.message.Message;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MessageHandler {
    private ClientConnection connection;
    private ClientConfig config;

    public MessageHandler(ClientConnection connection, ClientConfig config) {
        this.connection = connection;
        this.config = config;
    }

    public void startHandling() {
        System.out.println("Welcome to the chat room!");
        System.out.println("Enter messages to send, or use @@<command> to perform actions.");

        Scanner scanner = new Scanner(System.in);
        new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Object message = connection.receive();
                    if (message != null) {
                        processReceivedMessage(message);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error handling messages: " + e.getMessage());
            }
        }).start();

        while (true) {
            String input = scanner.nextLine();
            if (input.startsWith("@@")) {
                if (input.equalsIgnoreCase("@@quit")) {
                    connection.send(new Command(Command.CommandType.QUIT));
                    config.reset();
                    connection.close();
                    System.exit(0);
                    break;
                } else if (input.equalsIgnoreCase("@@list")) {
                    connection.send(new Command(Command.CommandType.USERLIST));
                } else if (input.equalsIgnoreCase("@@anonymous")) {
                    config.setAnonymous(!config.isAnonymous());
                    System.out.println("Anonymous mode: " + (config.isAnonymous() ? "Enabled" : "Disabled"));
                } else if (input.equalsIgnoreCase("@@showanonymous")) {
                    System.out.println("Current anonymous mode: " + (config.isAnonymous() ? "Enabled" : "Disabled"));
                } else {
                    System.out.println("Invalid command.");
                }
            } else if (input.startsWith("@")) {
                String[] parts = input.split(" ", 2);
                if (parts.length > 1) {
                    String receiver = parts[0].substring(1);
                    String message = parts[1];
                    connection.send(new Message(config.getUsername(), receiver, message, false, config.isAnonymous()));
                    System.out.println("[" + new Date() + "][" + config.getUsername() + "(You)" + " -> " + receiver + "]: " + message);
                }
            } else {
                String nowUsername = config.isAnonymous() ? "ANONYMOUS" : config.getUsername();
                connection.send(new Message(config.getUsername(), "ALL", input, true, config.isAnonymous()));
                System.out.println("[" + new Date() + "][" + nowUsername + "(You)" + "]: " + input);
            }
        }

        scanner.close();
    }

    private void processReceivedMessage(Object message) {
        if (message instanceof Command) {
            if (((Command) message).getCommandType() == Command.CommandType.ERROR) {
                System.out.println("Error: " + ((Command) message).getArgs()[0]);
            } else {
                System.out.println("Received command: " + ((Command) message).getCommandType());
            }
        } else if (message instanceof Message) {
            Message msg = (Message) message;
            if (msg.getIsBroadcast()) {
                System.out.println("[" + new Date() + "][" + msg.getSender() + "]: " + msg.getContent());
            } else {
                System.out.println("[" + new Date() + "][" + msg.getSender() + " -> " + msg.getReceiver() + "(You)" + "]: " + msg.getContent());
            }
        } else if (message instanceof List<?>) {
            List<String> users = (List<String>) message;
            System.out.println("Online users: " + users);
        }
    }
}
