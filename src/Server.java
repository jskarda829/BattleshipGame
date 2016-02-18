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

    public Server(JTextArea j){
        textBox = j;
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








        Thread t2 = new Thread(new ServerReadRunnable(receiveRead, textBox));

        t2.start();

        Thread t = new Thread(new ServerWriteRunnable(pwrite));

        t.start();

    }//close main()

    public void send(String message){
        pwrite.println("Player 1: " + message);
        pwrite.flush();
    }
}

