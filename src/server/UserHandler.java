package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import shared.Client;
import shared.Message;

public class UserHandler implements Runnable{
    private final SSLSocket sslSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Message message;
    private Client client;
    private ServerConnections connections;
    
    public UserHandler(SSLSocket sslSocket, ServerConnections connections){
        this.sslSocket = sslSocket;
        this.connections = connections;
    }
    
    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(sslSocket.getOutputStream());
            inputStream = new ObjectInputStream(sslSocket.getInputStream());
            client = (Client) inputStream.readObject();
            connections.addClient(client);
            message = new Message();
            message.setMessageType("availableClients");
            message.setAvailableClients(connections.getAvailableClients());
            outputStream.writeObject(message);
            do{
                message = (Message) inputStream.readObject();
                if(!message.getMessageType().equals("disconnect")){
                    if(!message.getAvailableClients().equals(connections.getAvailableClients())){
                        List<Client> tempList = new ArrayList<>(connections.getAvailableClients());
                        message.setAvailableClients(tempList);
                        message.setMessageType("newAvailableClients");
                    }
                    outputStream.writeObject(message);
                }
            }while(!message.getMessageType().equals("disconnect"));
            connections.deleteClient(client);
            outputStream.close();
            inputStream.close();
            sslSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
