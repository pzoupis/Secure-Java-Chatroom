package client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import net.i2p.I2PException;
import net.i2p.client.streaming.I2PSocket;
import net.i2p.data.DataFormatException;
import net.i2p.data.Destination;
import shared.Client;
import shared.Message;

/**
 * When a one user is selected for chat then an object of this class is created.
 * It handles the connection with that user and checks whether the connection
 * protocol is correct and the user wants to chat.
 * @author Pantelis Zoupis, pantelis.zoupis at gmail.com
 */
public class UnicastMode {

    private final Client client;
    private final Client currentUser;
    private final I2PHandler i2pHandler;
    private Destination destination;
    private I2PSocket i2pSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean accepted;
    
    /**
     * Constructs and initializes an object which has essential information about
     * the users that are about to chat. Also it handles the connection, connection
     * protocol and starts the chat window.
     * @param client
     * @param currentUser
     * @param i2pHandler 
     */
    public UnicastMode(Client client, Client currentUser, I2PHandler i2pHandler) {
        this.client = client;
        this.currentUser = currentUser;
        this.i2pHandler = i2pHandler;

        connectWithClient();
        connectionProtocol();
        startChat();
    }
    
    /**
     * Method that connects the current user with the user and creates input and
     * output streams.
     */
    private void connectWithClient() {
        try {
            destination = new Destination(this.client.getI2PDestination());
            i2pSocket = this.i2pHandler.getManager().connect(destination);
            outputStream = new ObjectOutputStream(i2pSocket.getOutputStream());
            inputStream = new ObjectInputStream(i2pSocket.getInputStream());
        } catch (DataFormatException ex) {
            System.err.println("Destination string incorrectly formatted.");
        } catch (I2PException ex) {
            System.err.println("General I2P exception occurred!");
        } catch (ConnectException ex) {
            System.err.println("Failed to connect!");
        } catch (NoRouteToHostException ex) {
            System.err.println("Couldn't find host!\n" + client.getI2PDestination());
        } catch (InterruptedIOException ex) {
            System.err.println("Sending/receiving was interrupted!");
        } catch (IOException ex) {
            System.err.println("Error occurred while sending/receiving!");
        }
    }
    
    /**
     * Method that implements the connection protocol in which the current user
     * sents his nickname. If the other user accepts the request for chat then the
     * current user also sents his I2P destination.
     */
    private void connectionProtocol() {
        try {
            Message message = new Message("nickname", currentUser.getNickName());
            outputStream.writeObject(message);
            message = (Message) inputStream.readObject();
            if (message.getMessageType().equals("confirmation")) {
                if (message.getMessage().equals("yes")) {
                    accepted = true;
                    message.setMessageType("i2pDestination");
                    message.setMessage(currentUser.getI2PDestination());
                    outputStream.writeObject(message);
                    outputStream.flush();
                } else {
                    accepted = false;
                    outputStream.close();
                    inputStream.close();
                    i2pSocket.close();
                }
            } else {
                accepted = false;
                outputStream.close();
                inputStream.close();
                i2pSocket.close();
            }
        } catch (IOException ex) {
            System.err.println("Error occurred while sending/receiving!");
        } catch (ClassNotFoundException ex) {
            System.err.println("Class not found");
        }
    }
    
    /**
     * Method that if the other user accept the request to chat, a chat window is
     * created.
     */
    private void startChat() {
        if (accepted) {
            ChatWindow chatWindow = new ChatWindow(this.client, this.currentUser, this.outputStream, this.inputStream);
            Thread thread = new Thread(chatWindow);
            thread.start();
        }
    }
}
