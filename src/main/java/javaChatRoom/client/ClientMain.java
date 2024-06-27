//package javaChatRoom.client;
//
//import javaChatRoom.client.ClientConfigManager.ClientConfigManager;
//import javaChatRoom.client.LocalUserListManager.LocalUserListManager;
//import javaChatRoom.client.connectionManager.ConnectionManager;
//import javaChatRoom.common.command.Command;
//import javaChatRoom.common.message.Message;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class ClientMain {
//    private ConnectionManager networkManager;
//    private LocalUserListManager userListManager;
//    private ClientConfigManager configManager;
//
//    public ClientMain() throws IOException {
//        networkManager = new ConnectionManager("localhost", 13000);
//        userListManager = new LocalUserListManager();
//        scheduleUserListUpdates();
//    }
//
//    private void scheduleUserListUpdates() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    networkManager.sendCommand(new Command(Command.CommandType.USERLIST));
//                    List<String> users = (List<String>) networkManager.receiveResponse();
//                    userListManager.updateUsers(users);
//                } catch (Exception e) {
//                    System.err.println("Failed to update user list: " + e.getMessage());
//                }
//            }
//        }, 0, 5000);
//    }
//
//    public void sendMessage(String input) {
//        if (input.startsWith("@")) {
//            int firstSpaceIndex = input.indexOf(' ');
//            if (firstSpaceIndex > 1) {
//                String username = input.substring(1, firstSpaceIndex);
//                String messageContent = input.substring(firstSpaceIndex + 1);
//
//                if (userListManager.getOnlineUsers().contains(username)) {
//                    // 如果用户名在在线列表中，发送私聊消息
//                    sendPrivateMessage(username, messageContent);
//                } else {
//                    // 如果不在在线列表中，作为广播消息处理
//                    sendBroadcastMessage(input);
//                }
//            } else {
//                // 没有有效的消息内容，作为广播消息处理
//                sendBroadcastMessage(input);
//            }
//        } else {
//            // 不是以@开头，作为广播消息处理
//            sendBroadcastMessage(input);
//        }
//    }
//
//    private void sendPrivateMessage(String receiver, String message) {
//        Message privateMessage = new Message("yourUsername", receiver, message, false, false);
//        try {
//            networkManager.sendMessage(privateMessage);
//            System.out.println("Sent private message to " + receiver);
//        } catch (IOException e) {
//            System.err.println("Failed to send private message: " + e.getMessage());
//        }
//    }
//
//    private void sendBroadcastMessage(String message) {
//        Message broadcastMessage = new Message("yourUsername", "ALL", message, true, false);
//        try {
//            networkManager.sendMessage(broadcastMessage);
//            System.out.println("Sent broadcast message");
//        } catch (IOException e) {
//            System.err.println("Failed to send broadcast message: " + e.getMessage());
//        }
//    }
//}
//
//    public static void main(String[] args) throws IOException {
//        new ClientMain();
//    }
//}
