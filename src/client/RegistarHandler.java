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
    private ConnectionHandler connectionHandler;
    
    public RegistarHandler(ConnectionHandler connectionHandler){
        this.connectionHandler = connectionHandler;
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(connectionHandler.getSocket().getOutputStream());
            inputStream = new ObjectInputStream(connectionHandler.getSocket().getInputStream());
            String nickName = JOptionPane.showInputDialog("Nickname");
            String i2pDestination = JOptionPane.showInputDialog("I2PDestination");
            client = new Client(nickName, i2pDestination);
            MainWindow mainWindow = new MainWindow();
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
            connectionHandler.getSocket().close();
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
