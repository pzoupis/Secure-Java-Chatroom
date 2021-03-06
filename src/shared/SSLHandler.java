package shared;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * This class handles the TLS connection between a server and a client.
 * @author Pantelis Zoupis, pantelis.zoupis at gmail.com
 */
public class SSLHandler {

    private String keyStoreName;
    private char[] keyStorePassword;
    private int serverPort;
    private KeyStore keyStore;
    private KeyManagerFactory keyManagerFactory;
    private SSLContext sslContext;
    private TrustManagerFactory trustManagerFactory;
    private SSLServerSocketFactory sslServerSocketFactory;
    private SSLServerSocket sslServerSocket;
    
    private String serverIP;
    private SSLSocketFactory sslSocketFactory;
    private SSLSocket sslSocket;
    private Socket socket;
    
    /**
     * Constructor for the server.
     * In order to create a {@code SSLSocket} we need a {@code SSLSocketFactory} which can be
     * created with a {@code SSLContext}. The {@code SSLContext} needs {@code KeyManagerFactory}
     * which determines which key to use from the {@code KeyStore} and a {@code TrustManagerFactory}
     * which determines which certificate to use.
     * 
     * @param serverPort the port that the server listens to.
     */
    public SSLHandler(int serverPort) { // Για τον server
        try {
            keyStoreName = "serverFiles\\server.jks";
            keyStorePassword = "pa$$word".toCharArray();
            this.serverPort = serverPort;
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(keyStoreName), keyStorePassword);
            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, keyStorePassword);
            sslContext = SSLContext.getInstance("TLSv1.2");
            trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            sslServerSocketFactory = sslContext.getServerSocketFactory();
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(this.serverPort);
        } catch (KeyStoreException ex) {
            Logger.getLogger(SSLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SSLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SSLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SSLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(SSLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(SSLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(SSLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Constructor for the client.
     * The client is using server's {@code serverIP} and {@code serverPort} to
     * create a {@code SSLSocket} and starts the handshake.
     * @param serverIP the server's IP address.
     * @param serverPort the port that server listens to.
     */
    public SSLHandler(String serverIP, int serverPort){ // για τον client
        try {
            System.setProperty("javax.net.ssl.trustStore", "clientFiles\\public.jks");
            this.serverIP = serverIP;
            this.serverPort = serverPort;
            sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslSocket = (SSLSocket) sslSocketFactory.createSocket(this.serverIP, this.serverPort);
            sslSocket.startHandshake();
            socket = sslSocket;
        } catch (IOException ex) {
            Logger.getLogger(SSLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Method that returns server's {@code SSLServerSocket}
     * @return the ssl server socket
     */
    public SSLServerSocket getSSLServerSocket(){
        return this.sslServerSocket;
    }
    
    /**
     * Method that returns client's {@code SSLSocket}
     * @return the ssl socket of the client
     */
    public SSLSocket getSSLSocket(){
        return this.sslSocket;
    }
    
    /**
     * Method that returns client's {@code Socket}
     * @return the socket of the client
     */
    public Socket getSocket(){
        return this.socket;
    }
}
