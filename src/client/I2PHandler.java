package client;


import net.i2p.client.I2PSession;
import net.i2p.client.streaming.I2PServerSocket;
import net.i2p.client.streaming.I2PSocketManager;
import net.i2p.client.streaming.I2PSocketManagerFactory;

public class I2PHandler {
    private final I2PSocketManager manager;
    private final I2PServerSocket serverSocket;
    private final I2PSession session;
    private final String i2pDestination;
    
    public I2PHandler(){
        manager = I2PSocketManagerFactory.createManager();
        serverSocket = manager.getServerSocket();
        session = manager.getSession();
        i2pDestination = session.getMyDestination().toBase64();
        System.err.println(i2pDestination);
    }
    
    public String getI2PDestination(){
        return i2pDestination;
    }
    public I2PSocketManager getManager(){
        return this.manager;
    }
}
