package client;
public class User {
    public static void main(String[] args){
        ConnectionHandler connectionHandler = new ConnectionHandler("127.0.0.1", 5555);
        RegistarHandler registarHandler = new RegistarHandler(connectionHandler);
        Thread thread = new Thread(registarHandler);
        thread.start();
    }
}
