package javaChatRoom.client.connectionManager;

import javaChatRoom.common.command.Command;
import javaChatRoom.common.communicationManager.CommunicationManager;
import javaChatRoom.common.message.Message;

import java.io.IOException;
import java.net.Socket;

public class ConnectionManager {
    private CommunicationManager commManager;

    public ConnectionManager(String serverAddress, int port) throws IOException {
        Socket socket = new Socket(serverAddress, port);
        this.commManager = new CommunicationManager(socket);
    }

    public void sendLoginRequest(String username, String password) throws IOException {
        Command loginCommand = new Command(Command.CommandType.LOGINREQUEST, username, password);
        commManager.sendObject(loginCommand);
    }

    public void sendRegisterRequest(String username, String password) throws IOException {
        Command registerCommand = new Command(Command.CommandType.REGISTERREQUEST, username, password);
        commManager.sendObject(registerCommand);
    }

    public void sendCommand(Command command) throws IOException {
        commManager.sendObject(command);
    }

    public Command receiveResponse() throws IOException, ClassNotFoundException {
        return (Command) commManager.receiveObject();
    }

    public void sendMessage(String sender, String receiver, String message, boolean isBroadcast, boolean isAnonymous) throws IOException {
        Message chatMessage = new Message(sender, receiver, message, isBroadcast, isAnonymous);
        commManager.sendObject(chatMessage);
    }

    public Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message) commManager.receiveObject();
    }

    public void closeConnection() throws IOException {
        commManager.close();
    }
}
