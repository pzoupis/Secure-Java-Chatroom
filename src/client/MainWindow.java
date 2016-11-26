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

    private final JFrame frame;
    private final JPanel panel;
    private final JLabel label;
    private final JButton btnDisconnect;
    private final JButton btnChat;
    private final DefaultListModel defaultListModel;
    private JList list;
    private final JScrollPane scrollPane;

    private boolean disconnect;
    private final I2PHandler i2pHandler;

    public MainWindow(I2PHandler i2pHandler) {
        this.i2pHandler = i2pHandler;

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
            } else if (list.getSelectedIndices().length == 1) {
                ChatWindow chatWindow = new ChatWindow((Client) list.getSelectedValue(), i2pHandler);
                Thread thread = new Thread(chatWindow);
                thread.start();
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
                } else if (list.getSelectedIndices().length == 1 && ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    ChatWindow chatWindow = new ChatWindow((Client) list.getSelectedValue(), i2pHandler);
                    Thread thread = new Thread(chatWindow);
                    thread.start();
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

    public void setList(List<Client> availableClients) {
        defaultListModel.clear();
        for (int i = 0; i < availableClients.size(); i++) {
            defaultListModel.addElement(availableClients.get(i));
        }
    }

    public boolean disconnectFromRegistar() {
        return this.disconnect;
    }
}
