import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.File;

/**
 * Created by Sean on 2/21/2016.
 */
public class SocketSignals {

    //this class will be filled with final static signals that will be sent over the socket to tell the reciever what is coming next

    public static final String BATTLESHIP_SIGNAL_CHAT = "SIG-10000";
    public static final String BATTLESHIP_SIGNAL_SHIPS_ARE_SET = "SIG-10001";
    public static final String BATTLESHIP_SIGNAL_READY_TO_START = "SIG-10002";
    public static final String BATTLESHIP_SIGNAL_SHOT_CORDINATES_INCOMING = "SIG-10003";
    public static final String BATTLESHIP_SIGNAL_YOUR_TURN= "SIG-10004";
    public static final String BATTLESHIP_SIGNAL_TARGET_HIT= "SIG-10005";
    public static final String BATTLESHIP_SIGNAL_TARGET_MISSED= "SIG-10006";
    public static final String BATTLESHIP_SIGNAL_CHECK_SHIPS = "SIG-10007";
    public static final String BATTLESHIP_SIGNAL_GAME_OVER = "SIG-10008";

    public static final Color BATTLESHIP_COLOR_SHIP_HIT = Color.red;
    public static final Color BATTLESHIP_COLOR_SHIP_MISS = Color.gray;

    public static final int CARRIER_INT = 1;
    public static final int BATTLESHIP_INT = 2;
    public static final int DESTROYER_INT = 3;
    public static final int SUBMARINE_INT = 4;
    public static final int PATROL_BOAT_INT = 5;

    public static final int FIRST_PIECE = 1;
    public static final int SECOND_PIECE = 2;
    public static final int THIRD_PIECE = 3;
    public static final int FOURTH_PIECE = 4;
    public static final int FIFTH_PIECE = 5;

    public static void playSound(String soundName){


        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch(Exception ex)
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace( );
        }
    }

}
