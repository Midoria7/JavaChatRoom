package javaChatRoom.client.messageHandler;

import javaChatRoom.client.ClientUI.ChatView;
import javaChatRoom.client.clientConfig.ClientConfig;
import javaChatRoom.client.clientConnection.ClientConnection;
import javaChatRoom.common.command.Command;
import javaChatRoom.common.message.Message;

import java.util.Date;
import java.util.List;

public class MessageHandler {
    private ClientConnection connection;
    private ClientConfig config;
    private ChatView chatView;

    public MessageHandler(ClientConnection connection, ClientConfig config) {
        this.connection = connection;
        this.config = config;
    }

    public void setChatView(ChatView chatView) {
        this.chatView = chatView;
    }

    public void startHandling() {
        chatView.displayMessage("Welcome to the chat room! You are logged in as " + config.getUsername() + ".");
        chatView.displayMessage("Enter messages to send, or use @@<command> to perform actions.");
        chatView.displayMessage("Type @@help for a list of available commands.");
        new Thread(() -> {
            try {
                while (true) {
                    Object message = connection.receive();
                    if (message != null) {
                        processReceivedMessage(message);
                    }
                }
            } catch (Exception e) {
                chatView.displayMessage("Error handling messages: " + e.getMessage());
            }
        }).start();
    }

    public void sendMessage(String messageText) {
        if (messageText.startsWith("@@")) {
            handleCommand(messageText.substring(2));
        } else if (messageText.startsWith("@")) {
            sendPrivateMessage(messageText);
        } else {
            sendPublicMessage(messageText);
        }
    }

    private void sendPublicMessage(String messageText) {
        Message message = new Message(config.getUsername(), "ALL", messageText, true, config.isAnonymous());
        connection.send(message);
        String displayText = formatMessageDisplay(config.getUsername(), "ALL", messageText);
        chatView.displayMessage(displayText);
    }

    private void sendPrivateMessage(String messageText) {
        String[] parts = messageText.split(" ", 2);
        if (parts.length > 1) {
            String receiver = parts[0].substring(1);
            String message = parts[1];
            connection.send(new Message(config.getUsername(), receiver, message, false, config.isAnonymous()));
            String displayText = formatMessageDisplay(config.getUsername(), receiver, message);
            chatView.displayMessage(displayText);
        }
    }

    private void handleCommand(String command) {
        switch (command.toLowerCase()) {
            case "quit":
                connection.send(new Command(Command.CommandType.QUIT));
                System.exit(0);
                break;
            case "list":
                connection.send(new Command(Command.CommandType.USERLIST));
                break;
            case "anonymous":
                config.setAnonymous(!config.isAnonymous());
                chatView.displayMessage("Anonymous mode: " + (config.isAnonymous() ? "Enabled" : "Disabled"));
                break;
            case "showanonymous":
                chatView.displayMessage("Current anonymous mode: " + (config.isAnonymous() ? "Enabled" : "Disabled"));
                break;
            case "help":
                chatView.displayMessage("Available commands: quit, list, anonymous, showanonymous, help");
                chatView.displayMessage("@@quit: Exit the chat room.");
                chatView.displayMessage("@@list: List all online users.");
                chatView.displayMessage("@@anonymous: Toggle anonymous mode.");
                chatView.displayMessage("@@showanonymous: Show current anonymous mode.");
                chatView.displayMessage("@@help: Show this help message.");
                break;
            default:
                chatView.displayMessage("Invalid command.");
                break;
        }
    }

    private void processReceivedMessage(Object message) {
        if (message instanceof Message) {
            Message msg = (Message) message;
            String displayText = formatMessageDisplay(msg.getSender(), msg.getReceiver(), msg.getContent());
            chatView.displayMessage(displayText);
        } else if (message instanceof Command) {
            Command command = (Command) message;
            chatView.displayMessage("Server: " + command.getArgs()[0]);
        } else if (message instanceof List<?>) {
            List<String> users = (List<String>) message;
            chatView.displayMessage("Users online: " + String.join(", ", users));
        }
    }

    private String formatMessageDisplay(String sender, String receiver, String content) {
        String displayName = config.isAnonymous() && sender.equals(config.getUsername()) ? "ANONYMOUS" : sender;
        return "[" + new Date() + "][" + displayName + (displayName.equals(config.getUsername()) ? "(You)" : "")
                + (receiver.equals("ALL") ? "" : (" -> " + receiver + (receiver.equals(config.getUsername()) ? "(You)" : ""))) + "] " + content;
    }
}
