/**
 * Created by Sean on 2/9/2016.
 */
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    /*
     *  1)Make 2 threads
     *      Thread 1) Reading Thread
     *          - never ending while loop
     *          - start with readline
     *          - then just print out the readline
     *
     *       Thread 2) Writing Thread
     *          - never ending while loop
     *          - start with get input from keyboard
     *          - then write into the socket
     */

    public ServerSocket sersock;
    public Socket sock;
    public BufferedReader keyRead;
    public OutputStream ostream;
    public PrintWriter pwrite;
    public InputStream istream;
    public BufferedReader receiveRead;
    JTextArea textBox;
    BattleshipDisplay battleshipDisplay;


    public Server(JTextArea j, BattleshipDisplay b){
        textBox = j;
        battleshipDisplay = b;
    }

    public void runServer() throws Exception
    {
        //vars
        sersock = new ServerSocket(3000);
        sock = sersock.accept( );
        // reading from keyboard (keyRead object)
        keyRead = new BufferedReader(new InputStreamReader(System.in));
        // sending to client (pwrite object)
        ostream = sock.getOutputStream();
        pwrite = new PrintWriter(ostream, true);

        // receiving from server ( receiveRead  object)
        istream = sock.getInputStream();
        receiveRead = new BufferedReader(new InputStreamReader(istream));

        //initialize all the vars
        //initialize(sersock, sock, keyRead, ostream, pwrite, istream, receiveRead);








        Thread t2 = new Thread(new ServerReadRunnable(receiveRead, textBox, battleshipDisplay));

        t2.start();

        Thread t = new Thread(new ServerWriteRunnable(pwrite));

        t.start();

    }//close main()

    public void send(String signal, String message, BattleshipGrid.CellPane cellPane){

        if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_CHAT)){

            //send a chat message
            //send chat signal then chat message itself
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_CHAT);
            pwrite.flush();
            pwrite.println("Player 1: " + message);

        } else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET)){

            //client ships are set and ready to play
            //write opponent ready to message box
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET);
            pwrite.flush();

        } else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_READY_TO_START)){

            //send signal to client
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_READY_TO_START);
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

            //send signal to client
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_TARGET_HIT);
            pwrite.flush();
            pwrite.println(cellPane.getRow());
            pwrite.flush();
            pwrite.println(cellPane.getColumn());
            pwrite.flush();

        }else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_TARGET_MISSED)){

            //send signal to client
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_TARGET_MISSED);
            pwrite.flush();
            pwrite.println(cellPane.getRow());
            pwrite.flush();
            pwrite.println(cellPane.getColumn());
            pwrite.flush();


        } else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_GAME_OVER)){
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_GAME_OVER);
            pwrite.flush();
        } else if(signal.equals(SocketSignals.BATTLESHIP_SIGNAL_CHECK_SHIPS)) {
            pwrite.println(SocketSignals.BATTLESHIP_SIGNAL_CHECK_SHIPS);
            pwrite.flush();
        }

        pwrite.flush();

    }//close send()
}

