package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler {
    private final int serverPort;
    private ServerSocket serverSocket;
    
    public ConnectionHandler(int serverPort){
        this.serverPort = serverPort;
        try {
            serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ServerSocket getServerSocket(){
        return this.serverSocket;
    }
}
