package server;

import java.util.ArrayList;
import java.util.List;
import shared.Client;

public class ServerConnections {
    private List<UserHandler> availableConnections;
    private List<Client> availableClients;
    
    public ServerConnections(){
        availableConnections = new ArrayList<>();
        availableClients = new ArrayList<>();
    }
    
    public synchronized void addClient(Client client){
        availableClients.add(client);
    }
    
    public synchronized void deleteClient(Client client){
        availableClients.remove(client);
    }
    
    public synchronized void addConnection(UserHandler clientHandler){
        availableConnections.add(clientHandler);
    }
    
    public synchronized void deleteConnection(UserHandler clientHandler){
        availableConnections.remove(clientHandler);
    }
    
    public synchronized List<UserHandler> getAvailableConnections(){
        return this.availableConnections;
    }
    
    public synchronized List<Client> getAvailableClients(){
        return this.availableClients;
    }
}
