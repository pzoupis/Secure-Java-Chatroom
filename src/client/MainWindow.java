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

public class MainWindow {

    private JFrame frame;
    private JPanel panel;
    private JLabel label;
    private JButton btnDisconnect;
    private JButton btnChat;
    private DefaultListModel defaultListModel;
    private JList list;
    private JScrollPane scrollPane;

    private boolean disconnect;

    public MainWindow() {
        frame = new JFrame();
        panel = new JPanel();
        label = new JLabel("Select users to chat");
        btnChat = new JButton("Chat with users");
        btnDisconnect = new JButton("Disconnect from Registar");
        defaultListModel = new DefaultListModel();
        list = new JList(defaultListModel);
        scrollPane = new JScrollPane(list);

        btnChat.addActionListener((ActionEvent e) -> {
            if (list.getSelectedIndices().length <= 0) {
            }
            else {
                System.out.println(list.getSelectedValuesList());
            }
        });

        btnDisconnect.addActionListener((ActionEvent e) -> {
            this.disconnect = true;
        });

        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(10);
        list.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if (list.getSelectedIndices().length <= 0) {
                    return;
                }
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println(list.getSelectedValuesList());
                }
            }
        });

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
    
    public void setList(List<Client> availableClients){
        defaultListModel.clear();
        for(int i = 0; i < availableClients.size(); i++){
            defaultListModel.addElement(availableClients.get(i).getNickName());
        }
    }
    
    public boolean disconnectFromRegistar(){
        return this.disconnect;
    }
}
