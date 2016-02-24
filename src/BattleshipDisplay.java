import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

/**
 * Created by jskarda on 2/16/16.
 */
public class BattleshipDisplay extends JFrame {

    //UI vars
    public JButton sendButton;
    public JTextField enterText;
    public JTextArea messageBox;
    public JButton hostButton;
    public JButton clientButton;
    public JTextField IPAddress;
    public JButton assignRandomShipsButton;

    //custom vars
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
        IPAddress.setBounds(575, 100, 250, 50);

        hostButton = new JButton("Host Game");
        hostButton.setBounds(575, 170, 125, 50);
        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isClient = false;
                loadBoard();
            }
        });

        clientButton = new JButton("Join Game");
        clientButton.setBounds(700, 170, 125, 50);
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
        setSize(1400, 800);
        setMaximumSize(new Dimension(1400, 800));
        setMinimumSize(new Dimension(1400, 800));
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
        bfgBottom.setBounds(700, 15, 600,500);
        bfgTop = new BattleshipGrid();
        bfgTop.setBounds(100, 15, 600, 500);
        sendButton = new JButton("Send");
        enterText = new JTextField("Enter message");
        messageBox = new JTextArea();
        //Set behavior of the messagebox/scrollpane
        messageBox.setEditable(false);
        JScrollPane j = new JScrollPane(messageBox);
        j.setBounds(150, 600, 1000, 100);
        j.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        j.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //set behavior of the text box for typing messages
        enterText.setBounds(150, 720, 930, 20);
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

        sendButton.setBounds(1080, 720, 80, 20);
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

        assignRandomShipsButton = new JButton("Random Ships");
        assignRandomShipsButton.setBounds(880, 550, 160, 20);
        assignRandomShipsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //assign random ships
                setRandomShips();
                assignRandomShipsButton.setVisible(false);
                //remove(assignRandomShipsButton);
            }
        });


        //add stuff to board
        add(bfgBottom);
        add(bfgTop);
        add(sendButton);
        add(enterText);
        add(j);
        add(assignRandomShipsButton);

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
    }//close loadBoard()

    private void setRandomShips(){
        print("Assigning random ships");
        boolean shipsAssigned = false;

        shipsAssigned = bfgBottom.assignRandomShips();

        messageBox.append("**_Your Ships are set_**\n");

        if(shipsAssigned == true){
            //send ships ready signal
            if(isClient){
                client.send(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET, null);
            }else{
                server.send(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET, null);
            }
        }
    }

    private void print(String s){
        System.out.println(s);
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
