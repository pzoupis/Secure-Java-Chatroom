package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.Client;
import shared.Message;
import shared.SSLHandler;

/**
 * Class in which the user communicates with the Registar.
 * Here we create input and output streams, the main application window which
 * lists the available users and every time a heartbeat is sent the list gets refreshed.
 * @author Pantelis Zoupis, pantelis.zoupis at gmail.com
 */
public class RegistarHandler implements Runnable{
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final Client currentUser;
    private Message message;
    private final SSLHandler sslHandler;
    private final I2PHandler i2pHandler;
    
    /**
     * Constructs and initializes an object with information about the user that
     * are useful for the registar.
     * @param sslHandler object that contains the client's socket.
     * @param i2pHandler object that contains the client's I2P information.
     * @param currentUser object that contains the client's nickname and I2P destination
     */
    public RegistarHandler(SSLHandler sslHandler, I2PHandler i2pHandler, Client currentUser){
        this.sslHandler = sslHandler;
        this.i2pHandler = i2pHandler;
        this.currentUser = currentUser;
    }
    
    /**
     * A thread in which the streams and main window are created. Also, inside
     * a loop the user sents a heartbeat and receives a list with available users
     * and refreshes his list in the main window.
     */
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
