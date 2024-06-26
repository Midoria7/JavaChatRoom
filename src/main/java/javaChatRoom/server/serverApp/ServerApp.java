package javaChatRoom.server.serverApp;

import javaChatRoom.server.ServerMain;
import javaChatRoom.server.serverLogger.ServerLogger;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class ServerApp extends JFrame {
    private JButton startButton, stopButton, showOnlineUsersButton, showAllUsersButton;
    private JTextArea logArea;
    private ServerMain serverMain;

    public ServerApp() {
        createUI();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
    }

    private void createUI() {
        logArea = new JTextArea(22, 70);
        logArea.setEditable(false);

        ServerLogger.setLogArea(logArea);  // Set the log area for ServerLogger

        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        showOnlineUsersButton = new JButton("Show Online Users");
        showAllUsersButton = new JButton("Show All Users");

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(logArea));
        panel.add(startButton);
        panel.add(stopButton);
        panel.add(showOnlineUsersButton);
        panel.add(showAllUsersButton);

        addEventHandlers();
        add(panel);
    }

    private void addEventHandlers() {
        startButton.addActionListener(e -> startServer());
        stopButton.addActionListener(e -> stopServer());
        showOnlineUsersButton.addActionListener(e -> displayUserList(true));
        showAllUsersButton.addActionListener(e -> displayUserList(false));
    }

    private void startServer() {
        logArea.append("Starting server...\n");
        new Thread(() -> {
            serverMain = new ServerMain(12345);
            serverMain.start();
        }).start();
    }

    private void stopServer() {
        if (serverMain != null) {
            serverMain.stopServer();
            logArea.append("Server stopped.\n");
        }
    }

    private void displayUserList(boolean online) {
        java.util.List<String> users = online ? serverMain.getOnlineUsers() : serverMain.getAllUsers();
        SwingUtilities.invokeLater(() -> createUserListWindow(users, online ? "Online Users" : "All Users"));
    }

    private void createUserListWindow(java.util.List<String> users, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JList<String> userList = new JList<>(users.toArray(new String[0]));
        if (users.isEmpty()) {
            userList.setListData(new String[]{"No users"});
        }
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setPreferredSize(new Dimension(300, 200)); // 设置滚动面板的首选大小
        frame.add(scrollPane);
        frame.pack(); // 调整窗口大小以适应组件的首选大小
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        try {
//            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ServerLogger.writeError("Error setting look and feel: " + e.getMessage());
        }
        EventQueue.invokeLater(() -> {
            ServerApp frame = new ServerApp();
            frame.setVisible(true);
            frame.setTitle("Server");
        });
    }
}
