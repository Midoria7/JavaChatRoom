package javaChatRoom.server.userManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javaChatRoom.server.serverLogger.ServerLogger;

public class UserManager {
    private List<User> users;
    private List<User> onlineUsers;
    private static final String USER_DATA_PATH = "user.ser";

    public UserManager() {
        users = new ArrayList<>();
        onlineUsers = new ArrayList<>();
        loadUsers();
    }

    public void clearUsers() {
        users.clear();
        saveUsers();
    }

    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_PATH))) {
            users = (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            ServerLogger.writeError("Error loading users: " + e.getMessage());
        }
    }

    public boolean isValidUser(String username, String password) {
        return users.stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }

    public void registerUser(String username, String password) {
        users.add(new User(username, password));
        saveUsers();
    }

    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            ServerLogger.writeError("Error saving users: " + e.getMessage());
        }
    }

    public void userLoggedIn(User user) {
        if (!onlineUsers.contains(user)) {
            onlineUsers.add(user);
        }
    }

    public void userLoggedOut(User user) {
        onlineUsers.remove(user);
    }

    public List<User> getOnlineUsers() {
        return new ArrayList<>(onlineUsers);
    }
}
