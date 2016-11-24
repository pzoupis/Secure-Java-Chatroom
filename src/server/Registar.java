package server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Registar {
    public static void main(String[] args) {
        ConnectionHandler connectionHandler = new ConnectionHandler(5555);
        ServerConnections connections = new ServerConnections();
        while(true){
            try {
                UserHandler ch = new UserHandler(connectionHandler.getServerSocket().accept(), connections);
                Thread thread = new Thread(ch);
                thread.start();
            } catch (IOException ex) {
                Logger.getLogger(Registar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
