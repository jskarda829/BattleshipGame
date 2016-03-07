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
        String receiveMessage;
        while (true){

            try {

                if((receiveMessage = bufferedReader.readLine()) != null)
                {

                    //client has read a socket code, need check what code it is
                    if(receiveMessage.equals(SocketSignals.BATTLESHIP_SIGNAL_CHAT)){
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

                        battleshipDisplay.markOpponentsShot(rowShotAt, colShotAt);


                    }  else if(receiveMessage.equals(SocketSignals.BATTLESHIP_SIGNAL_YOUR_TURN)) {

                        //its now your turn, call startTurn()

                        textBox.append("**_Your Turn_**\n");
                        battleshipDisplay.startTurn();


                    }  else if(receiveMessage.equals(SocketSignals.BATTLESHIP_SIGNAL_TARGET_HIT)) {
                        //read the row and col of where you shot and mark them

                        int rowShotAt, colShotAt;

                        receiveMessage = bufferedReader.readLine();
                        rowShotAt = Integer.parseInt(receiveMessage);

                        receiveMessage = bufferedReader.readLine();
                        colShotAt = Integer.parseInt(receiveMessage);

                        //you hit a ship
                        battleshipDisplay.markYourShot(rowShotAt, colShotAt, true);

                    }  else if(receiveMessage.equals(SocketSignals.BATTLESHIP_SIGNAL_TARGET_MISSED)) {
                        //read the row and col of where you shot and mark them

                        int rowShotAt, colShotAt;

                        receiveMessage = bufferedReader.readLine();
                        rowShotAt = Integer.parseInt(receiveMessage);

                        receiveMessage = bufferedReader.readLine();
                        colShotAt = Integer.parseInt(receiveMessage);

                        //you hit a ship
                        battleshipDisplay.markYourShot(rowShotAt, colShotAt, false);

                    }  else if(receiveMessage.equals(SocketSignals.BATTLESHIP_SIGNAL_CHECK_SHIPS)){
                        if(battleshipDisplay.checkGameOver()){
                            battleshipDisplay.setGameOver(true);
                            battleshipDisplay.server.send(SocketSignals.BATTLESHIP_SIGNAL_GAME_OVER, null, null);
                        }
                    } else if(receiveMessage.equals((SocketSignals.BATTLESHIP_SIGNAL_GAME_OVER))){
                        //the opponent lost, so you won
                        battleshipDisplay.youWon();
                    } else if(receiveMessage.equals(SocketSignals.CARRIER_SUNK_SIGNAL)){
                        textBox.append("You've sunk the opponents Carrier" + '\n');
                    } else if(receiveMessage.equals(SocketSignals.BATTLESHIP_SUNK_SIGNAL)){
                        textBox.append("You've sunk the opponents Battleship!" + '\n');
                    } else if(receiveMessage.equals(SocketSignals.DESTROYER_SUNK_SIGNAL)) {
                        textBox.append("You've sunk the opponents Destroyer!" + '\n');
                    } else if(receiveMessage.equals(SocketSignals.SUBMARINE_SUNK_SIGNAL)) {
                        textBox.append("You've sunk the opponents Submarine!" + '\n');
                    } else if(receiveMessage.equals(SocketSignals.PATROL_BOAT_SUNK_SIGNAL)) {
                        textBox.append("You've sunk the opponents Patrol Boat!" + '\n');
                    } else {

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//close run()
}