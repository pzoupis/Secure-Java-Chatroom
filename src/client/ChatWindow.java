package client;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
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
import net.i2p.I2PException;
import net.i2p.client.streaming.I2PSocket;
import net.i2p.data.DataFormatException;
import net.i2p.data.Destination;
import shared.Client;
import shared.Message;

public class ChatWindow implements Runnable{

    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JTextField textField;
    private JLabel label;
    private JButton btnSend;
    private JButton btnCloseChat;
    
    private boolean closeChat;

    private final Client client;
    private final Client currentUser;
    private I2PHandler i2pHandler;
    private I2PSocket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Message message;
    
    public ChatWindow(Client client, Client currentUser, I2PHandler i2pHandler, ObjectOutputStream outputStream, ObjectInputStream inputStream){
        this.client = client;
        this.currentUser = currentUser;
        this.i2pHandler = i2pHandler;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        
        createChatWindow();
        createActionListeners();
    }
    
    public ChatWindow(Client client, Client currentUser, I2PHandler i2pHandler) {
        this.client = client;
        this.i2pHandler = i2pHandler;
        this.currentUser = currentUser;
        
        createChatWindow();
        createActionListeners();
        
        Destination destination;
        try {
            destination = new Destination(this.client.getI2PDestination());
            socket = this.i2pHandler.getManager().connect(destination);
            System.out.println("Connected with client " + client.getNickName());
            System.out.println("Creating streams");
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("sending currentUser");
            Message userInfo = new Message(this.currentUser.getNickName());
            outputStream.writeObject(userInfo);
            userInfo = (Message) inputStream.readObject();
            if(userInfo.getMessage().equals("yes")){
                userInfo.setMessage(this.currentUser.getI2PDestination());
                outputStream.writeObject(userInfo);
                outputStream.flush();
            }
        } catch (DataFormatException ex) {
            System.err.println("Destination string incorrectly formatted.");
        } catch (I2PException ex) {
            System.err.println("General I2P exception occurred!");
        } catch (ConnectException ex) {
            System.err.println("Failed to connect!");
        } catch (NoRouteToHostException ex) {
            System.err.println("Couldn't find host!\n"+client.getI2PDestination());
        } catch (InterruptedIOException ex) {
            System.err.println("Sending/receiving was interrupted!");
        } catch (IOException ex) {
            System.err.println("Error occurred while sending/receiving!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Chat Window");
        frame.pack();
        frame.setVisible(true);
        
        closeChat = false;
    }
    
    private void createActionListeners(){
        btnCloseChat.addActionListener((e) -> {
            closeChat = true;
        });
        btnSend.addActionListener((ActionEvent e) -> {
            try {
                message = new Message(textField.getText());
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
                message = new Message(textField.getText());
                outputStream.writeObject(message);
                outputStream.flush();
                textArea.append(this.currentUser.getNickName() + ": " + textField.getText() + "\n");
                textField.setText("");
            } catch (IOException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public boolean getCloseChat(){
        return this.closeChat;
    }

    @Override
    public void run() {
        try {
            do{
                message = (Message) inputStream.readObject();
                textArea.append(client.getNickName() + ": " + message.getMessage() + "\n");
            }while(!message.getMessage().equals("quit"));
            outputStream.close();
            inputStream.close();
            socket.close();
            closeChat = true;
        } catch (IOException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
