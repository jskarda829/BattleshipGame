import javax.swing.*;
import java.awt.*;

/**
 * Created by jskarda on 3/3/16.
 */
public class Carrier extends Ship {
    public Carrier(){
        //this.size = new Dimension(50, 250);
        setImage(new ImageIcon("src/L5.jpg"));
        setPreferredSize(new Dimension(50, 250));
        setVisible(true);
    }
}
