package client;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

/**
 * The class that represents the chat window of the application. It creates the
 * window and adds the functionality.
 * @author Pantelis Zoupis, pantelis.zoupis at gmail.com
 */
public class ChatWindow implements Runnable{

    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JTextField textField;
    private JLabel label;
    private JButton btnSend;
    private JButton btnCloseChat;
    
    private final Client client;
    private final Client currentUser;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private Message message;
    
    /**
     * Constructs and initializes an object that has the names of the users chatting
     * and the output and input streams. Also it creates the window and adds
     * listeners to some components of the window.
     * @param client
     * @param currentUser
     * @param outputStream
     * @param inputStream 
     */
    public ChatWindow(Client client, Client currentUser, ObjectOutputStream outputStream, ObjectInputStream inputStream){
        this.client = client;
        this.currentUser = currentUser;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        
        createChatWindow();
        createActionListeners();
    }
    
    /**
     * Method that creates the window.
     */
    private void createChatWindow(){
        frame = new JFrame();
        panel = new JPanel();
        textArea = new JTextArea(10, 20);
        scrollPane = new JScrollPane(textArea);
        textField = new JTextField(30);
        label = new JLabel(this.currentUser.getNickName() + " is chating with: " + this.client.getNickName());
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
    
    /**
     * Method that adds listeners to the buttons and the textfield.
     * When the user press enter in the textfield he sents the message to the other
     * user.
     */
    private void createActionListeners(){
        btnCloseChat.addActionListener((e) -> {
            try {
                message = new Message("quit", "User " + this.currentUser.getNickName() + " left the chat.");
                outputStream.writeObject(message);
            } catch (IOException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnSend.addActionListener((ActionEvent e) -> {
            try {
                message = new Message("chatmessage", textField.getText());
                outputStream.writeObject(message);
                outputStream.flush();
                textArea.append(this.currentUser.getNickName() + ": " + textField.getText() + "\n");
                textField.setText("");
            } catch (IOException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        textField.addActionListener((e) -> {
            try {
                message = new Message("chatmessage", textField.getText());
                outputStream.writeObject(message);
                outputStream.flush();
                textArea.append(this.currentUser.getNickName() + ": " + textField.getText() + "\n");
                textField.setText("");
            } catch (IOException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    /**
     * In this method the user listens for incoming messages appends them in the
     * TextArea.
     */
    @Override
    public void run() {
        try {
            do{
                message = (Message) inputStream.readObject();
                textArea.append(client.getNickName() + ": " + message.getMessage() + "\n");
            }while(!message.getMessage().equals("quit"));
            outputStream.close();
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
