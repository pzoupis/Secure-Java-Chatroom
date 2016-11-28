package client;

import java.awt.Toolkit;
import javax.swing.JOptionPane;
import shared.Client;
import shared.SSLHandler;

/**
 * The main class for the clients.
 * I2PHandler creates the I2P server socket and the I2P destination.
 * Getting the I2P destination takes some time so we added a beep to alert as
 * when it's ready.
 * The user can input the nickname through an input dialog.
 * SSLHandler creates the SSLSocket to make a secure connection with the Registar.
 * ListeningForConnections creates a thread that checks for connections from other users.
 * RegistarHandler handles the connection between the registar and the user.
 * @author Pantelis Zoupis, pantelis.zoupis at gmail.com
 */
public class User {
    public static void main(String[] args){
        I2PHandler i2pHandler = new I2PHandler();
        Toolkit.getDefaultToolkit().beep();
        String nickname = JOptionPane.showInputDialog("Enter your nickname");
        
        Client currentUser = new Client(nickname, i2pHandler.getI2PDestination());
        
        SSLHandler sslHandler = new SSLHandler("127.0.0.1", 443);
        
        ListeningForConnections listeningForClients = new ListeningForConnections(i2pHandler, currentUser);
        Thread thread1 = new Thread(listeningForClients);
        thread1.start();
        
        RegistarHandler registarHandler = new RegistarHandler(sslHandler, i2pHandler, currentUser);
        Thread thread2 = new Thread(registarHandler);
        thread2.start();
    }
}
