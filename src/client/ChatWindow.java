package client;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatWindow {
    private final JFrame frame;
    private final JPanel panel;
    private final JTextArea textArea;
    private final JScrollPane scrollPane;
    private final JTextField textField;
    private JLabel label;
    private final JButton btnSend;
    private final JButton btnCloseChat;
    
    public ChatWindow(){
        frame = new JFrame();
        panel = new JPanel();
        textArea = new JTextArea(10,20);
        scrollPane = new JScrollPane(textArea);
        textField = new JTextField(30);
        label = new JLabel("Chating with: "+"Pantelis Zoupis Kai Elena Lisenko");
        btnSend = new JButton("Send");
        btnCloseChat = new JButton("Close chat");
        
        textArea.setText("Welcome");
        textArea.append("Pantelis Zoupis Kai Elena Lisenko geia ti kaneis????? Καλά είμαι;;;");
        textArea.setLineWrap(true);
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(scrollPane);
        panel.add(textField);
        panel.add(btnSend);
        panel.add(btnCloseChat);
        
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Chat Window");
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args){
        ChatWindow chatWindow = new ChatWindow();
    }
}
