import javax.swing.*;
import java.awt.*;

/**
 * Created by jskarda on 3/3/16.
 */
public class Battleship extends Ship {
    public Battleship(){
        setImage(new ImageIcon("src/L4.jpg"));
        setPreferredSize(new Dimension(50, 200));
        setVisible(true);
    }
}
