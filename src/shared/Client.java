package shared;

import java.io.Serializable;

/**
 * Client represents a user in the chatroom.
 * Users have a nickname and an i2p destination that are immutable.
 * They cannot be changed once they have been created.
 * 
 * @author Pantelis Zoupis, pantelis.zoupis at gmail.com
 */
public class Client implements Serializable{
    private final String nickName;
    private final String i2pDestination;
    
    /**
     * Constructs and initializes a user in the chatroom.
     * @param nickName the name that the user provides in the chatroom
     * @param i2pDestination the i2p destination of the user in the i2p network
     */
    public Client(String nickName, String i2pDestination){
        this.nickName = nickName;
        this.i2pDestination = i2pDestination;
    }
    
    /**
     * Method that returns the nickname of the user.
     * @return the nickname of the user
     */
    public String getNickName(){
        return this.nickName;
    }
    
    /**
     * Method that returns the i2p destination of the user.
     * @return the i2p destination of the user
     */
    public String getI2PDestination(){
        return this.i2pDestination;
    }
    
    /**
     * Converts the client to string in order to return the nickname of the user.
     * @return the nickname of the user
     */
    @Override
    public String toString(){
        return this.nickName;
    }
}
