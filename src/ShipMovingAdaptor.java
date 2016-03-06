import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Sean on 3/6/2016.
 */
public class ShipMovingAdaptor extends MouseAdapter{

    /*
     *  Add this to each grid cell
     *  This will pick up the ship and move it to the new location
     *
     */

    int whichShipWasClicked;
    int rowClicked, colClicked;
    int rowReleased, colReleased;
    boolean clickedShipVertical;
    int leftBound = 0;
    int rightBound = 500;
    int topBound = 0;
    int bottomBound = 500;
    int boardHeight = 10;
    int boardWidth = 10;

    Carrier carrier;
    Battleship battleship;
    Destroyer destroyer;
    Submarine submarine;
    PatrolBoat patrolBoat;

    int carrierHeight = 5;
    int battleshipHeight = 4;
    int destroyerHeight = 3;
    int subHeight = 3;
    int patrolBoatHeight = 2;

    String TAG = "ShipMovingAdaptor";

    BattleshipGrid.CellPane[][] gridSpaces;



    public ShipMovingAdaptor(    BattleshipGrid.CellPane[][] g,  Carrier cs, Battleship bs, Destroyer ds, Submarine ss, PatrolBoat pbs){

        gridSpaces = g;
        carrier = cs;
        battleship = bs;
        destroyer = ds;
        submarine = ss;
        patrolBoat = pbs;

    }

    public void mousePressed(MouseEvent e) {
        /*  - get which ship was clicked on
         *  -
        */

        if (e.getX() >= leftBound && e.getX() <= rightBound && e.getY() <= bottomBound && e.getY() >= topBound) {
            colClicked = (int)((Math.abs(e.getX())) / 50);
            rowClicked = (int)((e.getY()) / 50);
            System.out.println(TAG + " row: " + rowClicked);
            System.out.println(TAG + " col: " + colClicked);

            whichShipWasClicked = getShipThere();
            clickedShipVertical = getVertical();


        }

    }

    public void mouseReleased(MouseEvent e) {
        /*
         *  - get the row and col that the mouse was released on
         *  - check if the clicked ship can be dropped here
         */

        if (e.getX() >= leftBound && e.getX() <= rightBound && e.getY() <= bottomBound && e.getY() >= topBound) {
            colReleased = (int)((Math.abs(e.getX())) / 50);
            rowReleased = (int)((e.getY()) / 50);
            System.out.println(TAG + " row: " + rowReleased);
            System.out.println(TAG + " col: " + colReleased);


            if(whichShipWasClicked == SocketSignals.CARRIER_INT) {

                if (canDrop(rowReleased, colReleased, carrierHeight)) {
                    deleteOldShip();
                    dropCarrier(rowReleased, colReleased);

                }

            } /*else if (nameOfShipClicked.equals(NameBattleship)){

                if (canDrop(releasedRow, releasedCol, battleshipHeight)) {
                    dropBattleship(releasedRow, releasedCol);
                }

            }else if (nameOfShipClicked.equals(NameDestroyer)){

                if (canDrop(releasedRow, releasedCol, destroyerHeight)) {
                    dropDestroyer(releasedRow, releasedCol);
                }

            }else if (nameOfShipClicked.equals(NameSub)){

                if (canDrop(releasedRow, releasedCol, subHeight)) {
                    dropSub(releasedRow, releasedCol);
                }

            }else if (nameOfShipClicked.equals(NamePatrolBoat)){

                if (canDrop(releasedRow, releasedCol, patrolBoatHeight)) {
                    dropPatrolBoat(releasedRow, releasedCol);
                }

            }*/

        }



    }

    private int getShipThere(){

        if(gridSpaces[rowClicked][colClicked].getIsShipHere()){
            return gridSpaces[rowClicked][colClicked].getWhichShip();
        }else {
            return -1;
        }
    }

    private boolean canDrop(int releasedRow, int releasedCol, int shipHight){

        if(clickedShipVertical){
            //ship is vertical
            //check for height out of bounds problems
            if(releasedRow <= (boardHeight - shipHight)){
                //can drop

            }else{
                System.out.println("Cannot drop");
                return false;
            }

            //check for ship overlap problems
            for(int i = releasedRow; i < (releasedRow + shipHight); i++){
                if((gridSpaces[i][releasedCol].getIsShipHere()) && (!isSameShip(whichShipWasClicked,gridSpaces[i][releasedCol]))){
                    //there is a ship here and cannot place new ship here
                    return false;
                }
            }

            return true;

        }else{
            //ship is sideways
        }

        return false;

    }

    private boolean isSameShip(int whichShip, BattleshipGrid.CellPane cellPane){

        if(cellPane.getWhichShip() == whichShip){
            return true;
        }else{
            return false;
        }
    }

    private boolean getVertical(){
        if(whichShipWasClicked == SocketSignals.CARRIER_INT){
            return carrier.getIsVertical();
        }
        return false;
    }

    private void deleteOldShip(){

        BattleshipGrid.CellPane temp;
        int firstPieceRow, firstPieceCol;

        if(clickedShipVertical){
            //vertical ship

            temp = getFirstPiece();
            firstPieceRow = temp.getRow();
            firstPieceCol = temp.getColumn();

            if(whichShipWasClicked == SocketSignals.CARRIER_INT){

                gridSpaces[firstPieceRow][firstPieceCol].clearCell();
                gridSpaces[firstPieceRow + 1][firstPieceCol].clearCell();
                gridSpaces[firstPieceRow + 2][firstPieceCol].clearCell();
                gridSpaces[firstPieceRow + 3][firstPieceCol].clearCell();
                gridSpaces[firstPieceRow + 4][firstPieceCol].clearCell();

            }
        }else{
            //horizontal ship
        }
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

        //set the which ship logic
        gridSpaces[releasedRow][releasedCol].setWhichShip(SocketSignals.CARRIER_INT);
        gridSpaces[releasedRow + 1][releasedCol].setWhichShip(SocketSignals.CARRIER_INT);
        gridSpaces[releasedRow + 2][releasedCol].setWhichShip(SocketSignals.CARRIER_INT);
        gridSpaces[releasedRow + 3][releasedCol].setWhichShip(SocketSignals.CARRIER_INT);
        gridSpaces[releasedRow + 4][releasedCol].setWhichShip(SocketSignals.CARRIER_INT);

        //set the which ship logic
        gridSpaces[releasedRow][releasedCol].setShipPiece(SocketSignals.FIRST_PIECE);
        gridSpaces[releasedRow + 1][releasedCol].setShipPiece(SocketSignals.SECOND_PIECE);
        gridSpaces[releasedRow + 2][releasedCol].setShipPiece(SocketSignals.THIRD_PIECE);
        gridSpaces[releasedRow + 3][releasedCol].setShipPiece(SocketSignals.FOURTH_PIECE);
        gridSpaces[releasedRow + 4][releasedCol].setShipPiece(SocketSignals.FIFTH_PIECE);

        carrier.setVisible(false);

    }

    private BattleshipGrid.CellPane getFirstPiece(){
        if(clickedShipVertical) {
            if (gridSpaces[rowClicked][colClicked].getShipPiece() == SocketSignals.FIRST_PIECE) {
                return gridSpaces[rowClicked][colClicked];
            } else if (gridSpaces[rowClicked][colClicked].getShipPiece() == SocketSignals.SECOND_PIECE) {
                return gridSpaces[rowClicked - 1][colClicked];
            } else if (gridSpaces[rowClicked][colClicked].getShipPiece() == SocketSignals.THIRD_PIECE) {
                return gridSpaces[rowClicked - 2][colClicked];
            } else if (gridSpaces[rowClicked][colClicked].getShipPiece() == SocketSignals.FOURTH_PIECE) {
                return gridSpaces[rowClicked - 3][colClicked];
            } else if (gridSpaces[rowClicked][colClicked].getShipPiece() == SocketSignals.FIFTH_PIECE) {
                return gridSpaces[rowClicked - 4][colClicked];
            }
        }

        return null;
    }






}//close big class
