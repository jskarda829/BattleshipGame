import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Sean on 2/18/2016.
 */

public class ServerWriteRunnable implements Runnable {

    //vars
    PrintWriter printWriter;

    public ServerWriteRunnable(PrintWriter p){

        printWriter = p;

    }

    @Override
    public void run() {
        String message;
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));

        while (true){

            //get input from keyboard
            try {

                message = keyRead.readLine();
                printWriter.println("Server says: " + message);
                printWriter.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}