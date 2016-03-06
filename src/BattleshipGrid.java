import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * Created by jskarda on 2/16/16.
 */

public class BattleshipGrid extends JPanel {

    ShipMouseAdapter shipListener;
    ShipMovingAdaptor shipMovingAdaptor;
    MouseListener listener;
    public CellPane[][] gridSpaces;
    public Color COLOR_CELL_HAS_SHIP = Color.magenta;
    public static boolean shipBeingDragged = false;
    public boolean needToAddShips;

    Carrier c = new Carrier();
    Battleship b = new Battleship();
    Destroyer d = new Destroyer();
    Submarine s = new Submarine();
    PatrolBoat pb = new PatrolBoat();

    BattleshipGrid battleshipGrid = this;

    public BattleshipGrid (boolean addShips) {
        listener = new DragMouseAdapter();
        needToAddShips = addShips;
        setLayout(new BorderLayout());
        add(new TestPane());
        setVisible(true);
        setOpaque(true);
    }

    public class TestPane extends JPanel {


        public TestPane()  {
            setLayout(new GridBagLayout());
            gridSpaces = new CellPane[10][10];
            GridBagConstraints gbc = new GridBagConstraints();
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    gbc.gridx = col;
                    gbc.gridy = row;

                    CellPane cellPane = new CellPane(row, col);
                    Border border;
                    if (row < 9) {
                        if (col < 9) {
                            border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                        }
                    } else {
                        if (col < 9) {
                            border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                        }
                    }
                    cellPane.setBorder(border);
                    cellPane.setBackground(Color.BLACK);
                    add(cellPane, gbc);
                    gridSpaces[row][col] = cellPane;
                }
            }

            if(needToAddShips) {

                shipListener = new ShipMouseAdapter(shipBeingDragged, gridSpaces, c, b, d, s, pb, battleshipGrid);
                shipMovingAdaptor = new ShipMovingAdaptor(gridSpaces, c, b, d, s, pb);
                addMouseListener(shipMovingAdaptor);

                gbc.gridx = 10;
                gbc.gridy = 0;
                gbc.gridheight = 5;
                //Carrier c = new Carrier();
                c.addMouseListener(shipListener);
                c.setName("Carrier");

                ImageIcon ii = new ImageIcon("src/carrier_1.png");

                add(c, gbc);

                //Set gridbagcontraints for Battleship
                gbc.gridx = 10;
                gbc.gridy = 6;
                gbc.gridheight = 4;
                //Battleship b = new Battleship();
                b.addMouseListener(shipListener);
                b.setName("Battleship");
                //b.setTransferHandler(new TransferHandler("icon"));
                add(b, gbc);

                //set gridbagconstraints for destroyer
                gbc.gridx = 11;
                gbc.gridy = 0;
                gbc.gridheight = 3;
                //Destroyer d = new Destroyer();
                d.addMouseListener(shipListener);
                d.setName("Destroyer");
                //d.setTransferHandler(new TransferHandler("icon"));
                add(d, gbc);

                //set gridbagconstraints for submarine
                gbc.gridx = 11;
                gbc.gridy = 4;
                gbc.gridheight = 3;
                //Submarine s = new Submarine();
                s.addMouseListener(shipListener);
                s.setName("Sub");
                //s.setTransferHandler(new TransferHandler("icon"));
                add(s, gbc);

                //set gridbagcontraints for patrol boat
                gbc.gridx = 11;
                gbc.gridy = 8;
                gbc.gridheight = 2;
                //PatrolBoat pb = new PatrolBoat();
                pb.addMouseListener(shipListener);
                pb.setName("PatrolBoat");
                //pb.setTransferHandler(new TransferHandler("icon"));
                add(pb, gbc);

            }else{
                //dont need to add ships to this grid
            }


            addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            if (gridSpaces[i][j].getIsClicked()) {
                                if (!gridSpaces[i][j].isDone) {
                                    gridSpaces[i][j].setBackground(Color.BLACK);
                                    gridSpaces[i][j].setClicked(false);
//                                    System.out.println(gridSpaces[i][j].getX() + " " + gridSpaces[i][j].getY());
                                }
                            }
                        }
                    }
                    if (e.getX() <= gridSpaces[9][9].getX() + 50 && e.getY() <= 500) {
                        for (int i = 0; i < 10; i++) {
                            for (int j = 0; j < 10; j++) {
                                if (e.getX() > gridSpaces[i][j].getX() && e.getX() < gridSpaces[i][j].getX() + 50) {
                                    if (e.getY() > gridSpaces[i][j].getY() && e.getY() < gridSpaces[i][j].getY() + 50) {
                                        if (!gridSpaces[i][j].isDone) {
                                            gridSpaces[i][j].setBackground(Color.BLUE);
                                            gridSpaces[i][j].setClicked(true);
                                        }
                                    }
                                }
                            }
                        }
                        repaint();
                    }
                }

                public void mouseEntered(MouseEvent e) {
                    if (e.getX() <= gridSpaces[9][9].getX() + 50 && e.getY() <= 500) {
                        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                    } else {
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }

                public void mouseExited(MouseEvent e) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Dongus!");
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    System.out.println("battleship grid top, Ship being dragged: " + shipBeingDragged);
                    shipBeingDragged = false;
                    System.out.println("battleship grid bottom, Ship being dragged: " + shipBeingDragged);
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });


        }
        @Override
        public Dimension getPreferredSize(){
            return new Dimension(600,500);
        }
    }//close TestPane()

    //BattleshipGrid helper functions

    public boolean assignRandomShips(){
        //generate 2 random numbers, row and col to set that cell as filled
        Random random = new Random();
        int row, col;
        int max = 9, min = 0;
        int numShipsAssigned = 0;

        while(numShipsAssigned < 2){
            row = (random.nextInt(max - min + 1) + min);
            col = (random.nextInt(max - min + 1) + min);

            if(gridSpaces[row][col].getIsShipHere() == false) {
                gridSpaces[row][col].setIsShipHere(true);
                gridSpaces[row][col].setBackground(COLOR_CELL_HAS_SHIP);

                numShipsAssigned++;

//                System.out.print("Row: " + row + " Col: " + col);
            }else{
                //do nothing cuz theres already a ship there
//                System.out.print("Already ship there");
            }
        }

        System.out.println();

        return true;
    }


    public class CellPane extends JLabel {

        private Color defaultBackground = Color.BLACK;
        private boolean isClicked = false;
        private boolean isDone = false;
        private boolean isShipHere = false;
        private int row;
        private int column;
        private int whichShip; //1 - 5 carrier, battleship, destroyer, sub, patrol boat
        private int shipPiece; //1 top of ship

        public CellPane(int r, int c) {

            setOpaque(true);
            setForeground(defaultBackground);
            setMinimumSize(new Dimension(50, 50));
            setMaximumSize(new Dimension(50, 50));
            isShipHere = false;
            row = r;
            column = c;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(50, 50);
        }

        public boolean getIsClicked(){
            return isClicked;
        }

        public void setClicked(boolean trueFalse){
            isClicked = trueFalse;
        }

        public boolean getIsShipHere(){
            return isShipHere;
        }

        public void setIsShipHere(boolean b){
            isShipHere = b;
        }

        public int getRow(){return row;}

        public int getColumn(){return column;}

        public boolean getIsDone(){  return isDone; }

        public void setIsDone(boolean b){isDone = b;}

        public int getWhichShip() {
            return whichShip;
        }

        public void setWhichShip(int whichShip) {
            this.whichShip = whichShip;
        }

        public int getShipPiece() {
            return shipPiece;
        }

        public void setShipPiece(int shipPiece) {
            this.shipPiece = shipPiece;
        }

        public void clearCell(){
            isShipHere = false;
            whichShip = -1;
            shipPiece = -1;
            setIcon(null);

        }
    }//close CellPane class

//    class DragMouseAdapter extends MouseAdapter {
//        public void mousePressed(MouseEvent e) {
//            JComponent c = (JComponent) e.getSource();
//            TransferHandler handler = c.getTransferHandler();
//            handler.exportAsDrag(c, e, TransferHandler.COPY);
//        }
//    }

    public CellPane getTargetedCell(){
        //cycle through the grid cells to find the one clicked
        //mark the targeted cell as done

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if((gridSpaces[i][j].getIsClicked() == true) && (gridSpaces[i][j].getIsDone() == false)){
                    gridSpaces[i][j].setIsDone(true);
                    return gridSpaces[i][j];
                }
            }
        }

        return null;
    }

    public boolean markShot(int row, int col){

        if(gridSpaces[row][col].getIsShipHere() == true){
            System.out.print("Your ship has been hit!");
            gridSpaces[row][col].setBackground(SocketSignals.BATTLESHIP_COLOR_SHIP_HIT);
            gridSpaces[row][col].setIsDone(true);
            return true;
        }else{
            System.out.print("The enemy has missed!");
            gridSpaces[row][col].setBackground(SocketSignals.BATTLESHIP_COLOR_SHIP_MISS);
            gridSpaces[row][col].setIsDone(true);
            return false;
        }


    }

    public void markShot(int row, int col, boolean hit){

        if(hit == true){
//            System.out.print("You hit a ship!");
            gridSpaces[row][col].setBackground(SocketSignals.BATTLESHIP_COLOR_SHIP_HIT);
        }else{
            System.out.print("You missed!!");
            gridSpaces[row][col].setBackground(SocketSignals.BATTLESHIP_COLOR_SHIP_MISS);
        }
        gridSpaces[row][col].setIsDone(true);

    }

    public boolean isGameOver(){
//        System.out.println("checking is game over inside of grid");
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(gridSpaces[i][j].isShipHere && !gridSpaces[i][j].isDone){
                    return false;
                }
            }
        }
//        System.out.println("Game is over");
        return true;
    }

}


