/**
 * Created by Sean on 3/3/2016.
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

public class DragImage extends JComponent implements MouseMotionListener {
    static int imageWidth = 50, imageHeight = 50;
    int imageX, imageY;
    boolean didImageGetClicked = false;

    Image image;

    JLabel jl;



    public DragImage(Image i) {
        image = i;
        addMouseMotionListener(this);
        addMouseListener(new mouseListenerTest());
    }



    public void mouseDragged(MouseEvent e) {

        if(didImageGetClicked) {
            imageX = e.getX();
            imageY = e.getY();
            repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(image, imageX, imageY, this);
    }









    public static void main(String[] args) {
        String imageFile = "Park.PNG";   //154x162
        // Turn off double buffering
        //RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);

        Image image = Toolkit.getDefaultToolkit().getImage(DragImage.class.getResource(imageFile));
        image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT);

        JFrame frame = new JFrame("DragImage");
        frame.getContentPane().add(new DragImage(image));
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }




    class mouseListenerTest implements MouseListener{
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
