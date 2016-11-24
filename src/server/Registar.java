package server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

public class Registar {
    public static void main(String[] args) {
        //ConnectionHandler connectionHandler = new ConnectionHandler(5555);
        SSLServerHandler sslServerHandler = new SSLServerHandler();
        ServerConnections connections = new ServerConnections();
        while(true){
            try {
                UserHandler ch = new UserHandler((SSLSocket) sslServerHandler.getSSLServerSocket().accept(), connections);
                Thread thread = new Thread(ch);
                thread.start();
            } catch (IOException ex) {
                Logger.getLogger(Registar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
