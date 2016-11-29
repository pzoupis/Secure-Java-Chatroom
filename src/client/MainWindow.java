package client;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import shared.Client;

/**
 * This is the main window of the application. It has a list with the available
 * users for the current user to choose from in order to chat.
 * @author Pantelis Zoupis, pantelis.zoupis at gmail.com
 */
public class MainWindow{

    private JFrame frame;
    private JPanel panel;
    private JLabel label;
    private JButton btnDisconnect;
    private JButton btnChat;
    private DefaultListModel defaultListModel;
    private JList list;
    private JScrollPane scrollPane;

    private boolean disconnect;
    private final I2PHandler i2pHandler;
    private final Client currentUser;
    
    /**
     * Constructs and initializes an object with information about the user.
     * We also call the method {@code createMainWindow()} to create the main window
     * and {@code createActionListeners()} that creates listeners for the components
     * in the main window.
     * @param i2pHandler I2P information about the user.
     * @param currentUser The nickname and destination of the user.
     */
    public MainWindow(I2PHandler i2pHandler, Client currentUser) {
        this.i2pHandler = i2pHandler;
        this.currentUser = currentUser;
        
        createMainWindow();
        createActionListeners();
    }
    
    /**
     * Method that creates the main window.
     */
    private void createMainWindow(){
        frame = new JFrame();
        panel = new JPanel();
        label = new JLabel("Welcome " + this.currentUser.getNickName() + "\nSelect users to chat");
        btnChat = new JButton("Chat with users");
        btnDisconnect = new JButton("Disconnect from Registar");
        defaultListModel = new DefaultListModel();
        list = new JList(defaultListModel);
        scrollPane = new JScrollPane(list);
        
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(10);
        
        panel.add(label);
        panel.add(scrollPane);
        panel.add(btnChat);
        panel.add(btnDisconnect);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("TrackMeIfYouCanChat");
        frame.setSize(300, 300);
        frame.setVisible(true);

        disconnect = false;
    }
    
    /**
     * Method that creates the listeners for the components of the main window.
     * If the current user select users from the list he can chat with them by
     * pressing the button "Chat with users" or by pressing the button "Enter"
     * on the keyboard. The user can press the button "Disconnect from Registar"
     * to disconnect from the Registar and terminate the application.
     */
    private void createActionListeners(){
        btnChat.addActionListener((ActionEvent e) -> {
            if (list.getSelectedIndices().length <= 0) {
            } else if (list.getSelectedIndices().length == 1) {
                UnicastMode unicastMode = new UnicastMode((Client) list.getSelectedValue(), currentUser, i2pHandler);
            } else {
                MulticastMode multicastMode = new MulticastMode(list.getSelectedValuesList(), currentUser, i2pHandler);
            }
        });
        btnDisconnect.addActionListener((ActionEvent e) -> {
            this.disconnect = true;
        });
        list.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if (list.getSelectedIndices().length <= 0) {
                } else if (list.getSelectedIndices().length == 1 && ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    UnicastMode unicastMode = new UnicastMode((Client) list.getSelectedValue(), currentUser, i2pHandler);
                } else {
                    MulticastMode multicastMode = new MulticastMode(list.getSelectedValuesList(), currentUser, i2pHandler);
                }
            }
        });
    }
    
    /**
     * Method that refreshes the list with the available users.
     * @param availableClients 
     */
    public void setList(List<Client> availableClients) {
        defaultListModel.clear();
        for (int i = 0; i < availableClients.size(); i++) {
            defaultListModel.addElement(availableClients.get(i));
        }
    }
    
    /**
     * Method that becomes true if the user wants to exit the application. Otherwise it's false.
     * @return {@code true} if the user wants to disconnect. Otherwise it returns {@code false}.
     */
    public boolean disconnectFromRegistar() {
        return this.disconnect;
    }
}
