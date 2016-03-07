import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
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
    public JButton shipsAreReadyButton;

    //custom vars
    boolean isClient;
    public Server server;
    public Client client;
    private BattleshipGrid bfgTop;  //left side (opponent)
    private BattleshipGrid bfgBottom;   //right side (yours)
    private boolean canShoot = false;
    private boolean serverShips = false;
    private boolean clientShips = false;
    private boolean gameReadyToStart = false;
    private boolean isGameOver = false;
    private String serverIPAddress;
    private boolean carrierDead = false;
    private boolean battleshipDead = false;
    private boolean destroyerDead = false;
    private boolean submarineDead = false;
    private boolean patrolDead = false;



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
                    serverIPAddress = IPAddress.getText();
                    getContentPane().setBackground(Color.gray);
                    loadBoard();
                } else {
                    isClient = true;
                    serverIPAddress = "localhost";
                    getContentPane().setBackground(Color.gray);
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
        bfgBottom = new BattleshipGrid(true);
        bfgBottom.setBounds(700, 15, 600,500);
        bfgTop = new BattleshipGrid(false);
        bfgTop.setBounds(100, 15, 600, 500);
        sendButton = new JButton("Send");
        enterText = new JTextField("Enter message");
        messageBox = new JTextArea();
        //Set behavior of the messagebox/scrollpane
        messageBox.setEditable(false);
        messageBox.setAutoscrolls(true);
        JScrollPane j = new JScrollPane(messageBox);
        j.setBounds(150, 600, 1000, 100);
        j.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        j.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        j.setAutoscrolls(true);
        j.setWheelScrollingEnabled(true);
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
                            client.send(SocketSignals.BATTLESHIP_SIGNAL_CHAT, text, null);
                            messageBox.append("Player 2: " + text + '\n');
                        } else {
                            server.send(SocketSignals.BATTLESHIP_SIGNAL_CHAT, text, null);
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
                        client.send(SocketSignals.BATTLESHIP_SIGNAL_CHAT, text, null);
                        messageBox.append("Player 2: " + text + '\n');
                    } else {
                        server.send(SocketSignals.BATTLESHIP_SIGNAL_CHAT, text, null);
                        messageBox.append("Player 1: " + text + '\n');
                    }
                    //messageBox.append(text + '\n');
                    enterText.setText("");
                }
            }
        });


        assignRandomShipsButton = new JButton("Random Ships");
        assignRandomShipsButton.setBounds(790, 550, 160, 20);
        assignRandomShipsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(bfgBottom.c.isVisible() && bfgBottom.b.isVisible() && bfgBottom.s.isVisible() && bfgBottom.d.isVisible() && bfgBottom.pb.isVisible()) {
                    //assign random ships
                    setRandomShips();
                    bfgBottom.c.setVisible(false);
                    bfgBottom.b.setVisible(false);
                    bfgBottom.s.setVisible(false);
                    bfgBottom.d.setVisible(false);
                    bfgBottom.pb.setVisible(false);
                    assignRandomShipsButton.setVisible(false);
                    shipsAreReadyButton.setVisible(false);
                    bfgBottom.removeAllListeners();
                } else {
                    assignRandomShipsButton.setVisible(false);
                    shipsAreReadyButton.setVisible(false);
                    bfgBottom.removeAllListeners();
                }
            }
        });

        shipsAreReadyButton = new JButton("Ships Are Set");
        shipsAreReadyButton.setBounds(940, 550, 160, 20);
        shipsAreReadyButton.setVisible(true);
        shipsAreReadyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bfgBottom.areShipsPlaced()) {
                    shipsAreSet();
                    assignRandomShipsButton.setVisible(false);
                    shipsAreReadyButton.setVisible(false);
                    bfgBottom.removeAllListeners();
                }
            }
        });
        fireButton = new JButton();
        ImageIcon i = new ImageIcon("src/nuke_fire_100x100.jpg");
        fireButton.setIcon(i);
        fireButton.setToolTipText("Fire!");
        fireButton.setVisible(false);
        fireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


//                System.out.println("Taking a shot!");
                //get the row and col of the clicked targeted cell to send to other player
                BattleshipGrid.CellPane temp = null;

                temp = bfgTop.getTargetedCell();

                if(temp != null){
                    //send the targeted coordinates



                    if(isClient){
                        client.send(SocketSignals.BATTLESHIP_SIGNAL_SHOT_CORDINATES_INCOMING, null, temp);
                    }else{
                        server.send(SocketSignals.BATTLESHIP_SIGNAL_SHOT_CORDINATES_INCOMING, null, temp);
                    }



                }else{
//                    System.out.println("Temp is null");
                }

            }
        });
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
        add(shipsAreReadyButton);

        if(isClient){
            client = new Client(messageBox, this);
            try {
                client.runClient(serverIPAddress);
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
//        print("Assigning random ships");
        boolean shipsAssigned = false;

        shipsAssigned = bfgBottom.assignRandomShips();

        messageBox.append("**_Your Ships are set_**\n");

        if(shipsAssigned == true){
            //send ships ready signal
            if(isClient){
//                print("setRandomShips() ships assigned is true, isClient is trur");
                client.send(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET, null, null);
                clientShips = true;
            } else {
//                print("setRandomShips() ships assigned is true, isClient is false");
                serverShips = true;
                server.send(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET, null, null);
                startGameIfReady();
            }
        }
    }

    public void shipsAreSet(){
            //send ships ready signal
        if(isClient){
//               print("setRandomShips() ships assigned is true, isClient is trur");
            client.send(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET, null, null);
            clientShips = true;
        } else {
//               print("setRandomShips() ships assigned is true, isClient is false");
            serverShips = true;
            server.send(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET, null, null);
            startGameIfReady();
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
//        printInfo(TAG);

        if((serverShips == true) && (clientShips == true)){
//            System.out.println("Ready to start game");
            server.send(SocketSignals.BATTLESHIP_SIGNAL_READY_TO_START, null, null);
            messageBox.append("**_Game Ready to Start_**\n");
            gameReadyToStart = true;
            startTurn();
        }else {
//            System.out.println("Not Ready to start game");


        }
    }

    private void print(String s){
        System.out.println(s);
    }

    public void printInfo(String TAG){
//        print(TAG);
//        print("isClient: " + isClient);
//        print("serverShips: " + serverShips);
//        print("clientShips: " + clientShips);
    }

    public void startTurn(){
        /*
         *  Set fire button visible
         *  Listen for click on the opponent's grid
         *  onclick for fire button, get the row and col of the clicked (targeted) cell
         *  send that row and col to other player
         */

        isGameOver = bfgBottom.isGameOver();
        if(isClient){
            if(5 == bfgBottom.carrierHits && !carrierDead){
                client.send(SocketSignals.CARRIER_SUNK_SIGNAL, null, null);
                carrierDead = true;
            }
            if(4 == bfgBottom.battleshipHits && !battleshipDead){
                client.send(SocketSignals.BATTLESHIP_SUNK_SIGNAL, null, null);
                battleshipDead = true;
            }
            if(3 == bfgBottom.destroyerHits && !destroyerDead){
                client.send(SocketSignals.DESTROYER_SUNK_SIGNAL, null, null);
                destroyerDead = true;
            }
            if(3 == bfgBottom.submarineHits && !submarineDead){
                client.send(SocketSignals.SUBMARINE_SUNK_SIGNAL, null, null);
                submarineDead = true;
            }
            if(2 == bfgBottom.patrolBoatHits && !patrolDead){
                client.send(SocketSignals.PATROL_BOAT_SUNK_SIGNAL, null, null);
                patrolDead = true;
            }
        } else {
            if(5 == bfgBottom.carrierHits && !carrierDead){
                server.send(SocketSignals.CARRIER_SUNK_SIGNAL, null, null);
                carrierDead = true;
            }
            if(4 == bfgBottom.battleshipHits && !battleshipDead){
                server.send(SocketSignals.BATTLESHIP_SUNK_SIGNAL, null, null);
                battleshipDead = true;
            }
            if(3 == bfgBottom.destroyerHits && !destroyerDead){
                server.send(SocketSignals.DESTROYER_SUNK_SIGNAL, null, null);
                destroyerDead = true;
            }
            if(3 == bfgBottom.submarineHits && !submarineDead){
                server.send(SocketSignals.SUBMARINE_SUNK_SIGNAL, null, null);
                submarineDead = true;
            }
            if(2 == bfgBottom.patrolBoatHits && !patrolDead){
                server.send(SocketSignals.PATROL_BOAT_SUNK_SIGNAL, null, null);
                patrolDead = true;
            }
        }
        if(isGameOver){
            //you lost, opponent won

            if(isClient){
                client.send(SocketSignals.BATTLESHIP_SIGNAL_GAME_OVER, null, null);
            }else{
                server.send(SocketSignals.BATTLESHIP_SIGNAL_GAME_OVER, null, null);
            }

            youLost();

        }else {
            fireButton.setVisible(true);
        }



    }

    public void endTurn(){
        //set the fire button as invisible
        //send the end of turn signal
        fireButton.setVisible(false);

        if(isClient){
            client.send(SocketSignals.BATTLESHIP_SIGNAL_YOUR_TURN, null, null);
        }else{
            server.send(SocketSignals.BATTLESHIP_SIGNAL_YOUR_TURN, null, null);
        }


    /*
        if(isClient){
            client.send(SocketSignals.BATTLESHIP_SIGNAL_CHECK_SHIPS, null, null);
            print("sending check ships signal");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(isGameOver){
                print("Game over signal being sent");
                client.send(SocketSignals.BATTLESHIP_SIGNAL_GAME_OVER, null, null);
            } else {
                print("your turn signal being sent");
                client.send(SocketSignals.BATTLESHIP_SIGNAL_YOUR_TURN, null, null);
            }
        } else {
            server.send(SocketSignals.BATTLESHIP_SIGNAL_CHECK_SHIPS, null, null);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(isGameOver){
                print("Game over signal being sent");
                server.send(SocketSignals.BATTLESHIP_SIGNAL_GAME_OVER, null, null);
            } else {
                print("your turn signal being sent");
                server.send(SocketSignals.BATTLESHIP_SIGNAL_YOUR_TURN, null, null);
            }

        }

     */

    }

    public void markOpponentsShot(int row, int col){
        boolean hit;

        hit = bfgBottom.markShot(row, col);
        BattleshipGrid.CellPane t = new BattleshipGrid(false).new CellPane(row, col);

        if(hit == true){
            //a ship was hit so send the hit signal
            if(isClient){
                //send miss signal and row and col of the miss
                client.send(SocketSignals.BATTLESHIP_SIGNAL_TARGET_HIT, null,  t);
            }else{
                //send miss signal and row and col of the miss
                server.send(SocketSignals.BATTLESHIP_SIGNAL_TARGET_HIT, null,  t);
            }

        }else{
            //send the miss signal
            if(isClient){
                //send miss signal and row and col of the miss
                client.send(SocketSignals.BATTLESHIP_SIGNAL_TARGET_MISSED, null, t);
            }else{
                //send miss signal and row and col of the miss
                server.send(SocketSignals.BATTLESHIP_SIGNAL_TARGET_MISSED, null,  t);
            }
        }
    }

    public void markYourShot(int row, int col, boolean hit){
        bfgTop.markShot(row, col, hit);

        endTurn();
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

    public void setBoardAfterGameOver(){
        messageBox.append("GAME OVER NIGRO");
    }

    public void setGameOver(boolean b){
        messageBox.append("SETTING GAME OVER" + '\n');
        isGameOver = b;
    }

    public boolean checkGameOver(){
        messageBox.append("CHECKING GAME OVER" + '\n');
        boolean b = bfgBottom.isGameOver();
        if(b){
            setBoardAfterGameOver();
        } else {
            return b;
        }
        return b;
    }

    public void youWon(){
        messageBox.append("GAME OVER, YOU WIN" + '\n');
    }

    public void youLost(){
        messageBox.append("GAME OVER, YOU LOST" + '\n');
    }


}
