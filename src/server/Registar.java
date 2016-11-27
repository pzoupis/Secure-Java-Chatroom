package server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import shared.SSLHandler;

public class Registar {
    public static void main(String[] args) {
        System.out.println("Creating SSL Server Socket...");
        SSLHandler sslHandler = new SSLHandler(443);
        System.out.println("Creating a list to store users...");
        ServerConnections connections = new ServerConnections();
        do{
            try {
                System.out.println("Waiting for users...");
                UserHandler ch = new UserHandler((SSLSocket) sslHandler.getSSLServerSocket().accept(), connections);
                Thread thread = new Thread(ch);
                thread.start();
            } catch (IOException ex) {
                Logger.getLogger(Registar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }while(true);
    }
}
