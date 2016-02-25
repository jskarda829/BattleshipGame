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
    public JButton fireButton;

    //custom vars
    boolean isClient;
    public Server server;
    public Client client;
    private BattleshipGrid bfgTop;
    private BattleshipGrid bfgBottom;
    private boolean canShoot = false;
    private boolean serverShips = false;
    private boolean clientShips = false;
    private boolean gameReadyToStart = false;


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
            }
        });

        fireButton = new JButton();
        ImageIcon i = new ImageIcon("src/nuke_fire_100x100.jpg");
        fireButton.setIcon(i);
        //fireButton.setText("Fire");
        fireButton.setToolTipText("Fire!");
        fireButton.setBounds(634, 500, 100, 100);
        //fireButton.setVisible(false);


        //add stuff to board
        add(bfgBottom);
        add(bfgTop);
        add(sendButton);
        add(enterText);
        add(j);
        add(assignRandomShipsButton);
        add(fireButton);

        if(isClient){
            client = new Client(messageBox, this);
            try {
                client.runClient("fakeIP");//TODO set real ip from user
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
            server = new Server(messageBox, this);
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
                print("setRandomShips() ships assigned is true, isClient is trur");
                client.send(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET, null);
                clientShips = true;
            } else {
                print("setRandomShips() ships assigned is true, isClient is false");
                serverShips = true;
                server.send(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET, null);
                startGameIfReady();
            }
        }
    }





    public void startGameIfReady(){

        /*
         *  Called when the server player set their ships and when the opponent sets their ships
         *      it will check if both ships are set
         *          if not, do nothing
         *          if so, start the game
         *              to start game:
         *                  -pick who goes first, server can start for now..
         *                  -send sstart signal to client
         *                  -write to message box that game is ready to start
         */

        String TAG = "startGameIfReady()";
        printInfo(TAG);

        if((serverShips == true) && (clientShips == true)){
            System.out.println("Ready to start game");
            server.send(SocketSignals.BATTLESHIP_SIGNAL_READY_TO_START, null);
            messageBox.append("**_Game Ready to Start_**\n");
            gameReadyToStart = true;
        }else {
            System.out.println("Not Ready to start game");


        }
    }

    private void print(String s){
        System.out.println(s);
    }

    public void printInfo(String TAG){
        print(TAG);
        print("isClient: " + isClient);
        print("serverShips: " + serverShips);
        print("clientShips: " + clientShips);
    }

    public void startTurn(){
        /*
         *  Set fire button visible
         *  Listen for click on the opponent's grid
         *  onclick for fire button, get the row and col of the clicked (targeted) cell
         *  send that row and col to other player
         */
    }

    public void endTurn(){

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



    //getters and setters
    public boolean getClientShips(){
        return clientShips;
    }

    public void setClientShips(boolean b){
        clientShips = b;
    }

    public boolean getGameReadyToStart(){
        return gameReadyToStart;
    }

    public void setGameReadyToStart(boolean b){
        gameReadyToStart = b;
    }



}
