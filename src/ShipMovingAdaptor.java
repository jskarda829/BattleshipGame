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

    final int carrierHeight = 5;
    final int battleshipHeight = 4;
    final int destroyerHeight = 3;
    final int subHeight = 3;
    final int patrolBoatHeight = 2;

    int xOffset = 0;
    boolean leftShipsGone;
    boolean rightShipsGone;

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

    public void mouseClicked(MouseEvent e){

        setBounds();
        setShipsGone();

        //check if there are offsets that need to be applied
        if(leftShipsGone && rightShipsGone){
            xOffset = 50;
        }else if(leftShipsGone && !rightShipsGone){
            xOffset = 25;
        }else if(rightShipsGone && !leftShipsGone){
            xOffset = 25;
        }

//        print("e.getX(): " + e.getX());
//        print("e.getY(): " + e.getY());
//        System.out.println("e.getX() + xOffset: " + (e.getX() + xOffset));
//        System.out.println("gridSpaces[9][9].getX() + xOffset: " + (gridSpaces[9][9].getX() + xOffset));
//        System.out.println(xOffset);

        if (e.getX() <= gridSpaces[9][9].getX() + 50 + xOffset && e.getY() <= 500) {
            System.out.println("This is true: (e.getX() <= gridSpaces[9][9].getX() + 50 && e.getY() <= 500)");
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (e.getX() > gridSpaces[i][j].getX() && e.getX() < gridSpaces[i][j].getX() + 50 + xOffset) {
                        if (e.getY() > gridSpaces[i][j].getY() && e.getY() < gridSpaces[i][j].getY() + 50) {
                            if (gridSpaces[i][j].getIsShipHere()) {
                                System.out.println("Calling rotate ship");
                                rotateShip(e);
                            }
                        }
                    }
                }
            }
        }
    }

    public void rotateShip(MouseEvent e){
        if (e.getX() >= leftBound && e.getX() <= rightBound && e.getY() <= bottomBound && e.getY() >= topBound) {
            colClicked = (int)((e.getX() - xOffset) / 50);
            rowClicked = (int)((e.getY()) / 50);
            //System.out.println(TAG + " row: " + rowClicked);
            //System.out.println(TAG + " col: " + colClicked);

            whichShipWasClicked = getShipThere();
//            print("whichShipWasClicked: " + whichShipWasClicked);
            clickedShipVertical = gridSpaces[rowClicked][colClicked].getIsVertical();
//            print("clickedShipVertical: " + clickedShipVertical);
        }

        if(whichShipWasClicked == SocketSignals.CARRIER_INT){
            if(canRotate(colClicked, rowClicked, carrierHeight, clickedShipVertical)){
                deleteOldShip(carrierHeight);
                rotateCarrier(colClicked, rowClicked);
            }
        } else if(whichShipWasClicked == SocketSignals.BATTLESHIP_INT){
            if(canRotate(colClicked, rowClicked, carrierHeight, clickedShipVertical)){
                deleteOldShip(battleshipHeight);

            }
        } else if(whichShipWasClicked == SocketSignals.DESTROYER_INT){
            if(canRotate(colClicked, rowClicked, carrierHeight, clickedShipVertical)){
                deleteOldShip(destroyerHeight);

            }
        } else if(whichShipWasClicked == SocketSignals.SUBMARINE_INT){
            if(canRotate(colClicked, rowClicked, carrierHeight, clickedShipVertical)){
                deleteOldShip(subHeight);

            }
        } else if(whichShipWasClicked == SocketSignals.PATROL_BOAT_INT){
            if(canRotate(colClicked, rowClicked, carrierHeight, clickedShipVertical)){
                deleteOldShip(patrolBoatHeight);

            }
        }

    }
    public void mousePressed(MouseEvent e) {
        /*  - get which ship was clicked on
         *  -
        */

//        print("e.getX(): " + e.getX());
//        print("e.getY(): " + e.getY());

        setBounds();
        setShipsGone();

        //check if there are offsets that need to be applied
        if(leftShipsGone && rightShipsGone){
            xOffset = 50;
        }else if(leftShipsGone && !rightShipsGone){
            xOffset = 25;
        }else if(rightShipsGone && !leftShipsGone){
            xOffset = 25;
        }



        if (e.getX() >= leftBound && e.getX() <= rightBound && e.getY() <= bottomBound && e.getY() >= topBound) {
            colClicked = (int)((e.getX() - xOffset) / 50);
            rowClicked = (int)((e.getY()) / 50);
            //System.out.println(TAG + " row: " + rowClicked);
            //System.out.println(TAG + " col: " + colClicked);

            whichShipWasClicked = getShipThere();
//            print("whichShipWasClicked: " + whichShipWasClicked);
            clickedShipVertical = getVertical();
//            print("clickedShipVertical: " + clickedShipVertical);

        }

    }

    public void mouseReleased(MouseEvent e) {
        /*
         *  - get the row and col that the mouse was released on
         *  - check if the clicked ship can be dropped here
         */

        if (e.getX() >= leftBound && e.getX() <= rightBound && e.getY() <= bottomBound && e.getY() >= topBound) {
            colReleased = (int)((e.getX() - xOffset) / 50);
            rowReleased = (int)((e.getY()) / 50);
            //System.out.println(TAG + " row: " + rowReleased);
            //System.out.println(TAG + " col: " + colReleased);
//            print("e.getX(): " + e.getX());
//            print("e.getY(): " + e.getY());

            if(whichShipWasClicked == SocketSignals.CARRIER_INT) {

                if (canDrop(rowReleased, colReleased, carrierHeight)) {
                    deleteOldShip(carrierHeight);
                    dropCarrier(rowReleased, colReleased);

                }

            } else if (whichShipWasClicked == SocketSignals.BATTLESHIP_INT){

//                print("Clicked battleship");

                if (canDrop(rowReleased, colReleased, battleshipHeight)) {
//                    print("Can drop the battleship");
                    deleteOldShip(battleshipHeight);
                    dropBattleship(rowReleased, colReleased);
                }

            }else if(whichShipWasClicked == SocketSignals.DESTROYER_INT){

                if (canDrop(rowReleased, colReleased, destroyerHeight)) {
                    deleteOldShip(destroyerHeight);
                    dropDestroyer(rowReleased, colReleased);
                }

            }else if (whichShipWasClicked == SocketSignals.SUBMARINE_INT){

                if (canDrop(rowReleased, colReleased, subHeight)) {
                    deleteOldShip(subHeight);
                    dropSub(rowReleased, colReleased);
                }

            }else if (whichShipWasClicked == SocketSignals.PATROL_BOAT_INT){

                if (canDrop(rowReleased, colReleased, patrolBoatHeight)) {
                    deleteOldShip(patrolBoatHeight);
                    dropPatrolBoat(rowReleased, colReleased);
                }

            }

        }



    }



    private boolean canRotate(int clickedRow, int clickedCol, int shipHeight, boolean isVertical){
        int piece = gridSpaces[clickedRow][clickedCol].getShipPiece();
        if(isVertical) {
            if (piece == SocketSignals.FIRST_PIECE) {

            } else if (piece == SocketSignals.SECOND_PIECE) {
                clickedRow--;
            } else if (piece == SocketSignals.THIRD_PIECE) {
                clickedRow -= 2;
            } else if (piece == SocketSignals.FOURTH_PIECE) {
                clickedRow -= 3;
            } else if (piece == SocketSignals.FIFTH_PIECE) {
                clickedRow -= 4;
            }


            for(int i = 1; i < shipHeight; i++){
                if(gridSpaces[clickedRow][clickedCol + i].getIsShipHere()){
                    return false;
                }
            }
        } else {
            if (piece == SocketSignals.FIRST_PIECE) {

            } else if (piece == SocketSignals.SECOND_PIECE) {
                clickedCol--;
            } else if (piece == SocketSignals.THIRD_PIECE) {
                clickedCol -= 2;
            } else if (piece == SocketSignals.FOURTH_PIECE) {
                clickedCol -= 3;
            } else if (piece == SocketSignals.FIFTH_PIECE) {
                clickedCol -= 4;
            }
            for(int i = 1; i < shipHeight; i++){
                if(gridSpaces[clickedRow + i][clickedCol].getIsShipHere()){
                    return false;
                }
            }
        }
        return true;
    }
    private boolean canDrop(int releasedRow, int releasedCol, int shipHight){

        if(clickedShipVertical){
//            print("ship is vertical");
            //ship is vertical
            //check for height out of bounds problems
            if(releasedRow <= (boardHeight - shipHight)){
                //can drop

            }else{
//                System.out.println("Cannot drop");
                return false;
            }

            //check for ship overlap problems
            for(int i = releasedRow; i < (releasedRow + shipHight); i++){
                if((gridSpaces[i][releasedCol].getIsShipHere()) && (!isSameShip(whichShipWasClicked,gridSpaces[i][releasedCol]))){
                    //there is a ship here and cannot place new ship here
//                    print("another ship there, cannot move ship");
                    return false;
                }else{
//                    print("not another ship there, its ok to drop there");
                }
            }

            return true;

        }else{
            //ship is sideways
            //TODO
        }

//        print("At the end of can drop, it is false");

        return false;

    }

    private int getShipThere(){

        if(gridSpaces[rowClicked][colClicked].getIsShipHere()){
            return gridSpaces[rowClicked][colClicked].getWhichShip();
        }else {
            return -1;
        }
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
        }else if(whichShipWasClicked == SocketSignals.BATTLESHIP_INT){
            return battleship.getIsVertical();
        }else if(whichShipWasClicked == SocketSignals.DESTROYER_INT){
            return destroyer.getIsVertical();
        }else if(whichShipWasClicked == SocketSignals.SUBMARINE_INT){
            return submarine.getIsVertical();
        }else if(whichShipWasClicked == SocketSignals.PATROL_BOAT_INT){
            return patrolBoat.getIsVertical();
        }
        return false;
    }

    private void deleteOldShip(int shipLength){

        BattleshipGrid.CellPane temp;
        int firstPieceRow, firstPieceCol;

        if(clickedShipVertical){
            //vertical ship

            temp = getFirstPiece();
            firstPieceRow = temp.getRow();
            firstPieceCol = temp.getColumn();

            for(int i = firstPieceRow; i < firstPieceRow + shipLength; i++){
                gridSpaces[i][firstPieceCol].clearCell();
            }

            /*
            if(whichShipWasClicked == SocketSignals.CARRIER_INT){

                for(int i = firstPieceRow; i < firstPieceRow + shipLength; i++){
                    gridSpaces[i][firstPieceCol].clearCell();
                }

            } else if(whichShipWasClicked == SocketSignals.BATTLESHIP_INT){

                for(int i = firstPieceRow; i < firstPieceRow + shipLength; i++){
                    gridSpaces[i][firstPieceCol].clearCell();
                }

            } else if(whichShipWasClicked == SocketSignals.DESTROYER_INT){

                for(int i = firstPieceRow; i < firstPieceRow + shipLength; i++){
                    gridSpaces[i][firstPieceCol].clearCell();
                }

            }
            */


        }else{
            temp = getFirstPiece();
            firstPieceRow = temp.getRow();
            firstPieceCol = temp.getColumn();

            for(int i = firstPieceCol; i < firstPieceCol + shipLength; i++){
                gridSpaces[firstPieceRow][i].clearCell();
            }
        }
    }

    private void rotateCarrier(int clickedRow, int clickedCol){
        int piece = gridSpaces[clickedRow][clickedCol].getShipPiece();
        if(clickedShipVertical) {
            System.out.println(clickedRow);
            if (piece == SocketSignals.FIRST_PIECE) {

            } else if (piece == SocketSignals.SECOND_PIECE) {
                clickedRow--;
            } else if (piece == SocketSignals.THIRD_PIECE) {
                clickedRow -= 2;
            } else if (piece == SocketSignals.FOURTH_PIECE) {
                clickedRow -= 3;
            } else if (piece == SocketSignals.FIFTH_PIECE) {
                clickedRow -= 4;
            }
            System.out.println(clickedRow);
            gridSpaces[clickedRow][clickedCol].setIcon(new ImageIcon("pics/carrier_1 copy.png"));
            gridSpaces[clickedRow][clickedCol+1].setIcon(new ImageIcon("pics/carrier_2 copy.png"));
            gridSpaces[clickedRow][clickedCol+2].setIcon(new ImageIcon("pics/carrier_3 copy.png"));
            gridSpaces[clickedRow][clickedCol+3].setIcon(new ImageIcon("pics/carrier_4 copy.png"));
            gridSpaces[clickedRow][clickedCol+4].setIcon(new ImageIcon("pics/carrier_5 copy.png"));

            //set the shipIsHere logic
            gridSpaces[clickedRow][clickedCol].setIsShipHere(true);
            gridSpaces[clickedRow][clickedCol+1].setIsShipHere(true);
            gridSpaces[clickedRow][clickedCol+2].setIsShipHere(true);
            gridSpaces[clickedRow][clickedCol+3].setIsShipHere(true);
            gridSpaces[clickedRow][clickedCol+4].setIsShipHere(true);

            //set the which ship logic
            gridSpaces[clickedRow][clickedCol].setWhichShip(SocketSignals.CARRIER_INT);
            gridSpaces[clickedRow][clickedCol+1].setWhichShip(SocketSignals.CARRIER_INT);
            gridSpaces[clickedRow][clickedCol+2].setWhichShip(SocketSignals.CARRIER_INT);
            gridSpaces[clickedRow][clickedCol+3].setWhichShip(SocketSignals.CARRIER_INT);
            gridSpaces[clickedRow][clickedCol+4].setWhichShip(SocketSignals.CARRIER_INT);

            //set the which ship logic
            gridSpaces[clickedRow][clickedCol].setShipPiece(SocketSignals.FIRST_PIECE);
            gridSpaces[clickedRow][clickedCol+1].setShipPiece(SocketSignals.SECOND_PIECE);
            gridSpaces[clickedRow][clickedCol+2].setShipPiece(SocketSignals.THIRD_PIECE);
            gridSpaces[clickedRow][clickedCol+3].setShipPiece(SocketSignals.FOURTH_PIECE);
            gridSpaces[clickedRow][clickedCol+4].setShipPiece(SocketSignals.FIFTH_PIECE);

            gridSpaces[clickedRow][clickedCol].setIsVertical(false);
            gridSpaces[clickedRow][clickedCol+1].setIsVertical(false);
            gridSpaces[clickedRow][clickedCol+2].setIsVertical(false);
            gridSpaces[clickedRow][clickedCol+3].setIsVertical(false);
            gridSpaces[clickedRow][clickedCol+4].setIsVertical(false);


        } else {
            if (piece == SocketSignals.FIRST_PIECE) {

            } else if (piece == SocketSignals.SECOND_PIECE) {
                clickedCol--;
            } else if (piece == SocketSignals.THIRD_PIECE) {
                clickedCol -= 2;
            } else if (piece == SocketSignals.FOURTH_PIECE) {
                clickedCol -= 3;
            } else if (piece == SocketSignals.FIFTH_PIECE) {
                clickedCol -= 4;
            }


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

    }

    private void dropBattleship(int releasedRow, int releasedCol){

        //set the images
        gridSpaces[releasedRow][releasedCol].setIcon(new ImageIcon("pics/battleship_1.png"));
        gridSpaces[releasedRow + 1][releasedCol].setIcon(new ImageIcon("pics/battleship_2.png"));
        gridSpaces[releasedRow + 2][releasedCol].setIcon(new ImageIcon("pics/battleship_3.png"));
        gridSpaces[releasedRow + 3][releasedCol].setIcon(new ImageIcon("pics/battleship_4.png"));

        //set the shipIsHere logic
        gridSpaces[releasedRow][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 1][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 2][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 3][releasedCol].setIsShipHere(true);

        //set the which ship logic
        gridSpaces[releasedRow][releasedCol].setWhichShip(SocketSignals.BATTLESHIP_INT);
        gridSpaces[releasedRow + 1][releasedCol].setWhichShip(SocketSignals.BATTLESHIP_INT);
        gridSpaces[releasedRow + 2][releasedCol].setWhichShip(SocketSignals.BATTLESHIP_INT);
        gridSpaces[releasedRow + 3][releasedCol].setWhichShip(SocketSignals.BATTLESHIP_INT);

        //set the which ship logic
        gridSpaces[releasedRow][releasedCol].setShipPiece(SocketSignals.FIRST_PIECE);
        gridSpaces[releasedRow + 1][releasedCol].setShipPiece(SocketSignals.SECOND_PIECE);
        gridSpaces[releasedRow + 2][releasedCol].setShipPiece(SocketSignals.THIRD_PIECE);
        gridSpaces[releasedRow + 3][releasedCol].setShipPiece(SocketSignals.FOURTH_PIECE);


    }

    private void dropDestroyer(int releasedRow, int releasedCol){

        //set the images
        gridSpaces[releasedRow][releasedCol].setIcon(new ImageIcon("pics/destroyer_1.png"));
        gridSpaces[releasedRow + 1][releasedCol].setIcon(new ImageIcon("pics/destroyer_2.png"));
        gridSpaces[releasedRow + 2][releasedCol].setIcon(new ImageIcon("pics/destroyer_3.png"));

        //set the shipIsHere logic
        gridSpaces[releasedRow][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 1][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 2][releasedCol].setIsShipHere(true);

        //set the which ship logic
        gridSpaces[releasedRow][releasedCol].setWhichShip(SocketSignals.DESTROYER_INT);
        gridSpaces[releasedRow + 1][releasedCol].setWhichShip(SocketSignals.DESTROYER_INT);
        gridSpaces[releasedRow + 2][releasedCol].setWhichShip(SocketSignals.DESTROYER_INT);

        //set the which ship logic
        gridSpaces[releasedRow][releasedCol].setShipPiece(SocketSignals.FIRST_PIECE);
        gridSpaces[releasedRow + 1][releasedCol].setShipPiece(SocketSignals.SECOND_PIECE);
        gridSpaces[releasedRow + 2][releasedCol].setShipPiece(SocketSignals.THIRD_PIECE);

    }

    private void dropSub(int releasedRow, int releasedCol){

        //set the images
        gridSpaces[releasedRow][releasedCol].setIcon(new ImageIcon("pics/sub_1.png"));
        gridSpaces[releasedRow + 1][releasedCol].setIcon(new ImageIcon("pics/sub_2.png"));
        gridSpaces[releasedRow + 2][releasedCol].setIcon(new ImageIcon("pics/sub_3.png"));

        //set the shipIsHere logic
        gridSpaces[releasedRow][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 1][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 2][releasedCol].setIsShipHere(true);

        //set the which ship logic
        gridSpaces[releasedRow][releasedCol].setWhichShip(SocketSignals.SUBMARINE_INT);
        gridSpaces[releasedRow + 1][releasedCol].setWhichShip(SocketSignals.SUBMARINE_INT);
        gridSpaces[releasedRow + 2][releasedCol].setWhichShip(SocketSignals.SUBMARINE_INT);

        //set the which ship logic
        gridSpaces[releasedRow][releasedCol].setShipPiece(SocketSignals.FIRST_PIECE);
        gridSpaces[releasedRow + 1][releasedCol].setShipPiece(SocketSignals.SECOND_PIECE);
        gridSpaces[releasedRow + 2][releasedCol].setShipPiece(SocketSignals.THIRD_PIECE);

    }

    private void dropPatrolBoat(int releasedRow, int releasedCol){

        //set the images
        gridSpaces[releasedRow][releasedCol].setIcon(new ImageIcon("pics/patrol_boat_1.png"));
        gridSpaces[releasedRow + 1][releasedCol].setIcon(new ImageIcon("pics/patrol_boat_2.png"));

        //set the shipIsHere logic
        gridSpaces[releasedRow][releasedCol].setIsShipHere(true);
        gridSpaces[releasedRow + 1][releasedCol].setIsShipHere(true);

        //set the which ship logic
        gridSpaces[releasedRow][releasedCol].setWhichShip(SocketSignals.PATROL_BOAT_INT);
        gridSpaces[releasedRow + 1][releasedCol].setWhichShip(SocketSignals.PATROL_BOAT_INT);

        //set the which ship logic
        gridSpaces[releasedRow][releasedCol].setShipPiece(SocketSignals.FIRST_PIECE);
        gridSpaces[releasedRow + 1][releasedCol].setShipPiece(SocketSignals.SECOND_PIECE);

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
        } else {
            if (gridSpaces[rowClicked][colClicked].getShipPiece() == SocketSignals.FIRST_PIECE) {
                return gridSpaces[rowClicked][colClicked];
            } else if (gridSpaces[rowClicked][colClicked].getShipPiece() == SocketSignals.SECOND_PIECE) {
                return gridSpaces[rowClicked][colClicked - 1];
            } else if (gridSpaces[rowClicked][colClicked].getShipPiece() == SocketSignals.THIRD_PIECE) {
                return gridSpaces[rowClicked][colClicked - 2];
            } else if (gridSpaces[rowClicked][colClicked].getShipPiece() == SocketSignals.FOURTH_PIECE) {
                return gridSpaces[rowClicked][colClicked - 3];
            } else if (gridSpaces[rowClicked][colClicked].getShipPiece() == SocketSignals.FIFTH_PIECE) {
                return gridSpaces[rowClicked][colClicked - 4];
            }
        }

        return null;
    }

    private void print(String s){
        System.out.println(s);
    }

    private void setShipsGone(){

        if(!carrier.isVisible() && !battleship.isVisible()){
            leftShipsGone = true;
        }

        if(!destroyer.isVisible() && !submarine.isVisible() && !patrolBoat.isVisible()){
            rightShipsGone = true;
        }
    }

    private void setBounds(){
        if(leftShipsGone && rightShipsGone){
            leftBound = 50;
            rightBound = 550;
        }else if(leftShipsGone && !rightShipsGone){
            leftBound = 25;
            rightBound = 525;
        }else if(rightShipsGone && !leftShipsGone){
            leftBound = 25;
            rightBound = 525;
        }
    }





}//close big class
