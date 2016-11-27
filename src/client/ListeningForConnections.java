package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import javax.swing.JOptionPane;
import net.i2p.I2PException;
import net.i2p.client.streaming.I2PSocket;
import shared.Client;
import shared.Message;

public class ListeningForConnections implements Runnable {
    
    private final I2PHandler i2pHandler;
    private final Client currentUser;
    
    public ListeningForConnections(I2PHandler i2pHandler, Client currentUser){
        this.i2pHandler = i2pHandler;
        this.currentUser = currentUser;
    }
    
    @Override
    public void run() {
        while(true){
            try {
                System.out.println("Waiting for connections...");
                I2PSocket sock = this.i2pHandler.getI2PServerSocket().accept();
                System.out.println("Connected");
                if(sock != null){
                    System.out.println("Creating input/output streams...");
                    ObjectInputStream inputStream = new ObjectInputStream(sock.getInputStream());
                    ObjectOutputStream outputStream = new ObjectOutputStream(sock.getOutputStream());
                    Message message;
                    message = (Message) inputStream.readObject();
                    String nickname = message.getMessage();
                    String msg = "User " + nickname + " wants to chat with you.\nDo you want to start a chat?";
                    int choice = JOptionPane.showConfirmDialog(null, msg, "New Connection", JOptionPane.YES_NO_OPTION);
                    if(choice == 0){ // Ο χρήστης δέχτηκε τη σύνδεση
                        message.setMessage("yes");
                        outputStream.writeObject(message);
                        message = (Message) inputStream.readObject();
                        String i2pDestination = message.getMessage();
                        Client client = new Client(nickname, i2pDestination);
                        System.out.println("Opening chat window");
                        ChatWindow chatWindow = new ChatWindow(client, currentUser, i2pHandler, outputStream, inputStream);
                        Thread thread = new Thread(chatWindow);
                        thread.start();
                    } else{ // Ο χρήστης απέρριψε τη σύνδεση
                        message.setMessage("no");
                        outputStream.writeObject(message);
                        inputStream.close();
                        outputStream.close();
                        sock.close();
                    }
                }
            } catch (I2PException ex) {
                System.err.println("General I2P exception!");
            } catch (ConnectException ex) {
                System.err.println("Error connecting!");
            } catch (SocketTimeoutException ex) {
                System.err.println("Timeout!");
            } catch (IOException ex) {
                System.err.println("General read/write-exception!");
            } catch (ClassNotFoundException ex) {
                System.err.println("Class not found");
            }
        }
    }
}
