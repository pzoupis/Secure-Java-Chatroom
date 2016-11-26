package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import shared.Client;
import shared.Message;

public class RegistarHandler implements Runnable{
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Client client;
    private Message message;
    private final SSLClientHandler sslClientHandler;
    private final I2PHandler i2pHandler;
    
    public RegistarHandler(SSLClientHandler sslClientHandler, I2PHandler i2pHandler){
        this.sslClientHandler = sslClientHandler;
        this.i2pHandler = i2pHandler;
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(sslClientHandler.getSocket().getOutputStream());
            inputStream = new ObjectInputStream(sslClientHandler.getSocket().getInputStream());
            String nickName = JOptionPane.showInputDialog("Enter your nickname");
            client = new Client(nickName, i2pHandler.getI2PDestination());
            MainWindow mainWindow = new MainWindow(i2pHandler);
            outputStream.writeObject(client);
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
            sslClientHandler.getSSLSocket().close();
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
