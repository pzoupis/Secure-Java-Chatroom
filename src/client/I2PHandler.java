package client;

import net.i2p.client.I2PSession;
import net.i2p.client.streaming.I2PServerSocket;
import net.i2p.client.streaming.I2PSocketManager;
import net.i2p.client.streaming.I2PSocketManagerFactory;

/**
 * Class that handles the connection to the I2P network.
 * @author Pantelis Zoupis, pantelis.zoupis at gmail.com
 */
public class I2PHandler {
    private final I2PSocketManager manager;
    private final I2PServerSocket serverSocket;
    private final I2PSession session;
    private final String i2pDestination;
    
    /**
     * Constructs and initializes the client's I2P connection.
     * We get an I2P server socket and the I2P destination of the client in
     * the network.
     */
    public I2PHandler(){
        this.manager = I2PSocketManagerFactory.createManager();
        this.serverSocket = manager.getServerSocket();
        this.session = manager.getSession();
        this.i2pDestination = session.getMyDestination().toBase64();
        System.err.println(i2pDestination);
    }
    
    /**
     * Method that returns the I2P destination of the user.
     * @return I2P destination.
     */
    public String getI2PDestination(){
        return i2pDestination;
    }
    
    /**
     * Method that returns the I2P socket manager.
     * @return I2p socket manager.
     */
    public I2PSocketManager getManager(){
        return this.manager;
    }
    
    /**
     * Method that returns the I2P server socket that is being used to listen
     * for connections from other users in the I2P network.
     * @return I2P server socket.
     */
    public I2PServerSocket getI2PServerSocket(){
        return this.serverSocket;
    }
}
