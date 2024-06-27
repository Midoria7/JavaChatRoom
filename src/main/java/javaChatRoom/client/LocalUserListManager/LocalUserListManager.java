package javaChatRoom.client.LocalUserListManager;

import java.util.ArrayList;
import java.util.List;

public class LocalUserListManager {
    private List<String> onlineUsers = new ArrayList<>();

    public synchronized void updateUsers(List<String> users) {
        onlineUsers.clear();
        onlineUsers.addAll(users);
        // 这里可以加上通知UI更新的代码，比如通过观察者模式或直接调用UI组件的更新方法
        System.out.println("Updated online users: " + onlineUsers);
    }

    public synchronized List<String> getOnlineUsers() {
        return new ArrayList<>(onlineUsers);
    }
}
