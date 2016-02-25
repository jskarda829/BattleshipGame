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
    BattleshipDisplay battleshipDisplay;

    public ClientReadRunnable (BufferedReader b, JTextArea messageField, BattleshipDisplay bd){

        bufferedReader = b;
        textBox = messageField;
        battleshipDisplay = bd;
    }

    @Override
    public void run() {

        System.out.println("Client Read thread running...");

        String receiveMessage;

        while (true){

            try {
                //read the signal
                if((receiveMessage = bufferedReader.readLine()) != null)
                {

                    //server has read a socket code, need check what code it is
                    if(receiveMessage.equals(SocketSignals.BATTLESHIP_SIGNAL_CHAT)){

                        System.out.println("message is inconming");

                        //now know that a chat message is coming
                        try {
                            if((receiveMessage = bufferedReader.readLine()) != null)
                            {
                                //now recieved the message, need print it to screen
                                textBox.append(receiveMessage + '\n');
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if(receiveMessage.equals(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET)) {

                        System.out.println("Opponent ships are set");
                        textBox.append("**_Opponent Ships are set_**\n");

                    } else if(receiveMessage.equals(SocketSignals.BATTLESHIP_SIGNAL_READY_TO_START)) {

                        //print message to textbox then get vars ready for game

                        textBox.append("**_Game is starting_**\n");
                        battleshipDisplay.setGameReadyToStart(true);


                    } else {

                            System.out.println("Read something but its not a message, recieveMessage: " + receiveMessage);

                    }

                   // textBox.append(receiveMessage + '\n');
                    //System.out.println("The server says: " + receiveMessage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

