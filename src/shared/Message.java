package shared;
import java.io.Serializable;
import java.util.List;

/**
 * Message represents the message that users and registar are sending to each other.
 * 
 * @author Pantelis Zoupis, pantelis.zoupis at gmail.com
 */
public class Message implements Serializable{

    private String messageType;
    private String message;
    private List<Client> availableClients;
    
    /**
     * Default constructor of a message.
     */
    public Message(){
        
    }
    
    /**
     * Constructs and initializes a message object
     * 
     * @param messageType the type of the message
     * @param message the message
     */
    public Message(String messageType, String message){
        this.messageType = messageType;
        this.message = message;
    }
    
    /**
     * Method that returns the type of the message.
     * @return the type of the message
     */
    public String getMessageType() {
        return messageType;
    }
    
    /**
     * Method that returns the message
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Method that returns the list of available users in the chatroom.
     * @return list of available users
     */
    public List<Client> getAvailableClients() {
        return availableClients;
    }
    
    /**
     * Method that sets the type of message to {@code messageType}.
     * @param messageType the type of message to set
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    
    /**
     * Method that sets the message to {@code message}.
     * @param message the type of message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Method that sets the list of available users to {@code availableClients}
     * @param availableClients list with the available users.
     */
    public void setAvailableClients(List<Client> availableClients) {
        this.availableClients = availableClients;
    }
}
