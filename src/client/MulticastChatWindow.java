package client;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import shared.Client;
import shared.Message;

public class MulticastChatWindow implements Runnable {

    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JTextField textField;
    private JLabel label;
    private JButton btnSend;
    private JButton btnCloseChat;

    private final List<Client> clients;
    private final Client currentUser;
    private final List<ObjectOutputStream> outputStreams;
    private final List<ObjectInputStream> inputStreams;
    private Message message;

    public MulticastChatWindow(List<Client> clients, Client currentUser, List<ObjectOutputStream> outputStreams, List<ObjectInputStream> inputStreams) {
        this.clients = clients;
        this.currentUser = currentUser;
        this.outputStreams = outputStreams;
        this.inputStreams = inputStreams;

        createChatWindow();
        createActionListeners();
    }

    private void createChatWindow() {
        frame = new JFrame();
        panel = new JPanel();
        textArea = new JTextArea(10, 20);
        scrollPane = new JScrollPane(textArea);
        textField = new JTextField(30);
        String strLabel = "";
        for (Client client : clients) {
            strLabel = strLabel + client.getNickName() + " ";
        }
        label = new JLabel(this.currentUser.getNickName() + " is chating with: " + strLabel);
        btnSend = new JButton("Send");
        btnCloseChat = new JButton("Close chat");

        textArea.setText("");
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(scrollPane);
        panel.add(textField);
        panel.add(btnSend);
        panel.add(btnCloseChat);

        frame.add(panel);
        frame.setTitle("Chat Window");
        frame.pack();
        frame.setVisible(true);
    }

    private void createActionListeners() {
        btnCloseChat.addActionListener((e) -> {
            try {
                message = new Message("quit", "User " + this.currentUser.getNickName() + " left the chat.");
                for (ObjectOutputStream outputStream : outputStreams) {
                    outputStream.writeObject(message);
                    outputStream.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnSend.addActionListener((ActionEvent e) -> {
            try {
                message = new Message("chatmessage", textField.getText());
                for (ObjectOutputStream outputStream : outputStreams) {
                    outputStream.writeObject(message);
                    outputStream.flush();
                }
                textArea.append(this.currentUser.getNickName() + ": " + textField.getText() + "\n");
                textField.setText("");
            } catch (IOException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        textField.addActionListener((e) -> {
            try {
                message = new Message("chatmessage", textField.getText());
                for (ObjectOutputStream outputStream : outputStreams) {
                    outputStream.writeObject(message);
                    outputStream.flush();
                }
                textArea.append(this.currentUser.getNickName() + ": " + textField.getText() + "\n");
                textField.setText("");
            } catch (IOException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void run() {
//        try {
//            do {
//                message = (Message) inputStream.readObject();
//                textArea.append(client.getNickName() + ": " + message.getMessage() + "\n");
//            } while (!message.getMessage().equals("quit"));
//            outputStream.close();
//            inputStream.close();
//        } catch (IOException ex) {
//            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
