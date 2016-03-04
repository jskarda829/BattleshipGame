import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Sean on 3/4/2016.
 */
public class ShipMouseAdapter extends MouseAdapter {

    static boolean shipBeingDragged;
    BattleshipGrid.CellPane[][] gridSpaces;

    public ShipMouseAdapter(boolean b, BattleshipGrid.CellPane[][] g){
        shipBeingDragged = b;
        gridSpaces = g;
    }

    public void mousePressed(MouseEvent e) {

        shipBeingDragged = true;
        System.out.println("ShipMouseAdapter mousePressed, Ship being dragged: " + shipBeingDragged);

        JComponent c = (JComponent) e.getSource();



    }

    public void mouseReleased(MouseEvent e) {
        //super.mouseReleased(e);
        System.out.println("ShipMouseAdapter mouseReleased, Ship being dragged: " + shipBeingDragged);
        shipBeingDragged = false;
        System.out.println("ShipMouseAdapter mouseReleased, Ship being dragged: " + shipBeingDragged);

        System.out.println("e.getX: "  + e.getX() + " e.getY: " + e.getY());

        if (e.getX() >= -500 && e.getX() <= 0 && e.getY() <= 500 && e.getY() >= 0) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (e.getX() > gridSpaces[i][j].getX() && e.getX() < gridSpaces[i][j].getX() + 50) {
                        if (e.getY() > gridSpaces[i][j].getY() && e.getY() < gridSpaces[i][j].getY() + 50) {
                            System.out.println("grid cell at: " + i + " " + j);
                            //TODO get proper row and col of the mouse release to mark as having a ship
                        }
                    }
                }
            }
        }


        /*if (e.getX() <= gridSpaces[9][9].getX() + 50 && e.getY() <= 500) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (e.getX() > gridSpaces[i][j].getX() && e.getX() < gridSpaces[i][j].getX() + 50) {
                        if (e.getY() > gridSpaces[i][j].getY() && e.getY() < gridSpaces[i][j].getY() + 50) {
                            System.out.println("grid cell at: " + i + " " + j);
                        }
                    }
                }
            }
        }*/
    }





}
