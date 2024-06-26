package javaChatRoom.server.messageManager;

import org.junit.jupiter.api.Test;

public class MessageManagerTest {

    @Test
    void testAddMessage() {
        MessageManager messageManager = new MessageManager();
        messageManager.clearMessages();

        Message message1 = new Message("User1", "ALL", "Hello, ALL!");
        Message message2 = new Message("User1", "User2", "Hello, User2!");
        Message message3 = new Message("ANONYMOUS", "User2", "ANONYMOUS, User2!");

        messageManager.addMessage(message1);
        messageManager.addMessage(message2);
        messageManager.addMessage(message3);

        assert messageManager.getMessages().size() == 3;
        assert messageManager.getMessagesForUser("User1").size() == 2;

        System.out.println(messageManager.getMessages());
    }
}
