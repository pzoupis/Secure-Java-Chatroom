package client;
public class User {
    public static void main(String[] args){
        SSLClientHandler sslClientHandler = new SSLClientHandler("127.0.0.1", 5555);
        RegistarHandler registarHandler = new RegistarHandler(sslClientHandler);
        Thread thread = new Thread(registarHandler);
        thread.start();
    }
}
