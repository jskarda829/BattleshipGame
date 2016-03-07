import javax.swing.*;
import java.awt.*;

/**
 * Created by jskarda on 3/3/16.
 */
public abstract class Ship extends JLabel {
    private boolean isVerical = true;
    public void setImage(ImageIcon image){
        setIcon(image);
    }

    public boolean getIsVertical(){
        return isVerical;
    }

    private void setVertical(boolean b){
        isVerical = b;
    }
}
