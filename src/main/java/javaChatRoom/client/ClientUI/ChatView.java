package javaChatRoom.client.ClientUI;

import javaChatRoom.client.messageHandler.MessageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                performCloseOperation();
            }
        });
        pack();
        setVisible(true);
        sendButton.addActionListener(this::sendMessage);
    }

    private void performCloseOperation() {
        int confirmed = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out?", "User Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            messageHandler.sendMessage("@@quit");
            dispose();
        }
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
