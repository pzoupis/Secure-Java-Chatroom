package client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.i2p.I2PException;
import net.i2p.client.streaming.I2PSocket;
import net.i2p.data.DataFormatException;
import net.i2p.data.Destination;
import shared.Client;
import shared.Message;

public class MulticastMode {

    private final List<Client> clients;
    private final Client currentUser;
    private final I2PHandler i2pHandler;
    private List<I2PSocket> i2pSockets;
    private List<ObjectOutputStream> outputStreams;
    private List<ObjectInputStream> inputStreams;
    private boolean accepted;

    public MulticastMode(List<Client> clients, Client currentUser, I2PHandler i2pHandler) {
        this.clients = clients;
        this.currentUser = currentUser;
        this.i2pHandler = i2pHandler;

        connectsWithClients();
        connectionProtocol();
    }

    private void connectsWithClients() {
        try {
            for (Client client : clients) {
                Destination destination = new Destination(client.getI2PDestination());
                I2PSocket i2pSocket = i2pHandler.getManager().connect(destination);
                i2pSockets.add(i2pSocket);
                outputStreams.add(new ObjectOutputStream(i2pSocket.getOutputStream()));
                inputStreams.add(new ObjectInputStream(i2pSocket.getInputStream()));
            }
        } catch (DataFormatException ex) {
            Logger.getLogger(MulticastMode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (I2PException ex) {
            Logger.getLogger(MulticastMode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            Logger.getLogger(MulticastMode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoRouteToHostException ex) {
            Logger.getLogger(MulticastMode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedIOException ex) {
            Logger.getLogger(MulticastMode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MulticastMode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void connectionProtocol() {
        for (int i = 0; i < clients.size(); i++) {
            try {
                Message message = new Message("nickname", currentUser.getNickName());
                outputStreams.get(i).writeObject(message);
                message = (Message) inputStreams.get(i).readObject();
                if (message.getMessageType().equals("confirmation")) {
                    if (message.getMessage().equals("yes")) {
                        message.setMessageType("i2pDestination");
                        message.setMessage(currentUser.getI2PDestination());
                        outputStreams.get(i).writeObject(message);
                        outputStreams.get(i).flush();
                    }
                }
            } catch (IOException ex) {
                System.err.println("Error occurred while sending/receiving!");
            } catch (ClassNotFoundException ex) {
                System.err.println("Class not found");
            }
        }
    }

    private void startChat() {
        //ChatWindow chatWindow = new ChatWindow(this.clients, this.currentUser, this.outputStreams, this.inputStreams);
        //Thread thread = new Thread(chatWindow);
        //thread.start();
    }
}
