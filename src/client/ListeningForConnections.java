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
    private I2PSocket i2pSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Client client;
    private boolean accepted;
    
    public ListeningForConnections(I2PHandler i2pHandler, Client currentUser){
        this.i2pHandler = i2pHandler;
        this.currentUser = currentUser;
    }
    
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
    private void startChat(){
        if(accepted){
            ChatWindow chatWindow = new ChatWindow(this.client, this.currentUser, this.outputStream, this.inputStream);
            Thread thread = new Thread(chatWindow);
            thread.start();
        }
    }
    @Override
    public void run() {
        while(true){
            connectWithClient();
            connectionProtocol();
            startChat();
        }
    }
}
