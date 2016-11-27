package client;

import java.awt.Toolkit;
import javax.swing.JOptionPane;
import shared.Client;
import shared.SSLHandler;

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
