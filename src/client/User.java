package client;
public class User {
    public static void main(String[] args){
        I2PHandler i2pHandler = new I2PHandler();
        SSLClientHandler sslClientHandler = new SSLClientHandler("127.0.0.1", 5555);
        RegistarHandler registarHandler = new RegistarHandler(sslClientHandler, i2pHandler);
        Thread thread = new Thread(registarHandler);
        thread.start();
    }
}
