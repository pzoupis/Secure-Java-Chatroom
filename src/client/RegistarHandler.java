package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.Client;
import shared.Message;
import shared.SSLHandler;

public class RegistarHandler implements Runnable{
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final Client currentUser;
    private Message message;
    private final SSLHandler sslHandler;
    private final I2PHandler i2pHandler;
    
    public RegistarHandler(SSLHandler sslHandler, I2PHandler i2pHandler, Client currentUser){
        this.sslHandler = sslHandler;
        this.i2pHandler = i2pHandler;
        this.currentUser = currentUser;
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(sslHandler.getSocket().getOutputStream());
            inputStream = new ObjectInputStream(sslHandler.getSocket().getInputStream());
            MainWindow mainWindow = new MainWindow(i2pHandler, currentUser);
            outputStream.writeObject(currentUser);
            message = (Message) inputStream.readObject();
            while(!mainWindow.disconnectFromRegistar()){
                mainWindow.setList(message.getAvailableClients());
                message.setMessageType("heartbeat");
                Thread.sleep(10000);
                outputStream.writeObject(message);
                message = (Message) inputStream.readObject();
            }
            message.setMessageType("disconnect");
            outputStream.writeObject(message);
            inputStream.close();
            outputStream.close();
            sslHandler.getSSLSocket().close();
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(RegistarHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RegistarHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(RegistarHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
