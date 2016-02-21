import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Sean on 2/18/2016.
 */
public class ClientReadRunnable implements Runnable {

    //vars test
    BufferedReader bufferedReader;
    JTextArea textBox;

    public ClientReadRunnable (BufferedReader b, JTextArea messageField){

        bufferedReader = b;
        textBox = messageField;
    }

    @Override
    public void run() {

        System.out.println("Client Read thread running...");

        String receiveMessage;

        while (true){

            try {

                if((receiveMessage = bufferedReader.readLine()) != null)
                {
                    textBox.append(receiveMessage + '\n');
                    //System.out.println("The server says: " + receiveMessage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

