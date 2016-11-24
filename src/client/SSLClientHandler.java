package client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLClientHandler {

    private String serverIP;
    private int serverPort;
    private SSLSocketFactory sslSocketFactory;
    private SSLSocket sslSocket;

    public SSLClientHandler(String serverIP, int serverPort) {
        try {
            System.setProperty("javax.net.ssl.trustStore", "clientFiles\\public.jks");
            this.serverIP = serverIP;
            this.serverPort = serverPort;
            sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslSocket = (SSLSocket) sslSocketFactory.createSocket(this.serverIP, this.serverPort);
            sslSocket.startHandshake();
        } catch (IOException ex) {
            Logger.getLogger(SSLClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public SSLSocket getSSLSocket(){
        return this.sslSocket;
    }
}
