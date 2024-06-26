package javaChatRoom.server.clientHandler;

import javaChatRoom.common.message.Message;
import javaChatRoom.common.command.Command;
import javaChatRoom.server.userManager.UserManager;
import javaChatRoom.common.communicationManager.CommunicationManager;
import javaChatRoom.server.serverLogger.ServerLogger;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    private Socket socket;
    private UserManager userManager;
    private CommunicationManager commManager;

    public ClientHandler(Socket socket, UserManager userManager) {
        this.socket = socket;
        this.userManager = userManager;
        try {
            this.commManager = new CommunicationManager(socket);
        } catch (Exception e) {
            ServerLogger.writeError("Error setting up Communication Manager: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                Object obj = commManager.receiveObject();
                if (obj == null) {
                    break;
                }
                if (obj instanceof Message) {
                    handleMessage((Message) obj);
                } else if (obj instanceof Command) {
                    handleCommand((Command) obj);
                }
            }
        } catch (Exception e) {
            ServerLogger.writeError("Error handling client connection: " + e.getMessage());
        } finally {
            commManager.close();
        }
    }


    private void handleMessage(Message message) {
        if (message.getIsBroadcast()) {
            broadcastMessage(message);
        } else {
            sendMessage(message, message.getReceiver());
        }
    }

    private void broadcastMessage(Message message) {
        List<ClientHandler> clients = userManager.getConnectedClients();
        for (ClientHandler client : clients) {
            if (client != this) {
                String sender = message.getIsAnonymous() ? "ANONYMOUS" : message.getSender();
                client.commManager.sendObject(new Message(sender, message.getReceiver(), message.getContent(), true, true));
            }
        }
    }

    private void sendMessage(Message message, String sender) {
        Message outgoingMessage = new Message(sender, message.getReceiver(), message.getContent(), message.getIsBroadcast(), message.getIsAnonymous());
        commManager.sendObject(outgoingMessage);
    }

    private void handleCommand(Command command) {
        if (command.getCommandType() == Command.CommandType.USERLIST) {
            sendUserList();
        } else if (command.getCommandType() == Command.CommandType.LOGINREQUEST) {
            String username = command.getArgs()[0];
            String password = command.getArgs()[1];
            if (userManager.isValidUser(username, password)) {
                userManager.userLogIn(username, this);
                commManager.sendObject(new Command(Command.CommandType.LOGINRESPONSE, "SUCCESS"));
            } else {
                commManager.sendObject(new Command(Command.CommandType.LOGINRESPONSE, "FAILURE"));
            }
        } else if (command.getCommandType() == Command.CommandType.REGISTERREQUEST) {
            String username = command.getArgs()[0];
            String password = command.getArgs()[1];
            if (userManager.registerUser(username, password)) {
                commManager.sendObject(new Command(Command.CommandType.REGISTERRESPONSE, "SUCCESS"));
            } else {
                commManager.sendObject(new Command(Command.CommandType.REGISTERRESPONSE, "FAILURE"));
            }
        } else if (command.getCommandType() == Command.CommandType.QUIT) {
            userManager.userLoggedOut(this);
            commManager.close();
        }
    }

    private void sendUserList() {
        List<String> onlineUsers = userManager.getOnlineUsernames();
        commManager.sendObject(onlineUsers);
    }

    public void closeConnection() {
        try {
            interrupt();  // 中断线程
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            ServerLogger.writeError("Error closing client socket: " + e.getMessage());
        }
    }
}
