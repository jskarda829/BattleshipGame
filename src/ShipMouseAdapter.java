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
    int carrierHeight = 5;

    String NameCarrier = "Carrier";

    String nameOfShipClicked;


    public ShipMouseAdapter(boolean b, BattleshipGrid.CellPane[][] g){
        shipBeingDragged = b;
        gridSpaces = g;
    }

    public void mousePressed(MouseEvent e) {

        shipBeingDragged = true;
        System.out.println("ShipMouseAdapter mousePressed, Ship being dragged: " + shipBeingDragged);

        JComponent c = (JComponent) e.getSource();
        nameOfShipClicked = c.getName();

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

            if(nameOfShipClicked.equals(NameCarrier)) {
                if (canDrop(releasedRow, releasedCol, carrierHeight)) {
                    dropCarrier(releasedRow, releasedCol);
                }
            }

        }

    }

    //helper functions

    private boolean canDrop(int releasedRow, int releasedCol, int shipHight){

        //check for height out of bounds problems
        if(releasedRow <= (boardHeight - shipHight)){
            //can drop

        }else{
            System.out.println("Cannot drop");
            return false;
        }

        //check for ship overlap problems
        for(int i = releasedRow; i < (releasedRow + shipHight); i++){
            if(gridSpaces[i][releasedCol].getIsShipHere()){
                //there is a ship here and cannot place new ship here
                return false;
            }
        }

        return true;
    }

    private void dropCarrier(int releasedRow, int releasedCol){

        //set the images
        gridSpaces[releasedRow][releasedCol].setIcon(new ImageIcon("pics/carrier_1.png"));
        gridSpaces[releasedRow + 1][releasedCol].setIcon(new ImageIcon("pics/carrier_2.png"));
        gridSpaces[releasedRow + 2][releasedCol].setIcon(new ImageIcon("pics/carrier_3.png"));
        gridSpaces[releasedRow + 3][releasedCol].setIcon(new ImageIcon("pics/carrier_4.png"));
        gridSpaces[releasedRow + 4][releasedCol].setIcon(new ImageIcon("pics/carrier_5.png"));

        //set the shipIsHere logic
        gridSpaces[releasedRow][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 1][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 2][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 3][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 4][releasedCol].setIsShipHere(true);


    }




}
