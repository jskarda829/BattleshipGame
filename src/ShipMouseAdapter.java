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
    int releasedRow;
    int releasedCol;
    int boardHeight = 10;
    int boardWidth = 10;

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
            releasedCol = 9 - (int)(Math.abs(e.getX()) / 50);
            releasedRow = (int)(e.getY() / 50);
            System.out.println("row: " + releasedRow);
            System.out.println("col: " + releasedCol);

            if(canDrop(releasedRow, releasedCol, 5)) {
                //set the images
                gridSpaces[releasedRow][releasedCol].setIcon(new ImageIcon("pics/carrier_1.png"));
                gridSpaces[releasedRow + 1][releasedCol].setIcon(new ImageIcon("pics/carrier_2.png"));
                gridSpaces[releasedRow + 2][releasedCol].setIcon(new ImageIcon("pics/carrier_3.png"));
                gridSpaces[releasedRow + 3][releasedCol].setIcon(new ImageIcon("pics/carrier_4.png"));
                gridSpaces[releasedRow + 4][releasedCol].setIcon(new ImageIcon("pics/carrier_5.png"));
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

    //helper functions

    private boolean canDrop(int releasedRow, int releasedCol, int shipHight){

        if(releasedRow <= (boardHeight - shipHight)){
            //can drop

        }else{
            System.out.println("Cannot drop");
            return false;
        }

        return true;
    }




}
