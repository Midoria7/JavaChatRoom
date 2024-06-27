package javaChatRoom.client.ClientUI;

import javaChatRoom.client.messageHandler.MessageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

public class ChatView extends JFrame {
    private JPanel chatWindowPanel;
    private JTextField inputField;
    private JButton sendButton;
    private JScrollPane messageWindowPanel;
    private JTextArea messageTextArea;
    private MessageHandler messageHandler;

    public ChatView() {
        super("ChatView");
        setContentPane(chatWindowPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        sendButton.addActionListener(this::sendMessage);
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    private void sendMessage(ActionEvent e) {
        String messageText = inputField.getText();
        if (!messageText.isEmpty()) {
            inputField.setText("");
            messageHandler.sendMessage(messageText);
        }
    }

    public void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> messageTextArea.append(message + "\n"));
    }
}
