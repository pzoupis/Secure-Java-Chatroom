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

/**
 * A class that implements Runnable and is being used to listen for new connection
 * from the I2P network. 
 * @author Pantelis Zoupis, pantelis.zoupis at gmail.com
 */
public class ListeningForConnections implements Runnable {
    
    private final I2PHandler i2pHandler;
    private final Client currentUser;
    private I2PSocket i2pSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Client client;
    private boolean accepted;
    
    /**
     * Constructs and initializes an object with information about the user.
     * @param i2pHandler the user's I2P destination and server socket.
     * @param currentUser the user's name and address.
     */
    public ListeningForConnections(I2PHandler i2pHandler, Client currentUser){
        this.i2pHandler = i2pHandler;
        this.currentUser = currentUser;
    }
    
    /**
     * Method that listens for a connection in the I2P network and creates
     * output and input streams.
     */
    private void connectWithClient(){
        try {
            i2pSocket = this.i2pHandler.getI2PServerSocket().accept();
            if(i2pSocket != null){
                outputStream = new ObjectOutputStream(i2pSocket.getOutputStream());
                inputStream = new ObjectInputStream(i2pSocket.getInputStream());
            }
        } catch (I2PException ex) {
            System.err.println("General I2P exception!");
        } catch (ConnectException ex) {
            System.err.println("Error connecting!");
        } catch (SocketTimeoutException ex) {
            System.err.println("Timeout!");
        } catch (IOException ex) {
            System.err.println("General read/write-exception!");
        }
    }
    
    /**
     * Method that implements a protocol in which the user that wants to connect
     * sents the nickname. Then the current user is prompt with a question that
     * asks whether he wants to chat or not. If the current user accepts, sents
     * a confirmation message on the other user. If the answer is yes then the
     * current user gets the other users I2P destination in order to connect with
     * him. Otherwise the streams and sockets close.
     */
    private void connectionProtocol(){
        try {
            Message message;
            String nickname, i2pDestination;
            message = (Message) inputStream.readObject();
            if(message.getMessageType().equals("nickname")){
                nickname = message.getMessage();
                String msg = "User " + nickname + " wants to chat with you.\nDo you want to start a chat?";
                int choice = JOptionPane.showConfirmDialog(null, msg, "New Connection", JOptionPane.YES_NO_OPTION);
                if(choice == 0){
                    message.setMessageType("confirmation");
                    message.setMessage("yes");
                    outputStream.writeObject(message);
                    message = (Message) inputStream.readObject();
                    if(message.getMessageType().equals("i2pDestination")){
                        i2pDestination = message.getMessage();
                        client = new Client(nickname,i2pDestination);
                        accepted = true;
                    } else {
                        accepted = false;
                        outputStream.close();
                        inputStream.close();
                        i2pSocket.close();
                    }
                } else {
                    accepted = false;
                    outputStream.close();
                    inputStream.close();
                    i2pSocket.close();
                }
            } else {
                accepted = false;
                outputStream.close();
                inputStream.close();
                i2pSocket.close();
            }
        } catch (IOException ex) {
            System.err.println("General read/write-exception!");
        } catch (ClassNotFoundException ex) {
            System.err.println("Class not found");
        }
    }
    
    /**
     * If the current user agree to chat then a chat window is created.
     */
    private void startChat(){
        if(accepted){
            ChatWindow chatWindow = new ChatWindow(this.client, this.currentUser, this.outputStream, this.inputStream);
            Thread thread = new Thread(chatWindow);
            thread.start();
        }
    }
    
    /**
     * The above methods run in a loop so that more than one users can request a
     * connection with the current user.
     */
    @Override
    public void run() {
        while(true){
            connectWithClient();
            connectionProtocol();
            startChat();
        }
    }
}
