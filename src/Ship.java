import javax.swing.*;
import java.awt.*;

/**
 * Created by jskarda on 3/3/16.
 */
public abstract class Ship extends JLabel {
    //protected Dimension size;
    protected ImageIcon image;

    public void setImage(ImageIcon image){
        this.image = image;
        setIcon(this.image);
    }

}
