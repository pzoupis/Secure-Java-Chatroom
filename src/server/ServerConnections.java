package server;

import java.util.ArrayList;
import java.util.List;
import shared.Client;

/**
 * {@code ServerConnections} contains a list to store information about the
 * users that are available in the chatroom.
 * @author Pantelis Zoupis, pantelis.zoupis at gmail.com
 */
public class ServerConnections {
    private List<Client> availableClients;
    
    /**
     * Constructs a new ArrayList to store the users of the chatroom.
     */
    public ServerConnections(){
        availableClients = new ArrayList<>();
    }
    
    /**
     * Method that adds a client in the list of users.
     * @param client the client to be added in the list.
     */
    public synchronized void addClient(Client client){
        availableClients.add(client);
    }
    
    /**
     * Method that removes a client from the list of users.
     * @param client the client to be removed from the list.
     */
    public synchronized void deleteClient(Client client){
        availableClients.remove(client);
    }
    
    /**
     * Method that returns the list of available users.
     * @return the list of available users.
     */
    public synchronized List<Client> getAvailableClients(){
        return this.availableClients;
    }
}
