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
    BattleshipDisplay battleshipDisplay;

    public ServerReadRunnable (BufferedReader b, JTextArea j, BattleshipDisplay bd){

        bufferedReader = b;
        textBox = j;
        battleshipDisplay = bd;

    }

    @Override
    public void run() {

        System.out.println("Server Read thread running...");

        String receiveMessage;

        while (true){

            try {

                if((receiveMessage = bufferedReader.readLine()) != null)
                {

                    //client has read a socket code, need check what code it is
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

                    }else if(receiveMessage.equals(SocketSignals.BATTLESHIP_SIGNAL_SHIPS_ARE_SET)) {

                        String TAG = "ServerReadRunnable: message recieved: BATTLESHIP_SIGNAL_SHIPS_ARE_SET";
                        System.out.println("Opponent ships are set");
                        textBox.append("**_Opponent Ships are set_**\n");
                        battleshipDisplay.printInfo(TAG);
                        battleshipDisplay.setClientShips(true);
                        battleshipDisplay.startGameIfReady();

                    } else if(receiveMessage.equals(SocketSignals.BATTLESHIP_SIGNAL_SHOT_CORDINATES_INCOMING)) {

                        //read 2 messages, row and col for a shot and then send those to the battseship display

                        textBox.append("**_Enemy shot incoming_**\n");
                        int rowShotAt, colShotAt;

                        receiveMessage = bufferedReader.readLine();
                        rowShotAt = Integer.parseInt(receiveMessage);

                        receiveMessage = bufferedReader.readLine();
                        colShotAt = Integer.parseInt(receiveMessage);

                        battleshipDisplay.markOpponentShot(rowShotAt, colShotAt);


                    }  else if(receiveMessage.equals(SocketSignals.BATTLESHIP_SIGNAL_YOUR_TURN)) {

                        //its now your turn, call startTurn()

                        textBox.append("**_Your Turn_**\n");
                        battleshipDisplay.startTurn();


                    } else{

                        System.out.println("Read something but its not a message, recieveMessage: " + receiveMessage);
                    }

                    // textBox.append(receiveMessage + '\n');
                    //System.out.println("The server says: " + receiveMessage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//close run()
}