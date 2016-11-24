package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.Client;
import shared.Message;

public class UserHandler implements Runnable{
    private final Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Message message;
    private Client client;
    private ServerConnections connections;
    
    public UserHandler(Socket socket, ServerConnections connections){
        this.socket = socket;
        this.connections = connections;
    }
    
    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
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
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
