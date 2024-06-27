package javaChatRoom.client.ClientConfigManager;

public class ClientConfigManager {
    private String currentUsername = null;
    private boolean isAnonymous = false;

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }
}
