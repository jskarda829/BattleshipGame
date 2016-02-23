import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

/**
 * Created by jskarda on 2/16/16.
 */
public class BattleshipDisplay extends JFrame {

    public JButton sendButton;
    public JTextField enterText;
    public JTextArea messageBox;
    public JButton hostButton;
    public JButton clientButton;
    public JTextField IPAddress;
    boolean isClient;
    public Server server;
    public Client client;
    private BattleshipGrid bfgTop;
    private BattleshipGrid bfgBottom;


    public BattleshipDisplay() {
        initUI();
    }

    public void initUI(){

        setLayout(null);
        IPAddress = new JTextField();
        IPAddress.setBounds(250, 100, 250, 50);
        hostButton = new JButton("Host Game");
        hostButton.setBounds(250, 170, 125, 50);
        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isClient = false;
                loadBoard();
            }
        });
        clientButton = new JButton("Join Game");
        clientButton.setBounds(375, 170, 125, 50);
        clientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!IPAddress.getText().equals("")) {
                    isClient = true;
                    loadBoard();
                }
            }
        });



        add(IPAddress);
        add(hostButton);
        add(clientButton);
        setTitle("Battleship");
        setSize(800, 800);
        setMaximumSize(new Dimension(800, 800));
        setMinimumSize(new Dimension(800, 800));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void loadBoard(){
        remove(IPAddress);
        remove(hostButton);
        remove(clientButton);

        //Instantiate components
       // bfgBottom = new BattleshipGrid();
        //bfgBottom.setLocation(0, 0);
        bfgBottom = new BattleshipGrid();
        bfgBottom.setBounds(400, 170, 350,350);
        add(bfgBottom);
        System.out.println(bfgBottom.isVisible());
        System.out.println(bfgBottom.isOpaque());
        bfgTop = new BattleshipGrid();
        bfgTop.setLocation(225, 15);
        sendButton = new JButton("Send");
        enterText = new JTextField("Enter message");
        messageBox = new JTextArea();
        //Set behavior of the messagebox/scrollpane
        messageBox.setEditable(false);
        JScrollPane j = new JScrollPane(messageBox);
        j.setBounds(50, 600, 640, 100);
        j.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        j.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //set behavior of the text box for typing messages
        enterText.setBounds(50, 720, 640, 20);
        enterText.setFocusable(true);
        enterText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    String text = enterText.getText();
                    if (!text.equals("")) {
                        if (isClient) {
                            client.send(SocketSignals.BATTLESHIP_SIGNAL_CHAT, text);
                            messageBox.append("Player 2: " + text + '\n');
                        } else {
                            server.send(SocketSignals.BATTLESHIP_SIGNAL_CHAT, text);
                            messageBox.append("Player 1: " + text + '\n');
                        }
                        //messageBox.append(text + '\n');
                        enterText.setText("");
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        sendButton.setBounds(700, 720, 80, 20);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String text = enterText.getText();
                if (!text.equals("")) {
                    if (isClient) {
                        client.send(SocketSignals.BATTLESHIP_SIGNAL_CHAT, text);
                        messageBox.append("Player 2: " + text + '\n');
                    } else {
                        server.send(SocketSignals.BATTLESHIP_SIGNAL_CHAT, text);
                        messageBox.append("Player 1: " + text + '\n');
                    }
                    //messageBox.append(text + '\n');
                    enterText.setText("");
                }
            }
        });
        add(bfgBottom);
        add(bfgTop);
        add(sendButton);
        add(enterText);
        add(j);
        if(isClient){
            client = new Client(messageBox);
            try {
                client.runClient("fakeIP");//TODO set real ip from user
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
            server = new Server(messageBox);
            try {
                server.runServer();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        repaint();
        pack();
    }

    public static void main(String[] args) {

//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
                BattleshipDisplay ex = new BattleshipDisplay();
                ex.setVisible(true);
//            }
//        });
    }


}
