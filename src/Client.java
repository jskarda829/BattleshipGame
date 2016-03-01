/**
 * Created by Sean on 2/9/2016.
 *
 * http://way2java.com/networking/chat-program-two-way-communication/
 */
import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {


    Socket sock;
    BufferedReader keyRead;
    OutputStream ostream;
    PrintWriter pwrite;
    InputStream istream;
    BufferedReader receiveRead;
    JTextArea textBox;
    BattleshipDisplay battleshipDisplay;



    public Client(JTextArea j, BattleshipDisplay b){
        textBox = j;
        battleshipDisplay = b;
    }


    public void runClient(String IPAddress) throws Exception {
        sock = new Socket("localhost", 3000);
        // reading from keyboard (keyRead object)
        keyRead = new BufferedReader(new InputStreamReader(System.in));
        // sending to client (pwrite object)
        ostream = sock.getOutputStream();
        pwrite = new PrintWriter(ostream, true);

        // receiving from server ( receiveRead  object)
        istream = sock.getInputStream();
        receiveRead = new BufferedReader(new InputStreamReader(istream));

        System.out.println("Start the chitchat, type and press Enter key");


        //socket Threads

        Thread t2 = new Thread(new ClientReadRunnable(receiveRead, textBox, battleshipDisplay));

        t2.start();

        Thread t = new Thread(new ClientWriteRunnable(pwrite));

        t.start();
    }
    public void send(String signal, String message, BattleshipGrid.CellPane cellPane){

        if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_CHAT)){

            //send a chat message
            //send chat signal then chat message itself
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_CHAT);
            pwrite.flush();
            pwrite.println("Player 2: " + message);

        }else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET)){

            //server ships are set and ready to play
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET);
            pwrite.flush();

        }else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_SHOT_CORDINATES_INCOMING)){

            //get the row and col and send them over
            int r = cellPane.getRow();
            int c = cellPane.getColumn();

            //send signal to client
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_SHOT_CORDINATES_INCOMING);
            pwrite.flush();
            pwrite.println(r);
            pwrite.flush();
            pwrite.println(c);

        }else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_YOUR_TURN)){

            //send signal to client
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_YOUR_TURN);
            pwrite.flush();

        }else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_TARGET_HIT)){

            int r = cellPane.getRow();
            int c = cellPane.getColumn();

            //send signal to client
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_TARGET_HIT);
            pwrite.flush();
            pwrite.println(r);
            pwrite.flush();
            pwrite.println(c);

        }else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_TARGET_MISSED)){

            //send signal to client
            int r = cellPane.getRow();
            int c = cellPane.getColumn();

            //send signal to client
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_TARGET_MISSED);
            pwrite.flush();
            pwrite.println(r);
            pwrite.flush();
            pwrite.println(c);

        } else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_GAME_OVER)){
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_GAME_OVER);
            pwrite.flush();
        } else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_CHECK_SHIPS)){
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_CHECK_SHIPS);
            pwrite.flush();
        }

        pwrite.flush();
    }

}