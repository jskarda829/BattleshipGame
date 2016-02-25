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


}
