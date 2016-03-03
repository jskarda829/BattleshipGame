import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Sean on 3/3/2016.
 */

public class JLabelDragable extends JLabel implements MouseMotionListener {

    int imageWidth = 50, imageHeight = 50;
    int imageX, imageY;
    boolean didImageGetClicked = false;

    Image image;

    public JLabelDragable(Image i){
        image = i;

        setIcon(new ImageIcon("8BGfi.png"));

        setPreferredSize(new Dimension(50, 50));

        addMouseMotionListener(this);
        addMouseListener(new mouseListenerTest());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(didImageGetClicked) {
            imageX = e.getX();
            imageY = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(image, imageX, imageY, this);
    }


    class mouseListenerTest implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {


        }

        @Override
        public void mousePressed(MouseEvent e) {



            int mouseX, mouseY;
            mouseX = e.getX();
            mouseY = e.getY();

            System.out.println("e.getX(): " + e.getX() + " e.getY(): " + e.getY() + " imageX: " + imageX + " imageY:" + imageY);

            if(((mouseX >= imageX) && mouseX <= imageX + imageWidth) && ((mouseY >= imageY)&&(mouseY <= imageY + imageHeight))){
                System.out.println("You clicked the picture");
                didImageGetClicked = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            didImageGetClicked = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}
