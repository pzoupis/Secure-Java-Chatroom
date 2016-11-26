package shared;
import java.io.Serializable;
import java.util.List;
public class Message implements Serializable{
    private String messageType;
    private String message;
    private List<Client> availableClients;
    
    public Message(){
        
    }
    public Message(String message){
        this.message = message;
    }
    public Message(String messageType, String message){
        this.messageType = messageType;
        this.message = message;
    }
    
    public String getMessageType() {
        return messageType;
    }
    public String getMessage() {
        return message;
    }
    public List<Client> getAvailableClients() {
        return availableClients;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setAvailableClients(List<Client> availableClients) {
        this.availableClients = availableClients;
    }
}
