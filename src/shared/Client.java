package shared;

import java.io.Serializable;

public class Client implements Serializable{
    private String nickName;
    private String i2pDestination;
    
    public Client(String nickName, String i2pDestination){
        this.nickName = nickName;
        this.i2pDestination = i2pDestination;
    }
    
    public void setNickName(String nickName){
        this.nickName = nickName;
    }
    public void setI2PDestination(String i2pDestination){
        this.i2pDestination = i2pDestination;
    }
    public String getNickName(){
        return this.nickName;
    }
    public String getI2PDestination(){
        return this.i2pDestination;
    }
    @Override
    public String toString(){
        return "User: " + this.nickName + " with i2pDestination\n" + this.i2pDestination;
    }
}
