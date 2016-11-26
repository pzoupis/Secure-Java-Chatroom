package client;

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

    private final JFrame frame;
    private final JPanel panel;
    private final JTextArea textArea;
    private final JScrollPane scrollPane;
    private final JTextField textField;
    private final JLabel label;
    private final JButton btnSend;
    private final JButton btnCloseChat;
    
    private boolean closeChat;

    private final Client client;
    private final I2PHandler i2pHandler;
    private I2PSocket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Message message;

    public ChatWindow(Client client, I2PHandler i2pHandler) {
        this.client = client;
        this.i2pHandler = i2pHandler;

        frame = new JFrame();
        panel = new JPanel();
        textArea = new JTextArea(10, 20);
        scrollPane = new JScrollPane(textArea);
        textField = new JTextField(30);
        label = new JLabel("Chating with: " + this.client.getNickName());
        btnSend = new JButton("Send");
        btnCloseChat = new JButton("Close chat");
        
        btnCloseChat.addActionListener((e) -> {
            closeChat = true;
        });
        
        closeChat = false;
        
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

        Destination destination;
        try {
            destination = new Destination(client.getI2PDestination());
            socket = this.i2pHandler.getManager().connect(destination);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(new Message("startNewChat"));
            outputStream.flush();
        } catch (DataFormatException ex) {
            System.err.println("Destination string incorrectly formatted.");
        } catch (I2PException ex) {
            System.err.println("General I2P exception occurred!");
        } catch (ConnectException ex) {
            System.err.println("Failed to connect!");
        } catch (NoRouteToHostException ex) {
            System.err.println("Couldn't find host!");
        } catch (InterruptedIOException ex) {
            System.err.println("Sending/receiving was interrupted!");
        } catch (IOException ex) {
            System.out.println("Error occurred while sending/receiving!");
        }
        
        textField.addActionListener((e) -> {
            try {
                message = new Message(textField.getText());
                outputStream.writeObject(message);
                outputStream.flush();
                textArea.append("You: " + textField.getText());
                textField.setText("");
            } catch (IOException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void run() {
        try {
            do{
                message = (Message) inputStream.readObject();
                textArea.append(client.getNickName() + ": " + message.getMessage());
            }while(message.getMessage().equals("quit"));
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
