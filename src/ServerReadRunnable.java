import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Sean on 2/18/2016.
 */
public class ServerReadRunnable implements Runnable {

    //vars
    BufferedReader bufferedReader;
    JTextArea textBox;

    public ServerReadRunnable (BufferedReader b, JTextArea j){

        bufferedReader = b;
        textBox = j;

    }

    @Override
    public void run() {

        System.out.println("Server Read thread running...");

        String receiveMessage;

        while (true){

            try {

                if((receiveMessage = bufferedReader.readLine()) != null)
                {
                    //System.out.println("The client says: " + receiveMessage);
                    textBox.append(receiveMessage + '\n');
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//close run()
}