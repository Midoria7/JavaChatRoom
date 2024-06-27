package javaChatRoom.client.localMessageManager;

import javaChatRoom.common.message.Message;

import java.util.ArrayList;
import java.util.List;

public class LocalMessageManager {
    private List<Message> messages = new ArrayList<>();
    private List<MessageListener> listeners = new ArrayList<>();

    public interface MessageListener {
        void onNewMessage(Message message);
    }

    public synchronized void addMessage(Message message) {
        messages.add(message);
        notifyListeners(message);
    }

    public synchronized List<Message> getAllMessages() {
        return new ArrayList<>(messages);
    }

    public void addMessageListener(MessageListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    private void notifyListeners(Message message) {
        for (MessageListener listener : listeners) {
            listener.onNewMessage(message);
        }
    }
}
